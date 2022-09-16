package org.phenopackets.phenopackettools.validator.jsonschema;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
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
        MetaData meta = MetaDataBuilder.create("2021-07-01T19:32:35Z", "anonymous biocurator")
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

    private static final ObjectMapper mapper = new ObjectMapper();
    private JsonSchemaValidator validator;

    @BeforeEach
    public void setUp() {
        validator = JsonSchemaValidatorConfigurer.getBasePhenopacketValidator();
    }

    @Test
    public void testPhenopacketValidity() throws Exception {
        JsonNode jsonNode = mapPhenopacketToJsonNode(phenopacket);
        List<? extends ValidationResult> errors = validator.validate(jsonNode);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testLacksId() throws Exception {
        // the Phenopacket is not valid if we remove the id
        Phenopacket.Builder pb = phenopacket.toBuilder().clearId();
        JsonNode jsonNode = mapPhenopacketToJsonNode(pb);

        List<? extends ValidationResult> errors = validator.validate(jsonNode);

        assertEquals(1, errors.size());
        ValidationResult error = errors.get(0);
        assertEquals(JsonError.REQUIRED, error.category());
        assertEquals("$.id: is missing but it is required", error.message());
    }

    private static JsonNode mapPhenopacketToJsonNode(PhenopacketOrBuilder phenopacket) throws Exception {
        JsonFormat.Printer printer = JsonFormat.printer();
        String json = printer.print(phenopacket);
        return mapper.readTree(json);
    }


}
