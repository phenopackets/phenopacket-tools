package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;

/**
 * {@link ValidationWorkflowDispatcher} exposes endpoints for validating top-level elements of Phenopacket Schema
 * and dispatches the data into the appropriate {@link ValidationWorkflowRunner}.
 */
public interface ValidationWorkflowDispatcher {

    static ValidationWorkflowDispatcher of(ValidationWorkflowRunner<Phenopacket> phenopacketValidationRunner,
                                           ValidationWorkflowRunner<Family> familyValidationRunner,
                                           ValidationWorkflowRunner<Cohort> cohortValidationRunner) {
        return new ValidationWorkflowDispatcherImpl(phenopacketValidationRunner, familyValidationRunner, cohortValidationRunner);
    }

    /**
     * Validate a phenopacket starting from a pile of bytes.
     *
     * @param bytes that can represent a phenopacket in either
     *              of {@link org.phenopackets.phenopackettools.core.PhenopacketFormat}s.
     * @return validation results.
     */
    ValidationResults validatePhenopacket(byte[] bytes);

    /**
     * Validate a phenopacket starting from a string.
     *
     * @param string that can represent a phenopacket either
     *               in {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#JSON}
     *               or {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#YAML} format.
     * @return validation results.
     */
    ValidationResults validatePhenopacket(String string);

    /**
     * Validate a phenopacket starting from a protobuf object.
     *
     * @param phenopacket to be validated.
     * @return validation results.
     */
    ValidationResults validatePhenopacket(Phenopacket phenopacket);

    /**
     * Validate a family starting from a pile of bytes.
     *
     * @param bytes that can represent a family in either
     *              of {@link org.phenopackets.phenopackettools.core.PhenopacketFormat}s.
     * @return validation results.
     */
    ValidationResults validateFamily(byte[] bytes);

    /**
     * Validate a family starting from a string.
     *
     * @param string that can represent a family either
     *               in {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#JSON}
     *               or {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#YAML} format.
     * @return validation results.
     */
    ValidationResults validateFamily(String string);

    /**
     * Validate a family starting from a protobuf object.
     *
     * @param family to be validated.
     * @return validation results.
     */
    ValidationResults validateFamily(Family family);

    /**
     * Validate a cohort starting from a pile of bytes.
     *
     * @param bytes that can represent a cohort in either
     *              of {@link org.phenopackets.phenopackettools.core.PhenopacketFormat}s.
     * @return validation results.
     */
    ValidationResults validateCohort(byte[] bytes);

    /**
     * Validate a cohort starting from a string.
     *
     * @param string that can represent a cohort either
     *               in {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#JSON}
     *               or {@link org.phenopackets.phenopackettools.core.PhenopacketFormat#YAML} format.
     * @return validation results.
     */
    ValidationResults validateCohort(String string);

    /**
     * Validate a cohort starting from a protobuf object.
     *
     * @param cohort to be validated.
     * @return validation results.
     */
    ValidationResults validateCohort(Cohort cohort);

}
