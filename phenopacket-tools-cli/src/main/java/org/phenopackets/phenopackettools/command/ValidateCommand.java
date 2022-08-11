package org.phenopackets.phenopackettools.command;


import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.DefaultValidatorRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

import static org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidator.makeGenericJsonValidator;

@Command(name = "validate",
        mixinStandardHelpOptions = true)
public class ValidateCommand implements Callable<Integer> {

    @Parameters(arity = "1..*", description = "One or more phenopacket files")
    public List<Path> phenopackets;

    @Option(names = "--rare", description = "Apply HPO rare-disease constraints")
    public boolean rareHpoConstraints = false;

    @Override
    public Integer call() {
        var jsonValidators = List.of(makeGenericJsonValidator());
        DefaultValidatorRunner validatorRunner = new DefaultValidatorRunner(jsonValidators, List.of());
        List<? extends PhenopacketValidator> messageValidators = List.of();
        if (rareHpoConstraints) {
            // add HPO validator
        }

        for (Path phenopacket : phenopackets) {
            try (InputStream in = Files.newInputStream(phenopacket)) {
                List<ValidationResult> validationItems = validatorRunner.validate(in);
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