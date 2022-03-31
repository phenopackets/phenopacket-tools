package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.GenomicInterpretation;
import org.phenopackets.schema.v2.core.Interpretation;

import java.util.ArrayList;
import java.util.List;

public class InterpretationBuilder {

    private final Interpretation.Builder builder;
    private final List<GenomicInterpretation> genomicInterpretations;

    private InterpretationBuilder(String id, Interpretation.ProgressStatus status) {
        builder = Interpretation.newBuilder().setId(id).setProgressStatus(status);
        genomicInterpretations = new ArrayList<>();
    }

    public static Interpretation interpretation(String id, Interpretation.ProgressStatus status) {
        return Interpretation.newBuilder().setId(id).setProgressStatus(status).build();
    }

    public static Interpretation interpretation(String id, Interpretation.ProgressStatus status, Diagnosis dx) {
        return Interpretation.newBuilder().setId(id).setProgressStatus(status).setDiagnosis(dx).build();
    }

    public static Interpretation interpretation(String id, Interpretation.ProgressStatus status, Diagnosis dx, String summary) {
        return Interpretation.newBuilder().setId(id).setProgressStatus(status).setDiagnosis(dx).setSummary(summary).build();
    }

    public static InterpretationBuilder create(String id, Interpretation.ProgressStatus status) {
        return new InterpretationBuilder(id, status);
    }


    public InterpretationBuilder summary(String sm) {
        builder.setSummary(sm);
        return this;
    }

    public InterpretationBuilder diagnosis(Diagnosis diagnosis) {
        builder.setDiagnosis(diagnosis);
        return this;
    }

    public static InterpretationBuilder inProgress(String id) {
        return create(id, Interpretation.ProgressStatus.IN_PROGRESS);
    }

    public static InterpretationBuilder completed(String id) {
        return create(id, Interpretation.ProgressStatus.COMPLETED);
    }

    public static InterpretationBuilder solved(String id) {
        return create(id, Interpretation.ProgressStatus.SOLVED);
    }

    public static InterpretationBuilder unsolved(String id) {
        return create(id, Interpretation.ProgressStatus.UNSOLVED);
    }

    public Interpretation build() {
        return builder.build();
    }


}
