package org.phenopackets.phenopackettools.validator.jsonschema;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidatorOld;
import org.phenopackets.phenopackettools.validator.core.ErrorTypeOLD;
import org.phenopackets.phenopackettools.validator.core.ValidationItem;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
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

    private static final ClasspathJsonSchemaValidatorFactory FACTORY = ClasspathJsonSchemaValidatorFactory.defaultValidators();


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
    public void testPhenopacketValidity() throws InvalidProtocolBufferException {
        PhenopacketValidatorOld validator = FACTORY.getValidatorForType(ValidatorInfo.generic()).get();
        String json = JsonFormat.printer().print(phenopacket);
        List<ValidationItem> errors = validator.validate(json);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testLacksId() throws InvalidProtocolBufferException {
        PhenopacketValidatorOld validator = FACTORY.getValidatorForType(ValidatorInfo.generic()).get();
        // the Phenopacket is not valid if we remove the id
        Phenopacket p1 = Phenopacket.newBuilder(phenopacket).clearId().build();
        String json = JsonFormat.printer().print(p1);
//        System.out.println(json);
        List<ValidationItem> errors = validator.validate(json);
//        System.out.println(errors);
        assertEquals(1, errors.size());
        ValidationItem error = errors.get(0);
        assertEquals(ErrorTypeOLD.JSON_REQUIRED, error.errorType());
        assertEquals("$.id: is missing but it is required", error.message());
    }


}
