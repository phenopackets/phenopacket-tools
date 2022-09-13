package org.phenopackets.phenopackettools.validator.core;

/**
 * {@link ValidationResult} backed by a record.
 *
 * @param validatorInfo  Information about the validator used to create the error message
 * @param level          Error or Warning?
 * @param category       An error category
 * @param message        Specific error message
 * @author Peter N Robinson
 */
record ValidationResultDefault(ValidatorInfo validatorInfo,
                               ValidationLevel level,
                               String category,
                               String message) implements ValidationResult {

}
