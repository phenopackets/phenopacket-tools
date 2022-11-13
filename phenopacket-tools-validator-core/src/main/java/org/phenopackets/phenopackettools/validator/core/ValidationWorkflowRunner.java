package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.MessageOrBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * {@link ValidationWorkflowRunner} validates selected top-level element of the Phenopacket Schema.
 * <p>
 * The validation is performed on 3 input types: {@link #validate(MessageOrBuilder)} validates an existing top-level
 * element, {@link #validate(String)} validates input formatted either
 * in {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#JSON}
 * or {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#YAML},
 * and {@link #validate(byte[])} validates a pile of bytes that can be in either
 * of the {@link org.phenopackets.phenopackettools.core.PhenopacketFormat}s.
 * <p>
 * Validator provides a list with {@link ValidatorInfo} that describes validations
 * done by the {@link ValidationWorkflowRunner}.
 * <p>
 * The validation is generally done in 2 phases, <em>syntax</em> and <em>semantic</em> phases.
 * The <em>syntax</em> phase checks if the building blocks meet the requirements independently
 * (e.g. all required fields are defined for a {@link org.phenopackets.schema.v2.core.Resource}).
 * The <em>semantic</em> validation checks for presence of errors in the context of the entire top-level element
 * (e.g. a phenopacket contains an HPO term but an HPO {@link org.phenopackets.schema.v2.core.Resource} is missing
 * in {@link org.phenopackets.schema.v2.core.MetaData}).
 *
 * @param <T> type of the top-level element of the Phenopacket Schema.
 */
public interface ValidationWorkflowRunner<T extends MessageOrBuilder> {

    /**
     * @return a list with {@link ValidatorInfo}s describing the validations done
     * by the {@link ValidationWorkflowRunner}.
     */
    List<ValidatorInfo> validators();

    /**
     * Validate a top-level element starting from a pile of bytes.
     *
     * @param payload top-level element in one of the {@link org.phenopackets.phenopackettools.core.PhenopacketFormat}s.
     * @return the validation results.
     */
    ValidationResults validate(byte[] payload);

    /**
     * Validate a top-level element starting from a string.
     *
     * @param value top-level element in either {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#JSON}
     *              or {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#YAML}.
     * @return the validation results.
     */
    // TODO - include YAML validation.
    ValidationResults validate(String value);

    /**
     * Validate a top-level element starting from a protobuf item.
     *
     * @param item the top-level element as protobuf item.
     * @return the validation results.
     */
    ValidationResults validate(T item);

    default ValidationResults validate(InputStream is) throws IOException {
        return validate(is.readAllBytes());
    }

    default ValidationResults validate(Path path) throws IOException {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(path))) {
            return validate(is);
        }
    }
}
