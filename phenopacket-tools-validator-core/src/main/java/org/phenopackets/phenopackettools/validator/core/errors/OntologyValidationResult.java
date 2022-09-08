package org.phenopackets.phenopackettools.validator.core.errors;

import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

/**
 * A class to represent the result of validation by an Ontology (semantic)
 * validator
 * @param info Information about the validator used to create the error message
 * @param category an error category
 * @param level Error or Warning?
 * @param message Specific error message
 * @author Peter N Robinson
 */
public record OntologyValidationResult(ValidatorInfo info,
                                       String category,
                                       ValidationLevel level,
                                       String message) implements ValidationResult {




    @Override
    public ValidatorInfo validationInfo() {
        return this.info;
    }

    @Override
    public ValidationLevel level() {
        return this.level;
    }

    @Override
    public String category() {
        return this.category;
    }

    @Override
    public String message() {
        return this.message;
    }


    public static OntologyValidationResult invalidTermId(ValidatorInfo info, String msg) {
        return new OntologyValidationResult(info, "invalid TermId",
                ValidationLevel.VALIDATION_ERROR, msg);
    }

    public static  OntologyValidationResult obsoletedTermId(ValidatorInfo info, String msg) {
        return new OntologyValidationResult(info, "obsoleted TermId",
                ValidationLevel.VALIDATION_WARNING, msg);
    }



}
