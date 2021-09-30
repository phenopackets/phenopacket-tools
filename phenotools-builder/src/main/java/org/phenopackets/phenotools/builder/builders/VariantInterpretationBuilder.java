package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrsatile.v1.VariationDescriptor;
import org.phenopackets.schema.v2.core.AcmgPathogenicityClassification;
import org.phenopackets.schema.v2.core.TherapeuticActionability;
import org.phenopackets.schema.v2.core.VariantInterpretation;

public class VariantInterpretationBuilder {

    VariantInterpretation.Builder builder;

    public VariantInterpretationBuilder(VariationDescriptor descriptor) {
        builder = VariantInterpretation.newBuilder().setVariationDescriptor(descriptor);
    }

    public VariantInterpretationBuilder benign() {
        builder = builder.mergeFrom(builder.build()).setAcmgPathogenicityClassification(AcmgPathogenicityClassification.BENIGN);
        return this;
    }

    public VariantInterpretationBuilder likelyBenign() {
        builder = builder.mergeFrom(builder.build()).setAcmgPathogenicityClassification(AcmgPathogenicityClassification.LIKELY_BENIGN);
        return this;
    }

    public VariantInterpretationBuilder uncertainSignificance() {
        builder = builder.mergeFrom(builder.build()).setAcmgPathogenicityClassification(AcmgPathogenicityClassification.UNCERTAIN_SIGNIFICANCE);
        return this;
    }
    public VariantInterpretationBuilder likelyPathogenic() {
        builder = builder.mergeFrom(builder.build()).setAcmgPathogenicityClassification(AcmgPathogenicityClassification.LIKELY_PATHOGENIC);
        return this;
    }
    public VariantInterpretationBuilder pathogenic() {
        builder = builder.mergeFrom(builder.build()).setAcmgPathogenicityClassification(AcmgPathogenicityClassification.PATHOGENIC);
        return this;
    }
    public VariantInterpretationBuilder notActionable() {
        builder = builder.mergeFrom(builder.build()).setTherapeuticActionability(TherapeuticActionability.NOT_ACTIONABLE);
        return this;
    }

    public VariantInterpretationBuilder actionable() {
        builder = builder.mergeFrom(builder.build()).setTherapeuticActionability(TherapeuticActionability.ACTIONABLE);
        return this;
    }

    public static VariantInterpretationBuilder create(VariationDescriptor descriptor) {
        return new VariantInterpretationBuilder(descriptor);
    }

    public VariantInterpretation build() {
        return builder.build();
    }



}
