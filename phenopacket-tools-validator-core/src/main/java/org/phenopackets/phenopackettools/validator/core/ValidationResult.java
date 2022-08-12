package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.phenopackettools.validator.core.errors.InputError;
import org.phenopackets.phenopackettools.validator.core.errors.OntologyValidationResult;

public interface ValidationResult {


    ValidatorInfo validationInfo();

    ValidationLevel level();

    //String category();
    String category();
    String message();



    static ValidationResult inputError(String message) {
        return new InputError(message);
    }

}
