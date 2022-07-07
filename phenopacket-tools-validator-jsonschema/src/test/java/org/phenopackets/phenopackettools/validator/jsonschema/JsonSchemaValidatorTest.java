package org.phenopackets.phenopackettools.validator.jsonschema;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.ErrorTypeOLD;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidatorOld;
import org.phenopackets.phenopackettools.validator.core.ValidationItem;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.testdatagen.RareDiseasePhenopacket;
import org.phenopackets.phenopackettools.validator.testdatagen.SimplePhenopacket;
import org.phenopackets.schema.v2.Phenopacket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.google.protobuf.util.JsonFormat;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSchemaValidatorTest {

    private static final ClasspathJsonSchemaValidatorFactory FACTORY = ClasspathJsonSchemaValidatorFactory.defaultValidators();

    private static final SimplePhenopacket simplePhenopacket = new SimplePhenopacket();

    private static final RareDiseasePhenopacket rareDiseasePhenopacket = new RareDiseasePhenopacket();

    @Test
    public void testValidationOfSimpleValidPhenopacket() throws Exception {
        PhenopacketValidatorOld validator = FACTORY.getValidatorForType(ValidatorInfo.generic()).get();
        Phenopacket phenopacket = simplePhenopacket.getPhenopacket();
        String json =  JsonFormat.printer().print(phenopacket);
        List<? extends ValidationItem> errors = validator.validate(json);
        assertTrue(errors.isEmpty());
        // the Phenopacket is not valid if we remove the id
        phenopacket = Phenopacket.newBuilder(phenopacket).clearId().build();
        json =  JsonFormat.printer().print(phenopacket);
        errors = validator.validate(json);
        assertEquals(1, errors.size());
        ValidationItem error = errors.get(0);
        assertEquals(ErrorTypeOLD.JSON_REQUIRED, error.errorType());
        assertEquals("$.id: is missing but it is required", error.message());
    }

    /**
     * This example phenopacket does not contain anything except {"disney" : "donald"}
     * It does not contain an id or a metaData element and thus should fail.
     */
    @Test
    public void testValidationOfSimpleInValidPhenopacket() {
        PhenopacketValidatorOld validator = FACTORY.getValidatorForType(ValidatorInfo.generic()).get();

        String invalidPhenopacketJson = "{\"disney\" : \"donald\"}";

        List<? extends ValidationItem> errors = validator.validate(invalidPhenopacketJson);

        assertEquals(3, errors.size());
        ValidationItem error = errors.get(0);
        assertEquals(ErrorTypeOLD.JSON_REQUIRED, error.errorType());
        assertEquals("$.id: is missing but it is required", error.message());
        error = errors.get(1);
        assertEquals(ErrorTypeOLD.JSON_REQUIRED, error.errorType());
        assertEquals("$.metaData: is missing but it is required", error.message());
        error = errors.get(2);
        assertEquals(ErrorTypeOLD.JSON_ADDITIONAL_PROPERTIES, error.errorType());
        assertEquals("$.disney: is not defined in the schema and the schema does not allow additional properties", error.message());
    }

    @Test
    public void testRareDiseaseBethlemahmValidPhenopacket() throws Exception {
        PhenopacketValidatorOld validator = FACTORY.getValidatorForType(ValidatorInfo.rareDiseaseValidation()).get();

        Phenopacket bethlehamMyopathy = rareDiseasePhenopacket.getPhenopacket();
        String json =  JsonFormat.printer().print(bethlehamMyopathy);
        List<? extends ValidationItem> errors = validator.validate(json);

        assertTrue(errors.isEmpty());
    }

    @Test
    @Disabled // TODO - we should rework the testing strategy to invalidate a valid phenopacket and check that it raises the expected error
    public void testRareDiseaseBethlemahmInvalidValidPhenopacket() throws IOException {
        PhenopacketValidatorOld validator = FACTORY.getValidatorForType(ValidatorInfo.rareDiseaseValidation()).get();

        File invalidMyopathyPhenopacket = Path.of("src/test/resources/json/bethlehamMyopathyInvalidExample.json").toFile();
        List<ValidationItem> validationItems = validator.validate(invalidMyopathyPhenopacket);
        for (ValidationItem ve : validationItems) {
            System.out.println(ve);
        }
        assertEquals(1, validationItems.size());
        ValidationItem validationItem = validationItems.get(0);
        assertEquals(ErrorTypeOLD.JSON_REQUIRED, validationItem.errorType());
        assertEquals("$.phenotypicFeatures: is missing but it is required", validationItem.message());
    }


}