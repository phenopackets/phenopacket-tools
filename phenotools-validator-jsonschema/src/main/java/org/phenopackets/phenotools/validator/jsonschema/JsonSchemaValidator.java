package org.phenopackets.phenotools.validator.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.phenopackets.phenotools.validator.core.PhenopacketValidator;
import org.phenopackets.phenotools.validator.core.ValidatorInfo;
import org.phenopackets.phenotools.validator.core.except.PhenopacketValidatorRuntimeException;
import org.phenopackets.phenotools.validator.core.ValidationItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class JsonSchemaValidator implements PhenopacketValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemaValidator.class);

    /**
     * The latest version of the spec that is supported by our JSON SCHEMA library is 2019/09.
     */
    private static final SpecVersion.VersionFlag VERSION_FLAG = SpecVersion.VersionFlag.V201909;
    private final JsonSchema jsonSchema;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ValidatorInfo validatorInfo;


    /**
     * @param jsonSchema path to JSON schema specification file
     * @throws PhenopacketValidatorRuntimeException if <code>jsonSchema</code> is not a valid file or if the file is
     *                                              not a valid JSON schema specification
     */
    public static JsonSchemaValidator of(File jsonSchema, ValidatorInfo validatorInfo) {
        if (!jsonSchema.isFile()) {
            throw new PhenopacketValidatorRuntimeException("Could not open file at \"" + jsonSchema.getAbsolutePath() + "\"");
        }
        LOGGER.debug("Reading JSON schema from `{}`", jsonSchema.getAbsolutePath());
        try (InputStream inputStream = Files.newInputStream(jsonSchema.toPath())) {
            return of(inputStream, validatorInfo);
        } catch (Exception e) {
            throw new PhenopacketValidatorRuntimeException("Invalid JSON schema specification: " + e.getMessage());
        }
    }

    public static JsonSchemaValidator of(InputStream inputStream, ValidatorInfo validatorInfo) {
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(VERSION_FLAG);
        return new JsonSchemaValidator(schemaFactory.getSchema(inputStream), validatorInfo);
    }

    private JsonSchemaValidator(JsonSchema jsonSchema, ValidatorInfo validatorInfo) {
        this.jsonSchema = jsonSchema;
        this.validatorInfo = validatorInfo;
    }

    @Override
    public ValidatorInfo info() {
        return validatorInfo;
    }

    /**
     * Validate the {@code inputStream} content (assumed to be a Phenopacket formated in JSON)
     *
     * @return List of {@link ValidationItem} objects (empty list if there were no errors)
     */
    @Override
    public List<ValidationItem> validate(InputStream inputStream) {
        try {
            JsonNode json = objectMapper.readTree(inputStream);
            return jsonSchema.validate(json).stream()
                    .map(e -> new JsonValidationError(validatorInfo, e))
                    .collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            LOGGER.warn("Error while decoding JSON content: {}", e.getMessage(), e);
        } catch (RuntimeException e) {
            LOGGER.warn("Error while validating: {}", e.getMessage());
        }
        return List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonSchemaValidator that = (JsonSchemaValidator) o;
        return Objects.equals(jsonSchema, that.jsonSchema) && Objects.equals(objectMapper, that.objectMapper) && Objects.equals(validatorInfo, that.validatorInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonSchema, objectMapper, validatorInfo);
    }
}
