package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.TimeInterval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class TimeIntervalBuilderTest {

    /**
     * <a href="https://phenopacket-schema.readthedocs.io/en/v2/time-interval.html#example">https://phenopacket-schema.readthedocs.io/en/v2/time-interval.html#example</a>
     */
    @Test
    void timeIntervalFromStringTest() {
        TimeInterval instance = TimeIntervalBuilder.timeInterval("2020-03-15T13:00:00Z", "2020-03-25T09:00:00Z");
        assertThat(instance.getStart().getSeconds(), equalTo(1584277200L));
        assertThat(instance.getEnd().getSeconds(), equalTo(1585126800L));
    }

    @Test
    void timeIntervalFromTimestampTest() {
        Timestamp start = TimestampBuilder.fromISO8601("2020-03-15T13:00:00Z");
        Timestamp end = TimestampBuilder.fromISO8601("2020-03-25T09:00:00Z");
        TimeInterval instance = TimeIntervalBuilder.timeInterval(start, end);
        assertThat(instance.getStart(), equalTo(start));
        assertThat(instance.getEnd(), equalTo(end));
    }
}