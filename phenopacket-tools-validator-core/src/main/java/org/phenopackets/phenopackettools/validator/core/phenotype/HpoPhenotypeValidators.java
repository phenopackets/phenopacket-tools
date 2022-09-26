package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.schema.v2.*;

/**
 * Static factory class for getting {@link PhenopacketValidator}s for top-level Phenopacket schema components.
 */
public class HpoPhenotypeValidators {

    private HpoPhenotypeValidators() {
        // static factory class.
    }

    /**
     * Get {@link PhenopacketValidator} to validate {@link Phenopacket} using provided {@link Ontology}.
     *
     * @param hpo HPO ontology
     */
    public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketHpoPhenotypeValidator(Ontology hpo) {
        return new PhenopacketHpoPhenotypeValidator(hpo);
    }

    /**
     * Get {@link PhenopacketValidator} to validate {@link Family} using provided {@link Ontology}.
     *
     * @param hpo HPO ontology
     */
    public static PhenopacketValidator<FamilyOrBuilder> familyHpoPhenotypeValidator(Ontology hpo) {
        return new FamilyHpoPhenotypeValidator(hpo);
    }

    /**
     * Get {@link PhenopacketValidator} to validate {@link Cohort} using provided {@link Ontology}.
     *
     * @param hpo HPO ontology
     */
    public static PhenopacketValidator<CohortOrBuilder> cohortHpoPhenotypeValidator(Ontology hpo) {
        return new CohortHpoPhenotypeValidator(hpo);
    }

}
