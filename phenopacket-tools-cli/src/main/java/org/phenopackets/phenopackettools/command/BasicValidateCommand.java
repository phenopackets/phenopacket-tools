package org.phenopackets.phenopackettools.command;


import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "basic",
        description = "Basic validation of Phenopackets using generic JSON Schema",
        mixinStandardHelpOptions = true)
public class BasicValidateCommand implements Callable<Integer> {

    @Parameters(arity = "1..*", description = "One or more phenopacket files")
    public List<Path> phenopackets;

    @Option(names = {"--require"},
            arity = "*",
            description = "Path to JSON schema with additional requirements to enforce.")
    public List<Path> requirements;

    @Option(names ="--hpo", description = "Path to hp.json file")
    public Path hpJsonPath;

    @Override
    public Integer call() {
        // Prepare requirement schemas and semantic validators.
        List<URL> customJsonSchemas = prepareSchemaUrls();
        List<PhenopacketValidator<PhenopacketOrBuilder>> semanticValidators = prepareSemanticValidators();

        // Build the validation workflow runner
        ValidationWorkflowRunner<PhenopacketOrBuilder> runner = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                .addAllJsonSchemaUrls(customJsonSchemas)
                .addAllSemanticValidators(semanticValidators)
                .build();

        // Validate the phenopackets
        for (Path phenopacket : phenopackets) {
            try (InputStream in = Files.newInputStream(phenopacket)) {
                ValidationResults results = runner.validate(in);
                List<ValidationResult> validationItems = results.validationResults();
                Path fileName = phenopacket.getFileName();
                if (validationItems.isEmpty()) {
                    System.out.printf("%s - OK%n", fileName);
                    printSeparator();
                } else {
                    for (ValidationResult item : validationItems) {
                        System.out.printf("%s - %s:%s%n ", fileName, item.category(), item.message());
                    }
                    printSeparator();
                }

            } catch (IOException e) {
                System.out.println("Error opening the phenopacket: " + e);
            }
        }
        return 0;
    }

    private List<URL> prepareSchemaUrls() {
        List<URL> urls = new ArrayList<>();
        for (Path requirement : requirements) {
            try {
                urls.add(requirement.toUri().toURL());
            } catch (MalformedURLException e) {
                System.err.printf("Skipping JSON schema at %s, the path is invalid: %s%n", requirement.toAbsolutePath(), e.getMessage());
            }
        }
        return urls;
    }

    private List<PhenopacketValidator<PhenopacketOrBuilder>> prepareSemanticValidators() {
        Ontology hpo = OntologyLoader.loadOntology(hpJsonPath.toFile());
        PhenopacketValidator<PhenopacketOrBuilder> validator = HpoPhenotypeValidators.phenopacketHpoPhenotypeValidator(hpo);
        return List.of(validator);
    }

    private void printSeparator() {
        if (phenopackets.size() > 1) {
            System.out.println("---");
        }
    }

}