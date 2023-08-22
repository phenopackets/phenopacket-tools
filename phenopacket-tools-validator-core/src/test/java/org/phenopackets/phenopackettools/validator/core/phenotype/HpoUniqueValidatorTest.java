package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.TestData;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.*;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.phenopackets.phenopackettools.validator.core.phenotype.Utils.createPhenopacket;
import static org.phenopackets.phenopackettools.validator.core.phenotype.Utils.createPhenotypicFeature;

public class HpoUniqueValidatorTest {

    private static final MinimalOntology HPO = TestData.HPO;

    @Nested
    public class PhenopacketTest {

        private PhenopacketValidator<PhenopacketOrBuilder> validator;

        @BeforeEach
        public void setUp() {
            validator = HpoPhenotypeValidators.Unique.phenopacketValidator(HPO);
        }

        @Test
        public void testValidInput() {
            // Has some Abnormality of finger but no Arachnodactyly.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", "example-subject",
                    createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", true)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, is(empty()));
        }

        @Test
        public void testFailsIfObservedTermIsPresentTwice() {
            // Has Arachnodactyly twice.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", "example-subject",
                    createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", false),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", false)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.validatorInfo(), equalTo(validator.validatorInfo()));
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Non-unique phenotypic feature"));
            assertThat(result.message(), equalTo("""
        Phenotypic features of example-phenopacket must not contain the same observed feature Arachnodactyly (HP:0001166)
        more than once but the feature was present 2 times"""));
        }

        @Test
        public void testFailsIfExcludedTermIsPresentTwice() {
            // Has excluded Arachnodactyly twice.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", "example-subject",
                    createPhenotypicFeature("HP:0001167", "Abnormality of finger", true),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", true),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", true)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.validatorInfo(), equalTo(validator.validatorInfo()));
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Non-unique phenotypic feature"));
            assertThat(result.message(), equalTo("""
        Phenotypic features of example-phenopacket must not contain the same excluded feature Arachnodactyly (HP:0001166)
        more than once but the feature was present 2 times"""));
        }
    }

    /**
     * White-box testing - we know that the {@link PhenotypicFeature} is an attribute of a {@link Phenopacket}, so we
     * test the validation logic extensively in {@link HpoUniqueValidatorTest.PhenopacketTest}.
     * The {@link HpoUniqueValidatorTest.FamilyTest} test suite ensures there are not errors in valid input.
     */
    @Nested
    public class FamilyTest {

        private PhenopacketValidator<FamilyOrBuilder> validator;

        @BeforeEach
        public void setUp() {
            validator = HpoPhenotypeValidators.Unique.familyValidator(HPO);
        }

        @Test
        public void testValidInput() {
            Family family = Family.newBuilder()
                    .setProband(createPhenopacket("example-phenopacket", "example-subject",
                            createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", true))
                            .build())
                    .addRelatives(createPhenopacket("dad-phenopacket", "example-dad",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false),
                            createPhenotypicFeature("HP:0100807", "Long fingers", false))
                            .build())
                    .addRelatives(createPhenopacket("mom-phenopacket", "example-mom",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false),
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", true))
                            .build())
                    .build();

            List<ValidationResult> results = validator.validate(family);

            assertThat(results, is(empty()));
        }
    }

    /**
     * White-box testing (same as in {@link HpoUniqueValidatorTest.FamilyTest}) - we know that the {@link PhenotypicFeature}
     * is an attribute of a {@link Phenopacket}, so we test the validation logic extensively
     * in {@link HpoUniqueValidatorTest.PhenopacketTest}.
     * The {@link HpoUniqueValidatorTest.CohortTest} test suite ensures there are not errors in valid input.
     */
    @Nested
    public class CohortTest {

        private PhenopacketValidator<CohortOrBuilder> validator;

        @BeforeEach
        public void setUp() {
            validator = HpoPhenotypeValidators.Ancestry.cohortHpoAncestryValidator(HPO);
        }

        @Test
        public void testValidInput() {
            Cohort cohort = Cohort.newBuilder()
                    .addMembers(createPhenopacket("joe-phenopacket", "example-subject",
                            createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", true))
                            .build())
                    .addMembers(createPhenopacket("jim-phenopacket", "example-jim",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false),
                            createPhenotypicFeature("HP:0100807", "Long fingers", false))
                            .build())
                    .addMembers(createPhenopacket("jane-phenopacket", "example-jane",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false),
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", true))
                            .build())
                    .build();

            List<ValidationResult> results = validator.validate(cohort);

            assertThat(results, is(empty()));
        }
    }

}