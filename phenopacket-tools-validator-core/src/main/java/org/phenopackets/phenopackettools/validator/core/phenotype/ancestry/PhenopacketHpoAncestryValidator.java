package org.phenopackets.phenopackettools.validator.core.phenotype.ancestry;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.stream.Stream;

public class PhenopacketHpoAncestryValidator extends AbstractHpoAncestryValidator<PhenopacketOrBuilder> {

    public PhenopacketHpoAncestryValidator(Ontology hpo) {
        super(hpo);
    }

    @Override
    protected Stream<? extends PhenopacketOrBuilder> extractPhenopackets(PhenopacketOrBuilder message) {
        return Stream.of(message);
    }

}
