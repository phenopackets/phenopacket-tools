package org.phenopackets.phenopackettools.validator.core;

record ValidatorInfoDefault(String validatorId,
                            String validatorName,
                            String description) implements ValidatorInfo {
    // TODO - add descriptions
    static final ValidatorInfoDefault GENERIC = new ValidatorInfoDefault("GENERIC", "Validation of a generic Phenopacket", "");
    static final ValidatorInfoDefault RARE_DISEASE_VALIDATOR = new ValidatorInfoDefault("RARE_DISEASE_VALIDATOR", "Validation of rare disease Phenopacket constraints", "");
    static final ValidatorInfoDefault INPUT_VALIDATOR = new ValidatorInfoDefault("Input", "Input of phenopacket data", "Validation of data format");

}
