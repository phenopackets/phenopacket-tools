package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code ValidationResults} contain validation results for one Phenopacket schema <em>top-level element</em>
 * ({@link org.phenopackets.schema.v2.Phenopacket}, {@link org.phenopackets.schema.v2.Family},
 * or {@link org.phenopackets.schema.v2.Cohort}).
 * <p>
 * The results contain info regarding which validators were run ({@link #validators()}) and the issues found during
 * the validation ({@link #validationResults()}).
 */
@JsonSerialize(as = ValidationResults.class)
@JsonPropertyOrder({"validators", "validationResults"})
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
    @JsonGetter
    List<ValidatorInfo> validators();

    /**
     * @return a list of {@link ValidationResult} representing the issues found in the top-level element.
     */
    @JsonGetter
    List<ValidationResult> validationResults();

    /**
     * @return {@code true} if no issues have been found and the validated item is valid.
     */
    @JsonIgnore
    default boolean isValid() {
        return validationResults().isEmpty();
    }

    /**
     * A builder for creating {@link ValidationResults}.
     */
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
