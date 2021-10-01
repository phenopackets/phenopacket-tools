package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v2.core.TimeInterval;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.fromRFC3339;

public class TimeIntervalCreator {


    public static TimeInterval create(Timestamp start, Timestamp end) {
        return TimeInterval.newBuilder()
                .setStart(start)
                .setEnd(end)
                .build();
    }

    public static TimeInterval create(String start, String end) {
        return TimeInterval.newBuilder()
                .setStart(fromRFC3339(start))
                .setEnd(fromRFC3339(end))
                .build();
    }


}
