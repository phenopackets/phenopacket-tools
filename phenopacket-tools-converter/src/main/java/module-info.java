module org.phenopackets.phenopackettools.converter {
    requires transitive org.phenopackets.schema;
    requires org.phenopackets.phenopackettools.builder;

    exports org.phenopackets.phenopackettools.converter.converters;
}