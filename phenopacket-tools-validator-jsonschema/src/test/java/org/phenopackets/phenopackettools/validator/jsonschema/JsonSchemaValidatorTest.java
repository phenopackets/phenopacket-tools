package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.testdatagen.ExampleFamily;
import org.phenopackets.phenopackettools.validator.testdatagen.RareDiseasePhenopacket;
import org.phenopackets.phenopackettools.validator.testdatagen.SimplePhenopacket;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.protobuf.util.JsonFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSchemaValidatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonTamperer TAMPERER = new JsonTamperer();

    private static final Phenopacket RARE_DISEASE_PHENOPACKET = new RareDiseasePhenopacket().getPhenopacket();

    @Nested
    public class PhenopacketTest {

        private static final SimplePhenopacket simplePhenopacket = new SimplePhenopacket();


        private JsonSchemaValidator validator;

        @BeforeEach
        public void setUp() {
            validator = JsonSchemaValidatorConfigurer.getBasePhenopacketValidator();
        }

        @Test
        public void testValidationOfSimpleValidPhenopacket() throws Exception {
            // PhenopacketValidatorOld validator = FACTORY.getValidatorForType(ValidatorInfo.generic()).get();
            Phenopacket phenopacket = simplePhenopacket.getPhenopacket();
            String json = JsonFormat.printer().print(phenopacket);
            JsonNode jsonNode = MAPPER.readTree(json);
            List<? extends ValidationResult> errors = validator.validate(jsonNode);
            assertTrue(errors.isEmpty());
            // the Phenopacket is not valid if we remove the id
            phenopacket = Phenopacket.newBuilder(phenopacket).clearId().build();
            json = JsonFormat.printer().print(phenopacket);
            jsonNode = MAPPER.readTree(json);
            errors = validator.validate(jsonNode);
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
        public void testValidationOfSimpleInValidPhenopacket() throws Exception {

            String invalidPhenopacketJson = "{\"disney\" : \"donald\"}";

            JsonNode jsonNode = MAPPER.readTree(invalidPhenopacketJson);
            List<? extends ValidationResult> errors = validator.validate(jsonNode);
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
            String json =  JsonFormat.printer().print(RARE_DISEASE_PHENOPACKET);
            JsonNode jsonNode = MAPPER.readTree(json);
            List<? extends ValidationResult> errors = validator.validate(jsonNode);
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

            JsonNode jsonNode = MAPPER.readTree(invalidJson);
            List<? extends ValidationResult> errors = validator.validate(jsonNode);
            assertEquals(1, errors.size());
            ValidationResult error = errors.get(0);
            assertEquals(JsonError.ENUM, error.category());
            assertEquals("$.subject.sex: does not have a value in the enumeration [UNKNOWN_SEX, FEMALE, MALE, OTHER_SEX]", error.message());
            assertEquals(ValidationLevel.ERROR, error.level());
        }

        @Test
        @Disabled // TODO - we should rework the testing strategy to invalidate a valid phenopacket and check that it raises the expected error
        public void testRareDiseaseBethlemahmInvalidValidPhenopacket() throws IOException {

            File invalidMyopathyPhenopacket = Path.of("src/test/resources/json/bethlehamMyopathyInvalidExample.json").toFile();
            String payload = Files.readString(invalidMyopathyPhenopacket.toPath());
            JsonNode jsonNode = MAPPER.readTree(payload);
            List<? extends ValidationResult> validationItems = validator.validate(jsonNode);
            assertEquals(1, validationItems.size());
            ValidationResult validationResult = validationItems.get(0);
            assertEquals(JsonError.REQUIRED, validationResult.category());
            assertEquals("$.phenotypicFeatures: is missing but it is required", validationResult.message());
        }

    }

    @Nested
    public class FamilyTest {

        private JsonSchemaValidator validator;

        @BeforeEach
        public void setUp() {
            validator = JsonSchemaValidatorConfigurer.getBaseFamilyValidator();
        }

        @Test
        public void validFamilyYieldsNoErrors() throws Exception {
            JsonNode node = getFamilyNode();

            List<ValidationResult> results = validator.validate(node);
            assertThat(results, is(empty()));
        }

        @ParameterizedTest
        @CsvSource({
                "/id,                    DELETE,          '$.id: is missing but it is required'",
                "/proband,               DELETE,          '$.proband: is missing but it is required'",
                "/consanguinousParents,  DELETE,          '$.consanguinousParents: is missing but it is required'",
                "/pedigree,              DELETE,          '$.pedigree: is missing but it is required'",
                "/metaData,              DELETE,          '$.metaData: is missing but it is required'",
        })
        public void absenceOfTopLevelFamilyElementsYieldsErrors(String path, Action action, String expected) throws Exception {
            JsonNode node = getFamilyNode();
            JsonNode tampered = TAMPERER.tamper(node, path, action);

            List<ValidationResult> results = validator.validate(tampered);

            assertThat(results, is(not(empty())));
            assertThat(results.stream().map(ValidationResult::message).toList(), containsInAnyOrder(expected.split("/")));
        }

        @ParameterizedTest
        @CsvSource({
                "/pedigree/persons,     DELETE,          '$.pedigree.persons: is missing but it is required'",
                "/pedigree/persons[*],  DELETE,          '$.pedigree.persons: there must be a minimum of 1 items in the array'",
        })
        public void emptyPedigreeYieldsError(String path, Action action, String expected) throws Exception {
            JsonNode node = getFamilyNode();
            JsonNode tampered = TAMPERER.tamper(node, path, action);

            List<ValidationResult> results = validator.validate(tampered);

            assertThat(results, is(not(empty())));
            assertThat(results.stream().map(ValidationResult::message).toList(), containsInAnyOrder(expected.split("/")));
        }

        private static JsonNode getFamilyNode() throws InvalidProtocolBufferException, JsonProcessingException {
            Family family = ExampleFamily.getExampleFamily();
            String json = JsonFormat.printer().print(family);
            return MAPPER.readTree(json);
        }

    }

}