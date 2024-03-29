package org.phenopackets.phenopackettools.builder.builders;


import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.schema.v2.core.GenomicInterpretation;
import org.phenopackets.schema.v2.core.GenomicInterpretation.InterpretationStatus;
import org.phenopackets.schema.v2.core.VariantInterpretation;

public class GenomicInterpretationBuilder {


    private final GenomicInterpretation.Builder builder;

    private GenomicInterpretationBuilder(String subjectOrBiosampleId){
        builder = GenomicInterpretation.newBuilder().setSubjectOrBiosampleId(subjectOrBiosampleId);
    }

    public static GenomicInterpretationBuilder builder(String subjectOrBiosampleId) {
        return new GenomicInterpretationBuilder(subjectOrBiosampleId);
    }

    public GenomicInterpretationBuilder rejected() {
        builder.setInterpretationStatus(InterpretationStatus.REJECTED);
        return this;
    }

    public GenomicInterpretationBuilder candidate() {
        builder.setInterpretationStatus(InterpretationStatus.CANDIDATE);
        return this;
    }

    public GenomicInterpretationBuilder contributory() {
        builder.setInterpretationStatus(InterpretationStatus.CONTRIBUTORY);
        return this;
    }

    public GenomicInterpretationBuilder causative() {
        builder.setInterpretationStatus(InterpretationStatus.CAUSATIVE);
        return this;
    }

    public GenomicInterpretationBuilder unknown() {
        builder.setInterpretationStatus(InterpretationStatus.UNKNOWN_STATUS);
        return this;
    }

    public GenomicInterpretationBuilder geneDescriptor(GeneDescriptor geneDescriptor) {
        builder.setGene(geneDescriptor);
        return this;
    }

    public GenomicInterpretationBuilder variantInterpretation(VariantInterpretation variantInterpretation) {
        builder.setVariantInterpretation(variantInterpretation);
        return this;
    }


    public GenomicInterpretationBuilder variantInterpretation(VariantInterpretationBuilder vibuilder) {
        builder.setVariantInterpretation(vibuilder.build());
        return this;
    }

    public GenomicInterpretation build() {
        return builder.build();
    }

}
