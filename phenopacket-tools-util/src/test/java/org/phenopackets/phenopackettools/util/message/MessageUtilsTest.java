package org.phenopackets.phenopackettools.util.message;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.util.TestResources;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Biosample;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.phenopackets.schema.v2.core.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MessageUtilsTest {

    private static final Path TEST_DIR = TestResources.BASE_DIR.resolve("message");


    private static final Message.Builder COVID_PHENOPACKET = Phenopacket.newBuilder();

    @BeforeAll
    public static void beforeAll() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(TEST_DIR.resolve("covid.json"))) {
            JsonFormat.parser().merge(reader, COVID_PHENOPACKET);
        }
    }

    @Test
    public void findAllOntologyClasses_PhenotypicFeatures() {
        List<PhenotypicFeature> pfs = MessageUtils.findInstancesOfType(COVID_PHENOPACKET, PhenotypicFeature.class)
                .toList();
        assertThat(pfs, hasSize(8));
        List<String> labels = pfs.stream()
                .map(PhenotypicFeature::getType)
                .map(OntologyClass::getLabel)
                .toList();
        assertThat(labels, hasItems("Fever", "Flank pain", "Hematuria", "Stage 3 chronic kidney disease", "Myalgia",
                "Diarrhea", "Dyspnea", "Acute respiratory distress syndrome"));
    }

    @Test
    public void findAllOntologyClasses_Resources() {
        List<Resource> resources = MessageUtils.findInstancesOfType(COVID_PHENOPACKET, Resource.class)
                .toList();
        assertThat(resources, hasSize(2));
        List<String> labels = resources.stream()
                .map(Resource::getNamespacePrefix)
                .toList();
        assertThat(labels, hasItems("NCIT", "MONDO"));
    }

    @Test
    public void findAllOntologyClasses_NoMatch() {
        List<Biosample> resources = MessageUtils.findInstancesOfType(COVID_PHENOPACKET, Biosample.class)
                .toList();
        assertThat(resources, is(emptyCollectionOf(Biosample.class)));
    }
}