package org.phenopackets.phenopackettools.validator.core.phenotype.orgsys;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.Collection;
import java.util.stream.Stream;

public class CohortHpoOrganSystemValidator extends AbstractOrganSystemValidator<CohortOrBuilder> {

    public CohortHpoOrganSystemValidator(Ontology hpo, Collection<TermId> organSystemTermIds) {
        super(hpo, organSystemTermIds);
    }

    @Override
    protected Stream<? extends PhenopacketOrBuilder> getPhenopackets(CohortOrBuilder component) {
        return component.getMembersOrBuilderList().stream();
    }


}
