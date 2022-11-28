package org.phenopackets.phenopackettools.cli.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;
import static org.phenopackets.phenopackettools.builder.builders.TimeElements.childhoodOnset;

/**
 * This phenopacket is based on the description in
 * Li Q, et al.
 * Novel Partial Exon 51 Deletion in the Duchenne Muscular Dystrophy Gene Identified via Whole Exome Sequencing
 * and Long-Read Whole-Genome Sequencing.
 * Front Genet. 2021 Nov 26;12:762987. PMID: 34899847; PMCID: PMC8662377.
 */
public class DuchenneExon51Deletion implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "10-year old boy";
    private static final OntologyClass DuchenneMD = ontologyClass("MONDO:0010679", "Duchenne muscular dystrophy");
    private static final OntologyClass PRIMARY_NEOPLASM = ontologyClass("NCIT:C8509", "Primary Neoplasm");

    // Organs
    private static final OntologyClass EYE = ontologyClass("UBERON:0000970", "eye");
    private static final OntologyClass CURE = ontologyClass("NCIT:C62220", "Cure");
    private static final OntologyClass LEFT_EYE = ontologyClass("UBERON:0004548", "left eye");

    private final Phenopacket phenopacket;

    public DuchenneExon51Deletion() {
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.hpoVersion("2022-06-11"))
                .addResource(Resources.mondoVersion("2022-04-04"))
                .addResource(Resources.genoVersion("2022-03-05"))
                .build();
        Individual proband = IndividualBuilder.builder(PROBAND_ID).
                ageAtLastEncounter("P10Y").
                male().
                build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addPhenotypicFeatures(getPhenotypicFeatures())
                .addInterpretation(interpretation())
                .build();
    }


    /**
     * A 39-year-old female patient was referred to our Nephrology Department during late 2009.
     * She was newly diagnosed with SLE after a spontaneous miscarriage during the second trimester of pregnancy.
     * @return
     */
    Disease sle() {
        return DiseaseBuilder.builder(DuchenneMD)
                .onset(childhoodOnset())
                .build();
    }


    List<PhenotypicFeature> getPhenotypicFeatures() {
        var pf = PhenotypicFeatureBuilder.builder("HP:0031936", "Delayed ability to walk")
                .onset(TimeElements.age("P1Y6M")).build();
        var pf1 = PhenotypicFeatureBuilder.builder("HP:0003707", "Calf muscle pseudohypertrophy")
                .childhoodOnset().build();
        var pf2 = PhenotypicFeatureBuilder.builder("HP:0009046", "Difficulty running")
                .childhoodOnset().build();
        TimeElement tenY = TimeElements.age("P10Y");
        var pf3 = PhenotypicFeatureBuilder.builder("HP:0003458", "EMG: myopathic abnormalities")
                .onset(tenY).build();
        var pf4 = PhenotypicFeatureBuilder.builder("HP:0003236", "Elevated circulating creatine kinase concentration")
                .onset(tenY).build();

        return List.of(pf, pf1, pf2, pf3, pf4);
    }

    Interpretation interpretation() {
        Diagnosis diagnosis = DiagnosisBuilder.builder(DuchenneMD)
                .addGenomicInterpretation(dmdDeletion())
                .build();
        return InterpretationBuilder.builder("interpretation.id").solved(diagnosis);
    }

    private GenomicInterpretation dmdDeletion() {
        // NC_000023.11:g.31774144_31785736del
        // NM_004006.3:c.7310-11543_7359del
        // This deletion removed the 5' part of exon 51 (of NM_004006.3)
        CopyNumberBuilder cnvBuilder = CopyNumberBuilder.builder()
                .alleleLocation("refseq:NC_000023.11", 31774144, 31785736) //VRS uses inter-residue coordinates
                .oneCopy();
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder("variant-id")
                .variation(cnvBuilder.buildVariation())
                .genomic()
                .hemizygous()
                .geneContext(GeneDescriptorBuilder.of("HGNC:2928", "DMD"))
                .addExpression(Expressions.hgvsCdna("NM_004006.3:c.7310-11543_7359del"))
                .addExpression(Expressions.transcriptReference("NM_004006.3"));
        /*
         VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder("rs121913300")
                .variation(abuilder.buildVariation())
                .genomic()
                .heterozygous()
                .label("RB1 c.958C>T (p.Arg320Ter)")
                .genomic()
                .vcfHg38("NC_000013.11", 48367512, "C", "T")
                .alleleFrequency(25.0)
                .geneContext(GeneDescriptorBuilder.of("HGNC:9884", "RB1"))
                .addExpression(Expressions.hgvsCdna("NM_000321.2:c.958C>T"))
                .addExpression(Expressions.transcriptReference("NM_000321.2"));
         */



        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.builder(vbuilder);
        vibuilder.pathogenic();
        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder(PROBAND_ID);
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);
        return gbuilder.build();
    }


    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
