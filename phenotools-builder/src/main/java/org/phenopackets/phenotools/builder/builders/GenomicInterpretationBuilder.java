package org.phenopackets.phenotools.builder.builders;


import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.core.GenomicInterpretation;
import org.phenopackets.schema.v2.core.VariantInterpretation;

public class GenomicInterpretationBuilder {

    private final GenomicInterpretation.Builder builder;

    public GenomicInterpretationBuilder(String id, GenomicInterpretation.InterpretationStatus status) {
        builder = GenomicInterpretation.newBuilder().setSubjectOrBiosampleId(id).setInterpretationStatus(status);
    }

   public GenomicInterpretationBuilder geneDescriptor(GeneDescriptor descriptor) {
        if (builder.hasVariantInterpretation()) {
            throw new PhenotoolsRuntimeException("Attempt to add Gene Descriptor to builder that already has Variant Interpretation");
        }
        builder.setGene(descriptor);
        return this;
   }

   public GenomicInterpretationBuilder variantInterpretation(VariantInterpretation interpretation) {
        if (builder.hasGene()) {
            throw new PhenotoolsRuntimeException("Attempt to add Variant Interpretation to builder that already has Gene Descriptor");

        }
       builder.setVariantInterpretation(interpretation);
       return this;
   }


    public GenomicInterpretation build() {
        return builder.build();
    }

    public static GenomicInterpretationBuilder create(String identifier, GenomicInterpretation.InterpretationStatus status)  {
        return new GenomicInterpretationBuilder(identifier, status);
    }

    public static GenomicInterpretationBuilder rejected(String identifier) {
        return new GenomicInterpretationBuilder(identifier, GenomicInterpretation.InterpretationStatus.REJECTED);
    }

    public static GenomicInterpretationBuilder candidate(String identifier) {
        return new GenomicInterpretationBuilder(identifier, GenomicInterpretation.InterpretationStatus.CANDIDATE);
    }

    public static GenomicInterpretationBuilder contributory(String identifier) {
        return new GenomicInterpretationBuilder(identifier, GenomicInterpretation.InterpretationStatus.CONTRIBUTORY);
    }
    public static GenomicInterpretationBuilder causative(String identifier) {
        return new GenomicInterpretationBuilder(identifier, GenomicInterpretation.InterpretationStatus.CAUSATIVE);
    }


}
