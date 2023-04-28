package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Age;
import org.phenopackets.schema.v2.core.AgeRange;

import java.util.Optional;

class AgeConverter {

    private AgeConverter() {
    }

    static Optional<Age> toAge(org.phenopackets.schema.v1.core.Age v1Age) {
        if (v1Age.equals(org.phenopackets.schema.v1.core.Age.getDefaultInstance()) || v1Age.getAge().isEmpty())
            return Optional.empty();
        else {
            return Optional.of(Age.newBuilder()
                    .setIso8601Duration(v1Age.getAge())
                    .build());
        }
    }

    static Optional<AgeRange> toAgeRange(org.phenopackets.schema.v1.core.AgeRange v1AgeRange) {
        if (v1AgeRange.equals(org.phenopackets.schema.v1.core.AgeRange.getDefaultInstance()))
            return Optional.empty();

        Optional<Age> start = toAge(v1AgeRange.getStart());
        Optional<Age> end = toAge(v1AgeRange.getEnd());

        if (start.isEmpty() && end.isEmpty())
            return Optional.empty();
        else {
            AgeRange.Builder builder = AgeRange.newBuilder();
            start.ifPresent(builder::setStart);
            end.ifPresent(builder::setEnd);
            return Optional.of(builder.build());
        }
    }
}
