package org.phenopackets.phenotools.validator.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ValidatorRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorRunner.class);

    private final PhenopacketValidatorFactory validatorFactory;

    public ValidatorRunner(PhenopacketValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    public List<ValidationItem> validate(InputStream inputStream, List<ValidatorInfo> validations) {
        List<ValidationItem> items = new ArrayList<>();
        try {
            byte[] content = inputStream.readAllBytes();
            for (ValidatorInfo validationType : validations) {
                validatorFactory.getValidatorForType(validationType)
                        .map(validate(content))
                        .ifPresent(items::addAll);
            }
            return List.copyOf(items);

        } catch (IOException e) {
            LOGGER.error("Error occurred during validation {}", e.getMessage(), e);
        }
        return List.of();
    }

    private static Function<PhenopacketValidator, List<ValidationItem>> validate(byte[] content) {
        return validator -> {
            try (InputStream is = new ByteArrayInputStream(content)) {
                return validator.validate(is);
            } catch (IOException e) {
                LOGGER.error("Error occurred during validation {}", e.getMessage(), e);
            }
            return List.of();
        };
    }


}
