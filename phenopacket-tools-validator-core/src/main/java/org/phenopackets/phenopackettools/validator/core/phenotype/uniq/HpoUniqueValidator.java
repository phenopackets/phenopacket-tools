package org.phenopackets.phenopackettools.validator.core.phenotype.uniq;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.base.PhenolRuntimeException;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.phenotype.base.BaseHpoValidator;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Check that phenotypic features of a phenopacket are present at most once.
 * <p>
 * The validator groups the phenotypic features by the observation status
 * and tests that the phenotypic features are present at most once.
 */
public abstract class HpoUniqueValidator<T extends MessageOrBuilder> extends BaseHpoValidator<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HpoUniqueValidator.class);

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "HpoUniqueValidator",
            "HPO unique phenotypic feature validator",
            "Validate that phenopacket does not contain an HPO term more than once");
    private static final String CATEGORY = "Non-unique phenotypic feature";

    public static PhenopacketValidator<PhenopacketOrBuilder> phenopacketValidator(MinimalOntology hpo) {
        return new PhenopacketHpoUniqueValidator(hpo);
    }

    public static PhenopacketValidator<CohortOrBuilder> cohortValidator(MinimalOntology hpo) {
        return new CohortHpoUniqueValidator(hpo);
    }

    public static PhenopacketValidator<FamilyOrBuilder> familyValidator(MinimalOntology hpo) {
        return new FamilyHpoUniqueValidator(hpo);
    }

    private HpoUniqueValidator(MinimalOntology hpo) {
        super(hpo);
    }

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }

    @Override
    public List<ValidationResult> validate(T component) {
        return extractPhenopackets(component)
                .flatMap(pp -> validatePhenotypicFeatures(pp.getId(), pp.getPhenotypicFeaturesList()))
                .toList();
    }

    protected abstract Stream<? extends PhenopacketOrBuilder> extractPhenopackets(T message);

    private Stream<ValidationResult> validatePhenotypicFeatures(String id, Iterable<PhenotypicFeature> phenotypicFeatures) {
        // Count feature occurrences
        Map<Boolean, Map<String, Integer>> counter = new HashMap<>();
        for (PhenotypicFeature pf : phenotypicFeatures) {
            counter.computeIfAbsent(pf.getExcluded(), s -> new HashMap<>())
                    .merge(pf.getType().getId(), 1, Integer::sum);
        }

        // Build results, first for the present features then for the excluded.
        Stream.Builder<ValidationResult> results = Stream.builder();
        for (Map.Entry<String, Integer> e : counter.getOrDefault(false, Map.of()).entrySet()) {
            if (e.getValue() > 1) {
                String termName = extractTermName(e.getKey());
                String msg = """
                        Phenotypic features of %s must not contain the same observed feature %s (%s)
                        more than once but the feature was present %d times""".formatted(id,
                        termName, e.getKey(), e.getValue());
                results.add(ValidationResult.error(VALIDATOR_INFO, CATEGORY, msg));
            }
        }

        for (Map.Entry<String, Integer> e : counter.getOrDefault(true, Map.of()).entrySet()) {
            if (e.getValue() > 1) {
                String termName = extractTermName(e.getKey());
                String msg = """
                        Phenotypic features of %s must not contain the same excluded feature %s (%s)
                        more than once but the feature was present %d times""".formatted(id,
                        termName, e.getKey(), e.getValue());
                results.add(ValidationResult.error(VALIDATOR_INFO, CATEGORY, msg));
            }

        }

        return results.build();
    }

    private String extractTermName(String curie) {
        try {
            return hpo.termForTermId(TermId.of(curie))
                        .map(Term::getName)
                        .orElse(UNKNOWN);
        } catch (PhenolRuntimeException ex) {
            LOGGER.debug("Invalid term ID {}", curie, ex);
            return UNKNOWN;
        }
    }

    private static class PhenopacketHpoUniqueValidator extends HpoUniqueValidator<PhenopacketOrBuilder> {

        private PhenopacketHpoUniqueValidator(MinimalOntology hpo) {
            super(hpo);
        }

        @Override
        protected Stream<? extends PhenopacketOrBuilder> extractPhenopackets(PhenopacketOrBuilder message) {
            return Stream.of(message);
        }
    }

    private static class CohortHpoUniqueValidator extends HpoUniqueValidator<CohortOrBuilder> {

        public CohortHpoUniqueValidator(MinimalOntology hpo) {
            super(hpo);
        }

        @Override
        protected Stream<? extends PhenopacketOrBuilder> extractPhenopackets(CohortOrBuilder message) {
            return message.getMembersList().stream();
        }
    }

    private static class FamilyHpoUniqueValidator extends HpoUniqueValidator<FamilyOrBuilder> {

        public FamilyHpoUniqueValidator(MinimalOntology hpo) {
            super(hpo);
        }

        @Override
        protected Stream<? extends PhenopacketOrBuilder> extractPhenopackets(FamilyOrBuilder message) {
            Stream.Builder<PhenopacketOrBuilder> builder = Stream.builder();
            builder.accept(message.getProband());

            for (Phenopacket relative : message.getRelativesList())
                builder.add(relative);

            return builder.build();
        }
    }
}
