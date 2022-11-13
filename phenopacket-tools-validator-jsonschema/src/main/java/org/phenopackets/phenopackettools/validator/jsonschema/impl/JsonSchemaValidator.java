package org.phenopackets.phenopackettools.validator.jsonschema.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * {@link JsonSchemaValidator} applies a single {@link JsonSchema}
 * to the provided {@link JsonNode} and converts any errors into {@link ValidationResult}s
 * with {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR} level.
 */
public class JsonSchemaValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemaValidator.class);
    private static final Pattern VALIDATION_MSG_PT = Pattern.compile("^\\$\\.(?<location>[\\w\\[\\].]+):(?<msg>.*)$");

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
                .flatMap(validationMessageIntoValidationResult())
                .toList();
    }

    private Function<ValidationMessage, Stream<ValidationResult>> validationMessageIntoValidationResult() {
        return res -> {
            Matcher matcher = VALIDATION_MSG_PT.matcher(res.getMessage());
            if (matcher.matches()) {
                String msg = "'%s'%s".formatted(matcher.group("location"), matcher.group("msg"));
                return Stream.of(ValidationResult.error(validatorInfo, res.getType(), msg));
            } else {
                LOGGER.warn("Non-matching validation message: {}", res.getMessage());
                return Stream.empty();
            }
        };
    }

}
