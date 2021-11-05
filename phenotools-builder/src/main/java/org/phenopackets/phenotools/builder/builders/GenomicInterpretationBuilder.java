package org.phenopackets.phenotools.builder.builders;


import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.schema.v2.core.GenomicInterpretation;
import org.phenopackets.schema.v2.core.GenomicInterpretation.InterpretationStatus;
import org.phenopackets.schema.v2.core.VariantInterpretation;

public class GenomicInterpretationBuilder {

    private GenomicInterpretationBuilder(){
    }

    public static GenomicInterpretation genomicInterpretation(String id, InterpretationStatus status, GeneDescriptor geneDescriptor) {
        return GenomicInterpretation.newBuilder().setSubjectOrBiosampleId(id).setInterpretationStatus(status).setGene(geneDescriptor).build();
    }

    public static GenomicInterpretation genomicInterpretation(String id, InterpretationStatus status, VariantInterpretation variantInterpretation) {
        return GenomicInterpretation.newBuilder().setSubjectOrBiosampleId(id).setInterpretationStatus(status).setVariantInterpretation(variantInterpretation).build();
    }
}
