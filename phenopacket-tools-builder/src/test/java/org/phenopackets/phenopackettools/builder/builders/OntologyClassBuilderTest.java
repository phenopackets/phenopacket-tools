package org.phenopackets.phenopackettools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.Disease;
import org.phenopackets.schema.v2.core.OntologyClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class OntologyClassBuilderTest {

    @Test
    public void testBuilder() {
        OntologyClass longPR1 = OntologyClass.newBuilder()
                .setId("HP:0012248")
                .setLabel("Prolonged PR interval")
                .build();
        OntologyClass longPR2 = ontologyClass("HP:0012248", "Prolonged PR interval");
        assertThat(longPR1, equalTo(longPR2));
    }
}
