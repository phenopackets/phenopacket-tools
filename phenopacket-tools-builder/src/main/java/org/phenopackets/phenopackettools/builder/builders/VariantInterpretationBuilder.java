package org.phenopackets.phenopackettools.builder.builders;

import org.ga4gh.vrsatile.v1.VariationDescriptor;
import org.phenopackets.schema.v2.core.AcmgPathogenicityClassification;
import org.phenopackets.schema.v2.core.TherapeuticActionability;
import org.phenopackets.schema.v2.core.VariantInterpretation;

public class VariantInterpretationBuilder {

    final VariantInterpretation.Builder builder;

    private VariantInterpretationBuilder(VariationDescriptor descriptor) {
        builder = VariantInterpretation.newBuilder().setVariationDescriptor(descriptor);
    }

    public static VariantInterpretation of(VariationDescriptor descriptor, AcmgPathogenicityClassification acmgPathogenicityClassification) {
        return VariantInterpretation.newBuilder()
                .setVariationDescriptor(descriptor)
                .setAcmgPathogenicityClassification(acmgPathogenicityClassification)
                // will default to UNKNOWN_ACTIONABILITY
                .build();
    }

    public static VariantInterpretation of(VariationDescriptor descriptor, AcmgPathogenicityClassification acmgPathogenicityClassification, TherapeuticActionability therapeuticActionability) {
        return VariantInterpretation.newBuilder()
                .setVariationDescriptor(descriptor)
                .setAcmgPathogenicityClassification(acmgPathogenicityClassification)
                .setTherapeuticActionability(therapeuticActionability)
                .build();
    }

    public static VariantInterpretationBuilder builder(VariationDescriptor descriptor) {
        return new VariantInterpretationBuilder(descriptor);
    }

    public static VariantInterpretationBuilder builder(VariationDescriptorBuilder builder) {
        return new VariantInterpretationBuilder(builder.build());
    }

    public VariantInterpretationBuilder benign() {
        builder.setAcmgPathogenicityClassification(AcmgPathogenicityClassification.BENIGN);
        return this;
    }

    public VariantInterpretationBuilder likelyBenign() {
        builder.setAcmgPathogenicityClassification(AcmgPathogenicityClassification.LIKELY_BENIGN);
        return this;
    }

    public VariantInterpretationBuilder uncertainSignificance() {
        builder.setAcmgPathogenicityClassification(AcmgPathogenicityClassification.UNCERTAIN_SIGNIFICANCE);
        return this;
    }

    public VariantInterpretationBuilder likelyPathogenic() {
        builder.setAcmgPathogenicityClassification(AcmgPathogenicityClassification.LIKELY_PATHOGENIC);
        return this;
    }

    public VariantInterpretationBuilder pathogenic() {
        builder.setAcmgPathogenicityClassification(AcmgPathogenicityClassification.PATHOGENIC);
        return this;
    }

    public VariantInterpretationBuilder notActionable() {
        builder.setTherapeuticActionability(TherapeuticActionability.NOT_ACTIONABLE);
        return this;
    }

    public VariantInterpretationBuilder actionable() {
        builder.setTherapeuticActionability(TherapeuticActionability.ACTIONABLE);
        return this;
    }

    public VariantInterpretation build() {
        return builder.build();
    }
}
