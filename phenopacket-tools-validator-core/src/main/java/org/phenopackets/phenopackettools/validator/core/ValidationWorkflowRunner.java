package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.MessageOrBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * {@link ValidationWorkflowRunner} validates selected top-level element of the Phenopacket schema.
 * <p>
 * The validation is performed on 3 input types: {@link #validate(MessageOrBuilder)} validates an existing top-level
 * element, {@link #validate(String)} validates input formatted in JSON format,
 * and {@link #validate(byte[])} validates bytes that can be either in JSON or Protobuf binary exchange format.
 * <p>
 * Validator provides a list with {@link ValidatorInfo} that describes validations
 * done by the {@link ValidationWorkflowRunner}.
 *
 * @param <T> type of the top-level element of the Phenopacket schema.
 */
public interface ValidationWorkflowRunner<T extends MessageOrBuilder> {

    /**
     * @return a list with {@link ValidatorInfo}s describing the validations done
     * by the {@link ValidationWorkflowRunner}.
     */
    List<ValidatorInfo> validators();

    ValidationResults validate(byte[] payload);

    ValidationResults validate(String json);

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
