package org.phenopackets.phenopackettools.validator.core.errors;

import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

public class OntologyValidationResult implements ValidationResult {


   private final ValidatorInfo info;
    private final String message;

    private final String category;
    private final ValidationLevel level;


    private OntologyValidationResult(ValidatorInfo info,String category, ValidationLevel level, String message) {
        this.info = info;
        this.category = category;
        this.level = level;
        this.message = message;
    }


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
