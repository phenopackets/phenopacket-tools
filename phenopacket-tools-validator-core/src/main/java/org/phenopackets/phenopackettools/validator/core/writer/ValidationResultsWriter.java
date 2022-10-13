package org.phenopackets.phenopackettools.validator.core.writer;

import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;

import java.io.IOException;
import java.util.List;

/**
 * Write out validation results obtained from validation of a top-level Phenopacket schema element.
 */
public interface ValidationResultsWriter {

    /**
     * Write out the provided {@code validators} and {@code results}.
     *
     * @param validators a list with {@link ValidatorInfo} describing {@link org.phenopackets.phenopackettools.validator.core.PhenopacketValidator}
     *                   used to validate the top-level element.
     * @param results    a list with {@link ValidationResultsAndPath} received from the validator.
     * @throws IOException in case of IO errors, of course.
     */
    void writeValidationResults(List<ValidatorInfo> validators,
                                List<ValidationResultsAndPath> results) throws IOException;

}
