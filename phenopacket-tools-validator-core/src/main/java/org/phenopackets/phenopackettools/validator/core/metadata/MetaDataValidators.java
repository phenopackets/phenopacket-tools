package org.phenopackets.phenopackettools.validator.core.metadata;

import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

/**
 * Static factory class for providing {@link PhenopacketValidator}s for performing <em>metadata validation</em>.
 */
public class MetaDataValidators {

    private static volatile PhenopacketValidator<PhenopacketOrBuilder> PHENOPACKET_VALIDATOR = null;
    private static volatile PhenopacketValidator<FamilyOrBuilder> FAMILY_VALIDATOR = null;
    private static volatile PhenopacketValidator<CohortOrBuilder> COHORT_VALIDATOR = null;

    private MetaDataValidators() {
    }

    /**
     * Get {@link PhenopacketValidator} for checking that {@link org.phenopackets.schema.v2.core.MetaData}
     * of {@link PhenopacketOrBuilder} contains {@link org.phenopackets.schema.v2.core.Resource}s for prefixes of
     * all {@link org.phenopackets.schema.v2.core.OntologyClass} present
     * in given {@link PhenopacketOrBuilder}.
     *
     * @return the validator
     */
    public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketValidator() {
        if (PHENOPACKET_VALIDATOR == null) {
            synchronized (MetaDataValidators.class) {
                if (PHENOPACKET_VALIDATOR == null)
                    PHENOPACKET_VALIDATOR = new PhenopacketMetaDataValidator();
            }
        }
        return PHENOPACKET_VALIDATOR;
    }

    /**
     * Get {@link PhenopacketValidator} for checking that {@link org.phenopackets.schema.v2.core.MetaData}
     * of {@link FamilyOrBuilder} contains {@link org.phenopackets.schema.v2.core.Resource}s for prefixes of
     * all {@link org.phenopackets.schema.v2.core.OntologyClass} present
     * in given {@link FamilyOrBuilder}.
     *
     * @return the validator
     */
    public static PhenopacketValidator<FamilyOrBuilder> familyValidator() {
        if (FAMILY_VALIDATOR == null) {
            synchronized (MetaDataValidators.class) {
                if (FAMILY_VALIDATOR == null)
                    FAMILY_VALIDATOR = new FamilyMetaDataValidator();
            }
        }
        return FAMILY_VALIDATOR;
    }

    /**
     * Get {@link PhenopacketValidator} for checking that {@link org.phenopackets.schema.v2.core.MetaData}
     * of {@link CohortOrBuilder} contains {@link org.phenopackets.schema.v2.core.Resource}s for prefixes of
     * all {@link org.phenopackets.schema.v2.core.OntologyClass} present
     * in given {@link CohortOrBuilder}.
     *
     * @return the validator
     */
    public static PhenopacketValidator<CohortOrBuilder> cohortValidator() {
        if (COHORT_VALIDATOR == null) {
            synchronized (MetaDataValidators.class) {
                if (COHORT_VALIDATOR == null)
                    COHORT_VALIDATOR = new CohortMetaDataValidator();
            }
        }
        return COHORT_VALIDATOR;
    }

}
