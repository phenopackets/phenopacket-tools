package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.Message;
import com.fasterxml.jackson.databind.JsonNode;
import org.phenopackets.schema.v2.Phenopacket;

import java.util.List;

public interface PhenopacketValidator {

    List<? extends ValidationResult> validateJson(JsonNode jsonNode);
    List<? extends ValidationResult> validateMessage(Phenopacket phenopacket);

}
