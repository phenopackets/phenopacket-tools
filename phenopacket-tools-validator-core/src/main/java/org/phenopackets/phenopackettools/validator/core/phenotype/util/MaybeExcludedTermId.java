package org.phenopackets.phenopackettools.validator.core.phenotype.util;

import org.monarchinitiative.phenol.base.PhenolRuntimeException;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.Optional;

record MaybeExcludedTermId(TermId termId, boolean excluded) {

    static Optional<MaybeExcludedTermId> fromPhenotypicFeature(PhenotypicFeature phenotypicFeature) {
        TermId termId;
        try {
            termId = TermId.of(phenotypicFeature.getType().getId());
        } catch (PhenolRuntimeException e) {
            return Optional.empty();
        }
        return Optional.of(new MaybeExcludedTermId(termId, phenotypicFeature.getExcluded()));
    }
}
