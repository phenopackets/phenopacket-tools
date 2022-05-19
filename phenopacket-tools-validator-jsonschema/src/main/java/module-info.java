module org.phenopacket.tools.validator.jsonschema {
    requires transitive org.phenopacket.tools.validator.core;

    requires com.fasterxml.jackson.databind;
    requires json.schema.validator;
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.validator.jsonschema;
}