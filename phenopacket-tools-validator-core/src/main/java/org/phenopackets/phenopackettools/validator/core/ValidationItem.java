package org.phenopackets.phenopackettools.validator.core;

/**
 * The interface represents a single issue found during validation.
 * <p>
 * @author Daniel Danis
 * @author Peter N Robinson
 */
public interface ValidationItem {
    /**
     * @return basic description of the validator that produced this issue.
     */
    ValidatorInfo validatorInfo();

    /**
     * @return the error or warning produce by a validator
     */
    ValidationErrorType errorType();


}
