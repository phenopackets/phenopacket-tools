module org.phenopackets.phenopackettools.cli {
    requires org.phenopackets.phenopackettools.util;
    requires org.phenopackets.phenopackettools.converter;
    requires org.phenopackets.phenopackettools.builder;
    requires org.phenopackets.phenopackettools.validator.jsonschema;
    requires org.phenopackets.phenopackettools.validator.core;
    requires org.monarchinitiative.phenol.core;
    requires org.monarchinitiative.phenol.io;

    requires com.google.protobuf.util;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires commons.csv;
    requires info.picocli;
    requires org.slf4j;
    requires logback.classic;

    opens org.phenopackets.phenopackettools.command to info.picocli;
    opens org.phenopackets.phenopackettools.command.validate to info.picocli;
}