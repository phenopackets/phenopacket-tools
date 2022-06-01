module org.phenopacket.tools.cli {
    requires org.phenopacket.tools.converter;
    requires org.phenopacket.tools.builder;
    requires org.phenopacket.tools.validator.jsonschema;

    requires com.google.protobuf;
    requires com.google.protobuf.util;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires info.picocli;

    opens org.phenopackets.phenopackettools.command to info.picocli;
}