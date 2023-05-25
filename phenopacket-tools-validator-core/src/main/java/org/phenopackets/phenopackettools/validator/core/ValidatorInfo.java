package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A description of a {@link PhenopacketValidator}.
 */
@JsonSerialize(as = ValidatorInfo.class)
@JsonPropertyOrder({"validatorId", "validatorName", "description"})
public interface ValidatorInfo {

    static ValidatorInfo baseSyntaxValidation() {
        return ValidatorInfoDefault.BASE;
    }

    /**
     * Get {@link ValidatorInfo} to represent anonymous validator to associate with errors related to data format
     * errors.
     * <p>
     * The {@link ValidatorInfo} can be used if e.g. provided data is not a valid JSON but a valid JSON is expected.
     */
    static ValidatorInfo inputValidator() { return ValidatorInfoDefault.INPUT_VALIDATOR; }


    static ValidatorInfo of(String validatorId, String validatorName, String description) {
        return new ValidatorInfoDefault(validatorId, validatorName, description);
    }

    /**
     * @return string with a unique validator ID.
     */
    @JsonGetter
    String validatorId();

    /**
     * @return human-friendly validator name.
     */
    @JsonGetter
    String validatorName();

    /**
     * @return brief description of the validation provided by the validator.
     */
    @JsonGetter
    String description();

}
