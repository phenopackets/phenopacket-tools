package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class JsonSchemaValidatorImpl implements JsonSchemaValidator {

    /**
     * The latest version of the spec that is supported by our JSON SCHEMA library is 2019/09.
     */
    private static final SpecVersion.VersionFlag VERSION_FLAG = SpecVersion.VersionFlag.V201909;
    private final JsonSchema jsonSchema;
    private final ValidatorInfo validatorInfo;


    public static JsonSchemaValidator makeGenericJsonValidator() {
        String schemaPath = "phenopacket-schema-2-0.json";
        try (InputStream is = JsonSchemaValidatorImpl.class.getResourceAsStream(schemaPath)) {
            return JsonSchemaValidatorImpl.of(is, ValidatorInfo.genericJsonSchema());
        } catch (IOException e) {
            throw new PhenopacketValidatorRuntimeException("Invalid JSON schema specification: " + e.getMessage());
        }
    }

    /**
     *
     *
     * @param jsonSchema path to JSON schema specification file
     * @throws PhenopacketValidatorRuntimeException if <code>jsonSchema</code> is not a valid file or if the file is
     *                                              not a valid JSON schema specification
     */
    public static JsonSchemaValidator of(Path jsonSchema, ValidatorInfo validatorInfo) throws PhenopacketValidatorRuntimeException {
        try (InputStream inputStream = Files.newInputStream(jsonSchema)) {
            return of(inputStream, validatorInfo);
        } catch (Exception e) {
            throw new PhenopacketValidatorRuntimeException("Invalid JSON schema specification: " + e.getMessage());
        }
    }

    public static JsonSchemaValidator of(InputStream inputStream, ValidatorInfo validatorInfo) {
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(VERSION_FLAG);
        return new JsonSchemaValidatorImpl(schemaFactory.getSchema(inputStream), validatorInfo);
    }

    private JsonSchemaValidatorImpl(JsonSchema jsonSchema, ValidatorInfo validatorInfo) {
        this.jsonSchema = Objects.requireNonNull(jsonSchema);
        this.validatorInfo = Objects.requireNonNull(validatorInfo);
    }
    
    @Override
    public ValidatorInfo validatorInfo() {
        return validatorInfo;
    }

    @Override
    public List<ValidationResult> validate(JsonNode node) {
        return jsonSchema.validate(node).stream()
                .map(res -> ValidationResult.error(validatorInfo, res.getType(), res.getMessage()))
                .collect(Collectors.toList());
    }

}
