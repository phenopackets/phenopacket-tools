module org.phenopackets.phenopackettools.builder {
    // No need to make it transitive since we only use runtime exceptions.
    requires org.phenopackets.phenopackettools.core;
    requires transitive org.phenopackets.schema;
    // Required due to `TimestampBuilder`.
    //noinspection requires-transitive-automatic
    requires transitive com.google.protobuf;

    exports org.phenopackets.phenopackettools.builder;
    exports org.phenopackets.phenopackettools.builder.builders;
    exports org.phenopackets.phenopackettools.builder.constants;
}