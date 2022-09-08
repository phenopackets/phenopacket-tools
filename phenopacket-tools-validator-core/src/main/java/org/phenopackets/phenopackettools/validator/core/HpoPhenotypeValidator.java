package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.databind.JsonNode;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.validator.core.errors.OntologyValidationResult;
import org.phenopackets.phenopackettools.validator.core.impl.DefaultValidationInfo;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.phenopackets.schema.v2.Phenopacket;

import java.util.ArrayList;
import java.util.List;

@Deprecated(forRemoval = true) // or move elsewhere
public class HpoPhenotypeValidator implements PhenopacketValidator {


    private static final ValidatorInfo hpoValidatorInfo =
            DefaultValidationInfo.of("HpoPhenotypeValidator", "HPO Phenotypic feature validator", "");


    private final Ontology hpo;

    private final String hpoVersion;


    public HpoPhenotypeValidator(Ontology ontology) {
        this.hpo = ontology;
        this.hpoVersion = this.hpo.getMetaInfo().getOrDefault("data-version", "HPO");
    }


    /**
     * This validator expects to get a Phenopacket message as input and should only be called
     * via the other function
     */
    @Override
    public List< ValidationResult> validateJson(JsonNode jsonNode) {
        return List.of();
    }

    @Override
    public List<ValidationResult> validateMessage(Phenopacket phenopacket) {
        List<PhenotypicFeature> features = phenopacket.getPhenotypicFeaturesList();
        List<ValidationResult> errors = new ArrayList<>();
        // check that all terms have the current primary ID
        for (var feature: features) {
            OntologyClass clz = feature.getType();
            TermId tid = TermId.of(clz.getId());
            if (! hpo.containsTerm(tid)) {
                String msg = String.format("%s not found in %s", tid.getValue(),  this.hpoVersion);
                ValidationResult res = OntologyValidationResult.invalidTermId(hpoValidatorInfo,msg);
                errors.add(res);
            }
            // check if we are using the current primary id
            // this is a warning
            TermId primaryId = hpo.getPrimaryTermId(tid);
            if (! primaryId.equals(tid)) {
                String msg = String.format("Using obsoleted id (%s) instead of current primary id (%s)",
                        tid.getValue(),  primaryId.getValue());
                OntologyValidationResult res = OntologyValidationResult.obsoletedTermId(hpoValidatorInfo,msg);
                errors.add(res);
            }
        }
        return errors;
    }
}
