package org.phenopackets.phenopackettools.validator.jsonschema.v2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorRuntimeException;
import org.phenopackets.phenopackettools.validator.jsonschema.impl.JsonSchemaValidator;

import java.io.IOException;
import java.io.InputStream;

/**
 * Static utility class that contains logic for configuring a {@link JsonSchemaValidator} from an {@link InputStream}
 * for JSON schema document.
 */
public class JsonSchemaValidatorConfigurer {

    private static final String PHENOPACKET_SCHEMA_PATH = "phenopacket-schema.json";
    private static final String FAMILY_SCHEMA_PATH = "family-schema.json";
    private static final String COHORT_SCHEMA_PATH = "cohort-schema.json";

    /**
     * The latest version of the spec that is supported by our JSON SCHEMA library is 2019/09.
     */
    private static final SpecVersion.VersionFlag VERSION_FLAG = SpecVersion.VersionFlag.V201909;

    private static volatile ObjectMapper OBJECT_MAPPER = null;
    private static volatile JsonSchemaFactory SCHEMA_FACTORY = null;

    // Singletons for validation of base Phenopacket schema requirements.
    private static volatile JsonSchemaValidator BASE_PHENOPACKET_VALIDATOR = null;
    private static volatile JsonSchemaValidator BASE_FAMILY_VALIDATOR = null;
    private static volatile JsonSchemaValidator BASE_COHORT_VALIDATOR = null;

    private JsonSchemaValidatorConfigurer() {
        // static utility class
    }

    public static JsonSchemaValidator configureJsonSchemaValidator(InputStream is) throws IOException {
        JsonSchemaNodeAndInfo jsonSchemaAndInfo = readSchemaAndInfo(is);
        return configureSingletonSchemaValidator(jsonSchemaAndInfo);
    }

    public static JsonSchemaValidator getBasePhenopacketValidator() {
        if (BASE_PHENOPACKET_VALIDATOR == null) { // double-check locking
            synchronized (JsonSchemaValidatorConfigurer.class) {
                if (BASE_PHENOPACKET_VALIDATOR == null) {
                    BASE_PHENOPACKET_VALIDATOR = configureSingletonSchemaValidator(phenopacketJsonSchemaAndInfo());
                }
            }
        }

        return BASE_PHENOPACKET_VALIDATOR;
    }

    public static JsonSchemaValidator getBaseFamilyValidator() {
        if (BASE_FAMILY_VALIDATOR == null) {
            synchronized (JsonSchemaValidatorConfigurer.class) {
                if (BASE_FAMILY_VALIDATOR == null) {
                    BASE_FAMILY_VALIDATOR = configureSingletonSchemaValidator(familyJsonSchemaAndInfo());
                }
            }
        }

        return BASE_FAMILY_VALIDATOR;
    }

    public static JsonSchemaValidator getBaseCohortValidator() {
        if (BASE_COHORT_VALIDATOR == null) {
            synchronized (JsonSchemaValidatorConfigurer.class) {
                if (BASE_COHORT_VALIDATOR == null) {
                    BASE_COHORT_VALIDATOR = configureSingletonSchemaValidator(cohortJsonSchemaAndInfo());
                }
            }
        }

        return BASE_COHORT_VALIDATOR;
    }

    private static JsonSchemaValidator configureSingletonSchemaValidator(JsonSchemaNodeAndInfo schemaAndInfo) {
        JsonSchema schema = getJsonSchemaFactory().getSchema(schemaAndInfo.node());
        return new JsonSchemaValidator(schema, schemaAndInfo.info());
    }

    private static JsonSchemaFactory getJsonSchemaFactory() {
        if (SCHEMA_FACTORY == null) { // double-check locking
            synchronized (JsonSchemaValidatorConfigurer.class) {
                if (SCHEMA_FACTORY == null)
                    SCHEMA_FACTORY = JsonSchemaFactory.getInstance(VERSION_FLAG);
            }
        }

        return SCHEMA_FACTORY;
    }

