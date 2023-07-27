package org.phenopackets.phenopackettools.validator.core.phenotype.ancestry;

import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.stream.Stream;

public class FamilyHpoAncestryValidator extends AbstractHpoAncestryValidator<FamilyOrBuilder> {

    public FamilyHpoAncestryValidator(MinimalOntology hpo) {
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
