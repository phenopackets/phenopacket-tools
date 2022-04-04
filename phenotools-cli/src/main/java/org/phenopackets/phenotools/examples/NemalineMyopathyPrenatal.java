package org.phenopackets.phenotools.examples;

import org.ga4gh.vrsatile.v1.Expression;
import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.ArrayList;
import java.util.List;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;
import static org.phenopackets.phenotools.builder.builders.TimeElements.gestationalAge;
import static org.phenopackets.phenotools.builder.builders.Util.eye;

/**
 * From Clin. Exp. Obstet. Gynecol. - ISSN: 0390-6663
 * XLVII, n. 6, 2020
 * doi: 10.31083/j.ceog.2020.06.209
 * (Not in PubMed)
 */
class NemalineMyopathyPrenatal implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";

    private final Phenopacket phenopacket;

    NemalineMyopathyPrenatal() {
        var individual = IndividualBuilder.create(PROBAND_ID).male().ageAtLastEncounter("P8Y").build();
        var disease = DiseaseBuilder
                .create(ontologyClass("NCIT:C3171", "Acute Myeloid Leukemia"))
                .build();
        TimeElement age = TimeElements.age("P8Y");
        var biospy = BiosampleBuilder.create("SAMN05324082")
                .individualId("SAMN05324082-individual")
                .description("THP-1; 6 hours; DMSO; Replicate 1")
                .timeOfCollection(age)
                .taxonomy(ontologyClass("NCBITaxon:9606", "Homo sapiens"))
                .sampledTissue(ontologyClass("UBERON:0000178", "peripheral blood"))
                .histologicalDiagnosis(ontologyClass("EFO:0000221", "Acute Monocytic Leukemia"))
                .build();
        var metadata = MetaDataBuilder.create("2021-05-14T10:35:00Z", "anonymous biocurator")
                .resource(Resources.ncitVersion("21.05d"))
                .resource(Resources.hpoVersion("2022-02"))
                .resource(Resources.uberonVersion("2021-07-27"))
                .build();
        PhenopacketBuilder builder = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(individual)
                .phenotypicFeature(decreasedFetalMovement())
                 .biosample(muscleBiopsy())
                .interpretation(interpretation())
                ;

        builder.allPhenotypicFeatures(sonography33weeks());
        builder.allPhenotypicFeatures(apgar());
        phenopacket = builder.build();
    }

    List<PhenotypicFeature> apgar() {
        List<PhenotypicFeature> phenotypicFeatureList = new ArrayList<>();
        TimeElement newbornTime = TimeElements.congenitalOnset();
        PhenotypicFeature apgar1 = PhenotypicFeatureBuilder.create("HP:0030931", "1-minute APGAR score of 4")
                .onset(newbornTime).build();
        PhenotypicFeature apgar5 = PhenotypicFeatureBuilder.create("HP:0030922", "5-minute APGAR score of 2")
                .onset(newbornTime).build();
        PhenotypicFeature apgar10 = PhenotypicFeatureBuilder.create("HP:0033469", "10-minute APGAR score of 1")
                .onset(newbornTime).build();
        PhenotypicFeature laryngealEdema = PhenotypicFeatureBuilder.create("HP:0012027", "Laryngeal edema")
                .onset(newbornTime).build();
        PhenotypicFeature generalizedHypotonia = PhenotypicFeatureBuilder.create("HP:0001290", "Generalized hypotonia")
                .onset(newbornTime).build();

        phenotypicFeatureList.add(apgar1);
        phenotypicFeatureList.add(apgar5);
        phenotypicFeatureList.add(apgar10);
        phenotypicFeatureList.add(laryngealEdema);
        phenotypicFeatureList.add(generalizedHypotonia);
        return phenotypicFeatureList;
    }

    List<PhenotypicFeature> sonography33weeks() {
        List<PhenotypicFeature> phenotypicFeatureList = new ArrayList<>();
        TimeElement onset = gestationalAge(32,4);
        OntologyClass borderline = ontologyClass("HP:0012827", "Borderline");
        PhenotypicFeature edema = PhenotypicFeatureBuilder.create("HP:0025672", "Fetal skin edema")
                .onset(onset).build();
        PhenotypicFeature overlapping = PhenotypicFeatureBuilder.create("HP:0010557", "Overlapping fingers")
                .onset(onset).build();
        PhenotypicFeature polyhydram = PhenotypicFeatureBuilder.create("HP:0001561", "Polyhydramnios")
                .onset(onset).modifier(borderline).build();
        phenotypicFeatureList.add(edema);
        phenotypicFeatureList.add(overlapping);
        phenotypicFeatureList.add(polyhydram);
        return phenotypicFeatureList;
    }


    PhenotypicFeature decreasedFetalMovement() {
        PhenotypicFeatureBuilder builder = PhenotypicFeatureBuilder.create("HP:0001558", "Decreased fetal movement");
        TimeElement onset = gestationalAge(23);
        builder.onset(onset);
        return builder.build();
    }

    Biosample muscleBiopsy() {
        String biosampleId = "biosample.1";
        BiosampleBuilder builder = BiosampleBuilder.create(biosampleId);
        OntologyClass abdominalMuscle = ontologyClass("UBERON:0002378", "muscle of abdomen");
        builder.sampledTissue(abdominalMuscle);
        PhenotypicFeature nemalineRods = PhenotypicFeatureBuilder.create("HP:0003798","Nemaline bodies").build();
        builder.phenotypicFeature(nemalineRods);
        ProcedureBuilder pbuilder = ProcedureBuilder.create("NCIT:C51895", "Muscle Biopsy");
        TimeElement age = TimeElements.age("P1D");
        pbuilder.bodySite(abdominalMuscle).performed(age);
        builder.procedure(pbuilder.build());
        return builder.build();
    }

    Interpretation interpretation() {
        InterpretationBuilder ibuilder = InterpretationBuilder.solved("interpretation.id");
        OntologyClass nm8 = ontologyClass("MONDO:0014138", "nemaline myopathy 8");
        DiagnosisBuilder dbuilder = DiagnosisBuilder.create(nm8);
        dbuilder.genomicInterpretation(klhl40InterpretationV1());
        dbuilder.genomicInterpretation(klhl40InterpretationV2());
        ibuilder.diagnosis(dbuilder.build());
        return ibuilder.build();
    }


    /**
     * ES of umbilical-cord blood revealed two complex
     * heterozygous mutations to fetal Kelch-like 40 (KLHL40).
     * Pedigree-based Sanger sequencing confirmed that the mu-
     * tation c.602G > A (p.Trp201*) was inherited from the
     * mother (Figure 5) and c.1516A > C (p.Thr506Pro) from
     * the father (Figure 6)
     * @return
     */
    GenomicInterpretation klhl40InterpretationV1() {
        // 3: 42686220 (GRCh38) NM_152393.4(KLHL40):c.602G>A (p.Trp201Ter)
        // ClinVar VCV000060516.7
        // Canonical SPDI
        //    NC_000003.12:42686219:G:A
        // rs397509420
        //HGNC:30372
        AlleleBuilder abuilder = AlleleBuilder.create();
        //abuilder.setSequenceId("ga4gh:VA.GuPzvZoansqNHPoXkQLXKo31VkTpDKsM");
        abuilder.startEnd( 42686219, 42686220);
        abuilder.chromosomeLocation("chr3");
        abuilder.setAltAllele("A");
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.create("rs397509420");
        vbuilder.variation(abuilder.buildVariation());
        vbuilder.genomic();
        vbuilder.heterozygous();
        vbuilder.label("NM_152393.4(KLHL40):c.602G>A (p.Trp201Ter)");
        vbuilder.transcript();
        GeneDescriptor geneDescriptor = GeneDescriptorBuilder.create("HGNC:30372", "KLHL40").build();
        vbuilder.geneContext(geneDescriptor);
        Expression hgvs = Expressions.hgvsCdna("NM_152393.4(KLHL40):c.602G>A");
        Expression transcriptReference = Expressions.transcriptReference("NM_152393.4");
        vbuilder.expression(hgvs);
        vbuilder.expression(transcriptReference);
        // wrap in VariantInterpretation
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.create(vbuilder);
        vibuilder.pathogenic();
        vibuilder.actionable();


        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.create("interpretation.1");
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);
        return gbuilder.build();
    }

    /**
     *  c.1516A > C (p.Thr506Pro)
     * @return
     */
    GenomicInterpretation klhl40InterpretationV2() {
        //NM_152393.4(KLHL40):c.1516A>C (p.Thr506Pro)
        //         VCV000846771.4
        //  3: 42688963 (GRCh38)
        // Canonical SPDI
        //    NC_000003.12:42688962:A:C
        //         dbSNP: rs778022582
        //HGNC:30372
        AlleleBuilder abuilder = AlleleBuilder.create();
        //abuilder.setSequenceId("ga4gh:VA.GuPzvZoansqNHPoXkQLXKo31VkTpDKsM");
        abuilder.startEnd( 42688962, 42688963);
        abuilder.chromosomeLocation("chr3");
        abuilder.setAltAllele("C");
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.create("rs778022582");
        vbuilder.variation(abuilder.buildVariation());
        vbuilder.genomic();
        vbuilder.heterozygous();
        vbuilder.label("NM_152393.4(KLHL40):c.1516A>C (p.Thr506Pro)");
        vbuilder.transcript();
        GeneDescriptor geneDescriptor = GeneDescriptorBuilder.create("HGNC:30372", "KLHL40").build();
        vbuilder.geneContext(geneDescriptor);
        Expression hgvs = Expressions.hgvsCdna("NM_152393.4(KLHL40):c.1516A>C");
        Expression transcriptReference = Expressions.transcriptReference("NM_152393.4");
        vbuilder.expression(hgvs);
        vbuilder.expression(transcriptReference);
        // wrap in VariantInterpretation
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.create(vbuilder);
        vibuilder.pathogenic();
        vibuilder.actionable();


        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.create("interpretation.2");
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);
        return gbuilder.build();
    }


    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
