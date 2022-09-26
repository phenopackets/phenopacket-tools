package org.phenopackets.phenopackettools.validator.core;

/**
 * An {@link Exception} that is thrown in case the provided data has incorrect format.
 */
public class ConversionException extends Exception implements ValidationResult {

    private static final String VALIDATION_CATEGORY = "input";

    public ConversionException() {
        super();
    }

    public ConversionException(Throwable cause) {
        super(cause);
    }

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

    @Override
    public String message() {
        return getMessage();
    }
}
