package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.MessageOrBuilder;

import java.util.List;

/**
 * {@link PhenopacketValidator} validates a top-level component of Phenopacket schema.
 * <p>
 * The top-level component must be one of the following types:
 * <ul>
 *     <li>{@link org.phenopackets.schema.v2.Phenopacket}</li>
 *     <li>{@link org.phenopackets.schema.v2.Family}</li>
 *     <li>{@link org.phenopackets.schema.v2.Cohort}</li>
 * </ul>
 *
 * @param <T> type of the top-level component.
 */
public interface PhenopacketValidator<T extends MessageOrBuilder> {

    ValidatorInfo validatorInfo();

    List<ValidationResult> validate(T component);

}
