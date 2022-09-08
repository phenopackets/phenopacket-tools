package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.phenopackettools.validator.core.impl.DefaultValidationInfo;

/**
 * Information regarding validator.
 */
public interface ValidatorInfo {

    static ValidatorInfo genericJsonSchema() {
        return DefaultValidationInfo.generic();
    }

    /**
     * This class implements additional validation of a phenopacket that is intended to be used
     * for HPO rare disease phenotyping. By assumption, the phenopacket will have been first
     * checked against the {@link ValidatorInfo#genericJsonSchema()} specification. This class performs validation with the
     * file {@code hpo-rare-disease-schema.json}.
     */
    static ValidatorInfo rareDiseaseValidation() {
        return DefaultValidationInfo.rareDiseaseValidator();
    }

    static ValidatorInfo inputValidator() { return DefaultValidationInfo.inputValidator(); }


    static ValidatorInfo of(String validatorId, String validatorName, String description) {
        return DefaultValidationInfo.of(validatorId, validatorName, description);
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
