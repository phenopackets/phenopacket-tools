package org.phenopackets.phenopackettools.validator.jsonschema;

import com.networknt.schema.ValidationMessage;
import org.phenopackets.phenopackettools.validator.core.ValidationErrorType;
import org.phenopackets.phenopackettools.validator.core.ValidationItem;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.errors.JsonError;

import java.util.Objects;

/**
 * POJO to represent errors identified by JSON Schema validation.
 *
 * @author Peter N Robinson
 */
public final class JsonValidationError implements ValidationItem {

    String CATEGORY = "JSON";

    private final ValidatorInfo validatorInfo;
    private final ValidationErrorType errorType;

    public JsonValidationError(ValidatorInfo validatorInfo, ValidationMessage validationMessage) {
        this.validatorInfo = validatorInfo;
        String subcat = validationMessage.getType();
        switch (subcat) {
            case JsonError.REQUIRED -> errorType = JsonError.required(validationMessage.getMessage());
            case JsonError.ADDITIONAL_PROPERTIES -> errorType = JsonError.additionalProperties(validationMessage.getMessage());
            default -> errorType = JsonError.unknown(validationMessage.getType());
        }

    }


    @Override
    public ValidatorInfo validatorInfo() {
        return validatorInfo;
    }

    @Override
    public ValidationErrorType errorType() {
        return this.errorType;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonValidationError that = (JsonValidationError) o;
        return Objects.equals(validatorInfo, that.validatorInfo) && errorType == that.errorType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(validatorInfo, errorType);
    }

    @Override
    public String toString() {
        return "JsonValidationError{" +
                "validatorInfo='" + validatorInfo + '\'' +
                ", errorType=" + errorType +
                '}';
    }
}
