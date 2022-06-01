module org.phenopackets.phenopackettools.builder {
    requires transitive org.phenopackets.schema;
    requires transitive com.google.protobuf; // TODO - investigate

    exports org.phenopackets.phenopackettools.builder;
    exports org.phenopackets.phenopackettools.builder.builders;
    exports org.phenopackets.phenopackettools.builder.constants;
    exports org.phenopackets.phenopackettools.builder.exceptions;
}