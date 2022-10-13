package org.phenopackets.phenopackettools.command.validate;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidationResults;
import org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Base logic of the {@code validate *} command.
 * <p>
 * We prepare a {@link ValidationWorkflowRunner} to run validation of the bas requirements as well as
 * requirements provided in custom JSON schema documents followed by semantic validation.
 * Then we apply the validation on each phenopacket/family/cohort and report the results.
 *
 * @param <T>
 */
abstract class BaseValidateCommand<T extends MessageOrBuilder> implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseValidateCommand.class);

    @CommandLine.Parameters(arity = "1..*", description = "One or more phenopacket files")
    public List<Path> phenopackets;

    @CommandLine.Option(names = {"--require"},
            arity = "*",
            description = "Path to JSON schema with additional requirements to enforce.")
    protected List<Path> requirements = List.of();

    @CommandLine.Option(names ="--hpo",
            description = "Path to hp.json file")
    protected Path hpJson;

    @Override
    public Integer call() {
        // Prepare requirement schemas and semantic validators.
        List<URL> customJsonSchemas = prepareCustomSchemaUrls();
        List<PhenopacketValidator<T>> semanticValidators = configureSemanticValidators();

        // Create the validation workflow runner.
        ValidationWorkflowRunner<T> validationRunner = prepareValidationWorkflow(customJsonSchemas, semanticValidators);
        LOGGER.debug("Configured validation runner with {} validation(s)", validationRunner.validators().size());

        // Validate the phenopackets and report the results.
        for (Path item : phenopackets) {
            Path fileName = item.toAbsolutePath();
            LOGGER.debug("Validating '{}'", fileName);

            try (InputStream is = Files.newInputStream(item)) {
                ValidationResults results = validationRunner.validate(is);
                List<ValidationResult> validationResults = results.validationResults();
                if (validationResults.isEmpty()) {
                    System.out.printf("%s - OK%n", fileName);
                } else {
                    for (ValidationResult result : validationResults) {
                        System.out.printf("%s [%s] - %s: %s%n", fileName, result.level(), result.category(), result.message());
                    }
                }
                printSeparator();

            } catch (IOException e) {
                System.out.println("Error opening the phenopacket: " + e);
            }
        }
        return 0;
    }

    protected abstract ValidationWorkflowRunner<T> prepareValidationWorkflow(List<URL> customJsonSchemas,
                                                                             List<PhenopacketValidator<T>> semanticValidators);

    protected List<URL> prepareCustomSchemaUrls() {
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

    private void printSeparator() {
        if (phenopackets.size() > 1) {
            System.out.println("---");
        }
    }

    protected List<PhenopacketValidator<T>> configureSemanticValidators()  {
        // Right now we only have one semantic validator, but we'll extend this in the future.
        LOGGER.debug("Configuring semantic validators");
        List<PhenopacketValidator<T>> validators = new ArrayList<>();
        if (hpJson != null) {
            LOGGER.debug("Reading HPO from '{}}'", hpJson.toAbsolutePath());
            Ontology hpo = OntologyLoader.loadOntology(hpJson.toFile());
            validators.add(createHpoValidator(hpo));
        }

        LOGGER.debug("Configured {} semantic validator(s)", validators.size());
        return validators;
    }

    protected abstract PhenopacketValidator<T> createHpoValidator(Ontology hpo);

}
