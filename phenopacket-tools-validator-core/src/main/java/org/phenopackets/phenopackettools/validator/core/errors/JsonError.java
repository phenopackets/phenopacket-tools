package org.phenopackets.phenopackettools.validator.core.errors;

import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

public class JsonError implements ValidationResult {

    public static final String CATEGORY = "JSON";
    /** JSON schema error meaning that the JSON code contained a property not present in the schema. */
    public static final String ADDITIONAL_PROPERTIES = "additionalProperties";
    /** JSON schema error meaning that the JSON code failed to contain a property required by the schema. */
    public static final String REQUIRED = "required";
    /** Another kind of JSON error. */
    public static final String UNKNOWN = "unknown";

    private final String category;
    private final String message;


    public JsonError(String subcategory, String message) {
        this.category = subcategory;
        this.message = message;
    }

    @Override
    public ValidatorInfo validationInfo() {
        return null;
    }

    @Override
    public ValidationLevel level() {
        return null;
    }

    @Override
    public String category() {
        return this.category;
    }

    @Override
    public String message() {
        return this.message;
    }



    public static JsonError additionalProperties(String message) {
        return new JsonError(ADDITIONAL_PROPERTIES, message);
    }

    public static JsonError required(String message) {
        return new JsonError(REQUIRED, message);
    }

    public static JsonError unknown(String message) {
        return new JsonError(UNKNOWN, message);
    }
}
