package org.phenopackets.phenopackettools.command;


import org.phenopackets.phenopackettools.validator.core.PhenopacketValidatorFactory;
import org.phenopackets.phenopackettools.validator.core.ValidationItem;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.ValidatorRunner;
import org.phenopackets.phenopackettools.validator.jsonschema.ClasspathJsonSchemaValidatorFactory;
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

@Command(name = "validate",
        mixinStandardHelpOptions = true)
public class ValidateCommand implements Callable<Integer> {

    @Parameters(arity = "1..*", description = "One or more phenopacket files")
    public List<Path> phenopackets;

    @Option(names = "--rare", description = "Apply HPO rare-disease constraints")
    public boolean rareHpoConstraints = false;

    @Override
    public Integer call() {
        // What type of validation do we run?
        List<ValidatorInfo> validationTypes = new ArrayList<>();
        validationTypes.add(ValidatorInfo.generic()); // we run this by default
        if (rareHpoConstraints) {
            validationTypes.add(ValidatorInfo.rareDiseaseValidation());
        }

        PhenopacketValidatorFactory phenopacketValidatorFactory = ClasspathJsonSchemaValidatorFactory.defaultValidators();
        ValidatorRunner validatorRunner = new ValidatorRunner(List.of(), List.of());

        for (Path phenopacket : phenopackets) {
            try (InputStream in = Files.newInputStream(phenopacket)) {
                List<ValidationItem> validationItems = validatorRunner.validate(in, List.of());
                Path fileName = phenopacket.getFileName();
                if (validationItems.isEmpty()) {
                    System.out.printf("%s - OK%n", fileName);
                    printSeparator();
                } else {
                    for (ValidationItem item : validationItems) {
                        System.out.printf("%s - (%s) %s%n", fileName, item.errorType(), item.message());
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