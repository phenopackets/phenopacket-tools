package org.phenopackets.phenopackettools.validator.core.metadata;

import com.google.protobuf.util.Timestamps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PhenopacketMetaDataValidatorTest {

    private PhenopacketMetaDataValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new PhenopacketMetaDataValidator();
    }

    @Test
    public void testMinimalExample() throws Exception {
        Phenopacket minimalPhenopacket = Phenopacket.newBuilder().setId("example-phenopacket-id")
                .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                        .setType(OntologyClass.newBuilder()
                                // NOTE the incorrect prefix, `HPO` instead of `HP`, to make the validation fail.
                                .setId("HPO:0000280")
                                .setLabel("Coarse facial features")
                                .build())
                        .build())
                .setMetaData(MetaData.newBuilder()
                        .setCreated(Timestamps.parse("2021-05-14T10:35:00Z"))
                        .setCreatedBy("anonymous biocurator")
                        .addResources(Resource.newBuilder()
                                .setId("hp")
                                .setName("human phenotype ontology")
                                .setUrl("http://purl.obolibrary.org/obo/hp.owl")
                                .setVersion("2021-08-02")
                                .setNamespacePrefix("HP")
                                .setIriPrefix("http://purl.obolibrary.org/obo/HP_")
                                .build())
                        .setPhenopacketSchemaVersion("2.0.0")
                        .build())
                .build();


        List<ValidationResult> results = validator.validate(minimalPhenopacket);

        assertThat(results, is(not(empty())));
    }

}