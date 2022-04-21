package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.Interpretation;

public class InterpretationBuilder {

    private final Interpretation.Builder builder;

    private InterpretationBuilder(String interpretationId, Interpretation.ProgressStatus status) {
        builder = Interpretation.newBuilder().setId(interpretationId).setProgressStatus(status);
    }

    public static Interpretation of(String interpretationId, Interpretation.ProgressStatus status, Diagnosis diagnosis, String summary) {
        return Interpretation.newBuilder().setId(interpretationId).setProgressStatus(status).setDiagnosis(diagnosis).setSummary(summary).build();
    }

    public static InterpretationBuilder builder(String interpretationId) {
        return new InterpretationBuilder(interpretationId, Interpretation.ProgressStatus.UNKNOWN_PROGRESS);
    }

    public InterpretationBuilder summary(String summary) {
        builder.setSummary(summary);
        return this;
    }

    public Interpretation inProgress() {
        builder.setProgressStatus(Interpretation.ProgressStatus.IN_PROGRESS);
        return builder.build();
    }

    public Interpretation completed(Diagnosis diagnosis) {
        builder.setProgressStatus(Interpretation.ProgressStatus.COMPLETED);
        builder.setDiagnosis(diagnosis);
        return builder.build();
    }

    public Interpretation solved(Diagnosis diagnosis) {
        builder.setProgressStatus(Interpretation.ProgressStatus.SOLVED);
        builder.setDiagnosis(diagnosis);
        return builder.build();
    }

    public Interpretation unsolved() {
        builder.setProgressStatus(Interpretation.ProgressStatus.UNSOLVED);
        builder.clearDiagnosis();
        return builder.build();
    }

}
