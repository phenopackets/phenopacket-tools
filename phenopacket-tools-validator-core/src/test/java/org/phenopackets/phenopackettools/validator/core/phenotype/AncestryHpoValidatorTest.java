package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.TestData;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.*;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AncestryHpoValidatorTest {

    private static final Ontology HPO = TestData.HPO;

    @Nested
    public class PhenopacketTest {

        private PhenopacketValidator<PhenopacketOrBuilder> validator;

        @BeforeEach
        public void setUp() {
            validator = HpoPhenotypeValidators.Ancestry.phenopacketHpoAncestryValidator(HPO);
        }

        @Test
        public void testValidInput() {
            // Has some Abnormality of finger but no Arachnodactyly.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", true)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, is(empty()));
        }

        @Test
        public void testFailsIfTermAndAncestorIsObserved() {
            // Has some Abnormality of finger and Arachnodactyly. Only Arachnodactyly should be present.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", false)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.validatorInfo(), equalTo(validator.validatorInfo()));
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Violation of the annotation propagation rule"));
            assertThat(result.message(), equalTo("Phenotypic features of example-phenopacket must not contain both an observed term (Arachnodactyly, HP:0001166) and an observed ancestor (Abnormality of finger, HP:0001167)"));
        }

        @Test
        public void testFailsIfTermAndAncestorIsExcluded() {
            // Has neither Abnormality of finger nor Arachnodactyly. Only Abnormality of finger should be present.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", createPhenotypicFeature("HP:0001167", "Abnormality of finger", true),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", true)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Violation of the annotation propagation rule"));
            assertThat(result.message(), equalTo("Phenotypic features of example-phenopacket must not contain both an excluded term (Abnormality of finger, HP:0001167) and an excluded child (Arachnodactyly, HP:0001166)"));
        }

        @Test
        public void testFailsIfTermIsPresentAndAncestorIsExcluded() {
            // Has neither Abnormality of finger nor Arachnodactyly. Only Abnormality of finger should be present.
            Phenopacket pp = createPhenopacket(
                    "example-phenopacket", createPhenotypicFeature("HP:0001167", "Abnormality of finger", true),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", false)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Violation of the annotation propagation rule"));
            assertThat(result.message(), equalTo("Phenotypic features of example-phenopacket must not contain both an observed term (Arachnodactyly, HP:0001166) and an excluded ancestor (Abnormality of finger, HP:0001167)"));
        }
    }

    /**
     * White-box testing - we know that the {@link PhenotypicFeature} is an attribute of a {@link Phenopacket}, so we
     * test the validation logic extensively in {@link PhenopacketTest}. The {@link FamilyTest} test suite ensures
     * there are not errors in valid input.
     */
    @Nested
    public class FamilyTest {

        private PhenopacketValidator<FamilyOrBuilder> validator;

        @BeforeEach
        public void setUp() {
            validator = HpoPhenotypeValidators.Ancestry.familyHpoAncestryValidator(HPO);
        }

        @Test
        public void testValidInput() {
            Family family = Family.newBuilder()
                    .setProband(createPhenopacket("example-phenopacket",
                            createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", true))
                            .build())
                    .addRelatives(createPhenopacket("dad-phenopacket",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false),
                            createPhenotypicFeature("HP:0100807", "Long fingers", false))
                            .build())
                    .addRelatives(createPhenopacket("mom-phenopacket",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false),
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", true))
                            .build())
                    .build();

            List<ValidationResult> results = validator.validate(family);

            assertThat(results, is(empty()));
        }
    }

    /**
     * White-box testing (same as in {@link FamilyTest}) - we know that the {@link PhenotypicFeature}
     * is an attribute of a {@link Phenopacket}, so we test the validation logic extensively
     * in {@link PhenopacketTest}. The {@link CohortTest} test suite ensures there are not errors in valid input.
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
                    .addMembers(createPhenopacket("joe-phenopacket",
                            createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", true))
                            .build())
                    .addMembers(createPhenopacket("jim-phenopacket",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false),
                            createPhenotypicFeature("HP:0100807", "Long fingers", false))
                            .build())
                    .addMembers(createPhenopacket("jane-phenopacket",
                            createPhenotypicFeature("HP:0001238", "Slender finger", false),
                            createPhenotypicFeature("HP:0001166", "Arachnodactyly", true))
                            .build())
                    .build();

            List<ValidationResult> results = validator.validate(cohort);

            assertThat(results, is(empty()));
        }
    }

    private static Phenopacket.Builder createPhenopacket(String phenopacketId,
                                                         PhenotypicFeature... features) {
        return Phenopacket.newBuilder()
                .setId(phenopacketId)
                .addAllPhenotypicFeatures(Arrays.asList(features));
    }

    private static PhenotypicFeature createPhenotypicFeature(String id, String label, boolean excluded) {
        return PhenotypicFeature.newBuilder()
                .setType(OntologyClass.newBuilder()
                        .setId(id)
                        .setLabel(label)
                        .build())
                .setExcluded(excluded)
                .build();
    }

}
