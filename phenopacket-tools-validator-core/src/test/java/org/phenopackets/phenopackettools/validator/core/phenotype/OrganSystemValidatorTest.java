package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.schema.v2.*;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.List;
import java.util.Set;

import static org.phenopackets.phenopackettools.validator.core.phenotype.Utils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrganSystemValidatorTest {

    private static final Ontology HPO = TestData.HPO;
    private static final Set<TermId> ABNORMALITY_OF_LIMBS_ORGAN_SYSTEM = Set.of(TermId.of("HP:0040064"));
    // Not a real organ system, but for the sake of testing...
    private static final Set<TermId> SLENDER_FINGER_ORGAN_SYSTEM = Set.of(TermId.of("HP:0001238"));

    @Nested
    public class PhenopacketTest {

        private PhenopacketValidator<PhenopacketOrBuilder> abnormalityOfLimbValidator;
        private PhenopacketValidator<PhenopacketOrBuilder> slenderFingerValidator;

        @BeforeEach
        public void setUp() {
            abnormalityOfLimbValidator = HpoPhenotypeValidators.OrganSystem.phenopacketHpoOrganSystemValidator(HPO, ABNORMALITY_OF_LIMBS_ORGAN_SYSTEM);
            slenderFingerValidator = HpoPhenotypeValidators.OrganSystem.phenopacketHpoOrganSystemValidator(HPO, SLENDER_FINGER_ORGAN_SYSTEM);
        }

        @Test
        public void noValidationErrorsIfOrganSystemIsAnnotated() {
            // Has Arachnodactyly.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", "example-subject",
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", false)
            ).build();

            List<ValidationResult> results = abnormalityOfLimbValidator.validate(pp);

            assertThat(results, is(empty()));
        }

        @Test
        public void noValidationErrorsIfOrganSystemAbnormalityIsExcluded() {
            // Has Arachnodactyly.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", "example-subject",
                    createPhenotypicFeature("HP:0040064", "Abnormality of limbs", true)
            ).build();

            List<ValidationResult> results = abnormalityOfLimbValidator.validate(pp);

            assertThat(results, is(empty()));
        }

        @ParameterizedTest
        @CsvSource({
                "true",
                "false"
        })
        public void annotationAbsenceLeadsToAnError(boolean excluded) {
            // Long fingers and Slender finger are siblings, hence no annotation here.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", "example-subject",
                    createPhenotypicFeature("HP:0100807", "Long fingers", excluded)
            ).build();

            List<ValidationResult> results = slenderFingerValidator.validate(pp);

            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.validatorInfo(), equalTo(slenderFingerValidator.validatorInfo()));
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Missing organ system annotation"));
            assertThat(result.message(), equalTo("Missing annotation for Slender finger [HP:0001238] in 'example-subject'"));
        }
    }

    /**
     * White-box testing - we know that the {@link PhenotypicFeature} is an attribute of a {@link Phenopacket}, so we
     * test the validation logic extensively in {@link OrganSystemValidatorTest.PhenopacketTest}.
     * The {@link OrganSystemValidatorTest.FamilyTest} test suite ensures there are not errors in a valid input.
     */
    @Nested
    public class FamilyTest {

        private PhenopacketValidator<FamilyOrBuilder> abnormalityOfLimbValidator;

        @BeforeEach
        public void setUp() {
            abnormalityOfLimbValidator = HpoPhenotypeValidators.OrganSystem.familyHpoOrganSystemValidator(HPO, ABNORMALITY_OF_LIMBS_ORGAN_SYSTEM);
        }

        @Test
        public void testValidInput() {
            Family family = Family.newBuilder()
                    .setProband(createPhenopacket("example-phenopacket", "example-subject",
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", false))
                            .build())
                    .addRelatives(createPhenopacket("dad-phenopacket", "example-dad",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false))
                            .build())
                    .addRelatives(createPhenopacket("mom-phenopacket", "other-mom",
                            createPhenotypicFeature("HP:0100807", "Long fingers", false))
                            .build())
                    .build();

            List<ValidationResult> results = abnormalityOfLimbValidator.validate(family);

            assertThat(results, is(empty()));
        }
    }

    /**
     * White-box testing (same as in {@link OrganSystemValidatorTest.FamilyTest}) - we know that the {@link PhenotypicFeature}
     * is an attribute of a {@link Phenopacket}, so we test the validation logic extensively
     * in {@link OrganSystemValidatorTest.PhenopacketTest}.
     * The {@link OrganSystemValidatorTest.CohortTest} test suite ensures there are not errors in valid input.
     */
    @Nested
    public class CohortTest {

        private PhenopacketValidator<CohortOrBuilder> abnormalityOfLimbValidator;

        @BeforeEach
        public void setUp() {
            abnormalityOfLimbValidator = HpoPhenotypeValidators.OrganSystem.cohortHpoOrganSystemValidator(HPO, ABNORMALITY_OF_LIMBS_ORGAN_SYSTEM);
        }

        @Test
        public void testValidInput() {
            Cohort cohort = Cohort.newBuilder()
                    .addMembers(createPhenopacket("joe-phenopacket", "example-subject",
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", false))
                            .build())
                    .addMembers(createPhenopacket("jim-phenopacket", "example-jim",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false))
                            .build())
                    .addMembers(createPhenopacket("jane-phenopacket", "example-jane",
                            createPhenotypicFeature("HP:0100807", "Long fingers", false))
                            .build())
                    .build();

            List<ValidationResult> results = abnormalityOfLimbValidator.validate(cohort);

            assertThat(results, is(empty()));
        }
    }
}
