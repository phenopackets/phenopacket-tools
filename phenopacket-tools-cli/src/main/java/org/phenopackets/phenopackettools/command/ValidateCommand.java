package org.phenopackets.phenopackettools.command;


import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.phenopackettools.validator.core.metadata.MetaDataValidators;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner;
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
public class ValidateCommand extends SingleItemInputCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateCommand.class);

    @CommandLine.Option(names = {"--require"},
            arity = "*",
            description = "Path to JSON schema with additional requirements to enforce.")
    protected List<Path> requirements = List.of();

    @CommandLine.Option(names = "--hpo",
            description = "Path to hp.json file")
    protected Path hpJson;

    @Override
    public Integer call() {
        // (0) Print banner.
        printBanner();

        // (1) Read the input v2 message.
        Message message = readMessageOrExit(PhenopacketSchemaVersion.V2);

        // (2) Set up the validator.
        ValidationWorkflowRunner<MessageOrBuilder> runner = prepareWorkflowRunner();

        // (3) Validate the message(s).
        ValidationResults results = runner.validate(message);

        // (4) Write out the validation results.
        PrintStream printToStdoutSoFar = System.out;
        return writeValidationResults(results, printToStdoutSoFar);
    }

    private ValidationWorkflowRunner<MessageOrBuilder> prepareWorkflowRunner() {
        List<URL> customJsonSchemas = prepareCustomSchemaUrls();
        Object runner = switch (element) {
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
        for (Path requirement : requirements) {
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
     * <b>Warning</b> - it is important to request the {@link T} that is appropriate for the current {@link #element}.
     * The app will crash and burn if e.g. {@link T} is {@link PhenopacketOrBuilder} while {@link #element}
     * is {@link org.phenopackets.phenopackettools.util.format.PhenopacketElement#FAMILY}.
     */
    private <T extends MessageOrBuilder> List<PhenopacketValidator<T>> configureSemanticValidators()  {
        // Right now we only have one semantic validator, but we'll extend this in the future.
        LOGGER.debug("Configuring semantic validators");
        List<PhenopacketValidator<T>> validators = new ArrayList<>();
        if (hpJson != null) {
            LOGGER.debug("Reading HPO from '{}}'", hpJson.toAbsolutePath());
            Ontology hpo = OntologyLoader.loadOntology(hpJson.toFile());

            // The entire logic of this command stands and falls on correct state of `element` and the read message(s).
            // This method requires an appropriate combination of `T` and `element`, as described in Javadoc.
            // We suppress warning and perform an unchecked cast here, assuming `T` and `element` are appropriate.
            // The app will crash and burn if this is not the case.
            PhenopacketValidator<T> validator = switch (element) {
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


    private static int writeValidationResults(ValidationResults results, OutputStream os) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setCommentMarker('#')
                .build();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));

        try {
            CSVPrinter printer = format.print(writer);
            printHeader(results, printer);
            printValidationResults(results, printer);
        } catch (IOException e) {
            LOGGER.error("Error while writing out the validation results: {}", e.getMessage(), e);
            return 1;
        } finally {
            try {
                writer.flush();
                os.flush();
                if (os != System.out)
                    os.close();
            } catch (IOException e) {
                LOGGER.error("Error while flushing and closing the writer: {}", e.getMessage(), e);
            }
        }

        return 0; // We're done
    }

    private static void printHeader(ValidationResults results, CSVPrinter printer) throws IOException {
        // Print header
        printer.printComment("phenopacket-tools validate %s".formatted(PHENOPACKET_TOOLS_VERSION));
        printer.printComment("date=%s".formatted(LocalDateTime.now()));

        // Print validators
        for (ValidatorInfo validator : results.validators()) {
            printer.printComment("validator_id=%s;validator_name=%s;description=%s".formatted(validator.validatorId(), validator.validatorName(), validator.description()));
        }
    }

    private static void printValidationResults(ValidationResults results, CSVPrinter printer) throws IOException {
        // Header
        printer.printRecord("LEVEL", "VALIDATOR_ID", "CATEGORY", "MESSAGE");
        // Validation results
        for (ValidationResult result : results.validationResults()) {
            printer.print(result.level());
            printer.print(result.validatorInfo().validatorId());
            printer.print(result.category());
            printer.print(result.message());
            printer.println();
        }
    }
}