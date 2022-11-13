package org.phenopackets.phenopackettools.validator.core;

/**
 * {@code ValidationResult} contains results of a single validation step performed by a {@link PhenopacketValidator}.
 */
public interface ValidationResult {

    /**
     * Create a {@link ValidationLevel#WARNING} result from given data.
     */
    static ValidationResult warning(ValidatorInfo validatorInfo,
                                    String category,
                                    String message) {
        return of(validatorInfo, ValidationLevel.WARNING, category, message);
    }

    /**
     * Create a {@link ValidationLevel#ERROR} result from given data.
     */
    static ValidationResult error(ValidatorInfo validatorInfo,
                                  String category,
                                  String message) {
        return of(validatorInfo, ValidationLevel.ERROR, category, message);
    }

    /**
     * Create a {@code ValidationResult} from given data.
     */
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
