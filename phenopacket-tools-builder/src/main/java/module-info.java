module org.phenopackets.phenopackettools.builder {
    requires transitive org.phenopackets.schema;
    // Required due to `TimestampBuilder`.
    requires transitive com.google.protobuf;

    exports org.phenopackets.phenopackettools.builder;
    exports org.phenopackets.phenopackettools.builder.builders;
    exports org.phenopackets.phenopackettools.builder.constants;
    exports org.phenopackets.phenopackettools.builder.exceptions;
}