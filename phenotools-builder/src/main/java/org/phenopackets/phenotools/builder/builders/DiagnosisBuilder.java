package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.GenomicInterpretation;
import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.List;

public class DiagnosisBuilder {

    private DiagnosisBuilder() {
    }

    public static Diagnosis diagnosis(OntologyClass disease) {
        return Diagnosis.newBuilder().setDisease(disease).build();
    }

    public static Diagnosis diagnosis(OntologyClass disease, GenomicInterpretation genomicInterpretation) {
        return Diagnosis.newBuilder()
                .setDisease(disease)
                .addGenomicInterpretations(genomicInterpretation)
                .build();
    }

    public static Diagnosis diagnosis(OntologyClass disease, GenomicInterpretation... genomicInterpretations) {
        return diagnosis(disease, List.of(genomicInterpretations));
    }

    public static Diagnosis diagnosis(OntologyClass disease, List<GenomicInterpretation> genomicInterpretations) {
        return Diagnosis.newBuilder()
                .setDisease(disease)
                .addAllGenomicInterpretations(genomicInterpretations)
                .build();
    }
}
