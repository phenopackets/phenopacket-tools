package org.phenopackets.phenopackettools.converter.converters.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.phenopackets.schema.v1.core.Age;
import org.phenopackets.schema.v1.core.AgeRange;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class AgeConverterTest {

    @Test
    public void convertNonDefaultAge() {
        Age v1 = Age.newBuilder().setAge("P1Y").build();
        Optional<org.phenopackets.schema.v2.core.Age> result = AgeConverter.toAge(v1);
        assertThat(result.isPresent(), equalTo(true));

        org.phenopackets.schema.v2.core.Age v2 = result.get();
        assertThat(v2.getIso8601Duration(), equalTo(v1.getAge()));
    }

    @Test
    public void convertDefaultAge() {
        Age v1 = Age.newBuilder().build();
        Optional<org.phenopackets.schema.v2.core.Age> result = AgeConverter.toAge(v1);
        assertThat(result.isPresent(), equalTo(false));
    }

    @ParameterizedTest
    @CsvSource({
            "P1Y,  P2Y",
            " '',  P2Y",
            "P1Y,  ''",
    })
    public void convertNonDefaultAgeRange(String start, String end) {
        AgeRange v1 = AgeRange.newBuilder()
                .setStart(Age.newBuilder().setAge(start).build())
                .setEnd(Age.newBuilder().setAge(end).build())
                .build();

        Optional<org.phenopackets.schema.v2.core.AgeRange> result = AgeConverter.toAgeRange(v1);
        assertThat(result.isPresent(), equalTo(true));

        org.phenopackets.schema.v2.core.AgeRange v2 = result.get();

        assertThat(v2, not(sameInstance(org.phenopackets.schema.v2.core.AgeRange.getDefaultInstance())));
        assertThat(v2.getStart().getIso8601Duration(), equalTo(start));
        assertThat(v2.getEnd().getIso8601Duration(), equalTo(end));
    }

    @Test
    public void convertDefaultAgeRange() {
        AgeRange v1 = AgeRange.newBuilder().build();

        Optional<org.phenopackets.schema.v2.core.AgeRange> result = AgeConverter.toAgeRange(v1);
        assertThat(result.isPresent(), equalTo(false));
    }
}