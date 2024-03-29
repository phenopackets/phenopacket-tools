/**
 * A module for converting between the {@link org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion}s.
 */
module org.phenopackets.phenopackettools.converter {
    requires transitive org.phenopackets.schema;
    requires org.phenopackets.phenopackettools.core;
    requires org.phenopackets.phenopackettools.builder;
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.converter.converters;
}