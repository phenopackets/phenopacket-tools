package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.monarchinitiative.phenol.ontology.data.TermId;

/**
 * This class contains constants that correspond to the upper-level HPO organ-system phenotypic abnormalities.
 * They can be used together with the {@link org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators.OrganSystem}
 * validators, which enforce that a phenopacket contains at least one term from a set of organ systems (observed or excluded).
 * Note that users can also use any HPO term in this way -- the validator will enforce that the phenopacket has an HPO term that descends from it,
 * but the most common use cases are these organ-level terms
 * <pre>{@code
 * Ontology hpo = ...;
 * var requiredOrganSystems = Set.of(BLOOD, CARDIOVASCULAR, SKELETAL);
 * var validator = HpoPhenotypeValidators.OrganSystem.phenopacketHpoOrganSystemValidator(hpo, requiredOrganSystems);
 * }
 * </pre>
 */
public class HpoOrganSystems {

    public static final TermId ABNORMAL_CELLULAR = TermId.of("HP:0025354");
    public static final TermId BLOOD = TermId.of("HP:0001871");
    public static final TermId CONNECTIVE_TISSUE = TermId.of("HP:0003549");
    public static final TermId HEAD_AND_NECK = TermId.of("HP:0000152");
    public static final TermId LIMBS = TermId.of("HP:0040064");
    public static final TermId METABOLISM = TermId.of("HP:0001939");
    public static final TermId PRENATAL = TermId.of("HP:0001197");
    public static final TermId BREAST = TermId.of("HP:0000769");
    public static final TermId CARDIOVASCULAR = TermId.of("HP:0001626");
    public static final TermId DIGESTIVE = TermId.of("HP:0025031");
    public static final TermId EAR = TermId.of("HP:0000598");
    public static final TermId ENDOCRINE = TermId.of("HP:0000818");
    public static final TermId EYE = TermId.of("HP:0000478");
    public static final TermId GENITOURINARY = TermId.of("HP:0000119");
    public static final TermId IMMUNOLOGY = TermId.of("HP:0002715");
    public static final TermId INTEGUMENT = TermId.of("HP:0001574");
    public static final TermId MUSCLE = TermId.of("HP:0003011");
    public static final TermId NERVOUS_SYSTEM = TermId.of("HP:0000707");
    public static final TermId RESPIRATORY = TermId.of("HP:0002086");
    public static final TermId SKELETAL = TermId.of("HP:0000924");
    public static final TermId THORACIC_CAVITY = TermId.of("HP:0045027");
    public static final TermId VOICE = TermId.of("HP:0001608");
    public static final TermId CONSTITUTIONAL = TermId.of("HP:0025142");
    public static final TermId GROWTH = TermId.of("HP:0001507");
    public static final TermId NEOPLASM = TermId.of("HP:0002664");

    private HpoOrganSystems() {
    }
}
