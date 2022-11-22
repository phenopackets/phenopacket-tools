package org.phenopackets.phenopackettools.cli.command;


import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.base.PhenolRuntimeException;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.phenopackettools.validator.core.metadata.MetaDataValidators;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.phenopackettools.validator.core.writer.ValidationResultsAndPath;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner;
import org.phenopackets.phenopackettools.cli.writer.CSVValidationResultsWriter;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Command(name = "validate",
        description = "Validate top-level elements of the Phenopacket Schema.",
        sortOptions = false,
        mixinStandardHelpOptions = true)
public class ValidateCommand extends BaseIOCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateCommand.class);

    @CommandLine.ArgGroup(validate = false, heading = "Validate section:%n")
    public ValidateSection validateSection = new ValidateSection();

    public static class ValidateSection {
        @CommandLine.Option(names = {"-H", "--include-header"},
                description = {"Include header in the output", "Default: ${DEFAULT-VALUE}"})
        public boolean includeHeader = false;

        @CommandLine.Option(names = {"--require"},
                arity = "*",
                description = "Path to JSON schema with additional requirements to enforce.")
        public List<Path> requirements = List.of();

        @CommandLine.Option(names = "--hpo",
                description = "Path to hp.json file")
        public Path hpJson;

        @CommandLine.Option(names = {"-s", "--organ-system"},
                arity = "*",
                description = {"Organ system HPO term IDs",
                        "Default: empty"})
        public List<String> organSystems = List.of();
    }

    @Override
    protected Integer execute() {
        // (1) Read the input v2 message(s).
        List<MessageAndPath> messages = readMessagesOrExit(PhenopacketSchemaVersion.V2);

        // (2) Set up the validator.
        ValidationWorkflowRunner<MessageOrBuilder> runner = prepareWorkflowRunner();

        // (3) Validate the message(s).
        List<ValidationResultsAndPath> results = new ArrayList<>(messages.size());
        for (MessageAndPath mp : messages) {
            results.add(new ValidationResultsAndPath(runner.validate(mp.message()), mp.path()));
        }

        // (4) Write out the validation results into STDOUT.
        try {
            CSVValidationResultsWriter writer = new CSVValidationResultsWriter(System.out,
                    PHENOPACKET_TOOLS_VERSION,
                    LocalDateTime.now(),
                    validateSection.includeHeader);
            writer.writeValidationResults(runner.validators(), results);
            return 0;
        } catch (IOException e) {
            LOGGER.error("Error while writing out results: {}", e.getMessage(), e);
            return 1;
        }
    }

    private ValidationWorkflowRunner<MessageOrBuilder> prepareWorkflowRunner() {
        List<URL> customJsonSchemas = prepareCustomSchemaUrls();
        Object runner = switch (inputSection.element) {
            case PHENOPACKET -> {
                List<PhenopacketValidator<PhenopacketOrBuilder>> validators = configureSemanticValidators();
                yield JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                        .addAllJsonSchemaUrls(customJsonSchemas)
                        .addValidator(MetaDataValidators.phenopacketValidator())
                        .addValidators(validators)
                        .build();
            }
            case FAMILY -> {
                List<PhenopacketValidator<FamilyOrBuilder>> validators = configureSemanticValidators();
                yield JsonSchemaValidationWorkflowRunner.familyBuilder()
                        .addAllJsonSchemaUrls(customJsonSchemas)
                        .addValidator(MetaDataValidators.familyValidator())
                        .addValidators(validators)
                        .build();
            }
            case COHORT -> {
                List<PhenopacketValidator<CohortOrBuilder>> validators = configureSemanticValidators();
                yield JsonSchemaValidationWorkflowRunner.cohortBuilder()
                        .addAllJsonSchemaUrls(customJsonSchemas)
                        .addValidator(MetaDataValidators.cohortValidator())
                        .addValidators(validators)
                        .build();
            }
        };

        // Same as in `configureSemanticValidators`, we rely on the correct pairing of `element` and `message`s
        // to be validated. The code will explode if this assumption is invalid.
        //noinspection unchecked
        return (ValidationWorkflowRunner<MessageOrBuilder>) runner;
    }

    private List<URL> prepareCustomSchemaUrls() {
        LOGGER.debug("Preparing schemas for custom requirement validation");
        List<URL> urls = new ArrayList<>();
        for (Path requirement : validateSection.requirements) {
            try {
                urls.add(requirement.toUri().toURL());
            } catch (MalformedURLException e) {
                System.err.printf("Skipping JSON schema at '%s', the path is invalid: %s%n", requirement.toAbsolutePath(), e.getMessage());
            }
        }
        LOGGER.debug("Prepared {} custom schema(s)", urls.size());
        return urls;
    }

    /**
     * Prepare semantic validators for given {@link T}.
     * <p>
     * <b>Warning</b> - it is important to request the {@link T} that is appropriate
     * for the current {@link org.phenopackets.phenopackettools.cli.command.BaseIOCommand.InputSection#element}.
     * The app will crash and burn if e.g. {@link T} is {@link PhenopacketOrBuilder}
     * while {@link org.phenopackets.phenopackettools.cli.command.BaseIOCommand.InputSection#element}
     * is {@link PhenopacketElement#FAMILY}.
     */
    private <T extends MessageOrBuilder> List<PhenopacketValidator<T>> configureSemanticValidators() {
        // Right now we only have one semantic validator, but we'll extend this in the future.
        LOGGER.debug("Configuring semantic validators");
        List<PhenopacketValidator<T>> validators = new ArrayList<>();
        Ontology hpo = null;
        if (validateSection.hpJson != null) {
            LOGGER.debug("Reading HPO from {}", validateSection.hpJson.toAbsolutePath());
            hpo = OntologyLoader.loadOntology(validateSection.hpJson.toFile());

            // The entire logic of this command stands and falls on correct state of `element` and the read message(s).
            // This method requires an appropriate combination of `T` and `element`, as described in Javadoc.
            // We suppress warning and perform an unchecked cast here, assuming `T` and `element` are appropriate.
            // The app will crash and burn if this is not the case.
            switch (inputSection.element) {
                case PHENOPACKET -> {
                    //noinspection unchecked
                    validators.add((PhenopacketValidator<T>) HpoPhenotypeValidators.Primary.phenopacketHpoPhenotypeValidator(hpo));
                    //noinspection unchecked
                    validators.add((PhenopacketValidator<T>) HpoPhenotypeValidators.Ancestry.phenopacketHpoAncestryValidator(hpo));
                }
                case FAMILY -> {
                    //noinspection unchecked
                    validators.add((PhenopacketValidator<T>) HpoPhenotypeValidators.Primary.familyHpoPhenotypeValidator(hpo));
                    //noinspection unchecked
                    validators.add((PhenopacketValidator<T>) HpoPhenotypeValidators.Ancestry.familyHpoAncestryValidator(hpo));
                }
                case COHORT -> {
                    //noinspection unchecked
                    validators.add((PhenopacketValidator<T>) HpoPhenotypeValidators.Primary.cohortHpoPhenotypeValidator(hpo));
                    //noinspection unchecked
                    validators.add((PhenopacketValidator<T>) HpoPhenotypeValidators.Ancestry.cohortHpoAncestryValidator(hpo));
                }
            }
        }

        if (!validateSection.organSystems.isEmpty()) {
            PhenopacketValidator<T> validator = prepareOrganSystemValidator(hpo, validateSection.organSystems, inputSection.element);
            if (validator != null)
                validators.add(validator);

        }

        LOGGER.debug("Configured {} semantic validator(s)", validators.size());
        return validators;
    }

    private static <T extends MessageOrBuilder> PhenopacketValidator<T> prepareOrganSystemValidator(Ontology hpo,
                                                                                                    List<String> organSystems,
                                                                                                    PhenopacketElement element) {
        // Organ system validation can only be done when HPO is provided.
        if (hpo == null) {
            LOGGER.warn("Terms for organ system validation were provided but the path to HPO is unset. Use --hpo option to enable organ system validation.");
            return null;
        }

        // Prepare organ system IDs.
        List<TermId> organSystemIds = prepareOrganSystemIds(organSystems);

        // Create the validator.
        if (!organSystemIds.isEmpty()) {
            return switch (element) {
                case PHENOPACKET -> //noinspection unchecked
                        (PhenopacketValidator<T>) HpoPhenotypeValidators.OrganSystem.phenopacketHpoOrganSystemValidator(hpo, organSystemIds);
                case FAMILY -> //noinspection unchecked
                        (PhenopacketValidator<T>) HpoPhenotypeValidators.OrganSystem.familyHpoOrganSystemValidator(hpo, organSystemIds);
                case COHORT -> //noinspection unchecked
                        (PhenopacketValidator<T>) HpoPhenotypeValidators.OrganSystem.cohortHpoOrganSystemValidator(hpo, organSystemIds);
            };
        }

        return null;
    }

    private static List<TermId> prepareOrganSystemIds(List<String> organSystems) {
        LOGGER.trace("Found {} organ system IDs: {}", organSystems.size(), organSystems.stream()
                .collect(Collectors.joining(", ", "{", "}")));
        List<TermId> organSystemIds = organSystems.stream()
                .map(toTermId())
                .flatMap(Optional::stream)
                .toList();
        LOGGER.trace("{} organ system IDs are valid term IDs: {}", organSystemIds.size(),
                organSystemIds.stream()
                        .map(TermId::getValue)
                        .collect(Collectors.joining(", ", "{", "}")));
        return organSystemIds;
    }

    /**
     * @return a function that maps a {@link String} into a {@link TermId} or emits a warning if the value
     * cannot be mapped.
     */
    private static Function<String, Optional<TermId>> toTermId() {
        return value -> {
            try {
                return Optional.of(TermId.of(value));
            } catch (PhenolRuntimeException e) {
                LOGGER.warn("Invalid term ID {}", value);
                return Optional.empty();
            }
        };
    }

}