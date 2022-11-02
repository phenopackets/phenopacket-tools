package org.phenopackets.phenopackettools.validator.core.phenotype.orgsys;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.Collection;
import java.util.stream.Stream;

public class FamilyHpoOrganSystemValidator extends AbstractOrganSystemValidator<FamilyOrBuilder> {

    protected FamilyHpoOrganSystemValidator(Ontology hpo, Collection<TermId> organSystemTermIds) {
        super(hpo, organSystemTermIds);
    }

    @Override
    protected Stream<? extends PhenopacketOrBuilder> getPhenopackets(FamilyOrBuilder component) {
        return Stream.concat(
                Stream.of(component.getProband()),
                component.getRelativesList().stream()
        );
    }

}
