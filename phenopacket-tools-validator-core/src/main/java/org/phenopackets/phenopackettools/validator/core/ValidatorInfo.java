package org.phenopackets.phenopackettools.validator.core;

/**
 * Information regarding validator.
 */
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
    String validatorId();

    /**
     * @return human-friendly validator name.
     */
    String validatorName();

    /**
     * @return brief description of the validation provided by the validator.
     */
    String description();

}
