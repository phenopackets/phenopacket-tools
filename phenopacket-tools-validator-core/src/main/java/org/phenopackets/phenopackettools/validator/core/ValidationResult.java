package org.phenopackets.phenopackettools.validator.core;

public interface ValidationResult {

    static ValidationResult warning(ValidatorInfo validatorInfo,
                                    String category,
                                    String message) {
        return of(validatorInfo, ValidationLevel.WARNING, category, message);
    }

    static ValidationResult error(ValidatorInfo validatorInfo,
                                  String category,
                                  String message) {
        return of(validatorInfo, ValidationLevel.ERROR, category, message);
    }

    static ValidationResult of(ValidatorInfo validatorInfo,
                               ValidationLevel level,
                               String category,
                               String message) {
        return new ValidationResultDefault(validatorInfo, level, category, message);
    }

    /**
     * @return information about the validator used to create the {@link ValidationResult}.
     */
    ValidatorInfo validatorInfo();

    /**
     * @return level of the validation
     */
    ValidationLevel level();

    /**
     * @return an error category.
     */
    String category();

    /**
     * @return specific error message
     */
    String message();

}
