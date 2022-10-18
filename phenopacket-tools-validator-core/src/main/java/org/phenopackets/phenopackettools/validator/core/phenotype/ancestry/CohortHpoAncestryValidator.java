package org.phenopackets.phenopackettools.validator.core.phenotype.ancestry;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.stream.Stream;

public class CohortHpoAncestryValidator extends AbstractHpoAncestryValidator<CohortOrBuilder> {

    public CohortHpoAncestryValidator(Ontology hpo) {
        super(hpo);
    }

    @Override
    protected Stream<? extends PhenopacketOrBuilder> extractPhenopackets(CohortOrBuilder message) {
        return message.getMembersList().stream();
    }
}
