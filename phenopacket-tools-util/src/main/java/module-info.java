/**
 * A module with utility functions.
 */
module org.phenopackets.phenopackettools.util {
    requires transitive org.phenopackets.phenopackettools.core;
    requires com.google.protobuf;
    // The `print` package exposes `JsonFormat.Printer`, hence the transitive export.
    requires transitive com.google.protobuf.util;
    requires org.phenopackets.schema;

    exports org.phenopackets.phenopackettools.util.format;
    exports org.phenopackets.phenopackettools.util.message;
    exports org.phenopackets.phenopackettools.util.print;
}