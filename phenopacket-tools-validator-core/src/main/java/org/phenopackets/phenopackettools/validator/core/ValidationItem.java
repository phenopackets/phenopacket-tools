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

    // TODO - decide which enum to use here - either ErrorType or ValidationAspect
    ErrorType errorType();

    /**
     * @return string with description of the issue intended for human consumption.
     */
    String message();

}
