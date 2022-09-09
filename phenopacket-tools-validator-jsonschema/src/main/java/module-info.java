module org.phenopackets.phenopackettools.validator.jsonschema {
    requires transitive org.phenopackets.phenopackettools.validator.core;
    requires org.phenopackets.schema;
    requires com.fasterxml.jackson.databind;
    requires json.schema.validator;
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.validator.jsonschema;

    opens org.phenopackets.phenopackettools.validator.jsonschema;
}