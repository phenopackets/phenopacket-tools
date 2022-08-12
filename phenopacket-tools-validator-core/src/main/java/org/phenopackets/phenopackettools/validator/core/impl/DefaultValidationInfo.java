package org.phenopackets.phenopackettools.validator.core.impl;

import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

import java.util.Objects;

public class DefaultValidationInfo implements ValidatorInfo {

    private static final DefaultValidationInfo GENERIC = of("GENERIC", "Validation of a generic Phenopacket");
    private static final DefaultValidationInfo RARE_DISEASE_VALIDATOR = of("RARE_DISEASE_VALIDATOR", "Validation of rare disease Phenopacket constraints");

    private static final DefaultValidationInfo INPUT_VALIDATOR = of("Input", "Input of phenopacket data");


    public static ValidatorInfo generic() {
        return GENERIC;
    }

    public static ValidatorInfo rareDiseaseValidator() {
        return RARE_DISEASE_VALIDATOR;
    }

    public static ValidatorInfo inputValidator() { return INPUT_VALIDATOR; }

    private final String validatorId;
    private final String validatorName;

    public static DefaultValidationInfo of(String validatorId, String validatorName) {
        return new DefaultValidationInfo(validatorId, validatorName);
    }

    private DefaultValidationInfo(String validationId, String validatorName) {
        this.validatorId = validationId;
        this.validatorName = validatorName;
    }

    @Override
    public String validatorId() {
        return validatorId;
    }

    @Override
    public String validatorName() {
        return validatorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultValidationInfo that = (DefaultValidationInfo) o;
        return Objects.equals(validatorId, that.validatorId) && Objects.equals(validatorName, that.validatorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validatorId, validatorName);
    }

    @Override
    public String toString() {
        return "DefaultValidationInfo{" +
                "validatorId='" + validatorId + '\'' +
                ", validatorName='" + validatorName + '\'' +
                '}';
    }
}
