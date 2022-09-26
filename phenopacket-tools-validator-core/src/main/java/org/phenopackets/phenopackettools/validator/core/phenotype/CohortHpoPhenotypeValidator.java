package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.ArrayList;
import java.util.List;

class CohortHpoPhenotypeValidator extends BaseHpoPhenotypeValidator<CohortOrBuilder> {

    CohortHpoPhenotypeValidator(Ontology hpo) {
        super(hpo);
    }

    @Override
    public List<ValidationResult> validate(CohortOrBuilder component) {
        List<ValidationResult> results = new ArrayList<>();

        for (Phenopacket member : component.getMembersList()) {
            Individual subject = member.getSubject();
            for (PhenotypicFeature feature : member.getPhenotypicFeaturesList()) {
                checkPhenotypeFeature(subject.getId(), feature)
                        .forEach(results::add);
            }
        }

        return results;
    }
}
