package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.GenomicInterpretation;
import org.phenopackets.schema.v2.core.OntologyClass;

public class DiagnosisBuilder {

    private final Diagnosis.Builder builder;

    private DiagnosisBuilder(OntologyClass disease) {
        builder = Diagnosis.newBuilder().setDisease(disease);
    }

    public DiagnosisBuilder genomicInterpretation(GenomicInterpretation interpretation) {
        builder.addGenomicInterpretations(interpretation);
        return this;
    }

    public static DiagnosisBuilder create(OntologyClass disease) {
        return new DiagnosisBuilder(disease);
    }

    public Diagnosis build() {
        return builder.build();
    }
}
