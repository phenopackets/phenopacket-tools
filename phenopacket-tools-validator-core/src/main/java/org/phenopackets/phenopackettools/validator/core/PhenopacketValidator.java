package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.MessageOrBuilder;

import java.util.List;

/**
 * {@link PhenopacketValidator} represents a single step of the validation workflow.
 * The validator checks a top-level component of Phenopacket Schema.
 *
 * @param <T> type of the top-level element of the Phenopacket Schema.
 */
public interface PhenopacketValidator<T extends MessageOrBuilder> {

    /**
     * @return description of the validator and the validation logic.
     */
    ValidatorInfo validatorInfo();

    /**
     * Validate the {@code component} and summarize the results into a {@link List} of {@link ValidationResult}s.
     */
    List<ValidationResult> validate(T component);

}
