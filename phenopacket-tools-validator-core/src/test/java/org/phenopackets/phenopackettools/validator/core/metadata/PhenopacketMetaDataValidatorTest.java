package org.phenopackets.phenopackettools.validator.core.metadata;

import com.google.protobuf.util.Timestamps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.*;
import org.phenopackets.schema.v2.core.*;

import java.text.ParseException;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PhenopacketMetaDataValidatorTest {

    @Nested
    public class PhenopacketValidatorTest {

        private PhenopacketValidator<PhenopacketOrBuilder> validator;

        @BeforeEach
        public void setUp() {
            validator = MetaDataValidators.phenopacketValidator();
        }

        @Test
        public void validPhenopacketPassesValidation() throws Exception {
            PhenopacketOrBuilder phenopacket = createPhenopacket()
                    .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                            .setType(OntologyClass.newBuilder()
                                    .setId("HP:0000280")
                                    .setLabel("Coarse facial features")
                                    .build())
                            .addModifiers(OntologyClass.newBuilder()
                                    .setId("NCIT_C160628")
                                    .setLabel("Permanent")
                                    .build())
                            .build());

            List<ValidationResult> results = validator.validate(phenopacket);

            assertThat(results, is(empty()));
        }

        @Test
        public void noOntologyClassesPassesValidation() throws Exception {
            PhenopacketOrBuilder phenopacket = createPhenopacket();

            List<ValidationResult> results = validator.validate(phenopacket);

            assertThat(results, is(empty()));
        }

        @Test
        public void unknownPrefixFailsValidation() throws Exception {
            PhenopacketOrBuilder phenopacket = createPhenopacket()
                    .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                            .setType(OntologyClass.newBuilder()
                                    .setId("HPO:0000280")
                                    .setLabel("Coarse facial features")
                                    .build())
                            .build());

            List<ValidationResult> results = validator.validate(phenopacket);

            assertThat(results, hasSize(1));
            assertThat(results.get(0),
                    equalTo(ValidationResult.error(
                            validator.validatorInfo(),
                            "Ontology Not In MetaData",
                            "No ontology corresponding to ID: \"HPO:0000280\" found in MetaData")));
        }

        @Test
        public void misFormattedTermFailsValidation() throws Exception {
            PhenopacketOrBuilder phenopacket = createPhenopacket()
                    .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                            .setType(OntologyClass.newBuilder()
                                    .setId("HP0000280")
                                    .setLabel("Coarse facial features")
                                    .build())
                            .build());

            List<ValidationResult> results = validator.validate(phenopacket);

            assertThat(results, hasSize(1));
            assertThat(results.get(0),
                    equalTo(ValidationResult.error(
                            validator.validatorInfo(),
                            "Ontology class ID syntax",
                            "Malformed ontology class ID: \"HP0000280\"")));
        }

    }

    @Nested
    public class FamilyValidatorTest {

        private PhenopacketValidator<FamilyOrBuilder> validator;

        @BeforeEach
        public void setUp() {
            validator = MetaDataValidators.familyValidator();
        }

        @Test
        public void validFamilyPassesValidation() throws Exception {
            Family.Builder family = createFamily()
                    .setProband(createPhenopacket()
                            .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                                    .setType(OntologyClass.newBuilder()
                                            .setId("HP:0000280")
                                            .setLabel("Coarse facial features")
                                            .build())
                                    .addModifiers(OntologyClass.newBuilder()
                                            .setId("NCIT_C160628")
                                            .setLabel("Permanent")
                                            .build())
                                    .build()));

            List<ValidationResult> results = validator.validate(family);

            assertThat(results, is(empty()));
        }

        @Test
        public void noOntologyClassesPassesValidation() throws Exception {
            FamilyOrBuilder family = createFamily();

            List<ValidationResult> results = validator.validate(family);

            assertThat(results, is(empty()));
        }

        @Test
        public void unknownPrefixFailsValidation() throws Exception {
            Family.Builder family = createFamily()
                    .setProband(createPhenopacket()
                            .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                                    .setType(OntologyClass.newBuilder()
                                            .setId("HPO:0000280")
                                            .setLabel("Coarse facial features")
                                            .build())
                                    .build()));

            List<ValidationResult> results = validator.validate(family);

            assertThat(results, hasSize(1));
            assertThat(results.get(0),
                    equalTo(ValidationResult.error(
                            validator.validatorInfo(),
                            "Ontology Not In MetaData",
                            "No ontology corresponding to ID: \"HPO:0000280\" found in MetaData")));
        }

        @Test
        public void misFormattedTermFailsValidation() throws Exception {
            Family.Builder family = createFamily()
                    .setProband(createPhenopacket()
                            .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                                    .setType(OntologyClass.newBuilder()
                                            .setId("HPO0000280")
                                            .setLabel("Coarse facial features")
                                            .build())
                                    .build()));

            List<ValidationResult> results = validator.validate(family);

            assertThat(results, hasSize(1));
            assertThat(results.get(0),
                    equalTo(ValidationResult.error(
                            validator.validatorInfo(),
                            "Ontology class ID syntax",
                            "Malformed ontology class ID: \"HPO0000280\"")));
        }

        private static Family.Builder createFamily() throws ParseException {
            return Family.newBuilder()
                    .setId("example-family-id")
                    .setMetaData(createMetadata());
        }
    }

    @Nested
    public class CohortValidatorTest {

        private PhenopacketValidator<CohortOrBuilder> validator;

        @BeforeEach
        public void setUp() {
            validator = MetaDataValidators.cohortValidator();
        }


        @Test
        public void validCohortPassesValidation() throws Exception {
            Cohort.Builder cohort = createCohort()
                    .addMembers(createPhenopacket()
                            .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                                    .setType(OntologyClass.newBuilder()
                                            .setId("HP:0000280")
                                            .setLabel("Coarse facial features")
                                            .build())
                                    .addModifiers(OntologyClass.newBuilder()
                                            .setId("NCIT_C160628")
                                            .setLabel("Permanent")
                                            .build())
                                    .build()));

            List<ValidationResult> results = validator.validate(cohort);

            assertThat(results, is(empty()));
        }

        @Test
        public void noOntologyClassesPassesValidation() throws Exception {
            CohortOrBuilder cohort = createCohort();

            List<ValidationResult> results = validator.validate(cohort);

            assertThat(results, is(empty()));
        }

        @Test
        public void unknownPrefixFailsValidation() throws Exception {
            Cohort.Builder cohort = createCohort()
                    .addMembers(createPhenopacket()
                            .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                                    .setType(OntologyClass.newBuilder()
                                            .setId("HPO:0000280")
                                            .setLabel("Coarse facial features")
                                            .build())
                                    .build()));

            List<ValidationResult> results = validator.validate(cohort);

            assertThat(results, hasSize(1));
            assertThat(results.get(0),
                    equalTo(ValidationResult.error(
                            validator.validatorInfo(),
                            "Ontology Not In MetaData",
                            "No ontology corresponding to ID: \"HPO:0000280\" found in MetaData")));
        }

        @Test
        public void misFormattedTermFailsValidation() throws Exception {
            Cohort.Builder cohort = createCohort()
                    .addMembers(createPhenopacket()
                            .addPhenotypicFeatures(PhenotypicFeature.newBuilder()
                                    .setType(OntologyClass.newBuilder()
                                            .setId("HPO0000280")
                                            .setLabel("Coarse facial features")
                                            .build())
                                    .build()));

            List<ValidationResult> results = validator.validate(cohort);

            assertThat(results, hasSize(1));
            assertThat(results.get(0),
                    equalTo(ValidationResult.error(
                            validator.validatorInfo(),
                            "Ontology class ID syntax",
                            "Malformed ontology class ID: \"HPO0000280\"")));
        }

        private static Cohort.Builder createCohort() throws ParseException {
            return Cohort.newBuilder()
                    .setId("example-cohort-id")
                    .setMetaData(createMetadata());
        }
    }

    private static Phenopacket.Builder createPhenopacket() throws ParseException {
        return Phenopacket.newBuilder()
                .setId("example-phenopacket-id")
                .setMetaData(createMetadata());
    }

    private static MetaData createMetadata() throws ParseException {
        return MetaData.newBuilder()
                .setCreated(Timestamps.parse("2021-05-14T10:35:00Z"))
                .setCreatedBy("anonymous biocurator")
                .addResources(Resource.newBuilder()
                        .setId("hp")
                        .setName("human phenotype ontology")
                        .setUrl("http://purl.obolibrary.org/obo/hp.owl")
                        .setVersion("2021-08-02")
                        .setNamespacePrefix("HP")
                        .setIriPrefix("http://purl.obolibrary.org/obo/HP_")
                        .build())
                .addResources(Resource.newBuilder()
                        .setId("geno")
                        .setName("Genotype Ontology")
                        .setUrl("http://purl.obolibrary.org/obo/geno.owl")
                        .setVersion("2021-08-02")
                        .setNamespacePrefix("GENO")
                        .setIriPrefix("http://purl.obolibrary.org/obo/GENO_")
                        .build())
                .addResources(Resource.newBuilder()
                        .setId("ncit")
                        .setName("NCI Thesaurus")
                        .setUrl("http://purl.obolibrary.org/obo/ncit.owl")
                        .setVersion("2021-08-02")
                        .setNamespacePrefix("NCIT")
                        .setIriPrefix("http://purl.obolibrary.org/obo/NCIT_"))
                .setPhenopacketSchemaVersion("2.0.0")
                .build();
    }
}