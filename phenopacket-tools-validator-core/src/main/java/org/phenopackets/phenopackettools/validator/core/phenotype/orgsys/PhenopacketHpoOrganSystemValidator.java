package org.phenopackets.phenopackettools.validator.core.phenotype.orgsys;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.List;
import java.util.stream.Stream;

public class PhenopacketHpoOrganSystemValidator extends AbstractOrganSystemValidator<PhenopacketOrBuilder> {

    public PhenopacketHpoOrganSystemValidator(Ontology hpo,
                                              List<TermId> organSystemTerms) {
        super(hpo, organSystemTerms);
    }

    @Override
    protected Stream<? extends PhenopacketOrBuilder> getPhenopackets(PhenopacketOrBuilder component) {
        return Stream.of(component);
    }
}
