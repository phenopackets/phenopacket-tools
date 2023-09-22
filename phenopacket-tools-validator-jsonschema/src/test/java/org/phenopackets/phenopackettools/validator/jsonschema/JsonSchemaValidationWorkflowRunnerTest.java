package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.MessageOrBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;
import org.phenopackets.phenopackettools.io.PhenopacketParser;
import org.phenopackets.phenopackettools.io.PhenopacketParserFactory;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoOrganSystems;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonSchemaValidationWorkflowRunnerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonTamperer TAMPERER = new JsonTamperer();

    /**
     * Tests that all validation workflow runners (phenopacket, family, and cohort) must pass.
     */
    @Nested
    public class GeneralTest {

        @Test
        public void baseValidationWorkflowHasExpectedValidators() {
            String[] expected = {"BaseValidator", "MetaDataValidator"};
            JsonSchemaValidationWorkflowRunner<PhenopacketOrBuilder> phenopacket = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                    .build();
            List<String> actual = phenopacket.validators().stream().map(ValidatorInfo::validatorId).toList();
            assertThat(actual, hasItems(expected));
            assertThat(actual, hasSize(2));

            JsonSchemaValidationWorkflowRunner<FamilyOrBuilder> family = JsonSchemaValidationWorkflowRunner.familyBuilder()
                    .build();
            actual = family.validators().stream().map(ValidatorInfo::validatorId).toList();
            assertThat(actual, hasItems(expected));
            assertThat(actual, hasSize(2));

            JsonSchemaValidationWorkflowRunner<CohortOrBuilder> cohort = JsonSchemaValidationWorkflowRunner.cohortBuilder()
                    .build();
            actual = cohort.validators().stream().map(ValidatorInfo::validatorId).toList();
            assertThat(actual, hasItems(expected));
            assertThat(actual, hasSize(2));
        }
    }

    /**
     * Check required and recommended phenopacket fields.
     */
    @Nested
    public class PhenopacketFieldsTest {

        /**
         * We check that absence/mis-formatting of the REQUIRED fields produces a corresponding error(s).
         */
        @Nested
        public class RequiredFieldsTest {

            private JsonSchemaValidationWorkflowRunner<PhenopacketOrBuilder> runner;

            @BeforeEach
            public void setUp() {
                runner = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                        .build();
            }

            /**
             * There are two required top-level fields: `id` and `metaData`. Check that absence of both fields lead to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/id,                                  DELETE,          'id' is missing but it is required",
                    "/metaData,                            DELETE,          'metaData' is missing but it is required",
            })
            public void checkTopLevelPhenopacketConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of subject id leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/subject/id,                          DELETE,           'subject.id' is missing but it is required"
            })
            public void checkSubjectConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `status` in `vitalStatus` leads to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/subject/vitalStatus/status,          DELETE,           'subject.vitalStatus.status' is missing but it is required"
            })
            public void checkVitalStatusConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of phenotypic feature id leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/phenotypicFeatures[0]/type,          DELETE,           'phenotypicFeatures[0].type' is missing but it is required",
                    "/phenotypicFeatures[1]/type,          DELETE,           'phenotypicFeatures[1].type' is missing but it is required"
            })
            public void checkPhenotypicFeatureConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Check that absence of required `TimeElement` fields leads to the appropriate
             * {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    // TODO - this returns an error for each oneOf field
//                    "/phenotypicFeatures[0]/onset/gestationalAge,              DELETE,           '$.phenotypicFeatures[0].onset.gestationalAge.weeks: is missing but it is required'",
                    "/phenotypicFeatures[0]/onset/gestationalAge/weeks,        DELETE,           'phenotypicFeatures[0].onset.gestationalAge.weeks' is missing but it is required",
                    "/phenotypicFeatures[0]/onset/gestationalAge/weeks,        SET[-1],          'phenotypicFeatures[0].onset.gestationalAge.weeks' must have a minimum value of 0",
                    "/phenotypicFeatures[0]/onset/gestationalAge/days,         SET[-1],          'phenotypicFeatures[0].onset.gestationalAge.days' must have a minimum value of 0",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         DELETE,           'phenotypicFeatures[1].onset.age.iso8601duration' is missing but it is required",
                    "/phenotypicFeatures[2]/onset/ageRange/start,              DELETE,           'phenotypicFeatures[2].onset.ageRange.start' is missing but it is required",
                    "/phenotypicFeatures[2]/onset/ageRange/end,                DELETE,           'phenotypicFeatures[2].onset.ageRange.end' is missing but it is required",
                    // TODO - require end being at or after start
                    // We do not tamper with the ontology class and timestamp as we test their validity elsewhere.
                    "/phenotypicFeatures[5]/onset/interval/start,              DELETE,           'phenotypicFeatures[5].onset.interval.start' is missing but it is required",
                    "/phenotypicFeatures[5]/onset/interval/end,                DELETE,           'phenotypicFeatures[5].onset.interval.end' is missing but it is required",
            })
            public void checkTimeElementConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Check that error in the ISO8601 period leads to the appropriate
             * {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    // Age
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P1H],         'phenotypicFeatures[1].onset.age.iso8601duration' does not match the regex pattern ^P(?!$)(\\d+Y)?(\\d+M)?(\\d+W)?(\\d+D)?(T(?=\\d+[HMS])(\\d+H)?(\\d+M)?(\\d+S)?)?$",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[PT1D],        'phenotypicFeatures[1].onset.age.iso8601duration' does not match the regex pattern ^P(?!$)(\\d+Y)?(\\d+M)?(\\d+W)?(\\d+D)?(T(?=\\d+[HMS])(\\d+H)?(\\d+M)?(\\d+S)?)?$",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P],           'phenotypicFeatures[1].onset.age.iso8601duration' does not match the regex pattern ^P(?!$)(\\d+Y)?(\\d+M)?(\\d+W)?(\\d+D)?(T(?=\\d+[HMS])(\\d+H)?(\\d+M)?(\\d+S)?)?$",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[PT],          'phenotypicFeatures[1].onset.age.iso8601duration' does not match the regex pattern ^P(?!$)(\\d+Y)?(\\d+M)?(\\d+W)?(\\d+D)?(T(?=\\d+[HMS])(\\d+H)?(\\d+M)?(\\d+S)?)?$",
                    // Wrong token order.
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P10M4Y],      'phenotypicFeatures[1].onset.age.iso8601duration' does not match the regex pattern ^P(?!$)(\\d+Y)?(\\d+M)?(\\d+W)?(\\d+D)?(T(?=\\d+[HMS])(\\d+H)?(\\d+M)?(\\d+S)?)?$",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P1YT4M12H],      'phenotypicFeatures[1].onset.age.iso8601duration' does not match the regex pattern ^P(?!$)(\\d+Y)?(\\d+M)?(\\d+W)?(\\d+D)?(T(?=\\d+[HMS])(\\d+H)?(\\d+M)?(\\d+S)?)?$",
            })
            public void checkTimeElementConstraints_ISO8601_errors(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Check that correct ISO8601 period yields no
             * {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}s.
             */
            @ParameterizedTest
            @CsvSource({
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P1Y4M3W2DT1H23M44S]",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P1Y]",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P4M]",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P3W]",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[P2D]",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[PT1H]",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         SET[PT1H2M]",
                    })
            public void checkTimeElementConstraints_ISO8601_OK(String path, String action) {
                JsonNode tampered = TAMPERER.tamper(readBethlemPhenopacketNode(), path, Action.valueOf(action));

                ValidationResults results = runner.validate(tampered.toPrettyString());

                assertThat(results.isValid(), equalTo(true));
            }

            /**
             * Absence of phenotypic feature id leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/phenotypicFeatures[0]/evidence[0]/evidenceCode,          DELETE,           'phenotypicFeatures[0].evidence[0].evidenceCode' is missing but it is required",
            })
            public void checkEvidenceConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of measurement `assay`, `value`, or `complexValue` lead to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/measurements[0]/assay,                 DELETE,           'measurements[0].assay' is missing but it is required",
                    "/measurements[0]/value,                 DELETE,           'measurements[0].value' is missing but it is required|'measurements[0].complexValue' is missing but it is required",
                    "/measurements[1]/complexValue,          DELETE,           'measurements[1].value' is missing but it is required|'measurements[1].complexValue' is missing but it is required",
            })
            public void checkMeasurementConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of biosample `id` leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/biosamples[0]/id,                 DELETE,           'biosamples[0].id' is missing but it is required",
            })
            public void checkBiosampleConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of interpretation `id` or `progressStatus` leads to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/interpretations[0]/id,                 DELETE,           'interpretations[0].id' is missing but it is required",
                    "/interpretations[0]/progressStatus,     DELETE,           'interpretations[0].progressStatus' is missing but it is required",
            })
            public void checkInterpretationConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of diagnosis `disease` leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/interpretations[0]/diagnosis/disease,   DELETE,           'interpretations[0].diagnosis.disease' is missing but it is required",
            })
            public void checkDiagnosisConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `subjectOrBiosampleId`, `interpretationStatus` or `gene`/`variantInterpretation`
             * leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/subjectOrBiosampleId,   DELETE,          'interpretations[0].diagnosis.genomicInterpretations[0].subjectOrBiosampleId' is missing but it is required",
                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/interpretationStatus,   DELETE,          'interpretations[0].diagnosis.genomicInterpretations[0].interpretationStatus' is missing but it is required",
                    // TODO - as of now this leads to 2 errors instead of just one
//                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/interpretationStatus,   SET[gibberish],  '$.interpretations[0].diagnosis.genomicInterpretations[0].interpretationStatus: is missing but it is required'",
                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/variantInterpretation,  DELETE,          'interpretations[0].diagnosis.genomicInterpretations[0].gene' is missing but it is required|'interpretations[0].diagnosis.genomicInterpretations[0].variantInterpretation' is missing but it is required",
                    "/interpretations[0]/diagnosis/genomicInterpretations[1]/gene,                   DELETE,          'interpretations[0].diagnosis.genomicInterpretations[1].gene' is missing but it is required|'interpretations[0].diagnosis.genomicInterpretations[1].variantInterpretation' is missing but it is required",
            })
            public void checkGenomicInterpretationConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `valueId` or `symbol`
             * leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/interpretations[0]/diagnosis/genomicInterpretations[1]/gene/valueId,           DELETE,          'interpretations[0].diagnosis.genomicInterpretations[1].gene.valueId' is missing but it is required",
                    "/interpretations[0]/diagnosis/genomicInterpretations[1]/gene/symbol,            DELETE,          'interpretations[0].diagnosis.genomicInterpretations[1].gene.symbol' is missing but it is required",
            })
            public void checkGeneDescriptorConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `acmgPathogenicityClassification`, `therapeuticActionability` or `variationDescriptor`
             * leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/variantInterpretation/acmgPathogenicityClassification,    DELETE,          'interpretations[0].diagnosis.genomicInterpretations[0].variantInterpretation.acmgPathogenicityClassification' is missing but it is required",
                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/variantInterpretation/therapeuticActionability,           DELETE,          'interpretations[0].diagnosis.genomicInterpretations[0].variantInterpretation.therapeuticActionability' is missing but it is required",
                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/variantInterpretation/variationDescriptor,                DELETE,          'interpretations[0].diagnosis.genomicInterpretations[0].variantInterpretation.variationDescriptor' is missing but it is required",
            })
            public void checkVariantInterpretationConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            @ParameterizedTest
            @CsvSource({
                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/variantInterpretation/variationDescriptor/id,              DELETE,          'interpretations[0].diagnosis.genomicInterpretations[0].variantInterpretation.variationDescriptor.id' is missing but it is required",
                    "/interpretations[0]/diagnosis/genomicInterpretations[0]/variantInterpretation/variationDescriptor/moleculeContext, DELETE,          'interpretations[0].diagnosis.genomicInterpretations[0].variantInterpretation.variationDescriptor.moleculeContext' is missing but it is required",
            })
            public void checkVariationDescriptorConstraints(String path, String action, String expected) {
                testErrors(runner, readRetinoblastomaPhenopacketNode(), path, action, expected, true);
            }

            /**
             * As of Nov 9, 2022, the {@link org.ga4gh.vrs.v1.Variation} validator does not check presence
             * of required fields. The validator can only check presence of {@code oneof} fields.
             * <p>
             * Note that the {@code path} is split into a prefix and sub-path to increase legibility of the
             * test parameters.
             */
            @ParameterizedTest
            @CsvSource({
                    "/variation/copyNumber,                DELETE,          'REPLACE.allele' is missing but it is required|'REPLACE.haplotype' is missing but it is required|'REPLACE.copyNumber' is missing but it is required|'REPLACE.text' is missing but it is required|'REPLACE.variationSet' is missing but it is required",
            })
            public void removingAOneOfFieldFromVariationProducesValidationError(String subPath, String action, String subExpected) {
                String pathPrefix = "/interpretations[0]/diagnosis/genomicInterpretations[0]/variantInterpretation/variationDescriptor";
                String path = pathPrefix.concat(subPath);

                String validationMessagePrefix = "interpretations[0].diagnosis.genomicInterpretations[0].variantInterpretation.variationDescriptor.variation";
                String expectedValidationMessage = subExpected.replaceAll("REPLACE", validationMessagePrefix);
                testErrors(runner, readRetinoblastomaPhenopacketNode(), path, action, expectedValidationMessage, true);
            }

            /**
             * Absence of `term` leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/diseases[0]/term,                DELETE,          'diseases[0].term' is missing but it is required",
            })
            public void checkDiseaseConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `action` item leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/medicalActions[0]/procedure,                DELETE,          'medicalActions[0].procedure' is missing but it is required|'medicalActions[0].treatment' is missing but it is required|'medicalActions[0].radiationTherapy' is missing but it is required|'medicalActions[0].therapeuticRegimen' is missing but it is required",
                    "/medicalActions[1]/treatment,                DELETE,          'medicalActions[1].procedure' is missing but it is required|'medicalActions[1].treatment' is missing but it is required|'medicalActions[1].radiationTherapy' is missing but it is required|'medicalActions[1].therapeuticRegimen' is missing but it is required",
                    "/medicalActions[2]/radiationTherapy,         DELETE,          'medicalActions[2].procedure' is missing but it is required|'medicalActions[2].treatment' is missing but it is required|'medicalActions[2].radiationTherapy' is missing but it is required|'medicalActions[2].therapeuticRegimen' is missing but it is required",
                    "/medicalActions[3]/therapeuticRegimen,       DELETE,          'medicalActions[3].procedure' is missing but it is required|'medicalActions[3].treatment' is missing but it is required|'medicalActions[3].radiationTherapy' is missing but it is required|'medicalActions[3].therapeuticRegimen' is missing but it is required",
            })
            public void checkMedicalActionConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `code` leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/medicalActions[0]/procedure/code,           DELETE,          'medicalActions[0].procedure.code' is missing but it is required"
            })
            public void checkProcedureConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `agent` leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/medicalActions[1]/treatment/agent,         DELETE,          'medicalActions[1].treatment.agent' is missing but it is required"
            })
            public void checkTreatmentConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `modality`, `bodySite`, `dosage`, and `fractions` leads to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/medicalActions[2]/radiationTherapy/modality,         DELETE,          'medicalActions[2].radiationTherapy.modality' is missing but it is required",
                    "/medicalActions[2]/radiationTherapy/bodySite,         DELETE,          'medicalActions[2].radiationTherapy.bodySite' is missing but it is required",
                    "/medicalActions[2]/radiationTherapy/dosage,           DELETE,          'medicalActions[2].radiationTherapy.dosage' is missing but it is required",
                    "/medicalActions[2]/radiationTherapy/fractions,        DELETE,          'medicalActions[2].radiationTherapy.fractions' is missing but it is required"
            })
            public void checkRadiationTherapyConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `externalReference`, `ontologyClass`, and `regimenStatus` leads to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/medicalActions[3]/therapeuticRegimen/externalReference,   DELETE,          'medicalActions[3].therapeuticRegimen.ontologyClass' is missing but it is required|'medicalActions[3].therapeuticRegimen.externalReference' is missing but it is required",
                    "/medicalActions[4]/therapeuticRegimen/ontologyClass,       DELETE,          'medicalActions[4].therapeuticRegimen.ontologyClass' is missing but it is required|'medicalActions[4].therapeuticRegimen.externalReference' is missing but it is required",
                    "/medicalActions[3]/therapeuticRegimen/regimenStatus,       DELETE,          'medicalActions[3].therapeuticRegimen.regimenStatus' is missing but it is required"
            })
            public void checkTherapeuticRegimenConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of file uri leads to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/files[0]/uri,          DELETE,           'files[0].uri' is missing but it is required",
            })
            public void checkFileConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }

            /**
             * Absence of `created`, `createdBy`, and `phenopacketSchemaVersion` leads to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/metaData/created,                      DELETE,      true,      'metaData.created' is missing but it is required",
                    "/metaData/createdBy,                    DELETE,      true,      'metaData.createdBy' is missing but it is required",
                    "/metaData/resources[*],                 DELETE,     false,      'metaData.resources' there must be a minimum of 1 items in the array",
                    "/metaData/phenopacketSchemaVersion,     DELETE,      true,      'metaData.phenopacketSchemaVersion' is missing but it is required",
            })
            public void checkMetaDataConstraints(String path, String action, boolean validateCount, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, validateCount);
            }

            /**
             * Absence of `timestamp` leads to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/metaData/resources[0]/id,              DELETE,    true,       'metaData.resources[0].id' is missing but it is required",
                    "/metaData/resources[0]/name,            DELETE,    true,       'metaData.resources[0].name' is missing but it is required",
                    "/metaData/resources[0]/namespacePrefix, DELETE,   false,       'metaData.resources[0].namespacePrefix' is missing but it is required",
                    "/metaData/resources[0]/url,             DELETE,    true,       'metaData.resources[0].url' is missing but it is required",
                    "/metaData/resources[0]/version,         DELETE,    true,       'metaData.resources[0].version' is missing but it is required",
                    "/metaData/resources[0]/iriPrefix,       DELETE,    true,       'metaData.resources[0].iriPrefix' is missing but it is required",
            })
            public void checkResourceConstraints(String path, String action, boolean validateCount, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, validateCount);
            }

            /**
             * Absence of `timestamp` leads to
             * an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/metaData/updates[0]/timestamp,         DELETE,           'metaData.updates[0].timestamp' is missing but it is required",
            })
            public void checkUpdateConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected, true);
            }
        }


        @Nested
        public class RecommendedFieldsTest {
            // TODO - subject, phenotypicFeature, len(phenotypicFeature/evidence) > 0,
        }

        private static JsonNode readBethlemPhenopacketNode() {
            return readJsonTree(TestData.BETHLEM_MYOPATHY_PHENOPACKET_JSON);
        }

        private static JsonNode readRetinoblastomaPhenopacketNode() {
            return readJsonTree(TestData.RETINOBLASTOMA_PHENOPACKET_JSON);
        }

        private static JsonNode readJsonTree(Path jsonPath) {
            try (InputStream is = Files.newInputStream(jsonPath)){
                return MAPPER.readTree(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Nested
    public class FamilyFieldsTest {

        @Nested
        public class RequiredFieldsTest {

            private JsonSchemaValidationWorkflowRunner<FamilyOrBuilder> runner;

            @BeforeEach
            public void setUp() {
                runner = JsonSchemaValidationWorkflowRunner.familyBuilder()
                        .build();
            }

            @Test
            public void validFamilyYieldsNoErrors() {
                JsonNode node = readExampleFamilyNode();

                ValidationResults results = runner.validate(node.toPrettyString());

                assertThat(results.isValid(), is(true));
            }

            @ParameterizedTest
            @CsvSource({
                    "/id,                    DELETE,          'id' is missing but it is required",
                    "/proband,               DELETE,          'proband' is missing but it is required",
                    "/consanguinousParents,  DELETE,          'consanguinousParents' is missing but it is required",
                    "/pedigree,              DELETE,          'pedigree' is missing but it is required",
                    "/metaData,              DELETE,          'metaData' is missing but it is required",
            })
            public void absenceOfTopLevelFamilyElementsYieldsErrors(String path, String action, String expected) {
                testErrors(runner, readExampleFamilyNode(), path, action, expected, true);
            }

            @ParameterizedTest
            @CsvSource({
                    "/pedigree/persons,      DELETE,          'pedigree.persons' is missing but it is required",
                    "/pedigree/persons[*],   DELETE,          'pedigree.persons' there must be a minimum of 1 items in the array",
            })
            public void emptyPedigreeYieldsError(String path, String action, String expected) {
                testErrors(runner, readExampleFamilyNode(), path, action, expected, true);
            }


        }

        @Nested
        public class RecommendedFieldsTest {
            // TODO - implement
        }

        private static JsonNode readExampleFamilyNode() {
            try (InputStream is = Files.newInputStream(TestData.EXAMPLE_FAMILY_JSON)){
                return MAPPER.readTree(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Nested
    public class CohortFieldsTest {

        private JsonSchemaValidationWorkflowRunner<CohortOrBuilder> runner;

        @BeforeEach
        public void setUp() {
            runner = JsonSchemaValidationWorkflowRunner.cohortBuilder()
                    .build();
        }

        @Nested
        public class RequiredFieldsTest {

            /**
             * This is all it takes to validate a cohort. All other fields are tested in {@link PhenopacketFieldsTest}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/id,                   DELETE,          'id' is missing but it is required",
                    "/members[*],           DELETE,          'members' there must be a minimum of 1 items in the array",
                    "/metaData,             DELETE,          'metaData' is missing but it is required",
            })
            public void checkCohortConstraints(String path, String action, String expected) {
                testErrors(runner, readExampleCohortNode(), path, action, expected, true);
            }

        }

        private static JsonNode readExampleCohortNode() {
            try (InputStream is = Files.newInputStream(TestData.EXAMPLE_COHORT_JSON)){
                return MAPPER.readTree(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * The tests that are part of the user guide. Ensure that the user guide is updated if the tests do not compile.
     * The tests do not need to be run, just to compile (hence @Disabled).
     */
    @Nested
    @Disabled
    public class DocumentationTest {

        @Test
        public void baseValidationWorkflowRunner() throws Exception {
            // Prepare the runner
            ValidationWorkflowRunner<PhenopacketOrBuilder> runner = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                    .build();

            // A path
            Path path = Path.of("bethlem-myopathy.json");
            ValidationResults results = runner.validate(path);

            // An input stream
            try (InputStream is = new FileInputStream(path.toFile())) {
                results = runner.validate(is);
            }

            // A byte array
            try (InputStream is = new FileInputStream(path.toFile())) {
                results = runner.validate(is.readAllBytes());
            }

            // A JSON or YAML string
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String jsonString = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                results = runner.validate(jsonString);
            }

            // Or a phenopacket.
            PhenopacketParser parser = PhenopacketParserFactory.getInstance()
                    .forFormat(PhenopacketSchemaVersion.V2);
            Phenopacket phenopacket = (Phenopacket) parser.parse(path);
            results = runner.validate(phenopacket);
        }

        @Test
        public void availableBuilders() {
            ValidationWorkflowRunner<PhenopacketOrBuilder> phenopacket = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                    .build();

            ValidationWorkflowRunner<FamilyOrBuilder> family = JsonSchemaValidationWorkflowRunner.familyBuilder()
                    .build();

            ValidationWorkflowRunner<CohortOrBuilder> cohort = JsonSchemaValidationWorkflowRunner.cohortBuilder()
                    .build();
        }

        @Test
        public void workflowIntrospection() {
            ValidationWorkflowRunner<PhenopacketOrBuilder> runner = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                    .build();

            List<ValidatorInfo> validators = runner.validators();
        }

        @Test
        public void validationResults() throws Exception {
            // Prepare the runner
            ValidationWorkflowRunner<PhenopacketOrBuilder> runner = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                    .build();

            // A path
            Path path = Path.of("bethlem-myopathy.json");
            ValidationResults results = runner.validate(path);

            assert results.isValid();
            List<ValidatorInfo> validators = results.validators();
            List<ValidationResult> issues = results.validationResults();

            ValidationResult issue = issues.get(0);

            // The validator that pointed out the issue.
            ValidatorInfo validatorInfo = issue.validatorInfo();

            // The issue severity (warning or error).
            ValidationLevel level = issue.level();

            // Category of the issue, useful for grouping the issues.
            // One validator can produce issues with different categories.
            String category = issue.category();

            // A message targeted for the user.
            String message = issue.message();
        }

        @Test
        public void elaborateExample() throws Exception {
            JsonSchemaValidationWorkflowRunnerBuilder<PhenopacketOrBuilder> builder = JsonSchemaValidationWorkflowRunner.phenopacketBuilder();

            Path customSchema = Path.of("hpo-rare-disease-schema.json");
            builder.addJsonSchema(customSchema);

            Ontology hpo = OntologyLoader.loadOntology(new File("hp.json"));
            PhenopacketValidator<PhenopacketOrBuilder> primary = HpoPhenotypeValidators.Primary.phenopacketHpoPhenotypeValidator(hpo);
            builder.addValidator(primary);

            PhenopacketValidator<PhenopacketOrBuilder> ancestry = HpoPhenotypeValidators.Ancestry.phenopacketHpoAncestryValidator(hpo);
            builder.addValidator(ancestry);

            List<TermId> organSystemIds = List.of(HpoOrganSystems.EYE, HpoOrganSystems.CARDIOVASCULAR, HpoOrganSystems.RESPIRATORY);
            PhenopacketValidator<PhenopacketOrBuilder> organSystem = HpoPhenotypeValidators.OrganSystem.phenopacketHpoOrganSystemValidator(hpo, organSystemIds);
            builder.addValidator(organSystem);
        }
    }

    /**
     * In principle, we do the same kind of testing in all parameterized tests.
     * We use the {@link #TAMPERER} to tamper with a {@code node}, performing a certain {@code action} on a {@code path}
     * to make the {@code invalid}.
     * Then we validate the invalid node with the {@code runner} and check we receive the expected {@code errors}.
     * <p>
     * Sometimes, absence of an element leads to other errors. For instance, absence of a namespace prefix in HP resource
     * triggers missing ontology class definition for all HPO terms used in phenotypic feature.
     * To check only presence of a specific error instead of all, we can set {@code validateCount} to {@code false}.
     * <p>
     * This is what is done in this method.
     */
    private static <T extends MessageOrBuilder> void testErrors(ValidationWorkflowRunner<T> runner,
                                                                JsonNode node,
                                                                String path,
                                                                String action,
                                                                String errors,
                                                                boolean validateCount) {
        JsonNode tampered = TAMPERER.tamper(node, path, Action.valueOf(action));

        ValidationResults results = runner.validate(tampered.toPrettyString());

        Collection<String> tokens = Arrays.asList(errors.split("\\|"));
        if (validateCount) {
            assertThat(results.validationResults(), hasSize(tokens.size()));
            // All messages must be present
            assertThat(results.validationResults().stream().map(ValidationResult::message).toList(), containsInAnyOrder(tokens.toArray()));
        } else {
            // At least one message must be present
            assertThat(results.validationResults().stream().map(ValidationResult::message).anyMatch(tokens::contains), is(true));
        }

        // Finally, the method is testing errors, so all levels must be errors.
        assertThat(results.validationResults().stream().allMatch(r -> r.level().isError()), is(true));
    }


}