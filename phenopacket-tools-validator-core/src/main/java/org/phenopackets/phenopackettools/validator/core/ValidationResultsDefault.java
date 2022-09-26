package org.phenopackets.phenopackettools.validator.core;

import java.util.List;

record ValidationResultsDefault(List<ValidatorInfo> validators,
                                List<ValidationResult> validationResults) implements ValidationResults {
    static ValidationResultsDefault EMPTY = new ValidationResultsDefault(List.of(), List.of());

}
