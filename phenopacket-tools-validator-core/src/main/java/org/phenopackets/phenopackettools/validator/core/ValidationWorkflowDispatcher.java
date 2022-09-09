package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;

/**
 * {@link ValidationWorkflowDispatcher} exposes endpoints for validating top-level elements of Phenopacket schema
 * and dispatches the data into the appropriate {@link ValidationWorkflowRunner}.
 */
public interface ValidationWorkflowDispatcher {

    ValidationResults validatePhenopacket(byte[] bytes);
    ValidationResults validatePhenopacket(String string);
    ValidationResults validatePhenopacket(Phenopacket phenopacket);

    ValidationResults validateFamily(byte[] bytes);
    ValidationResults validateFamily(String string);
    ValidationResults validateFamily(Family family);

    ValidationResults validateCohort(byte[] bytes);
    ValidationResults validateCohort(String string);
    ValidationResults validateCohort(Cohort cohort);

}
