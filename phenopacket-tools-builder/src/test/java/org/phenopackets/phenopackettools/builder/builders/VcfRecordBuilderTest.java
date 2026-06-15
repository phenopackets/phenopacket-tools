package org.phenopackets.phenopackettools.builder.builders;

import org.ga4gh.vrsatile.v1.VcfRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class VcfRecordBuilderTest {

    @ParameterizedTest
    @CsvSource({
            "PASS,      PASS",
            "pass,      PASS",
            "q50,       q50",
            "q50;q10,   q50;q10",
            "q50;pass,  PASS",
            "pass;q50,  q50",
    })
    public void addFilter(String filter, String expected) {
        VcfRecord record = VcfRecordBuilder.builder("GRCh37", "chr1", 123_456, "C", "G")
                .filter(filter)
                .build();

        assertThat(record.getFilter(), equalTo(expected));
    }
}