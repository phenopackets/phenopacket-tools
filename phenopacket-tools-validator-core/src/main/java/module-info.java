module org.phenopackets.phenopackettools.validator.core {

    exports org.phenopackets.phenopackettools.validator.core;
    exports org.phenopackets.phenopackettools.validator.core.except;
    exports org.phenopackets.phenopackettools.validator.core.metadata;
    exports org.phenopackets.phenopackettools.validator.core.phenotype;
    exports org.phenopackets.phenopackettools.validator.core.writer;

    requires org.phenopackets.phenopackettools.core;
    requires org.monarchinitiative.phenol.core;
    requires org.phenopackets.schema;

    requires transitive com.google.protobuf;
    requires com.google.protobuf.util;

    requires org.slf4j;

    // TODO - re-enable or remove
//    opens org.phenopackets.phenopackettools.validator.core;
}