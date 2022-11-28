/**
 * Defines the base APIs for phenopacket validation.
 */
module org.phenopackets.phenopackettools.validator.core {

    exports org.phenopackets.phenopackettools.validator.core;
    exports org.phenopackets.phenopackettools.validator.core.except;
    exports org.phenopackets.phenopackettools.validator.core.metadata;
    exports org.phenopackets.phenopackettools.validator.core.phenotype;
    exports org.phenopackets.phenopackettools.validator.core.writer;

    requires org.phenopackets.phenopackettools.core;
    requires org.phenopackets.phenopackettools.util;
    requires org.monarchinitiative.phenol.core;
    requires org.phenopackets.schema;

    // There are many places where the protobuf classes are part of the API, e.g. as type parameter
    // of PhenopacketFormatConverter.
    requires transitive com.google.protobuf;

    requires org.slf4j;

    // TODO - re-enable or remove
//    opens org.phenopackets.phenopackettools.validator.core;
}