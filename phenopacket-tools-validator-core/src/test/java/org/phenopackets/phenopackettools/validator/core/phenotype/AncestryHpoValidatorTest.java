package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.TestData;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

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
                    createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", true)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, is(empty()));
        }

        @Test
        public void testFailsIfTermAndAncestorIsObserved() {
            // Has some Abnormality of finger and Arachnodactyly. Only Arachnodactyly should be present.
            Phenopacket pp = createPhenopacket(
                    createPhenotypicFeature("HP:0001167", "Abnormality of finger", false),
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
                    createPhenotypicFeature("HP:0001167", "Abnormality of finger", true),
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
                    createPhenotypicFeature("HP:0001167", "Abnormality of finger", true),
                    createPhenotypicFeature("HP:0001166", "Arachnodactyly", false)
            ).build();

            List<ValidationResult> results = validator.validate(pp);

            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Violation of the annotation propagation rule"));
            assertThat(result.message(), equalTo("Phenotypic features of example-phenopacket must not contain both an observed term (Arachnodactyly, HP:0001166) and an excluded ancestor (Abnormality of finger, HP:0001167)"));
        }

        private static Phenopacket.Builder createPhenopacket(PhenotypicFeature excludedArachnodactyly, PhenotypicFeature observedAbnormalityOfFinger) {
            return Phenopacket.newBuilder()
                    .setId("example-phenopacket")
                    .addPhenotypicFeatures(excludedArachnodactyly)
                    .addPhenotypicFeatures(observedAbnormalityOfFinger);
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

}
