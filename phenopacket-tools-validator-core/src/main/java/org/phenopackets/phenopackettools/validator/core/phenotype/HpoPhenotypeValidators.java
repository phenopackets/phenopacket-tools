package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.ancestry.CohortHpoAncestryValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.ancestry.FamilyHpoAncestryValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.ancestry.PhenopacketHpoAncestryValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.orgsys.CohortHpoOrganSystemValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.orgsys.FamilyHpoOrganSystemValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.orgsys.PhenopacketHpoOrganSystemValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.primary.CohortHpoPhenotypeValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.primary.FamilyHpoPhenotypeValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.primary.PhenopacketHpoPhenotypeValidator;
import org.phenopackets.schema.v2.*;

import java.util.Collection;

/**
 * Static factory class for getting {@link PhenopacketValidator}s for top-level Phenopacket schema components.
 */
public class HpoPhenotypeValidators {

    private HpoPhenotypeValidators() {
        // static factory class.
    }

    /**
     * Get {@link PhenopacketValidator} to validate {@link Phenopacket} using provided {@link MinimalOntology}.
     *
     * @param hpo HPO ontology
     * @deprecated use {@link Primary#phenopacketHpoPhenotypeValidator(MinimalOntology)} instead
     */
    // REMOVE(v1.0.0)
    @Deprecated(forRemoval = true)
    public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketHpoPhenotypeValidator(MinimalOntology hpo) {
        return Primary.phenopacketHpoPhenotypeValidator(hpo);
    }

    /**
     * Get {@link PhenopacketValidator} for validate {@link Family} using provided {@link MinimalOntology}.
     *
     * @param hpo HPO ontology
     * @deprecated use {@link Primary#familyHpoPhenotypeValidator(MinimalOntology)} instead
     */
    // REMOVE(v1.0.0)
    @Deprecated(forRemoval = true)
    public static PhenopacketValidator<FamilyOrBuilder> familyHpoPhenotypeValidator(MinimalOntology hpo) {
        return Primary.familyHpoPhenotypeValidator(hpo);
    }

    /**
     * Get {@link PhenopacketValidator} for performing primary validation {@link Cohort} using provided {@link MinimalOntology},
     * as described in {@link org.phenopackets.phenopackettools.validator.core.phenotype.primary.AbstractHpoPhenotypeValidator}.
     *
     * @param hpo HPO ontology
     * @deprecated use {@link Primary#cohortHpoPhenotypeValidator(MinimalOntology)} instead
     */
    // REMOVE(v1.0.0)
    @Deprecated(forRemoval = true)
    public static PhenopacketValidator<CohortOrBuilder> cohortHpoPhenotypeValidator(MinimalOntology hpo) {
        return Primary.cohortHpoPhenotypeValidator(hpo);
    }

    /**
     * A static factory class for providing {@link org.phenopackets.phenopackettools.validator.core.PhenopacketValidator}s
     * that check if HPO terms of the Phenopacket schema elements are present in
     * a given {@link org.monarchinitiative.phenol.ontology.data.Ontology} and if the terms are non-obsolete.
     */
    public static class Primary {
        /**
         * Get {@link PhenopacketValidator} to validate {@link Phenopacket} using provided {@link MinimalOntology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketHpoPhenotypeValidator(MinimalOntology hpo) {
            return new PhenopacketHpoPhenotypeValidator(hpo);
        }

        /**
         * Get {@link PhenopacketValidator} for validate {@link Family} using provided {@link MinimalOntology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<FamilyOrBuilder> familyHpoPhenotypeValidator(MinimalOntology hpo) {
            return new FamilyHpoPhenotypeValidator(hpo);
        }

        /**
         * Get {@link PhenopacketValidator} for performing primary validation {@link Cohort} using provided {@link MinimalOntology},
         * as described in {@link org.phenopackets.phenopackettools.validator.core.phenotype.primary.AbstractHpoPhenotypeValidator}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<CohortOrBuilder> cohortHpoPhenotypeValidator(MinimalOntology hpo) {
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
         * Get {@link PhenopacketValidator} to validate ancestry {@link Phenopacket} using provided {@link MinimalOntology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketHpoAncestryValidator(MinimalOntology hpo) {
            return new PhenopacketHpoAncestryValidator(hpo);
        }

        /**
         * Get {@link PhenopacketValidator} to validate ancestry {@link Family} using provided {@link MinimalOntology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<FamilyOrBuilder> familyHpoAncestryValidator(MinimalOntology hpo) {
            return new FamilyHpoAncestryValidator(hpo);
        }

        /**
         * Get {@link PhenopacketValidator} to validate ancestry {@link Cohort} using provided {@link MinimalOntology}.
         *
         * @param hpo HPO ontology
         */
        public static PhenopacketValidator<CohortOrBuilder> cohortHpoAncestryValidator(MinimalOntology hpo) {
            return new CohortHpoAncestryValidator(hpo);
        }
    }

