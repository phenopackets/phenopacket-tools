package org.phenopackets.phenopackettools.validator.core.phenotype.orgsys;

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
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

abstract class AbstractOrganSystemValidator<T extends MessageOrBuilder> extends BaseHpoValidator<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOrganSystemValidator.class);

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "HpoOrganSystemValidator",
            "HPO organ system validator",
            "Validate that HPO terms are well formatted, present, and non-obsolete based on the provided HPO");

    private static final String MISSING_ORGAN_SYSTEM_CATEGORY = "Missing organ system annotation";

    protected final List<TermId> organSystemTermIds;

    protected AbstractOrganSystemValidator(Ontology hpo,
                                           Collection<TermId> organSystemTermIds) {
        super(hpo);
        this.organSystemTermIds = Objects.requireNonNull(organSystemTermIds).stream()
                .distinct()
                .filter(organSystemTermIdIsInOntology(hpo))
                .sorted()
                .toList();
    }

    private static Predicate<TermId> organSystemTermIdIsInOntology(Ontology hpo) {
        return organSystemTermId -> {
            if (hpo.containsTerm(organSystemTermId)) {
                return true;
            } else {
                LOGGER.warn("{} is not present in the ontology", organSystemTermId.getValue());
                return false;
            }
        };
    }

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }

    @Override
    public List<ValidationResult> validate(T component) {
        return getPhenopackets(component)
                .flatMap(p -> checkPhenotypicFeatures(p.getSubject().getId(), p.getPhenotypicFeaturesList()))
                .toList();
    }

    protected abstract Stream<? extends PhenopacketOrBuilder> getPhenopackets(T component);

    private Stream<ValidationResult> checkPhenotypicFeatures(String individualId, List<PhenotypicFeature> features) {
        // Get a list of observed phenotypic feature term IDs.
        List<TermId> phenotypeFeatures = features.stream()
                .filter(pf -> !pf.getExcluded()) // TODO - should we only work with the observed features?
                .map(PhenotypicFeature::getType)
                .map(toTermId(individualId))
                .flatMap(Optional::stream)
                .toList();


        Stream.Builder<ValidationResult> results = Stream.builder();
        // Check we have at least one phenotypeFeature (pf) that is a descendant of given organSystemId
        // and report otherwise.
        organSystemLoop:
        for (TermId organSystemId : organSystemTermIds) {
            for (TermId pf : phenotypeFeatures) {
                if (OntologyAlgorithm.existsPath(hpo, pf, organSystemId)) {
                    continue organSystemLoop; // It only takes one termId to annotate an organ system.
                }
            }

            // If we get here, then the organSystemId is not annotated, and we report a validation error.
            Term organSystem = hpo.getTermMap().get(organSystemId);
            ValidationResult result = ValidationResult.error(VALIDATOR_INFO,
                    MISSING_ORGAN_SYSTEM_CATEGORY,
                    "Missing annotation for %s [%s] in '%s'"
                            .formatted(organSystem.getName(), organSystem.id().getValue(), individualId));
            results.add(result);
        }

        return results.build();
    }

    /**
     * @return a function that maps {@link OntologyClass} into a {@link TermId} and emit warning otherwise.
     */
    private static Function<OntologyClass, Optional<TermId>> toTermId(String individualId) {
        return oc -> {
            try {
                return Optional.of(TermId.of(oc.getId()));
            } catch (PhenolRuntimeException e) {
                LOGGER.warn("Invalid term ID {} in individual {}", oc.getId(), individualId);
                return Optional.empty();
            }
        };
    }
}
