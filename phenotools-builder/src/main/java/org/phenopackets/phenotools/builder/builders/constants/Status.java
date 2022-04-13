package org.phenopackets.phenotools.builder.builders.constants;

import org.phenopackets.schema.v2.core.AcmgPathogenicityClassification;
import org.phenopackets.schema.v2.core.GenomicInterpretation.InterpretationStatus;
import org.phenopackets.schema.v2.core.Interpretation.ProgressStatus;
import org.phenopackets.schema.v2.core.TherapeuticActionability;

import static org.phenopackets.schema.v2.core.Interpretation.ProgressStatus.*;

/**
 * Catch-all class for succinct provision of status codes.
 */
public class Status {

    private Status() {
    }


    public static ProgressStatus inProgress() {
        return IN_PROGRESS;
    }

    public static ProgressStatus completed() {
        return COMPLETED;
    }

    public static ProgressStatus solved() {
        return SOLVED;
    }

    public static ProgressStatus unsolved() {
        return UNSOLVED;
    }


    public static InterpretationStatus rejected() {
        return InterpretationStatus.REJECTED;
    }

    public static InterpretationStatus candidate() {
        return InterpretationStatus.CANDIDATE;
    }

    public static InterpretationStatus contributory() {
        return InterpretationStatus.CONTRIBUTORY;
    }

    public static InterpretationStatus causative() {
        return InterpretationStatus.CAUSATIVE;
    }


    public static AcmgPathogenicityClassification benign() {
        return AcmgPathogenicityClassification.BENIGN;
    }

    public static AcmgPathogenicityClassification likelyBenign() {
        return AcmgPathogenicityClassification.LIKELY_BENIGN;
    }

    public static AcmgPathogenicityClassification uncertainSignificance() {
        return AcmgPathogenicityClassification.UNCERTAIN_SIGNIFICANCE;
    }

    public static AcmgPathogenicityClassification likelyPathogenic() {
        return AcmgPathogenicityClassification.LIKELY_PATHOGENIC;
    }

    public static AcmgPathogenicityClassification pathogenic() {
        return AcmgPathogenicityClassification.PATHOGENIC;
    }


    public static TherapeuticActionability notActionable() {
        return TherapeuticActionability.NOT_ACTIONABLE;
    }

    public static TherapeuticActionability actionable() {
        return TherapeuticActionability.ACTIONABLE;
    }
}
