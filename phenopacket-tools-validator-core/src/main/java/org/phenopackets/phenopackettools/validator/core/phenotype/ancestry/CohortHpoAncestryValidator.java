package org.phenopackets.phenopackettools.validator.core.phenotype.ancestry;

import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.stream.Stream;

public class CohortHpoAncestryValidator extends AbstractHpoAncestryValidator<CohortOrBuilder> {

    public CohortHpoAncestryValidator(MinimalOntology hpo) {
        super(hpo);
    }

    @Override
    protected Stream<? extends PhenopacketOrBuilder> extractPhenopackets(CohortOrBuilder message) {
        return message.getMembersList().stream();
    }
}
