module org.phenopacket.tools.converter {
    // TODO - needs to be fixed after modular phenopacket schema is deployed to MC
    requires transitive phenopacket.schema;
    requires com.google.protobuf;

    // TODO - do we want to export the `v2` package as well?
    exports org.phenopackets.phenopackettools.converter.converters;
}