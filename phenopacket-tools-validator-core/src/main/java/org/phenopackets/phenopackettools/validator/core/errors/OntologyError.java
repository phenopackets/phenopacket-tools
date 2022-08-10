package org.phenopackets.phenopackettools.validator.core.errors;

import org.phenopackets.phenopackettools.validator.core.ValidationErrorType;

public record OntologyError(String category,
                            String subcategory,
                            String message) implements ValidationErrorType {


   //

    public static OntologyError of(String message) {
        return new OntologyError("invalid ontology", "todo-subcat", message);
    }


}
