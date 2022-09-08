package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.databind.JsonNode;
import org.phenopackets.schema.v2.Phenopacket;

import java.util.List;

@Deprecated(forRemoval = true) // or move away from here
public interface PhenopacketValidator {

    List<ValidationResult> validateJson(JsonNode jsonNode);
    List<ValidationResult> validateMessage(Phenopacket phenopacket);

}
