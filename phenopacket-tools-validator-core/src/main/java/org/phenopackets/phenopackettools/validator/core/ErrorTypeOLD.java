package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorRuntimeException;

@Deprecated(forRemoval = true)
public enum ErrorTypeOLD {
    /** JSON schema error meaning that the JSON code contained a property not present in the schema. */
    JSON_ADDITIONAL_PROPERTIES("additionalProperties"),
    /** JSON schema error meaning that the JSON code failed to contain a property required by the schema. */
    JSON_REQUIRED("required"),
    /** The type of an object is not as required by the schema, e.g., we get a string instead of an array. */
    JSON_TYPE("type"),
    SUBJECT_MISSING_AGE("subject missing age"),
    MISSING_SUBJECT("missing subject"),
    INVALID_ONTOLOGY("invalid ontology"),
    MISSING_PHENOTYPIC_FEATURE("missing phenotypic feature");

    private final String name;


    ErrorTypeOLD(String value) {
        this.name = value;
    }

    @Override
    public String toString() {
        return this.name;
    }


    public static ErrorTypeOLD stringToErrorType(String error) {
        switch (error) {
            case "additionalProperties": return JSON_ADDITIONAL_PROPERTIES;
            case "required": return JSON_REQUIRED;
            case "type": return JSON_TYPE;

            default:
                throw new PhenopacketValidatorRuntimeException("Did not recognize error type: \"" + error + "\"");
        }
    }
}
