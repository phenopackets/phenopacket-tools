package org.phenopackets.phenopackettools.command;


import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.phenopackettools.validator.core.metadata.MetaDataValidators;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.phenopackettools.validator.core.writer.ValidationResultsAndPath;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner;
import org.phenopackets.phenopackettools.writer.CSVValidationResultsWriter;
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

@Command(name = "validate",
        description = "Validate top-level elements of the Phenopacket schema.",
        sortOptions = false,
        mixinStandardHelpOptions = true)
public class ValidateCommand extends BaseIOCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateCommand.class);

    @CommandLine.ArgGroup(validate = false, heading = "Validate section:%n")
    public ValidateSection validateSection = new ValidateSection();

    public static class ValidateSection {
        @CommandLine.Option(names = {"--require"},
                arity = "*",
                description = "Path to JSON schema with additional requirements to enforce.")
        protected List<Path> requirements = List.of();

        @CommandLine.Option(names = "--hpo",
                description = "Path to hp.json file")
        protected Path hpJson;
    }

    @Override
    public Integer call() {
        // (0) Print banner.
        printBanner();

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
            CSVValidationResultsWriter writer = new CSVValidationResultsWriter(System.out, PHENOPACKET_TOOLS_VERSION, LocalDateTime.now());
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
                List<PhenopacketValidator<PhenopacketOrBuilder>> semanticValidators = configureSemanticValidators();
                yield JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                        .addAllJsonSchemaUrls(customJsonSchemas)
                        .addSemanticValidator(MetaDataValidators.phenopacketValidator())
                        .addAllSemanticValidators(semanticValidators)
                        .build();
            }
            case FAMILY -> {
                List<PhenopacketValidator<FamilyOrBuilder>> semanticValidators = configureSemanticValidators();
                yield JsonSchemaValidationWorkflowRunner.familyBuilder()
                        .addAllJsonSchemaUrls(customJsonSchemas)
                        .addSemanticValidator(MetaDataValidators.familyValidator())
                        .addAllSemanticValidators(semanticValidators)
                        .build();
            }
            case COHORT -> {
                List<PhenopacketValidator<CohortOrBuilder>> semanticValidators = configureSemanticValidators();
                yield JsonSchemaValidationWorkflowRunner.cohortBuilder()
                        .addAllJsonSchemaUrls(customJsonSchemas)
                        .addSemanticValidator(MetaDataValidators.cohortValidator())
                        .addAllSemanticValidators(semanticValidators)
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
     * for the current {@link org.phenopackets.phenopackettools.command.BaseIOCommand.InputSection#element}.
     * The app will crash and burn if e.g. {@link T} is {@link PhenopacketOrBuilder}
     * while {@link org.phenopackets.phenopackettools.command.BaseIOCommand.InputSection#element}
     * is {@link org.phenopackets.phenopackettools.util.format.PhenopacketElement#FAMILY}.
     */
    private <T extends MessageOrBuilder> List<PhenopacketValidator<T>> configureSemanticValidators() {
        // Right now we only have one semantic validator, but we'll extend this in the future.
        LOGGER.debug("Configuring semantic validators");
        List<PhenopacketValidator<T>> validators = new ArrayList<>();
        if (validateSection.hpJson != null) {
            LOGGER.debug("Reading HPO from '{}}'", validateSection.hpJson.toAbsolutePath());
            Ontology hpo = OntologyLoader.loadOntology(validateSection.hpJson.toFile());

            // The entire logic of this command stands and falls on correct state of `element` and the read message(s).
            // This method requires an appropriate combination of `T` and `element`, as described in Javadoc.
            // We suppress warning and perform an unchecked cast here, assuming `T` and `element` are appropriate.
            // The app will crash and burn if this is not the case.
            PhenopacketValidator<T> validator = switch (inputSection.element) {
                case PHENOPACKET -> //noinspection unchecked
                        (PhenopacketValidator<T>) HpoPhenotypeValidators.phenopacketHpoPhenotypeValidator(hpo);
                case FAMILY -> //noinspection unchecked
                        (PhenopacketValidator<T>) HpoPhenotypeValidators.familyHpoPhenotypeValidator(hpo);
                case COHORT -> //noinspection unchecked
                        (PhenopacketValidator<T>) HpoPhenotypeValidators.cohortHpoPhenotypeValidator(hpo);
            };
            validators.add(validator);
        }

        LOGGER.debug("Configured {} semantic validator(s)", validators.size());
        return validators;
    }

}