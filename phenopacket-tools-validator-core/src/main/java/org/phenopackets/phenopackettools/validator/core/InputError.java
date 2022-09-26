package org.phenopackets.phenopackettools.validator.core;

/**
 * {@link ValidationResult} returned when encountering a format error.
 * @param message message to present the user.
 */
record InputError(String message) implements ValidationResult {

    private static final String VALIDATION_CATEGORY = "input";


    @Override
    public ValidatorInfo validatorInfo() {
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
