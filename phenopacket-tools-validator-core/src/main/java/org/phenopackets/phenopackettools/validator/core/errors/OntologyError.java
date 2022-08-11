package org.phenopackets.phenopackettools.validator.core.errors;

import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

public record OntologyError(String category,
                            String subcategory,
                            String message) implements ValidationResult {


   //

    public static OntologyError of(String message) {
        return new OntologyError("invalid ontology", "todo-subcat", message);
    }


    @Override
    public ValidatorInfo validationInfo() {
        return null;
    }

    @Override
    public ValidationLevel level() {
        return null;
    }
}
