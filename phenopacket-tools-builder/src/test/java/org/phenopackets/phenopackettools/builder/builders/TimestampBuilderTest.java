package org.phenopackets.phenopackettools.builder.builders;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TimestampBuilderTest {

    @Test
    public void testFromISO8601Timestamp() throws ParseException {
        Timestamp instance = TimestampBuilder.fromISO8601("2021-11-05T12:38:00Z");
        assertThat(instance, equalTo(Timestamps.parse("2021-11-05T12:38:00Z")));
    }

    @Test
    public void testFromISO8601Date() throws ParseException {
        Timestamp instance = TimestampBuilder.fromISO8601("2021-11-05");
        assertThat(instance, equalTo(Timestamps.parse("2021-11-05T00:00:00Z")));
    }
}