package org.phenopackets.phenopackettools.core;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The enum members represent the top-level elements of the Phenopacket schema.
 */
public enum PhenopacketElement {

    PHENOPACKET,
    FAMILY,
    COHORT;

    @Override
    public String toString() {
        return switch (this) {
            case PHENOPACKET -> "phenopacket";
            case FAMILY -> "family";
            case COHORT -> "cohort";
        };
    }

    public static PhenopacketElement parse(String value) {
        switch (value.toLowerCase()) {
            case "phenopacket":
                return PHENOPACKET;
            case "family":
                return FAMILY;
            case "cohort":
                return COHORT;
            default:
                String expected = String.join(Arrays.stream(PhenopacketElement.values())
                        .map(Enum::name)
                        .map(String::toLowerCase)
                        .collect(Collectors.joining(", ", "{", "}")));
                throw new IllegalArgumentException("Expected one of %s but got %s".formatted(expected, value));
        }
    }

}
