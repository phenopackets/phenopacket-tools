package org.phenopackets.phenopackettools.validator.core.errors;

import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

public class InputError implements ValidationResult {

    private static final String VALIDATION_CATEGORY = "input";

    private final String message;

    public InputError(String message) {
        this.message = message;
    }


    @Override
    public ValidatorInfo validationInfo() {
        return ValidatorInfo.inputValidator();
    }

    @Override
    public ValidationLevel level() {
        return ValidationLevel.VALIDATION_ERROR;
    }

    @Override
    public String category() {
        return VALIDATION_CATEGORY;
    }

    @Override
    public String message() {
        return this.message;
    }

}
