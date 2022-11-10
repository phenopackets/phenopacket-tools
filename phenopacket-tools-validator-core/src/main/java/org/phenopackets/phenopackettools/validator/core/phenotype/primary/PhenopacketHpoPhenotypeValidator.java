package org.phenopackets.phenopackettools.validator.core.phenotype.primary;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.ArrayList;
import java.util.List;

public class PhenopacketHpoPhenotypeValidator extends AbstractHpoPhenotypeValidator<PhenopacketOrBuilder> {

    public PhenopacketHpoPhenotypeValidator(Ontology hpo) {
        super(hpo);
    }

    @Override
    public List<ValidationResult> validate(PhenopacketOrBuilder component) {
        List<ValidationResult> results = new ArrayList<>();

        for (PhenotypicFeature feature : component.getPhenotypicFeaturesList()) {
            checkPhenotypeFeature(component, feature)
                    .forEach(results::add);
        }

        return results;
    }
}
