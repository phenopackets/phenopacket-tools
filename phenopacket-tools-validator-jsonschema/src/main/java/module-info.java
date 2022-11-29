/**
 * Defines a {@link org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner} with <em>base</em>
 * validation backed by a JSON schema.
 * <p>
 * The module provides {@link org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner}
 * an implementation of {@link org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner}
 * backed by a JSON schema validator.
 *
 * @see org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner
 */
module org.phenopackets.phenopackettools.validator.jsonschema {
    requires org.phenopackets.phenopackettools.util;
    requires transitive org.phenopackets.phenopackettools.validator.core;
    requires org.phenopackets.schema;
    requires com.fasterxml.jackson.databind;
    requires json.schema.validator;
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.validator.jsonschema;

    opens org.phenopackets.phenopackettools.validator.jsonschema;
    opens org.phenopackets.phenopackettools.validator.jsonschema.v2;
}