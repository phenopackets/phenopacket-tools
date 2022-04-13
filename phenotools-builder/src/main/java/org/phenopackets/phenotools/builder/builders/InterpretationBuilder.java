package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.GenomicInterpretation;
import org.phenopackets.schema.v2.core.Interpretation;

import java.util.ArrayList;
import java.util.List;

public class InterpretationBuilder {

    private final Interpretation.Builder builder;
    private final List<GenomicInterpretation> genomicInterpretations;

    private InterpretationBuilder(String interpretationId, Interpretation.ProgressStatus status) {
        builder = Interpretation.newBuilder().setId(interpretationId).setProgressStatus(status);
        genomicInterpretations = new ArrayList<>();
    }

    public static Interpretation interpretation(String interpretationId, Interpretation.ProgressStatus status, Diagnosis dx, String summary) {
        return Interpretation.newBuilder().setId(interpretationId).setProgressStatus(status).setDiagnosis(dx).setSummary(summary).build();
    }

    public static InterpretationBuilder builder(String interpretationId, Interpretation.ProgressStatus status) {
        return new InterpretationBuilder(interpretationId, status);
    }


    public InterpretationBuilder summary(String summary) {
        builder.setSummary(summary);
        return this;
    }

    public InterpretationBuilder diagnosis(Diagnosis diagnosis) {
        builder.setDiagnosis(diagnosis);
        return this;
    }

    public static InterpretationBuilder inProgress(String interpretationId) {
        return builder(interpretationId, Interpretation.ProgressStatus.IN_PROGRESS);
    }

    public static InterpretationBuilder completed(String interpretationId) {
        return builder(interpretationId, Interpretation.ProgressStatus.COMPLETED);
    }

    public static InterpretationBuilder solved(String interpretationId) {
        return builder(interpretationId, Interpretation.ProgressStatus.SOLVED);
    }

    public static InterpretationBuilder unsolved(String interpretationId) {
        return builder(interpretationId, Interpretation.ProgressStatus.UNSOLVED);
    }

    public Interpretation build() {
        return builder.build();
    }


}
