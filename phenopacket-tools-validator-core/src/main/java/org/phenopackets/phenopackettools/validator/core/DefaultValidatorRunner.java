package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorException;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorRuntimeException;
import org.phenopackets.phenopackettools.validator.core.impl.DefaultPhenopacketIngestor;
import org.phenopackets.phenopackettools.validator.core.impl.Ingestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DefaultValidatorRunner implements ValidationWorkflowRunner{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultValidatorRunner.class);

    private final List<? extends PhenopacketValidator> jsonValidators;
    private final List<? extends PhenopacketValidator> messageValidators;


    public DefaultValidatorRunner(List<? extends PhenopacketValidator> jsonValidators,
                                  List<? extends PhenopacketValidator> messageValidators) {
        this.messageValidators = jsonValidators;
        this.jsonValidators = messageValidators;
    }


    public List<ValidationResult> validate(InputStream stream) {
        try {
            Ingestor ingestor = new DefaultPhenopacketIngestor(stream);
            JsonNode jsonNode = ingestor.jsonNode();
            Message message = ingestor.message();
            return validateImpl(jsonNode, message);
        } catch (PhenopacketValidatorException e) {
            return List.of(ValidationResult.inputError(e.getMessage()));
        }
    }

    @Override
    public List<ValidationResult> validate(byte[] content) {
        try {
            Ingestor ingestor = new DefaultPhenopacketIngestor(content);
            JsonNode jsonNode = ingestor.jsonNode();
            Message message = ingestor.message();
            return validateImpl(jsonNode, message);
        } catch (PhenopacketValidatorException e) {
            return List.of(ValidationResult.inputError(e.getMessage()));
        }
    }

    @Override
    public List<ValidationResult> validate(byte[] content, Charset charset) {
        try {
            Ingestor ingestor = new DefaultPhenopacketIngestor(content, charset);
            JsonNode jsonNode = ingestor.jsonNode();
            Message message = ingestor.message();
            return validateImpl(jsonNode, message);
        } catch (PhenopacketValidatorException e) {
            return List.of(ValidationResult.inputError(e.getMessage()));
        }
    }

    @Override
    public List<ValidationResult> validate(String content) {
        try {
            Ingestor ingestor = new DefaultPhenopacketIngestor(content);
            JsonNode jsonNode = ingestor.jsonNode();
            Message message = ingestor.message();
            return validateImpl(jsonNode, message);
        } catch (PhenopacketValidatorException e) {
            return List.of(ValidationResult.inputError(e.getMessage()));
        }
    }


    private List<ValidationResult> validateImpl(JsonNode jsonNode, Message message) {
        List<ValidationResult> results = new ArrayList<>();
        for (var validator : jsonValidators) {
            results.addAll(validator.validateJson(jsonNode));
        }
        for (var validator : messageValidators) {
            results.addAll(validator.validateMessage(message));
        }
        return results;
    }

}
