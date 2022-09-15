package org.phenopackets.phenopackettools.command;


import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidator;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidatorImpl;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "basic", description = "Basic validation of Phenopackets using generic JSON Schema",
        mixinStandardHelpOptions = true)
public class BasicValidateCommand implements Callable<Integer> {

    @Parameters(arity = "1..*", description = "One or more phenopacket files")
    public List<Path> phenopackets;

    @Option(names = "--rare", description = "Apply HPO rare-disease constraints")
    public boolean rareHpoConstraints = false;

    @Option(names ="--hpo", description = "Path to hp.json file")
    public Path hpJsonPath;

    @Override
    public Integer call() {
        JsonSchemaValidator requirementsValidator = JsonSchemaValidatorImpl.makeGenericJsonValidator();
        ValidationWorkflowRunner<PhenopacketOrBuilder> runner = new JsonSchemaValidationWorkflowRunner<>(
                PhenopacketFormatConverters.phenopacketConverter(),
                requirementsValidator,
                List.of());


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

    private void printSeparator() {
        if (phenopackets.size() > 1) {
            System.out.println("---");
        }
    }

}