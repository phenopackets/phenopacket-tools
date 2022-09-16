package org.phenopackets.phenopackettools.command.validate;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidationResults;
import org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner;
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
        List<PhenopacketValidator<T>> semanticValidators = prepareSemanticValidators();

        // Create the validation workflow runner.
        ValidationWorkflowRunner<T> validationRunner = prepareValidationWorkflow(customJsonSchemas, semanticValidators);

        // Validate the phenopackets and report the results.
        for (Path item : phenopackets) {
            Path fileName = item.getFileName();

            try (InputStream is = Files.newInputStream(item)) {
                ValidationResults results = validationRunner.validate(is);
                List<ValidationResult> validationResults = results.validationResults();
                if (validationResults.isEmpty()) {
                    System.out.printf("%s - OK%n", fileName);
                } else {
                    for (ValidationResult result : validationResults) {
                        System.out.printf("%s - %s:%s%n ", fileName, result.category(), result.message());
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
        List<URL> urls = new ArrayList<>();
        for (Path requirement : requirements) {
            try {
                urls.add(requirement.toUri().toURL());
            } catch (MalformedURLException e) {
                System.err.printf("Skipping JSON schema at '%s', the path is invalid: %s%n", requirement.toAbsolutePath(), e.getMessage());
            }
        }
        return urls;
    }

    private void printSeparator() {
        if (phenopackets.size() > 1) {
            System.out.println("---");
        }
    }

    protected List<PhenopacketValidator<T>> prepareSemanticValidators()  {
        // Right now we only have one semantic validator, but we'll extend this in the future.
        List<PhenopacketValidator<T>> validators = new ArrayList<>();
        if (hpJson != null) {
            Ontology hpo = OntologyLoader.loadOntology(hpJson.toFile());
            validators.add(createHpoValidator(hpo));
        }
        return validators;
    }

    protected abstract PhenopacketValidator<T> createHpoValidator(Ontology hpo);

}
