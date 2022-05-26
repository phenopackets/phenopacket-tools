module org.phenopacket.tools.builder {
    // TODO - needs to be fixed after modular phenopacket schema is deployed to MC
    requires transitive phenopacket.schema;

    requires com.google.protobuf;

    exports org.phenopackets.phenopackettools.builder;
    exports org.phenopackets.phenopackettools.builder.builders;
    exports org.phenopackets.phenopackettools.builder.constants;
    exports org.phenopackets.phenopackettools.builder.exceptions;
}