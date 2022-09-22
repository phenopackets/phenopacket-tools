module org.phenopackets.phenopackettools.validator.core {
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.validator.core;
    exports org.phenopackets.phenopackettools.validator.core.except;
    exports org.phenopackets.phenopackettools.validator.core.phenotype;

    requires transitive com.google.protobuf;
    requires com.google.protobuf.util;
    requires org.phenopackets.schema;

    requires org.monarchinitiative.phenol.core;

    opens org.phenopackets.phenopackettools.validator.core;
    exports org.phenopackets.phenopackettools.validator.core.metadata;
}