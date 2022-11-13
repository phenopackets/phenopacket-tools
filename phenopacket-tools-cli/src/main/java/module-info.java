module org.phenopackets.phenopackettools.cli {
    requires org.phenopackets.phenopackettools.util;
    requires org.phenopackets.phenopackettools.io;
    requires org.phenopackets.phenopackettools.converter;
    requires org.phenopackets.phenopackettools.builder;
    requires org.phenopackets.phenopackettools.validator.jsonschema;
    requires org.phenopackets.phenopackettools.validator.core;
    requires org.monarchinitiative.phenol.core;
    requires org.monarchinitiative.phenol.io;

    requires info.picocli;
    requires commons.csv;
    requires org.slf4j;
    requires logback.classic;

    opens org.phenopackets.phenopackettools.cli.command to info.picocli;
    opens org.phenopackets.phenopackettools.cli.command.validate to info.picocli;
}