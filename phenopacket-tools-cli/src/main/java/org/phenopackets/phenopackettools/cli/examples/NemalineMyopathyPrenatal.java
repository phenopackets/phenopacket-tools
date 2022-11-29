package org.phenopackets.phenopackettools.cli.examples;

import org.ga4gh.vrs.v1.Variation;
import org.ga4gh.vrsatile.v1.Expression;
import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;
import static org.phenopackets.phenopackettools.builder.builders.TimeElements.gestationalAge;

/**
 * From Clin. Exp. Obstet. Gynecol. - ISSN: 0390-6663
 * XLVII, n. 6, 2020
 * doi: 10.31083/j.ceog.2020.06.209
 * (Not in PubMed)
 */
public class NemalineMyopathyPrenatal implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final OntologyClass NEMALINE_MYOPATHY_8 = ontologyClass("MONDO:0014138", "nemaline myopathy 8");

    private final Phenopacket phenopacket;

    public NemalineMyopathyPrenatal() {

        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.hpoVersion("2022-02"))
                .addResource(Resources.mondoVersion("2022-04-04"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addResource(Resources.genoVersion("2022-03-05"))
                .build();
        var vitalStatus = VitalStatusBuilder.deceased().causeOfDeath(NEMALINE_MYOPATHY_8).build();
        var individual = IndividualBuilder.builder(PROBAND_ID)
                .male()
                .ageAtLastEncounter("P1D")
                .vitalStatus(vitalStatus).build();
        PhenopacketBuilder builder = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(individual)
                .addPhenotypicFeature(decreasedFetalMovement())
                .addBiosample(muscleBiopsy())
                .addInterpretation(interpretation())
                .addPhenotypicFeatures(sonography33weeks())
                .addPhenotypicFeatures(apgar());
        phenopacket = builder.build();
    }

    List<PhenotypicFeature> apgar() {
        TimeElement newbornTime = TimeElements.congenitalOnset();
        PhenotypicFeature apgar1 = PhenotypicFeatureBuilder.builder("HP:0030931", "1-minute APGAR score of 4")
                .onset(newbornTime).build();
        PhenotypicFeature apgar5 = PhenotypicFeatureBuilder.builder("HP:0030922", "5-minute APGAR score of 2")
                .onset(newbornTime).build();
        PhenotypicFeature apgar10 = PhenotypicFeatureBuilder.builder("HP:0033469", "10-minute APGAR score of 1")
                .onset(newbornTime).build();
        PhenotypicFeature laryngealEdema = PhenotypicFeatureBuilder.builder("HP:0012027", "Laryngeal edema")
                .onset(newbornTime).build();
        PhenotypicFeature generalizedHypotonia = PhenotypicFeatureBuilder.builder("HP:0001290", "Generalized hypotonia")
                .onset(newbornTime).build();
        return List.of(apgar1, apgar5, apgar10, laryngealEdema, generalizedHypotonia);
    }

    List<PhenotypicFeature> sonography33weeks() {
        TimeElement onset = gestationalAge(32,4);
        OntologyClass borderline = ontologyClass("HP:0012827", "Borderline");
        PhenotypicFeature edema = PhenotypicFeatureBuilder.builder("HP:0025672", "Fetal skin edema")
                .onset(onset).build();
        PhenotypicFeature overlapping = PhenotypicFeatureBuilder.builder("HP:0010557", "Overlapping fingers")
                .onset(onset).build();
        PhenotypicFeature polyhydram = PhenotypicFeatureBuilder.builder("HP:0001561", "Polyhydramnios")
                .onset(onset).addModifier(borderline).build();
        return List.of(edema, overlapping, polyhydram);
    }


    PhenotypicFeature decreasedFetalMovement() {
        return PhenotypicFeatureBuilder.builder("HP:0001558", "Decreased fetal movement")
                .onset(gestationalAge(23))
                .build();
    }

    Biosample muscleBiopsy() {
        OntologyClass abdominalMuscle = ontologyClass("UBERON:0002378", "muscle of abdomen");
        Procedure muscleBiopsy = ProcedureBuilder.builder("NCIT:C51895", "Muscle Biopsy")
                .bodySite(abdominalMuscle)
                .performed(TimeElements.age("P1D"))
                .build();
        return BiosampleBuilder.builder("biosample.1")
                .sampledTissue(abdominalMuscle)
                .addPhenotypicFeature("HP:0003798","Nemaline bodies")
                .procedure(muscleBiopsy)
                .build();
    }

    Interpretation interpretation() {
        Diagnosis diagnosis = DiagnosisBuilder.builder(NEMALINE_MYOPATHY_8)
                .addGenomicInterpretation(klhl40InterpretationV1())
                .addGenomicInterpretation(klhl40InterpretationV2())
                .build();
        return InterpretationBuilder.builder("interpretation.id").solved(diagnosis);
    }


    /**
     * ES of umbilical-cord blood revealed two complex
     * heterozygous mutations to fetal Kelch-like 40 (KLHL40).
     * Pedigree-based Sanger sequencing confirmed that the mu-
     * tation c.602G > A (p.Trp201*) was inherited from the
     * mother (Figure 5) and c.1516A > C (p.Thr506Pro) from
     * the father (Figure 6)
     */
    GenomicInterpretation klhl40InterpretationV1() {
        // 3: 42686220 (GRCh38) NM_152393.4(KLHL40):c.602G>A (p.Trp201Ter)
        // ClinVar VCV000060516.7
        // Canonical SPDI
        //    NC_000003.12:42686219:G:A
        // rs397509420
        //HGNC:30372
        //abuilder.setSequenceId("ga4gh:VA.GuPzvZoansqNHPoXkQLXKo31VkTpDKsM");
        Variation variation = AlleleBuilder.builder()
                .sequenceId("NC_000003.12")
                .interbaseStartEnd(42686219, 42686220)
                .altAllele("A")
                .buildVariation();
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder("rs397509420")
                .variation(variation)
                .genomic()
                .heterozygous()
                .label("NM_152393.4(KLHL40):c.602G>A (p.Trp201Ter)")
                .transcript();
        GeneDescriptor geneDescriptor = GeneDescriptorBuilder.builder("HGNC:30372", "KLHL40").build();
        vbuilder.geneContext(geneDescriptor);
        Expression hgvs = Expressions.hgvsCdna("NM_152393.4(KLHL40):c.602G>A");
        Expression transcriptReference = Expressions.transcriptReference("NM_152393.4");
        vbuilder.addExpression(hgvs);
        vbuilder.addExpression(transcriptReference);
        // wrap in VariantInterpretation
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.builder(vbuilder);
        vibuilder.pathogenic();

        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder("interpretation.1");
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);
        return gbuilder.build();
    }

    /**
     *  c.1516A > C (p.Thr506Pro)
     */
    GenomicInterpretation klhl40InterpretationV2() {
        //NM_152393.4(KLHL40):c.1516A>C (p.Thr506Pro)
        //         VCV000846771.4
        //  3: 42688963 (GRCh38)
        // Canonical SPDI
        //    NC_000003.12:42688962:A:C
        //         dbSNP: rs778022582
        //HGNC:30372
        AlleleBuilder abuilder = AlleleBuilder.builder()
                .sequenceId("NC_000003.12")
                .interbaseStartEnd( 42688962, 42688963)
                .altAllele("C");
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder("rs778022582")
                .variation(abuilder.buildVariation())
                .genomic()
                .heterozygous()
                .label("NM_152393.4(KLHL40):c.1516A>C (p.Thr506Pro)")
                .transcript();
        GeneDescriptor geneDescriptor = GeneDescriptorBuilder.builder("HGNC:30372", "KLHL40").build();
        vbuilder.geneContext(geneDescriptor);
        Expression hgvs = Expressions.hgvsCdna("NM_152393.4(KLHL40):c.1516A>C");
        Expression transcriptReference = Expressions.transcriptReference("NM_152393.4");
        vbuilder.addExpression(hgvs);
        vbuilder.addExpression(transcriptReference);
        // wrap in VariantInterpretation
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.builder(vbuilder);
        vibuilder.pathogenic();

        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder("interpretation.2");
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);
        return gbuilder.build();
    }


    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
