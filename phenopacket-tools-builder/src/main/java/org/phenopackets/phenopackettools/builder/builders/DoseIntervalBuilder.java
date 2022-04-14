package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.DoseInterval;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.TimeInterval;

public class DoseIntervalBuilder {

    private DoseIntervalBuilder() {
    }

    public static DoseInterval doseInterval(Quantity quantity, OntologyClass scheduleFrequency, TimeInterval interval) {
        return DoseInterval.newBuilder()
                .setQuantity(quantity)
                .setScheduleFrequency(scheduleFrequency)
                .setInterval(interval)
                .build();
    }

    public static DoseInterval doseInterval(Quantity quantity, OntologyClass scheduleFrequency, String start, String end) {
        var interval = TimeIntervalBuilder.timeInterval(start, end);
        return DoseInterval.newBuilder()
                .setQuantity(quantity)
                .setScheduleFrequency(scheduleFrequency)
                .setInterval(interval)
                .build();
    }
}
