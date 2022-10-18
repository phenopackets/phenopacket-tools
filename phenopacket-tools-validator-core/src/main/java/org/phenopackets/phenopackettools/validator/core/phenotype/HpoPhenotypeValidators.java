package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.ancestry.CohortHpoAncestryValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.ancestry.FamilyHpoAncestryValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.ancestry.PhenopacketHpoAncestryValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.primary.CohortHpoPhenotypeValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.primary.FamilyHpoPhenotypeValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.primary.PhenopacketHpoPhenotypeValidator;
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
     * @deprecated use {@link Primary#phenopacketHpoPhenotypeValidator(Ontology)} instead
     */
    // TODO - remove prior v1
    @Deprecated(forRemoval = true)
    public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketHpoPhenotypeValidator(Ontology hpo) {
        return Primary.phenopacketHpoPhenotypeValidator(hpo);
    }

    /**
     * Get {@link PhenopacketValidator} for validate {@link Family} using provided {@link Ontology}.
     *
     * @param hpo HPO ontology
     * @deprecated use {@link Primary#familyHpoPhenotypeValidator(Ontology)} instead
     */
    // TODO - remove prior v1
    @Deprecated(forRemoval = true)
    public static PhenopacketValidator<FamilyOrBuilder> familyHpoPhenotypeValidator(Ontology hpo) {
        return Primary.familyHpoPhenotypeValidator(hpo);
    }

    /**
     * Get {@link PhenopacketValidator} for performing primary validation {@link Cohort} using provided {@link Ontology},
     * as described in {@link org.phenopackets.phenopackettools.validator.core.phenotype.primary.AbstractHpoPhenotypeValidator}.
     *
     * @param hpo HPO ontology
     * @deprecated use {@link Primary#cohortHpoPhenotypeValidator(Ontology)} instead
     */
    // TODO - remove prior v1
    @Deprecated(forRemoval = true)
    public static PhenopacketValidator<CohortOrBuilder> cohortHpoPhenotypeValidator(Ontology hpo) {
        return Primary.cohortHpoPhenotypeValidator(hpo);
    }

    /**
     * A static factory class for providing {@link org.phenopackets.phenopackettools.validator.core.PhenopacketValidator}s
     * that check if HPO terms of the Phenopacket schema elements are present in
     * a given {@link org.monarchinitiative.phenol.ontology.data.Ontology} and if the terms are non-obsolete.
     */
    public static class Primary {
        /**
         * Get {@link PhenopacketValidator} to validate {@link Phenopacket} using provided {@link Ontology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketHpoPhenotypeValidator(Ontology hpo) {
            return new PhenopacketHpoPhenotypeValidator(hpo);
        }

        /**
         * Get {@link PhenopacketValidator} for validate {@link Family} using provided {@link Ontology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<FamilyOrBuilder> familyHpoPhenotypeValidator(Ontology hpo) {
            return new FamilyHpoPhenotypeValidator(hpo);
        }

        /**
         * Get {@link PhenopacketValidator} for performing primary validation {@link Cohort} using provided {@link Ontology},
         * as described in {@link org.phenopackets.phenopackettools.validator.core.phenotype.primary.AbstractHpoPhenotypeValidator}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<CohortOrBuilder> cohortHpoPhenotypeValidator(Ontology hpo) {
            return new CohortHpoPhenotypeValidator(hpo);
        }
    }

    /**
     * A static factory class for providing validators for pointing out violations of the annotation propagation rule.
     * <p>
     * The validator checks observed and excluded phenotype terms. The observed terms are checked for a presence of
     * an observed or an excluded ancestor, and a presence of such ancestor is pointed out as an error.
     * For instance, Abnormality of finger or <em>"NOT"</em> Abnormality of finger must not be present
     * in a patient annotated by Arachnodactyly. The <em>most specific</em> term (Arachnodactyly) must be used.
     * <p>
     * For the excluded terms, the validator checks for presence of an excluded children. Here, the least specific term
     * must be used. For instance, <em>"NOT"</em> Arachnodactyly must not be present in a patient annotated
     * with <em>"NOT"</em> Abnormality of finger. Only the <em>"NOT"</em> Abnormality of finger must be used.
     */
    public static class Ancestry {

        private Ancestry() {
        }

        /**
         * Get {@link PhenopacketValidator} to validate ancestry {@link Phenopacket} using provided {@link Ontology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketHpoAncestryValidator(Ontology hpo) {
            return new PhenopacketHpoAncestryValidator(hpo);
        }

        /**
         * Get {@link PhenopacketValidator} to validate ancestry {@link Family} using provided {@link Ontology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<FamilyOrBuilder> familyHpoAncestryValidator(Ontology hpo) {
            return new FamilyHpoAncestryValidator(hpo);
        }

        /**
         * Get {@link PhenopacketValidator} to validate ancestry {@link Cohort} using provided {@link Ontology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<CohortOrBuilder> cohortHpoAncestryValidator(Ontology hpo) {
            return new CohortHpoAncestryValidator(hpo);
        }
    }

}
