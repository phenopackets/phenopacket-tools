package org.phenopackets.phenopackettools.validator.core.errors;

import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

public record InputError(String message) implements ValidationResult {

    private static final String VALIDATION_CATEGORY = "input";


    @Override
    public ValidatorInfo validationInfo() {
        return ValidatorInfo.inputValidator();
    }

    @Override
    public ValidationLevel level() {
        return ValidationLevel.ERROR;
    }

    @Override
    public String category() {
        return VALIDATION_CATEGORY;
    }

}
