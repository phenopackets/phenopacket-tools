package org.phenopackets.phenopackettools.validator.core.phenotype.ancestry;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.base.PhenolRuntimeException;
import org.monarchinitiative.phenol.ontology.algo.OntologyAlgorithm;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.phenotype.base.BaseHpoValidator;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class for pointing out violations of the annotation propagation rule.
 * <p>
 * The validator checks observed and excluded phenotype terms. The observed terms are checked for a presence of
 * an observed or an excluded ancestor, and a presence of such ancestor is pointed out as an error.
 * For instance, Abnormality of finger or <em>"NOT"</em> Abnormality of finger must not be present
 * in a patient annotated by Arachnodactyly. The <em>most specific</em> term (Arachnodactyly) must be used.
 * <p>
 * For the excluded terms, the validator checks for presence of an excluded children. Here, the least specific term
 * must be used. For instance, <em>"NOT"</em> Arachnodactyly must not be present in a patient annotated
 * with <em>"NOT"</em> Abnormality of finger. Only the <em>"NOT"</em> Abnormality of finger must be used.
 */
public abstract class AbstractHpoAncestryValidator<T extends MessageOrBuilder> extends BaseHpoValidator<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHpoAncestryValidator.class);

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "HpoAncestryValidator",
            "HPO ancestry phenotypic feature validator",
            "Validate that phenopacket does not contain an HPO term and its ancestor based on the provided HPO");
    private static final String APR_VIOLATION = "Violation of the annotation propagation rule";
    private static final String UNKNOWN = "UNKNOWN_NAME";

    AbstractHpoAncestryValidator(Ontology hpo) {
        super(hpo);
    }

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }

    @Override
    public List<ValidationResult> validate(T component) {
        return extractPhenopackets(component)
                .flatMap(pp -> validatePhenopacketPhenotypicFeatures(pp.getId(), pp.getPhenotypicFeaturesList()))
                .toList();
    }

    protected abstract Stream<? extends PhenopacketOrBuilder> extractPhenopackets(T message);

    private Stream<ValidationResult> validatePhenopacketPhenotypicFeatures(String id, List<PhenotypicFeature> phenotypicFeatures) {
        Map<Boolean, Set<TermId>> featuresByExclusion = phenotypicFeatures.stream()
                .map(toMaybeObservedTermId())
                .flatMap(Optional::stream)
                // Use `partitioningBy` instead of `groupingBy` to ensure the map contains keys
                // for both `true` and `false`. Then extract `TermId` and collect in a `Set`.
                .collect(Collectors.partitioningBy(MaybeExcludedTermId::excluded,
                        Collectors.mapping(MaybeExcludedTermId::termId, Collectors.toSet())));


        Stream.Builder<ValidationResult> results = Stream.builder();

        // Check that the component does not contain both observed term and its ancestor.
        Set<? extends TermId> allObserved = featuresByExclusion.get(false);
        Set<? extends TermId> allExcluded = featuresByExclusion.get(true);
        for (TermId observed : allObserved) {
            for (TermId ancestor : OntologyAlgorithm.getAncestorTerms(hpo, observed, false)) {
                if (allObserved.contains(ancestor))
                    results.add(constructResultForAnObservedTerm(id, observed, ancestor, false));
                if (allExcluded.contains(ancestor))
                    results.add(constructResultForAnObservedTerm(id, observed, ancestor, true));
            }
        }

        // Check that the component does not have negated descendant
        for (TermId excluded : allExcluded) {
            for (TermId child : OntologyAlgorithm.getDescendents(hpo, excluded)) {
                if (child.equals(excluded))
                    // skip the parent term
                    continue;
                if (allExcluded.contains(child))
                    results.add(constructResultForAnExcludedTerm(id, excluded, child));
            }
        }

        return results.build();
    }

    private static Function<PhenotypicFeature, Optional<MaybeExcludedTermId>> toMaybeObservedTermId() {
        return pf -> {
            TermId termId;
            try {
                termId = TermId.of(pf.getType().getId());
            } catch (PhenolRuntimeException e) {
                LOGGER.warn("Skipping ancestry validation of malformed term ID {}", pf.getType().getId());
                return Optional.empty();
            }
            return Optional.of(new MaybeExcludedTermId(termId, pf.getExcluded()));
        };
    }

    private ValidationResult constructResultForAnObservedTerm(String id, TermId observedId, TermId ancestorId, boolean ancestorIsExcluded) {
        Term observedTerm = hpo.getTermMap().get(observedId);
        String observedTermName = observedTerm == null ? UNKNOWN : observedTerm.getName();
        Term ancestorTerm = hpo.getTermMap().get(ancestorId);
        String ancestorTermName = ancestorTerm == null ? UNKNOWN : ancestorTerm.getName();
        String message;
        if (ancestorIsExcluded)
            message = "Phenotypic features of %s must not contain both an observed term (%s, %s) and an excluded ancestor (%s, %s)".formatted(
                    id, observedTermName, observedId.getValue(), ancestorTermName, ancestorId.getValue());
        else
            message = "Phenotypic features of %s must not contain both an observed term (%s, %s) and an observed ancestor (%s, %s)".formatted(
                id, observedTermName, observedId.getValue(), ancestorTermName, ancestorId.getValue());

        return ValidationResult.error(VALIDATOR_INFO, APR_VIOLATION, message);
    }

    private ValidationResult constructResultForAnExcludedTerm(String id, TermId excluded, TermId child) {
        Term excludedTerm = hpo.getTermMap().get(excluded);
        String excludedTermName = excludedTerm == null ? UNKNOWN : excludedTerm.getName();
        Term childTerm = hpo.getTermMap().get(child);
        String childTermName = childTerm == null ? UNKNOWN : childTerm.getName();
        String message = "Phenotypic features of %s must not contain both an excluded term (%s, %s) and an excluded child (%s, %s)".formatted(
                id, excludedTermName, excluded.getValue(), childTermName, child.getValue());

        return ValidationResult.error(VALIDATOR_INFO, APR_VIOLATION, message);
    }

    private record MaybeExcludedTermId(TermId termId, boolean excluded) {
    }
}
