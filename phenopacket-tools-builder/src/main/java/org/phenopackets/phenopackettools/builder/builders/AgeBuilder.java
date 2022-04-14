package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.phenopackettools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.core.Age;
import org.phenopackets.schema.v2.core.AgeRange;
import org.phenopackets.schema.v2.core.GestationalAge;

import java.time.Period;
import java.time.format.DateTimeParseException;

public class AgeBuilder {

    private AgeBuilder() {
    }

    public static Age age(String iso8601duration) {
        try {
            Period.parse(iso8601duration);
        } catch (DateTimeParseException ex) {
            throw new PhenotoolsRuntimeException("Invalid iso8601 age (period) string: \"" + iso8601duration + "\".");
        }
        return Age.newBuilder().setIso8601Duration(iso8601duration).build();
    }

    public static AgeRange ageRange(String iso8601durationStart, String iso8601durationEnd) {
        Age start = age(iso8601durationStart);
        Age end = age(iso8601durationEnd);
        return AgeRange.newBuilder().setStart(start).setEnd(end).build();
    }

    public static GestationalAge gestationalAge(int weeks, int days) {
        return GestationalAge.newBuilder().setWeeks(weeks).setDays(days).build();
    }
}