    /**
     * A static factory class for providing validators for checking annotation of organ systems.
     * <p>
     * The validators check if each phenopacket or family/cohort member have annotation
     * for an organ system represented by a top-level HPO term
     * (e.g. <a href="https://hpo.jax.org/app/browse/term/HP:0040064">Abnormality of limbs</a>).
     * The annotation comprises either one or more observed descendants
     * (e.g. <a href="https://hpo.jax.org/app/browse/term/HP:0001166">Arachnodactyly</a>),
     * or excluded top-level HPO term
     * (<em>NOT</em> <a href="https://hpo.jax.org/app/browse/term/HP:0040064">Abnormality of limbs</a>).
     * <p>
     */
    public static class OrganSystem {
        private OrganSystem() {
        }

        /**
         * Get {@link PhenopacketValidator} to validate annotation of organ systems in a {@link Phenopacket}
         * using provided {@link MinimalOntology} and a collection of organ system {@link TermId}s.
         * <p>
         * <b>NOTE:</b> the organ system {@link TermId} that is absent from the {@link MinimalOntology} is disregarded
         * and not used for validation.
         *
         * @param hpo HPO ontology
         * @param organSystemTermIds a collection of HPO {@link TermId}s corresponding to organ systems.
         */
        public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketHpoOrganSystemValidator(MinimalOntology hpo,
                                                                                                    Collection<TermId> organSystemTermIds) {
            return new PhenopacketHpoOrganSystemValidator(hpo, organSystemTermIds);
        }

        /**
         * Get {@link PhenopacketValidator} to validate annotation of organ systems in a {@link Family}
         * using provided {@link MinimalOntology} and a collection of organ system {@link TermId}s.
         * <p>
         * <b>NOTE:</b> the organ system {@link TermId} that is absent from the {@link MinimalOntology} is disregarded
         * and not used for validation.
         *
         * @param hpo HPO ontology
         * @param organSystemTermIds a collection of HPO {@link TermId}s corresponding to organ systems.
         */
        public static PhenopacketValidator<FamilyOrBuilder> familyHpoOrganSystemValidator(MinimalOntology hpo,
                                                                                          Collection<TermId> organSystemTermIds) {
            return new FamilyHpoOrganSystemValidator(hpo, organSystemTermIds);
        }

        /**
         * Get {@link PhenopacketValidator} to validate annotation of organ systems in a {@link Cohort}
         * using provided {@link MinimalOntology} and a collection of organ system {@link TermId}s.
         * <p>
         * <b>NOTE:</b> the organ system {@link TermId} that is absent from the {@link MinimalOntology} is disregarded
         * and not used for validation.
         *
         * @param hpo HPO ontology
         * @param organSystemTermIds a collection of HPO {@link TermId}s corresponding to organ systems.
         */
        public static PhenopacketValidator<CohortOrBuilder> cohortHpoOrganSystemValidator(MinimalOntology hpo,
                                                                                          Collection<TermId> organSystemTermIds) {
            return new CohortHpoOrganSystemValidator(hpo, organSystemTermIds);
        }
    }

}
