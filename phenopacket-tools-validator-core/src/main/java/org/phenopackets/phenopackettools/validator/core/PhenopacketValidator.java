package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.Message;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface PhenopacketValidator {

    List<? extends ValidationResult> validateJson(JsonNode jsonNode);
    List<? extends ValidationResult> validateMessage(Message message);

}
