package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.phenopackettools.core.PhenopacketToolsException;

/**
 * A {@link PhenopacketToolsException} that is thrown by {@link org.phenopackets.phenopackettools.validator.core.convert.PhenopacketConverter}
 * in case the provided data has incorrect format.
 * <p>
 * This can happen if e.g. the {@code payload} to
 * {@link org.phenopackets.phenopackettools.validator.core.convert.PhenopacketConverter#toJson(byte[])}
 * is not valid JSON.
 * <p>
 * {@code ConversionException} implements {@link ValidationResult} so that it can be reported
 * by a {@link PhenopacketValidator}.
 */
public class ConversionException extends PhenopacketToolsException implements ValidationResult {

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
