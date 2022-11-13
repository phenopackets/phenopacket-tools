/**
 * A module for reading and writing top-level elements of Phenopacket Schema.
 */
module org.phenopackets.phenopackettools.io {
    requires org.phenopackets.phenopackettools.util;

    requires org.phenopackets.schema;
    requires com.google.protobuf;
    requires com.google.protobuf.util;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.io;
}