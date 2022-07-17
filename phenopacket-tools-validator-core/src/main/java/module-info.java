module org.phenopackets.phenopackettools.validator.core {
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.validator.core;
    exports org.phenopackets.phenopackettools.validator.core.except;

    requires com.google.protobuf;
    requires com.google.protobuf.util;
    requires org.phenopackets.schema;

    opens org.phenopackets.phenopackettools.validator.core;
}