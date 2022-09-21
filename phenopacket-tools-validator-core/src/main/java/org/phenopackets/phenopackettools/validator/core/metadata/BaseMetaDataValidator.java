package org.phenopackets.phenopackettools.validator.core.metadata;

import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

import java.util.List;

abstract public class BaseMetaDataValidator<T extends MessageOrBuilder> implements PhenopacketValidator<T> {

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "MetaDataValidator",
            "MetaDataValidator for Phenopacket, Family, and Cohort",
            "Validate that the MetaData section includes information about all ontologies used");

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }


}
