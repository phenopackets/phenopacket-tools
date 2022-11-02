package org.phenopackets.phenopackettools.validator.core.phenotype;

import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.Arrays;

public class Utils {

    static Phenopacket.Builder createPhenopacket(String phenopacketId,
                                                 String subjectId,
                                                 PhenotypicFeature... features) {
        return Phenopacket.newBuilder()
                .setId(phenopacketId)
                .setSubject(Individual.newBuilder()
                        .setId(subjectId)
                        .build())
                .addAllPhenotypicFeatures(Arrays.asList(features));
    }

    static PhenotypicFeature createPhenotypicFeature(String id, String label, boolean excluded) {
        return PhenotypicFeature.newBuilder()
                .setType(OntologyClass.newBuilder()
                        .setId(id)
                        .setLabel(label)
                        .build())
                .setExcluded(excluded)
                .build();
    }

}
