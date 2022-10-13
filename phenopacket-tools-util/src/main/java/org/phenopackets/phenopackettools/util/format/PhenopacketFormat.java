package org.phenopackets.phenopackettools.util.format;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The enum members represent the supported representations of the top-level elements of the Phenopacket schema.
 */
public enum PhenopacketFormat {

    PROTOBUF,
    JSON,
    YAML;

    /**
     * Get file name suffix for the given {@link PhenopacketFormat} (e.g. {@code .json} for JSON).
     */
    public String suffix() {
        return switch (this) {
            case PROTOBUF -> ".pb";
            case JSON -> ".json";
            case YAML -> ".yaml";
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case PROTOBUF -> "protobuf";
            case JSON -> "json";
            case YAML -> "yaml";
        };
    }

    public static PhenopacketFormat parse(String value) {
        switch (value.toLowerCase()) {
            case "protobuf":
            case "pb":
                return PROTOBUF;
            case "json":
                return JSON;
            case "yaml":
                return YAML;
            default:
                String expected = String.join(Arrays.stream(PhenopacketFormat.values())
                        .map(Enum::name)
                        .map(String::toLowerCase)
                        .collect(Collectors.joining(", ", "{", "}")));
                throw new IllegalArgumentException("Expected one of %s but got %s".formatted(expected, value));
        }
    }

}
