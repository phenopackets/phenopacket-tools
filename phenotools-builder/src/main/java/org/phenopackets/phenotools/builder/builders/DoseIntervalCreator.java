package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.DoseInterval;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.TimeInterval;

public class DoseIntervalCreator {

    public static DoseInterval create(Quantity quantity, OntologyClass schedule_frequency, TimeInterval interval) {
        return DoseInterval.newBuilder()
                .setQuantity(quantity)
                .setScheduleFrequency(schedule_frequency)
                .setInterval(interval)
                .build();
    }

    public static DoseInterval create(Quantity quantity, OntologyClass schedule_frequency, String start, String end) {
        var interval = TimeIntervalCreator.create(start, end);
        return DoseInterval.newBuilder()
                .setQuantity(quantity)
                .setScheduleFrequency(schedule_frequency)
                .setInterval(interval)
                .build();
    }
}
