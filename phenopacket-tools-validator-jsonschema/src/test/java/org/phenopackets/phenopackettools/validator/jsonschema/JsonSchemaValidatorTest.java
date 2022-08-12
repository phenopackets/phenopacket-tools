package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.errors.JsonError;
import org.phenopackets.phenopackettools.validator.testdatagen.RareDiseasePhenopacket;
import org.phenopackets.phenopackettools.validator.testdatagen.SimplePhenopacket;
import org.phenopackets.schema.v2.Phenopacket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.protobuf.util.JsonFormat;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSchemaValidatorTest {
    private static final JsonSchemaValidator validator = JsonSchemaValidator.makeGenericJsonValidator();

    private static final SimplePhenopacket simplePhenopacket = new SimplePhenopacket();

    private static final RareDiseasePhenopacket rareDiseasePhenopacket = new RareDiseasePhenopacket();

    @Test
    public void testValidationOfSimpleValidPhenopacket() throws Exception {
       // PhenopacketValidatorOld validator = FACTORY.getValidatorForType(ValidatorInfo.generic()).get();
        Phenopacket phenopacket = simplePhenopacket.getPhenopacket();
        String json = JsonFormat.printer().print(phenopacket);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        List<? extends ValidationResult> errors = validator.validateJson(jsonNode);
        assertTrue(errors.isEmpty());
        // the Phenopacket is not valid if we remove the id
        phenopacket = Phenopacket.newBuilder(phenopacket).clearId().build();
        json = JsonFormat.printer().print(phenopacket);
        jsonNode = mapper.readTree(json);
        errors = validator.validateJson(jsonNode);
        assertEquals(1, errors.size());
        ValidationResult error = errors.get(0);
        assertEquals(JsonError.REQUIRED, error.category());
        assertEquals("$.id: is missing but it is required", error.message());
    }

    /**
     * This example phenopacket does not contain anything except {"disney" : "donald"}
     * It does not contain an id or a metaData element and thus should fail.
     */
    @Test
    public void testValidationOfSimpleInValidPhenopacket() throws JsonProcessingException {

        String invalidPhenopacketJson = "{\"disney\" : \"donald\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(invalidPhenopacketJson);
        List<? extends ValidationResult> errors = validator.validateJson(jsonNode);
        assertEquals(3, errors.size());
        ValidationResult error = errors.get(0);
        // JsonError.CATEGORY is "JSON"
        assertEquals(JsonError.REQUIRED, error.category());
        assertEquals("$.id: is missing but it is required", error.message());
        error = errors.get(1);
        assertEquals(JsonError.REQUIRED, error.category());
       assertEquals("$.metaData: is missing but it is required", error.message());
        error = errors.get(2);

        assertEquals(JsonError.ADDITIONAL_PROPERTIES, error.category());
        assertEquals("$.disney: is not defined in the schema and the schema does not allow additional properties", error.message());
    }

    @Test
    public void testRareDiseaseBethlemahmValidPhenopacket() throws Exception {
        Phenopacket bethlehamMyopathy = rareDiseasePhenopacket.getPhenopacket();
        String json =  JsonFormat.printer().print(bethlehamMyopathy);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        List<? extends ValidationResult> errors = validator.validateJson(jsonNode);
        assertTrue(errors.isEmpty());
    }

    /**
     * This line is erroneous because it does not correspond to the enumeration
     *  "sex": "random text here"
     */
    @Test
    public void invalidEnum() throws JsonProcessingException {
        String invalidJson = """
                {
                  "id": "id-C",
                  "subject": {
                    "id": "proband C",
                    "timeAtLastEncounter": {
                      "age": {
                        "iso8601duration": "P27Y"
                      }
                    },
                    "sex": "random text here"
                  },
                  "metaData": {
                    "created": "2021-05-14T10:35:00Z",
                    "createdBy": "anonymous biocurator",
                    "resources": [{
                      "id": "hp",
                      "name": "human phenotype ontology",
                      "url": "http://purl.obolibrary.org/obo/hp.owl",
                      "version": "2021-08-02",
                      "namespacePrefix": "HP",
                      "iriPrefix": "http://purl.obolibrary.org/obo/HP_"
                    }],
                    "phenopacketSchemaVersion": "2.0"
                  }
                }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(invalidJson);
        List<? extends ValidationResult> errors = validator.validateJson(jsonNode);
        assertEquals(1, errors.size());
        ValidationResult error = errors.get(0);
        assertEquals(JsonError.ENUM, error.category());
        assertEquals("$.subject.sex: does not have a value in the enumeration [UNKNOWN_SEX, FEMALE, MALE]", error.message());
        assertEquals(ValidationLevel.VALIDATION_ERROR, error.level());
    }

    @Test
    @Disabled // TODO - we should rework the testing strategy to invalidate a valid phenopacket and check that it raises the expected error
    public void testRareDiseaseBethlemahmInvalidValidPhenopacket() throws IOException {

        File invalidMyopathyPhenopacket = Path.of("src/test/resources/json/bethlehamMyopathyInvalidExample.json").toFile();
        String payload = Files.readString(invalidMyopathyPhenopacket.toPath());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(payload);
        List<? extends ValidationResult> validationItems = validator.validateJson(jsonNode);
        for (ValidationResult ve : validationItems) {
            System.out.println(ve);
        }
        assertEquals(1, validationItems.size());
        ValidationResult validationResult = validationItems.get(0);
        assertEquals(JsonError.REQUIRED, validationResult.category());
       assertEquals("$.phenotypicFeatures: is missing but it is required", validationResult.message());
    }


}