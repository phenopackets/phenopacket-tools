package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;

import java.util.Objects;

/**
 * A {@link ValidationWorkflowDispatcher} implementation that uses 3 {@link ValidationWorkflowRunner} to validate
 * top-level elements of the Phenopacket schema.
 */
public class ValidationWorkflowDispatcherImpl implements ValidationWorkflowDispatcher {

    private final ValidationWorkflowRunner<Phenopacket> phenopacketValidationRunner;
    private final ValidationWorkflowRunner<Family> familyValidationRunner;
    private final ValidationWorkflowRunner<Cohort> cohortValidationRunner;


    public ValidationWorkflowDispatcherImpl(ValidationWorkflowRunner<Phenopacket> phenopacketValidationRunner,
                                            ValidationWorkflowRunner<Family> familyValidationRunner,
                                            ValidationWorkflowRunner<Cohort> cohortValidationRunner) {
        this.phenopacketValidationRunner = Objects.requireNonNull(phenopacketValidationRunner);
        this.familyValidationRunner = Objects.requireNonNull(familyValidationRunner);
        this.cohortValidationRunner = Objects.requireNonNull(cohortValidationRunner);
    }

    @Override
    public ValidationResults validatePhenopacket(Phenopacket phenopacket) {
        return phenopacketValidationRunner.validate(phenopacket);
    }

    @Override
    public ValidationResults validatePhenopacket(String string) {
        return phenopacketValidationRunner.validate(string);
    }

    @Override
    public ValidationResults validatePhenopacket(byte[] bytes) {
        return phenopacketValidationRunner.validate(bytes);
    }

    @Override
    public ValidationResults validateFamily(Family family) {
        return familyValidationRunner.validate(family);
    }

    @Override
    public ValidationResults validateFamily(String string) {
        return familyValidationRunner.validate(string);
    }

    @Override
    public ValidationResults validateFamily(byte[] bytes) {
        return familyValidationRunner.validate(bytes);
    }

    @Override
    public ValidationResults validateCohort(Cohort cohort) {
        return cohortValidationRunner.validate(cohort);
    }

    @Override
    public ValidationResults validateCohort(String string) {
        return cohortValidationRunner.validate(string);
    }

    @Override
    public ValidationResults validateCohort(byte[] bytes) {
        return cohortValidationRunner.validate(bytes);
    }

}
