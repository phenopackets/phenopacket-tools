package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * {@code ValidationResult} contains results of a single validation step performed by a {@link PhenopacketValidator}.
 */
@JsonSerialize(as = ValidationResult.class)
@JsonPropertyOrder({"validatorInfo", "level", "category", "message"})
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
    @JsonGetter
    ValidatorInfo validatorInfo();

    /**
     * @return level of the validation
     */
    @JsonGetter
    ValidationLevel level();

    /**
     * @return an error category.
     */
    @JsonGetter
    String category();

    /**
     * @return specific error message
     */
    @JsonGetter
    String message();

}
