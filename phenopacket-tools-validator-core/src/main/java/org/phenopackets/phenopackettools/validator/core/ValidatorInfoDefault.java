package org.phenopackets.phenopackettools.validator.core;

record ValidatorInfoDefault(String validatorId,
                            String validatorName,
                            String description) implements ValidatorInfo {
    static final ValidatorInfoDefault BASE = new ValidatorInfoDefault("Base", "Base syntax validator", "The base syntax validation of a phenopacket, family, or cohort");
    static final ValidatorInfoDefault INPUT_VALIDATOR = new ValidatorInfoDefault("Input", "Data format validator", "The validator for checking data format issues (e.g. presence of a required field in JSON document)");

}
