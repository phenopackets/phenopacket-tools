package org.phenopackets.phenotools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.phenotools.builder.constants.Status;
import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.GenomicInterpretation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

class DiagnosisBuilderTest {

    @Test
    void testDiagnosisBuilder() {
        var variationDescriptor =
                VariationDescriptorBuilder.builder("variant id")
                        .heterozygous()
                        .hgvs("NM_014915.2:c.-128G>A")
                        .build();
        var col6a1VariantInterpretation = VariantInterpretationBuilder.variantInterpretation(variationDescriptor, Status.pathogenic());
        var genomicInterpretationBuilder =
                GenomicInterpretationBuilder.builder("genomic interpretation id");
        genomicInterpretationBuilder.causative();
        genomicInterpretationBuilder.variantInterpretation(col6a1VariantInterpretation);
        var expectedGenomicInterpretation = genomicInterpretationBuilder.build();
        var thrombocytopenia2 = ontologyClass("OMIM:188000", "Thrombocytopenia 2");
        Diagnosis diagnosis = DiagnosisBuilder.builder(thrombocytopenia2).
                addGenomicInterpretation(expectedGenomicInterpretation)
                .build();
        assertThat(diagnosis.getDisease(), equalTo(thrombocytopenia2));
        assertEquals(1, diagnosis.getGenomicInterpretationsCount());
        GenomicInterpretation genomicInterpretation1 = diagnosis.getGenomicInterpretations(0);
        assertEquals(expectedGenomicInterpretation, genomicInterpretation1);
    }

    @Test
    void testDiagnosisBuilderMinimalData() {
        var thrombocytopenia2 = ontologyClass("OMIM:188000", "Thrombocytopenia 2");
        Diagnosis diagnosis = DiagnosisBuilder.builder(thrombocytopenia2).build();
        assertThat(diagnosis.getDisease(), equalTo(thrombocytopenia2));
        assertThat(diagnosis.getGenomicInterpretationsCount(), equalTo(0));
    }
}