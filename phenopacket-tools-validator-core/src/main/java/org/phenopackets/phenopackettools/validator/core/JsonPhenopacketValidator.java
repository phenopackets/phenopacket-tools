package org.phenopackets.phenopackettools.validator.core;

import java.util.List;

public interface JsonPhenopacketValidator {

    ValidatorInfo info();

    List<ValidationItem> validate(String json);

}
