package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.Interpretation;

public class InterpretationBuilder {

    Interpretation.Builder builder;

    InterpretationBuilder(String id, Interpretation.ProgressStatus status) {
        builder = Interpretation.newBuilder().setId(id).setProgressStatus(status);
    }

    InterpretationBuilder diagnosis(Diagnosis dx) {
        builder = builder.mergeFrom(builder.build()).setDiagnosis(dx);
        return this;
    }


    InterpretationBuilder summary(String sm) {
        builder = builder.mergeFrom(builder.build()).setSummary(sm);
        return this;
    }


    public static InterpretationBuilder create(String id, Interpretation.ProgressStatus status) {
        return new InterpretationBuilder(id, status);
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


}
