package org.phenopackets.phenopackettools.validator.jsonschema;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.errors.JsonError;
import org.phenopackets.phenopackettools.validator.testdatagen.PhenopacketUtil;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Disease;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.phenopackets.phenopackettools.validator.testdatagen.PhenopacketUtil.*;

/**
 * This class creates a simple phenopacket with a Disease object and creates some variations with
 * validation errors.
 *
 * @author Peter N Robinson
 */
public class JsonSchemaDiseaseValidatorTest {

    private static final JsonSchemaValidator validator = JsonSchemaValidator.makeGenericJsonValidator();


    private static Disease mondoDisease() {
        var chagas = ontologyClass("MONDO:0005491", "Chagas cardiomyopathy");
        var nyha3 = ontologyClass("NCIT:C66907", "New York Heart Association Class III");
        var childhood = TimeElement.newBuilder().setOntologyClass(CHILDHOOD_ONSET).build();
        return Disease.newBuilder()
                .setTerm(chagas)
                .setOnset(childhood)
                .addDiseaseStage(nyha3)
                .build();
    }

    private static Phenopacket phenopacketWithDisease() {
        Resource hpo = hpoResource("2021-08-02");
        Resource mondo = mondoResource("2021-09-01");
        MetaData meta = PhenopacketUtil.MetaDataBuilder.create("2021-07-01T19:32:35Z", "anonymous biocurator")
                .submittedBy("anonymous submitter")
                .addResource(hpo)
                .addResource(mondo)
                .addExternalReference("PMID:20842687",
                        "Severe dystonic encephalopathy without hyperphenylalaninemia associated with an 18-bp deletion within the proximal GCH1 promoter")
                .build();
        Disease chagasCardiomyopathy = mondoDisease();
        return Phenopacket.newBuilder()
                .setId("A")
                .addDiseases(chagasCardiomyopathy)
                .setMetaData(meta)
                .build();
    }

    private static final Phenopacket phenopacket = phenopacketWithDisease();

    @Test
    public void testPhenopacketValidity() throws InvalidProtocolBufferException, JsonProcessingException {
        String json = JsonFormat.printer().print(phenopacket);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        List<? extends ValidationResult> errors = validator.validateJson(jsonNode);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testLacksId() throws InvalidProtocolBufferException, JsonProcessingException {
        // the Phenopacket is not valid if we remove the id
        Phenopacket p1 = Phenopacket.newBuilder(phenopacket).clearId().build();
        String json = JsonFormat.printer().print(p1);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        List<? extends ValidationResult> errors = validator.validateJson(jsonNode);
        assertEquals(1, errors.size());
        ValidationResult error = errors.get(0);
        assertEquals(JsonError.REQUIRED, error.category());
        assertEquals("$.id: is missing but it is required", error.message());
    }


}
