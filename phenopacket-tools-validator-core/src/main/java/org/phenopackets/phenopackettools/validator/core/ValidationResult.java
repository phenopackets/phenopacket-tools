package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.phenopackettools.validator.core.errors.InputError;
import org.phenopackets.phenopackettools.validator.core.errors.OntologyError;

public interface ValidationResult {


    ValidatorInfo validationInfo();

    ValidationLevel level();

    //String category();
    String category();
    String message();


    static ValidationResult ontologyError(String message) {
        return OntologyError.of(message);
    }

    static ValidationResult inputError(String message) {
        return new InputError(message);
    }

}
