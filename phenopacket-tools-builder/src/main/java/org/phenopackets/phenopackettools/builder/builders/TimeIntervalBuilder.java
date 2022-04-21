package org.phenopackets.phenopackettools.builder.builders;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v2.core.TimeInterval;

import static org.phenopackets.phenopackettools.builder.builders.TimestampBuilder.fromISO8601;

public class TimeIntervalBuilder {

    private TimeIntervalBuilder() {
    }

    public static TimeInterval of(Timestamp start, Timestamp end) {
        return TimeInterval.newBuilder()
                .setStart(start)
                .setEnd(end)
                .build();
    }

    public static TimeInterval of(String start, String end) {
        return TimeInterval.newBuilder()
                .setStart(fromISO8601(start))
                .setEnd(fromISO8601(end))
                .build();
    }
}
