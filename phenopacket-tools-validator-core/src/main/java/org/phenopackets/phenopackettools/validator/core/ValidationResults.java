package org.phenopackets.phenopackettools.validator.core;

import java.util.List;

/**
 * {@link ValidationResults} contain validation results for one Phenopacket schema <em>top-level element</em>
 * (phenopacket, family, or cohort).
 * The results contain info regarding which validators were run and the issues found during the validation.
 */
public interface ValidationResults {

    static ValidationResults of(List<ValidatorInfo> validators,
                                List<ValidationResult> validationResults) {
        return new ValidationResultsDefault(validators, validationResults);
    }

    /**
     * @return a list of {@link ValidatorInfo} representing validators applied to the top-level element.
     */
    List<ValidatorInfo> validators();

    /**
     * @return a list of {@link ValidationResult} representing the issues found in the top-level element.
     */
    List<ValidationResult> validationResults();

}
