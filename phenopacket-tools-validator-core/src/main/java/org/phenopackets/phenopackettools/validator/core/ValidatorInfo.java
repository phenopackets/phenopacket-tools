package org.phenopackets.phenopackettools.validator.core;

public interface ValidatorInfo {

    static ValidatorInfo generic() {
        return DefaultValidationInfo.generic();
    }

    /**
     * This class implements additional validation of a phenopacket that is intended to be used
     * for HPO rare disease phenotyping. By assumption, the phenopacket will have been first
     * checked against the {@link ValidatorInfo#generic()} specification. This class performs validation with the
     * file {@code hpo-rare-disease-schema.json}.
     */
    static ValidatorInfo rareDiseaseValidation() {
        return DefaultValidationInfo.rareDiseaseValidator();
    }

    static ValidatorInfo of(String validatorId, String validatorName) {
        return DefaultValidationInfo.of(validatorId, validatorName);
    }

    String validatorId();

    String validatorName();

    int hashCode();

    boolean equals(Object o);
}
