package org.phenopackets.phenopackettools.validator.core.impl;

import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

import java.util.Objects;

public class DefaultValidationInfo implements ValidatorInfo {

    // TODO - add descriptions
    private static final DefaultValidationInfo GENERIC = of("GENERIC", "Validation of a generic Phenopacket", "");
    private static final DefaultValidationInfo RARE_DISEASE_VALIDATOR = of("RARE_DISEASE_VALIDATOR", "Validation of rare disease Phenopacket constraints", "");
    private static final DefaultValidationInfo INPUT_VALIDATOR = of("Input", "Input of phenopacket data", "");


    public static ValidatorInfo generic() {
        return GENERIC;
    }

    public static ValidatorInfo rareDiseaseValidator() {
        return RARE_DISEASE_VALIDATOR;
    }

    public static ValidatorInfo inputValidator() { return INPUT_VALIDATOR; }

    private final String validatorId;
    private final String validatorName;
    private final String description;

    public static DefaultValidationInfo of(String validationId, String validatorName, String description) {
        return new DefaultValidationInfo(validationId, validatorName, description);
    }

    private DefaultValidationInfo(String validationId, String validatorName, String description) {
        this.validatorId = validationId;
        this.validatorName = validatorName;
        this.description = description;
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
    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultValidationInfo that = (DefaultValidationInfo) o;
        return Objects.equals(validatorId, that.validatorId) && Objects.equals(validatorName, that.validatorName) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validatorId, validatorName, description);
    }

    @Override
    public String toString() {
        return "DefaultValidationInfo{" +
                "validatorId='" + validatorId + '\'' +
                ", validatorName='" + validatorName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
