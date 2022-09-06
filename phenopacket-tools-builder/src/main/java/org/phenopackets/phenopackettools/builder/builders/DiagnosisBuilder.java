package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.GenomicInterpretation;
import org.phenopackets.schema.v2.core.OntologyClass;

public class DiagnosisBuilder {

    private final Diagnosis.Builder builder;

    private DiagnosisBuilder(OntologyClass disease) {
        builder = Diagnosis.newBuilder().setDisease(disease);
    }

    public DiagnosisBuilder addGenomicInterpretation(GenomicInterpretation interpretation) {
        builder.addGenomicInterpretations(interpretation);
        return this;
    }

    public static DiagnosisBuilder builder(OntologyClass disease) {
        return new DiagnosisBuilder(disease);
    }
    public static DiagnosisBuilder builder(String id, String label) {
        OntologyClass dx = OntologyClass.newBuilder().setId(id).setLabel(label).build();
        return new DiagnosisBuilder(dx);
    }

    public Diagnosis build() {
        return builder.build();
    }
}
