package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorRuntimeException;
import org.phenopackets.schema.v2.Phenopacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class JsonSchemaValidator implements PhenopacketValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemaValidator.class);

    /**
     * The latest version of the spec that is supported by our JSON SCHEMA library is 2019/09.
     */
    private static final SpecVersion.VersionFlag VERSION_FLAG = SpecVersion.VersionFlag.V201909;
    private final JsonSchema jsonSchema;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ValidatorInfo validatorInfo;


    public static JsonSchemaValidator makeGenericJsonValidator() {
        String schemaPath = "/schema/phenopacket-schema-2-0.json";
        InputStream inputStream = JsonSchemaValidator.class.getResourceAsStream(schemaPath);
        if (inputStream == null)
            throw new PhenopacketValidatorRuntimeException("Invalid JSON schema path `" + schemaPath + '`');
        return JsonSchemaValidator.of(inputStream, ValidatorInfo.genericJsonSchema());
    }

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


    @Override
    public List<ValidationResult> validateJson(JsonNode jsonNode) {
        List<ValidationResult> results = new ArrayList<>();
        for (var res : jsonSchema.validate(jsonNode)) {
            results.add(new JsonValidationError(validatorInfo, res));
        }
        return results;
    }

    /**
     * This validator does not work on protobuf messages
     * @param message
     * @return empty list
     */
    @Override
    public List<ValidationResult> validateMessage(Phenopacket message) {
        return List.of();
    }
}
