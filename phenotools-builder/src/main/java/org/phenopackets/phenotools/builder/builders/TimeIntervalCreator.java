package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v2.core.TimeInterval;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.fromISO8601;

public class TimeIntervalCreator {


    public static TimeInterval create(Timestamp start, Timestamp end) {
        return TimeInterval.newBuilder()
                .setStart(start)
                .setEnd(end)
                .build();
    }

    public static TimeInterval create(String start, String end) {
        return TimeInterval.newBuilder()
                .setStart(fromISO8601(start))
                .setEnd(fromISO8601(end))
                .build();
    }


}
