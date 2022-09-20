package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidationResults;
import org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner;
import org.phenopackets.phenopackettools.validator.testdatagen.ExampleFamily;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonSchemaValidationWorkflowRunnerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonTamperer TAMPERER = new JsonTamperer();

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
                    "/id,                                  DELETE,          '$.id: is missing but it is required'",
                    "/metaData,                            DELETE,          '$.metaData: is missing but it is required'",
            })
            public void checkTopLevelPhenopacketConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected);
            }

            /**
             * Absence of subject id leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/subject/id,                          DELETE,           '$.subject.id: is missing but it is required'"
            })
            public void checkSubjectConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected);
            }

            /**
             * Absence of phenotypic feature id leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/phenotypicFeatures[0]/type,          DELETE,           '$.phenotypicFeatures[0].type: is missing but it is required'",
                    "/phenotypicFeatures[1]/type,          DELETE,           '$.phenotypicFeatures[1].type: is missing but it is required'"
            })
            public void checkPhenotypicFeatureConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected);
            }

            /**
             * Check that absence of required `TimeElement` fields leads to the appropriate
             * {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    // TODO - this returns an error for each oneOf field
//                    "/phenotypicFeatures[0]/onset/gestationalAge,              DELETE,           '$.phenotypicFeatures[0].onset.gestationalAge.weeks: is missing but it is required'",
                    "/phenotypicFeatures[0]/onset/gestationalAge/weeks,        DELETE,           '$.phenotypicFeatures[0].onset.gestationalAge.weeks: is missing but it is required'",
                    "/phenotypicFeatures[0]/onset/gestationalAge/weeks,        SET[-1],          '$.phenotypicFeatures[0].onset.gestationalAge.weeks: must have a minimum value of 0'",
                    "/phenotypicFeatures[0]/onset/gestationalAge/days,         SET[-1],          '$.phenotypicFeatures[0].onset.gestationalAge.days: must have a minimum value of 0'",
                    "/phenotypicFeatures[1]/onset/age/iso8601duration,         DELETE,           '$.phenotypicFeatures[1].onset.age.iso8601duration: is missing but it is required'",
                    // TODO - add test for ensuring that the duration is in an ISO8601 pattern
                    "/phenotypicFeatures[2]/onset/ageRange/start,              DELETE,           '$.phenotypicFeatures[2].onset.ageRange.start: is missing but it is required'",
                    "/phenotypicFeatures[2]/onset/ageRange/end,                DELETE,           '$.phenotypicFeatures[2].onset.ageRange.end: is missing but it is required'",
                    // TODO - require end being at or after start
                    // We do not tamper with the ontology class and timestamp as we test their validity elsewhere.
                    "/phenotypicFeatures[5]/onset/interval/start,              DELETE,           '$.phenotypicFeatures[5].onset.interval.start: is missing but it is required'",
                    "/phenotypicFeatures[5]/onset/interval/end,                DELETE,           '$.phenotypicFeatures[5].onset.interval.end: is missing but it is required'",
            })
            public void checkTimeElementConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected);
            }

            /**
             * Absence of phenotypic feature id leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/phenotypicFeatures[0]/evidence[0]/evidenceCode,          DELETE,           '$.phenotypicFeatures[0].evidence[0].evidenceCode: is missing but it is required'",
            })
            public void checkEvidenceConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected);
            }

            /**
             * Absence of measurement  id leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/measurements[0]/assay,                 DELETE,           '$.measurements[0].assay: is missing but it is required'",
                    // TODO - continue with the cases below
//                    "/measurements[0]/value,                 DELETE,           '$.phenotypicFeatures[0].evidence[0].evidenceCode: is missing but it is required'",
//                    "/measurements[1]/complexValue,          DELETE,           '$.phenotypicFeatures[0].evidence[0].evidenceCode: is missing but it is required'",
            })
            public void checkMeasurementConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected);
            }






            /**
             * Absence of measurement  id leads to an {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel#ERROR}.
             */
            @ParameterizedTest
            @CsvSource({
                    "/files[0]/uri,          DELETE,           '$.files[0].uri: is missing but it is required'",
            })
            public void checkFileConstraints(String path, String action, String expected) {
                testErrors(runner, readBethlemPhenopacketNode(), path, action, expected);
            }
        }


        @Nested
        public class RecommendedFieldsTest {
            // TODO - subject, phenotypicFeature, len(phenotypicFeature/evidence) > 0,
        }

        private static JsonNode readBethlemPhenopacketNode() {
            try (InputStream is = Files.newInputStream(TestData.BETHLEM_MYOPATHY_PHENOPACKET_JSON)){
                return MAPPER.readTree(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Nested
    public class FamilyFieldsTest {

        private static JsonNode getCompleteFamilyJson() throws InvalidProtocolBufferException, JsonProcessingException {
            Family family = ExampleFamily.getExampleFamily();
            String json = JsonFormat.printer().print(family);
            return MAPPER.readTree(json);
        }

        @Nested
        public class RequiredFieldsTest {

            private JsonSchemaValidationWorkflowRunner<FamilyOrBuilder> runner;

            @BeforeEach
            public void setUp() {
                runner = JsonSchemaValidationWorkflowRunner.familyBuilder()
                        .build();
            }

            @Test
            public void validFamilyYieldsNoErrors() throws Exception {
                JsonNode node = getCompleteFamilyJson();

                ValidationResults results = runner.validate(node.toPrettyString());

                assertThat(results.isValid(), is(true));
            }

            @ParameterizedTest
            @CsvSource({
                    "/id,                    DELETE,          '$.id: is missing but it is required'",
                    "/proband,               DELETE,          '$.proband: is missing but it is required'",
                    "/consanguinousParents,  DELETE,          '$.consanguinousParents: is missing but it is required'",
                    "/pedigree,              DELETE,          '$.pedigree: is missing but it is required'",
                    "/metaData,              DELETE,          '$.metaData: is missing but it is required'",
            })
            public void absenceOfTopLevelFamilyElementsYieldsErrors(String path, String action, String expected) throws Exception {
                testErrors(runner, getCompleteFamilyJson(), path, action, expected);
            }

            @ParameterizedTest
            @CsvSource({
                    "/pedigree/persons,     DELETE,          '$.pedigree.persons: is missing but it is required'",
                    "/pedigree/persons[*],  DELETE,          '$.pedigree.persons: there must be a minimum of 1 items in the array'",
            })
            public void emptyPedigreeYieldsError(String path, String action, String expected) throws Exception {
                testErrors(runner, getCompleteFamilyJson(), path, action, expected);
            }


        }

        @Nested
        public class RecommendedFieldsTest {
            // TODO - implement
        }

    }


    @Nested
    public class RequiredCohortFieldsTest {


    }

    /**
     * In principle, we do the same kind of testing in all parameterized tests.
     * We use the {@link #TAMPERER} to tamper with a {@code node}, performing a certain {@code action} on a {@code path}
     * to make the {@code invalid}.
     * Then we validate the invalid node with the {@code runner} and check we receive the expected {@code errors}.
     * <p>
     * This is what is done in this method.
     */
    private static <T extends MessageOrBuilder> void testErrors(ValidationWorkflowRunner<T> runner,
                                                                JsonNode node,
                                                                String path,
                                                                String action,
                                                                String errors) {
        JsonNode tampered = TAMPERER.tamper(node, path, Action.valueOf(action));

        ValidationResults results = runner.validate(tampered.toPrettyString());
        // TODO - remove after the tests are completed
//        results.validationResults().stream()
//                .map(ValidationResult::message)
//                .forEach(System.err::println);

        String[] tokens = errors.split("\\|");
        assertThat(results.validationResults(), hasSize(tokens.length));
        assertThat(results.validationResults().stream().allMatch(r -> r.level().isError()), is(true));
        assertThat(results.validationResults().stream().map(ValidationResult::message).toList(), containsInAnyOrder(tokens));
    }


}