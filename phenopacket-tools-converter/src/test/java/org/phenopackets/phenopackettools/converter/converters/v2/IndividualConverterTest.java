package org.phenopackets.phenopackettools.converter.converters.v2;

import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.test.TestData;
import org.phenopackets.schema.v1.core.Individual;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class IndividualConverterTest {

    @Test
    public void convertNonDefaultIndividual() {
        Individual v1 = TestData.V1.comprehensivePhenopacket().getSubject();

        Optional<org.phenopackets.schema.v2.core.Individual> result = IndividualConverter.toIndividual(v1);
        assertThat(result.isPresent(), equalTo(true));

        org.phenopackets.schema.v2.core.Individual v2 = result.get();
        assertThat(v2.getId(), equalTo("14 year-old boy"));

    }

    @Test
    public void convertDefaultIndividual() {
        Individual v1 = Individual.newBuilder().build();

        Optional<org.phenopackets.schema.v2.core.Individual> result = IndividualConverter.toIndividual(v1);
        assertThat(result.isEmpty(), equalTo(true));

    }
}