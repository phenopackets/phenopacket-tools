package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Age;
import org.phenopackets.schema.v2.core.AgeRange;

public class AgeConverter {

    private AgeConverter() {
    }

    public static Age toAge(org.phenopackets.schema.v1.core.Age v1Age) {
        return Age.newBuilder().setIso8601Duration(v1Age.getAge()).build();
    }

    public static AgeRange toAgeRange(org.phenopackets.schema.v1.core.AgeRange v1AgeRange) {
        return AgeRange.newBuilder()
                .setStart(toAge(v1AgeRange.getStart()))
                .setEnd(toAge(v1AgeRange.getEnd()))
                .build();
    }
}
