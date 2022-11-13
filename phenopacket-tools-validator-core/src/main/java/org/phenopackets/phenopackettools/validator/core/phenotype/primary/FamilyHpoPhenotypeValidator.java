package org.phenopackets.phenopackettools.validator.core.phenotype.primary;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.ArrayList;
import java.util.List;

public class FamilyHpoPhenotypeValidator extends AbstractHpoPhenotypeValidator<FamilyOrBuilder> {

    public FamilyHpoPhenotypeValidator(Ontology hpo) {
        super(hpo);
    }

    @Override
    public List<ValidationResult> validate(FamilyOrBuilder component) {
        List<ValidationResult> results = new ArrayList<>();

        // First check the proband.
        {
            Phenopacket proband = component.getProband();
            for (PhenotypicFeature feature : proband.getPhenotypicFeaturesList()) {
                checkPhenotypeFeature(proband, feature)
                        .forEach(results::add);
            }
        }

        // Then the relatives.
        for (Phenopacket relative : component.getRelativesList()) {
            for (PhenotypicFeature feature : relative.getPhenotypicFeaturesList()) {
                checkPhenotypeFeature(relative, feature)
                        .forEach(results::add);
            }
        }

        return results;
    }
}
