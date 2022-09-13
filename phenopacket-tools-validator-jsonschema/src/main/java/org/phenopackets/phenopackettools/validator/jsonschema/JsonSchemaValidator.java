package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

import java.util.List;

public interface JsonSchemaValidator {

    ValidatorInfo validatorInfo();

    List<ValidationResult> validate(JsonNode node);

}
