package org.phenopackets.phenopackettools.validator.core.phenotype.util;

import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    private Util() {
        // static utility class
    }

    public static PhenotypicFeaturesByExclusionStatus partitionByExclusionStatus(Collection<PhenotypicFeature> phenotypicFeatures) {
        Map<Boolean, Set<TermId>> byExclusion = phenotypicFeatures.stream()
                .map(toMaybeObservedTermId())
                .flatMap(Optional::stream)
                // Use `partitioningBy` instead of `groupingBy` to ensure the map contains keys
                // for both `true` and `false`. Then extract `TermId` and collect in a `Set`.
                .collect(Collectors.partitioningBy(MaybeExcludedTermId::excluded,
                        Collectors.mapping(MaybeExcludedTermId::termId, Collectors.toSet())));
        return new PhenotypicFeaturesByExclusionStatus(byExclusion.get(false), byExclusion.get(true));
    }

    private static Function<PhenotypicFeature, Optional<MaybeExcludedTermId>> toMaybeObservedTermId() {
        return pf -> MaybeExcludedTermId.fromPhenotypicFeature(pf)
                .or(() -> {
                    // Let's log the malformed term.
                    LOGGER.warn("Skipping validation of malformed term ID {}", pf.getType().getId());
                    return Optional.empty();
                });
    }
}