    private static JsonNode readJsonSchemaNode(InputStream is) throws IOException {
        if (OBJECT_MAPPER == null) { // double-check locking
            synchronized (JsonSchemaValidatorConfigurer.class) {
                if (OBJECT_MAPPER == null)
                    OBJECT_MAPPER = new ObjectMapper();
            }
        }

        return OBJECT_MAPPER.readTree(is);
    }

    /**
     * @return {@link JsonSchemaNodeAndInfo} for validating basic requirements of Phenopacket schema on {@link org.phenopackets.schema.v2.PhenopacketOrBuilder}.
     */
    private static JsonSchemaNodeAndInfo phenopacketJsonSchemaAndInfo() {
        try (InputStream is = JsonSchemaValidatorConfigurer.class.getResourceAsStream(PHENOPACKET_SCHEMA_PATH)) {
            JsonNode schemaNode = readJsonSchemaNode(is);
            return new JsonSchemaNodeAndInfo(schemaNode, ValidatorInfo.genericJsonSchema());
        } catch (IOException e) {
            throw new PhenopacketValidatorRuntimeException("Invalid JSON schema specification: " + e.getMessage());
        }
    }

    /**
     * @return {@link JsonSchemaNodeAndInfo} for validating basic requirements of Phenopacket schema on {@link org.phenopackets.schema.v2.FamilyOrBuilder}.
     */
    private static JsonSchemaNodeAndInfo familyJsonSchemaAndInfo() {
        try (InputStream is = JsonSchemaValidatorConfigurer.class.getResourceAsStream(FAMILY_SCHEMA_PATH)) {
            JsonNode schemaNode = readJsonSchemaNode(is);
            return new JsonSchemaNodeAndInfo(schemaNode, ValidatorInfo.genericJsonSchema());
        } catch (IOException e) {
            throw new PhenopacketValidatorRuntimeException("Invalid JSON schema specification: " + e.getMessage());
        }
    }

    /**
     * @return {@link JsonSchemaNodeAndInfo} for validating basic requirements of Phenopacket schema on {@link org.phenopackets.schema.v2.CohortOrBuilder}.
     */
    private static JsonSchemaNodeAndInfo cohortJsonSchemaAndInfo() {
        try (InputStream is = JsonSchemaValidatorConfigurer.class.getResourceAsStream(COHORT_SCHEMA_PATH)) {
            JsonNode schemaNode = readJsonSchemaNode(is);
            return new JsonSchemaNodeAndInfo(schemaNode, ValidatorInfo.genericJsonSchema());
        } catch (IOException e) {
            throw new PhenopacketValidatorRuntimeException("Invalid JSON schema specification: " + e.getMessage());
        }
    }

    private static JsonSchemaNodeAndInfo readSchemaAndInfo(InputStream is) throws IOException {
        JsonNode schemaNode = readJsonSchemaNode(is);
        ValidatorInfo validatorInfo = decodeValidatorInfo(schemaNode);

        return new JsonSchemaNodeAndInfo(schemaNode, validatorInfo);
    }

    private static ValidatorInfo decodeValidatorInfo(JsonNode schemaNode) {
        String schema = getNodeAsTextOrDefaultText(schemaNode, "$schema", "UNKNOWN_SCHEMA");
        String title = getNodeAsTextOrDefaultText(schemaNode, "title", "UNKNOWN_TITLE");
        String description = getNodeAsTextOrDefaultText(schemaNode, "description", "UNKNOWN VALIDATOR");

        return ValidatorInfo.of(schema, title, description);
    }

    private static String getNodeAsTextOrDefaultText(JsonNode schemaNode, String fieldName, String defaultValue) {
        return schemaNode.has(fieldName)
                ? schemaNode.get(fieldName).asText()
                : defaultValue;
    }

    private record JsonSchemaNodeAndInfo(JsonNode node, ValidatorInfo info) {
    }
}
