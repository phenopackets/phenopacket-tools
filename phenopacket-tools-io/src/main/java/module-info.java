/**
 * A module for reading and writing top-level elements of Phenopacket Schema.
 */
module org.phenopackets.phenopackettools.io {
    requires transitive org.phenopackets.phenopackettools.core; // due to being part of PhenopacketPrinterFactory API
    requires org.phenopackets.phenopackettools.util;

    requires org.phenopackets.schema;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.io;
}