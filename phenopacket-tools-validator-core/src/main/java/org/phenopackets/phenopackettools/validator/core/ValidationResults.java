package org.phenopackets.phenopackettools.validator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ValidationResults} contain validation results for one Phenopacket schema <em>top-level element</em>
 * (phenopacket, family, or cohort).
 * The results contain info regarding which validators were run and the issues found during the validation.
 */
public interface ValidationResults {

    static ValidationResults of(List<ValidatorInfo> validators,
                                List<ValidationResult> validationResults) {
        if (validators.isEmpty() && validationResults.isEmpty())
            return empty();
        return new ValidationResultsDefault(validators, validationResults);
    }

    static ValidationResults empty() {
        return ValidationResultsDefault.EMPTY;
    }

    static Builder builder() {
        return new Builder();
    }

    /**
     * @return a list of {@link ValidatorInfo} representing validators applied to the top-level element.
     */
    List<ValidatorInfo> validators();

    /**
     * @return a list of {@link ValidationResult} representing the issues found in the top-level element.
     */
    List<ValidationResult> validationResults();

    class Builder {

        private final List<ValidatorInfo> validators = new ArrayList<>();
        private final List<ValidationResult> validationResults = new ArrayList<>();

        private Builder(){
            // private no-op
        }

        public Builder addResult(ValidatorInfo info, ValidationResult result) {
            this.validators.add(info);
            this.validationResults.add(result);
            return this;
        }

        public Builder addResults(ValidatorInfo info, List<ValidationResult> results) {
            this.validators.add(info);
            this.validationResults.addAll(results);
            return this;
        }

        public ValidationResults build() {
            return ValidationResults.of(validators, validationResults);
        }

    }
}
