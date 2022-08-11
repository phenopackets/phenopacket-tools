package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.phenopackettools.validator.core.errors.OntologyError;

public interface ValidationErrorType {

    String category();
    String subcategory();
    String message();


    static ValidationErrorType ontologyError(String message) {
        return OntologyError.of(message);
    }

}
