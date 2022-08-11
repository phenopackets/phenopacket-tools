package org.phenopackets.phenopackettools.validator.jsonschema;

import com.networknt.schema.ValidationMessage;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

import java.util.Objects;

/**
 * POJO to represent errors identified by JSON Schema validation.
 *
 * @author Peter N Robinson
 */
public final class JsonValidationError implements ValidationResult  {

    private final ValidatorInfo validatorInfo;
    private final ValidationLevel level;
    private final String category;
    private final String message;


    public JsonValidationError(ValidatorInfo validatorInfo, ValidationMessage validationMessage) {
        this.validatorInfo = validatorInfo;
        this.category = validationMessage.getType();
        this.message = validationMessage.getMessage();
        this.level = ValidationLevel.VALIDATION_ERROR;
    }


    @Override
    public ValidatorInfo validationInfo() {
        return validatorInfo;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonValidationError that = (JsonValidationError) o;
        return Objects.equals(validatorInfo, that.validatorInfo) &&
                this.level.equals(that.level) &&
                this.category.equals(that.category) &&
                this.message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validatorInfo, level, category, message);
    }

    @Override
    public String toString() {
        return "JsonValidationError{" +
                "validatorInfo='" + validatorInfo + '\'' +
                "category='" + category + '\'' +
                "message='" + message + '\'' +
                ", level=" + level +
                '}';
    }



    @Override
    public ValidationLevel level() {
        return ValidationLevel.VALIDATION_ERROR;
    }

    @Override
    public String category() {
        return this.category;
    }

    @Override
    public String message() {
        return this.message;
    }
}
