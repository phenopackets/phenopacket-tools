package org.phenopackets.phenopackettools.validator.core.phenotype;

import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.TestData;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HpoPhenotypeValidatorTest {

    private static final Ontology HPO = TestData.HPO;

    @Nested
    public class PhenopacketTest {

        private PhenopacketHpoPhenotypeValidator validator;

        @BeforeEach
        public void setUp() {
            validator = new PhenopacketHpoPhenotypeValidator(HPO);
        }

        @Test
        public void testMinimalValidInput() throws Exception {
            String payload = """
                    {
                      "subject": {
                        "id": "proband A"
                      },
                      "phenotypicFeatures": [{
                        "type": {
                          "id": "HP:0001238",
                          "label": "Slender finger"
                        }
                      }, {
                        "type": {
                          "id": "HP:0100807",
                          "label": "Long fingers"
                        }
                      }]
                    }""";
            Phenopacket.Builder phenopacket = Phenopacket.newBuilder();
            JsonFormat.parser().merge(payload, phenopacket);

            List<ValidationResult> results = validator.validate(phenopacket);
            assertThat(results.isEmpty(), equalTo(true));
        }


        @Test
        public void testMissingTermId() throws Exception {
            // Tapered finger is not present in the used `hp.module.json`
            String minimalPhenopacketJson = """
                    {
                      "subject": {
                        "id": "proband A"
                      },
                      "phenotypicFeatures": [{
                        "type": {
                          "id": "HP:0001182",
                          "label": "Tapered finger"
                        }
                      }, {
                        "type": {
                          "id": "HP:0100807",
                          "label": "Long fingers"
                        }
                      }]
                    }""";
            Phenopacket.Builder phenopacket = Phenopacket.newBuilder();
            JsonFormat.parser().merge(minimalPhenopacketJson, phenopacket);

            List<ValidationResult> results = validator.validate(phenopacket);
            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Invalid TermId"));
            assertThat(result.message(), equalTo("HP:0001182 in 'proband A' not found in http://purl.obolibrary.org/obo/hp/releases/2021-06-08/hp.json"));
        }

        @Test
        public void testObsoleteTermId() throws Exception {
            // `HP:0001505` is obsoleted and Arachnodactyly should be used instead.
            String minimalPhenopacketJson = """
                    {
                      "subject": {
                        "id": "proband A"
                      },
                      "phenotypicFeatures": [{
                        "type": {
                          "id": "HP:0100807",
                          "label": "Long fingers"
                        }
                      },{
                        "type": {
                          "id": "HP:0001505",
                          "label": "Some random obsoleted concept"
                        }
                      }]
                    }""";
            Phenopacket.Builder phenopacket = Phenopacket.newBuilder();
            JsonFormat.parser().merge(minimalPhenopacketJson, phenopacket);

            List<ValidationResult> results = validator.validate(phenopacket);
            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.WARNING));
            assertThat(result.category(), equalTo("Obsoleted TermId"));
            assertThat(result.message(), equalTo("Using obsoleted id (HP:0001505) instead of current primary id (HP:0001166) in 'proband A'"));
        }

        @Test
        public void testMistypedTermId() throws Exception {
            // `HP_0100807` is an invalid value.
            String minimalPhenopacketJson = """
                    {
                      "subject": {
                        "id": "proband A"
                      },
                      "phenotypicFeatures": [{
                        "type": {
                          "id": "HP_0100807",
                          "label": "Long fingers"
                        }
                      }]
                    }""";
            Phenopacket.Builder phenopacket = Phenopacket.newBuilder();
            JsonFormat.parser().merge(minimalPhenopacketJson, phenopacket);

            List<ValidationResult> results = validator.validate(phenopacket);
            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Invalid TermId"));
            assertThat(result.message(), equalTo("The HP_0100807 found in 'proband A' is not a valid value"));
        }
    }

    /**
     * We only test one error type as the other types are tested in {@link PhenopacketTest} above.
     */
    @Nested
    public class FamilyTest {
        private FamilyHpoPhenotypeValidator validator;

        @BeforeEach
        public void setUp() {
            validator = new FamilyHpoPhenotypeValidator(HPO);
        }

        @Test
        public void testMinimalValidInput() throws Exception {
            String payload = """
                    {
                      "proband": {
                        "subject": {
                          "id": "Flynn"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      },
                      "relatives": [{
                        "subject": {
                          "id": "Walt"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }, {
                        "subject": {
                          "id": "Skyler"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }]
                    }""";
            Family.Builder family = Family.newBuilder();
            JsonFormat.parser().merge(payload, family);

            List<ValidationResult> results = validator.validate(family);
            assertThat(results.isEmpty(), equalTo(true));
        }

        @Test
        public void testInvalidIdInProband() throws Exception {
            String payload = """
                    {
                      "proband": {
                        "subject": {
                          "id": "Flynn"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP_0001238",
                            "label": "Slender finger"
                          }
                        }]
                      },
                      "relatives": [{
                        "subject": {
                          "id": "Walt"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }, {
                        "subject": {
                          "id": "Skyler"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }]
                    }""";
            Family.Builder family = Family.newBuilder();
            JsonFormat.parser().merge(payload, family);

            List<ValidationResult> results = validator.validate(family);
            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Invalid TermId"));
            assertThat(result.message(), equalTo("The HP_0001238 found in 'Flynn' is not a valid value"));
        }

        @Test
        public void testInvalidIdInRelative() throws Exception {
            String payload = """
                    {
                      "proband": {
                        "subject": {
                          "id": "Flynn"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      },
                      "relatives": [{
                        "subject": {
                          "id": "Walt"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP_0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }, {
                        "subject": {
                          "id": "Skyler"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }]
                    }""";
            Family.Builder family = Family.newBuilder();
            JsonFormat.parser().merge(payload, family);

            List<ValidationResult> results = validator.validate(family);
            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Invalid TermId"));
            assertThat(result.message(), equalTo("The HP_0001238 found in 'Walt' is not a valid value"));
        }
    }

    /**
     * We only test one error type as the other types are tested in {@link PhenopacketTest} above.
     */
    @Nested
    public class CohortTest {

        private CohortHpoPhenotypeValidator validator;

        @BeforeEach
        public void setUp() {
            validator = new CohortHpoPhenotypeValidator(HPO);
        }

        @Test
        public void testMinimalValidInput() throws Exception {
            String payload = """
                    {
                      "members": [{
                        "subject": {
                          "id": "Thing 1"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }, {
                        "subject": {
                          "id": "Thing 1"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }]
                    }
                    """;

            Cohort.Builder cohort = Cohort.newBuilder();
            JsonFormat.parser().merge(payload, cohort);

            List<ValidationResult> results = validator.validate(cohort);
            assertThat(results.isEmpty(), equalTo(true));
        }

        @Test
        public void testInvalidId() throws Exception {
            String payload = """
                    {
                      "members": [{
                        "subject": {
                          "id": "Thing 1"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP_0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }, {
                        "subject": {
                          "id": "Thing 1"
                        },
                        "phenotypicFeatures": [{
                          "type": {
                            "id": "HP:0001238",
                            "label": "Slender finger"
                          }
                        }]
                      }]
                    }
                    """;

            Cohort.Builder cohort = Cohort.newBuilder();
            JsonFormat.parser().merge(payload, cohort);

            List<ValidationResult> results = validator.validate(cohort);
            assertThat(results, hasSize(1));
            ValidationResult result = results.get(0);
            assertThat(result.level(), equalTo(ValidationLevel.ERROR));
            assertThat(result.category(), equalTo("Invalid TermId"));
            assertThat(result.message(), equalTo("The HP_0001238 found in 'Thing 1' is not a valid value"));
        }
    }



}