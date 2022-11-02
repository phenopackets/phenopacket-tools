package org.phenopackets.phenopackettools.validator.core.phenotype.util;

import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.Set;

public record PhenotypicFeaturesByExclusionStatus(Set<TermId> observedPhenotypicFeatures,
                                                  Set<TermId> excludedPhenotypicFeatures) {
}
