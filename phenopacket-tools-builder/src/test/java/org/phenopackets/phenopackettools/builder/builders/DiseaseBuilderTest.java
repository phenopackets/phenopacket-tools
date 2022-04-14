package org.phenopackets.phenopackettools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.Disease;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

class DiseaseBuilderTest {

    @Test
    void testMinimalData() {
        Disease disease = DiseaseBuilder.disease("MONDO:0004994", "cardiomyopathy");
        assertThat(disease.getTerm(), equalTo(ontologyClass("MONDO:0004994", "cardiomyopathy")));
    }

    @Test
    void testBuilder() {
        Disease disease = DiseaseBuilder.builder("OMIM:164400", "Spinocerebellar ataxia 1")
                .onset(TimeElements.age("P38Y7M"))
                .build();
        assertThat(disease.getTerm(), equalTo(ontologyClass("OMIM:164400", "Spinocerebellar ataxia 1")));
        assertThat(disease.getOnset(), equalTo(TimeElements.age("P38Y7M")));
    }
}