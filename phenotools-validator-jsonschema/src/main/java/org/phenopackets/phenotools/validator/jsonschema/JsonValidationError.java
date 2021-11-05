package org.phenopackets.phenotools.validator.jsonschema;

import com.networknt.schema.ValidationMessage;
import org.phenopackets.phenotools.validator.core.ErrorType;
import org.phenopackets.phenotools.validator.core.ValidationItem;
import org.phenopackets.phenotools.validator.core.ValidatorInfo;

import java.util.Objects;

/**
 * POJO to represent errors identified by JSON Schema validation.
 *
 * @author Peter N Robinson
 */
public final class JsonValidationError implements ValidationItem {

    private final ValidatorInfo validatorInfo;
    private final ErrorType errorType;
    private final String message;

    public JsonValidationError(ValidatorInfo validatorInfo, ValidationMessage validationMessage) {
        this.validatorInfo = validatorInfo;
        this.errorType = ErrorType.stringToErrorType(validationMessage.getType());
        this.message = validationMessage.getMessage();
    }

    @Override
    public ValidatorInfo validatorInfo() {
        return validatorInfo;
    }

    @Override
    public ErrorType errorType() {
        return this.errorType;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonValidationError that = (JsonValidationError) o;
        return Objects.equals(validatorInfo, that.validatorInfo) && errorType == that.errorType && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validatorInfo, errorType, message);
    }

    @Override
    public String toString() {
        return "JsonValidationError{" +
                "validatorInfo='" + validatorInfo + '\'' +
                ", errorType=" + errorType +
                ", message='" + message + '\'' +
                '}';
    }
}
