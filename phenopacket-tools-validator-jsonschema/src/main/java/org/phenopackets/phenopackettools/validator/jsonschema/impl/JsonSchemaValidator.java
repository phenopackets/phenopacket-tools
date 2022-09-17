package org.phenopackets.phenopackettools.validator.jsonschema.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link JsonSchemaValidator} applies a single {@link JsonSchema}
 * to the provided {@link JsonNode} and converts any errors into {@link ValidationResult}s
 * with {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR} level.
 */
public class JsonSchemaValidator {

    private final JsonSchema jsonSchema;
    private final ValidatorInfo validatorInfo;

    public JsonSchemaValidator(JsonSchema jsonSchema, ValidatorInfo validatorInfo) {
        this.jsonSchema = Objects.requireNonNull(jsonSchema);
        this.validatorInfo = Objects.requireNonNull(validatorInfo);
    }

    public ValidatorInfo validatorInfo() {
        return validatorInfo;
    }

    public List<ValidationResult> validate(JsonNode node) {
        return jsonSchema.validate(node).stream()
                .map(res -> ValidationResult.error(validatorInfo, res.getType(), res.getMessage()))
                .collect(Collectors.toList());
    }

}
