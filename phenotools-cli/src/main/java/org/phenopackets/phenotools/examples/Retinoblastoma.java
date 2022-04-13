package org.phenopackets.phenotools.examples;

import org.ga4gh.vrsatile.v1.Expression;
import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.of;

public class Retinoblastoma implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final String BIOSAMPLE_ID = "biosample.1";
    private static final OntologyClass BIOPSY = of("NCIT:C15189", "Biopsy");
    private static final OntologyClass RETINOBLASTOMA = of("NCIT:C7541", "Retinoblastoma");
    private static final OntologyClass PRIMARY_NEOPLASM = of("NCIT:C8509", "Primary Neoplasm");

    // Organs
    private static final OntologyClass EYE = of("UBERON:0000970", "eye");
    private static final OntologyClass CURE = of("NCIT:C62220", "Cure");
    private final OntologyClass leftEye = of("UBERON:0004548", "left eye");

    private final Phenopacket phenopacket;
    public Retinoblastoma() {
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .resource(Resources.ncitVersion("21.05d"))
                .resource(Resources.efoVersion("3.34.0"))
                .resource(Resources.uberonVersion("2021-07-27"))
                .resource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();
        Individual proband = IndividualBuilder.builder(PROBAND_ID).
                ageAtLastEncounter("P6M").
                female().
                XX().
                build();
        // VCF file with results of germline whole-genome sequencing
        File wgsFile = FileBuilder.of("file://data/germlineWgs.vcf.gz");

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .allMeasurements(getMeasurements())
                .allPhenotypicFeatures(getPhenotypicFeatures())
                .disease(getDisease())
                .medicalAction(melphalan())
                .medicalAction(chemoRegimen())
                .medicalAction(enucleation())
                .biosample(enucleatedEye())
                .interpretation(interpretation())
                .file(wgsFile)
                .build();
    }


    Interpretation interpretation() {
        InterpretationBuilder ibuilder = InterpretationBuilder.solved("interpretation.id");
        GenomicInterpretation somatic = somaticRb1Missense();
         DiagnosisBuilder dbuilder = DiagnosisBuilder.builder(RETINOBLASTOMA);
         dbuilder.genomicInterpretation(somatic);
         dbuilder.genomicInterpretation(germlineRb1Deletion());
         ibuilder.diagnosis(dbuilder.build());
         return ibuilder.build();
    }


    /**
     * Variation ID:126824 (ClinGen pathogenic)
     * @return Genomic interpretation related to a somatic missense mutation in the RB1 gene.
     */
    GenomicInterpretation somaticRb1Missense() {
        AlleleBuilder abuilder = AlleleBuilder.builder();
        abuilder.setSequenceId("ga4gh:VA.GuPzvZoansqNHPoXkQLXKo31VkTpDKsM");
        abuilder.startEnd( 48941647, 48941648);
        abuilder.setAltAllele("T");
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder("rs121913300");
        vbuilder.variation(abuilder.buildVariation())
                .genomic()
                .heterozygous()
                .label("RB1 c.958C>T (p.Arg320Ter)")
                .transcript();
        vbuilder.vcfHg38("NC_000013.11", 48367512, "C", "T");
        vbuilder.alleleFrequency(25.0);
        GeneDescriptor geneDescriptor = GeneDescriptorBuilder.builder("HGNC:9884", "RB1").build();
        vbuilder.geneContext(geneDescriptor);
        Expression hgvs = Expressions.hgvsCdna("NM_000321.2:c.958C>T");
        Expression transcriptReference = Expressions.transcriptReference("NM_000321.2");
        vbuilder.expression(hgvs);
        vbuilder.expression(transcriptReference);
        // wrap in VariantInterpretation
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.builder(vbuilder);
        vibuilder.pathogenic();
        vibuilder.actionable();


        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder(PROBAND_ID);
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);

        return gbuilder.build();
    }


    GenomicInterpretation germlineRb1Deletion() {
        CopyNumberBuilder abuilder = CopyNumberBuilder.builder();
        abuilder.copyNumberId("ga4gh:VCN.AFfJws1M4Lg8w1O3XknmHYc9TU2hHYpp");
        abuilder.alleleLocation("refseq:NC_000013.14",26555377, 62280955);//VRS uses inter-residue coordinates
        abuilder.oneCopy();
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder();
        vbuilder.variation(abuilder.buildVariation());
        vbuilder.mosaicism(40.0);
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.builder(vbuilder);
        vibuilder.pathogenic();
        vibuilder.actionable();


        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder(BIOSAMPLE_ID);
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);

        return gbuilder.build();

    }

    /**
     * Can be used for the multisample VCF file for germline WGS in this family
     * @return a pedigree object in which just the proband is affected (de novo variant)
     */
    Pedigree pedigree() {
        String family_id = "family.1";
        String mother_id = "maternal1";
        String father_id = "paternal1";
        Pedigree.Person mother = PersonBuilder
                .builderWithParentsAsFounders(family_id, mother_id)
                .female()
                .unaffected()
                .build();
        Pedigree.Person father = PersonBuilder
                .builderWithParentsAsFounders(family_id, father_id)
                .male()
                .unaffected()
                .build();
        Pedigree.Person child = PersonBuilder
                .builder(family_id, PROBAND_ID, father_id, mother_id)
                .female()
                .affected()
                .build();
        PedigreeBuilder pbuilder = PedigreeBuilder.builder();
        pbuilder.person(father);
        pbuilder.person(mother);
        pbuilder.person(child);
        return pbuilder.build();
    }


    Biosample enucleatedEye() {
        TimeElement age = TimeElements.age("P8M2W");
        BiosampleBuilder builder = BiosampleBuilder.builder(BIOSAMPLE_ID);
        builder.sampledTissue(EYE);
        //Retinoblastoma with tumor invading optic nerve past lamina cribrosa but not to surgical resection line and exhibiting massive choroidal invasion.
        builder.pathologicalTnmFinding(of("NCIT:C88735",
                "Retinoblastoma pT3b TNM Finding v7"));
        //Retinoblastoma with no regional lymph node involvement.
        builder.pathologicalTnmFinding(of("NCIT:C88741",
                "Retinoblastoma pN0 TNM Finding v7"));

        //
        OntologyClass fwRosette = of("NCIT:C35941", "Flexner-Wintersteiner Rosette Formation");
        PhenotypicFeature pfRosette = PhenotypicFeatureBuilder.builder(fwRosette).build();
        builder.phenotypicFeature(pfRosette);
        OntologyClass apoptosisNecrosis = of("NCIT:C132485", "Apoptosis and Necrosis");
        PhenotypicFeature pfApoptosis = PhenotypicFeatureBuilder.builder(apoptosisNecrosis).build();
        builder.phenotypicFeature(pfApoptosis);
        OntologyClass maxTumorSizeTest = OntologyClassBuilder.of("LOINC:33728-7", "Size.maximum dimension in Tumor");
        Value maxTumorSize = ValueBuilder.of(Unit.mm(), 15);
        Measurement maxTumorSizeMeasurement = MeasurementBuilder.value(maxTumorSizeTest, maxTumorSize).timeObserved(age).build();
        builder.measurement(maxTumorSizeMeasurement);

        ProcedureBuilder pbuilder = ProcedureBuilder.builder("NCIT:C48601", "Enucleation");

        pbuilder.bodySite(leftEye).performed(age);
        builder.procedure(pbuilder.build());
        builder.tumorProgression(PRIMARY_NEOPLASM);
        // VCF file with results of whole-genome sequencing on this tumor
        File wgsFile = FileBuilder.of("file://data/fileSomaticWgs.vcf.gz");
        builder.file(wgsFile);
        return builder.build();
    }



    MedicalAction melphalan() {
        OntologyClass melphalan = of("DrugCentral:1678", "melphalan");
        OntologyClass administration = of("NCIT:C38222",  "Intraarterial Route of Administration");
       //0.4 mg/kg (up to a starting dose of 5 mg)
        Quantity quantity = QuantityBuilder.of(Unit.mgPerKg(), 0.4);
        TimeInterval interval = TimeIntervalBuilder.of("2020-09-02", "2020-09-02");
        OntologyClass once = of("NCIT:C64576", "Once");

        DoseInterval doseInterval = DoseIntervalBuilder.of(quantity, once, interval);


        Treatment treatment = TreatmentBuilder.builder(melphalan)
                .routeOfAdministration(administration)
                .doseInterval(doseInterval).build();

        return MedicalActionBuilder.builder(treatment)
                .adverseEvent(of("HP:0025637", "Vasospasm"))
                .treatmentTarget(RETINOBLASTOMA)
                .treatmentIntent(CURE)
                .treatmentTerminationReason(of("NCIT:C41331", "Adverse Event"))
                .build();
    }


    MedicalAction enucleation() {
        ProcedureBuilder builder = ProcedureBuilder.builder("NCIT:C48601", "Enucleation");
        TimeElement age = TimeElements.age("P8M2W");
        builder.bodySite(leftEye).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(RETINOBLASTOMA)
                .treatmentIntent(CURE);
        return mabuilder.build();
    }

    /**
     * NCIT:C10894, Carboplatin/Etoposide/Vincristine
     * @return MedicalAction message representing treatment with a regimen of these three chemotherapeutics
     */
    MedicalAction chemoRegimen() {
        TherapeuticRegimenBuilder builder = TherapeuticRegimenBuilder.builder("NCIT:C10894", "Carboplatin/Etoposide/Vincristine");
        builder.completed();
        TimeElement start = TimeElements.age("P7M");
        TimeElement end = TimeElements.age("P8M");
        builder.startTime(start).endTime(end);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(RETINOBLASTOMA)
                .treatmentIntent(CURE);
        return mabuilder.build();
    }



    Disease getDisease() {
        // NCIT:C27980
        // Stage E
        //  Group E = LOINC:LA24739-7
        // Retinoblastoma ,  NCIT:C7541
        // No metastasis is NCIT:C140678, Retinoblastoma cM0 TNM Finding v8
        //Retinoblastoma with no signs or symptoms of intracranial or distant metastasis. (from AJCC 8th Ed.) [ NCI ]
        OntologyClass stageE = of("LOINC:LA24739-7", "Group E");
        OntologyClass noMetastasis = of("NCIT:C140678", "Retinoblastoma cM0 TNM Finding v8");

        TimeElement age4m = TimeElements.age("P4M");
        return DiseaseBuilder.builder(RETINOBLASTOMA)
                .onset(age4m)
                .diseaseStage(stageE)
                .clinicalTnmFinding(noMetastasis)
                .primarySite(leftEye)
                .build();
    }




    List<PhenotypicFeature> getPhenotypicFeatures() {
        OntologyClass clinodactyly = of("HP:0030084", "Clinodactyly");
        TimeElement age3months = TimeElements.age("P3M");
        PhenotypicFeature clinodactylyPf = PhenotypicFeatureBuilder.
                builder(clinodactyly).
                modifier(Laterality.right()).
                onset(age3months).
                build();
        OntologyClass leukocoria  = of("HP:0000555", "Leukocoria");
        TimeElement age4months = TimeElements.age("P4M");
        PhenotypicFeature leukocoriaPf = PhenotypicFeatureBuilder.
                builder(leukocoria)
                .modifier(Laterality.unilateral())
                .onset(age4months)
                .build();
        OntologyClass strabismus  = of("HP:0000486", "Strabismus");
        TimeElement age5months = TimeElements.age("P5M15D");
        PhenotypicFeature strabismusPf = PhenotypicFeatureBuilder.
                builder(strabismus)
                .modifier(Laterality.unilateral())
                .onset(age5months)
                .build();
        OntologyClass retinalDetachment  = of("HP:0000541", "Retinal detachment");
        TimeElement age6months = TimeElements.age("P6M");
        PhenotypicFeature retinalDetachmentPf = PhenotypicFeatureBuilder.
                builder(retinalDetachment)
                .modifier(Laterality.unilateral())
                .onset(age6months)
                .build();
        return List.of(clinodactylyPf, leukocoriaPf, strabismusPf, retinalDetachmentPf);
    }


    /*
    The intraocular pressure was 25 mmHg in the right eye and 15 mmHg in the left eye,
    measured with the Perkins tonometer.
     */
    List<Measurement> getMeasurements() {
        OntologyClass iop = of("56844-4","Intraocular pressure of Eye");
        ReferenceRange ref = ReferenceRangeBuilder.of(iop, 10, 21);
        OntologyClass leftEyeIop =
                OntologyClassBuilder.of("LOINC:79893-4", "Left eye Intraocular pressure");
        Value leftEyeValue = ValueBuilder.of(Unit.mmHg(), 25, ref);
        OntologyClass rightEyeIop =
                OntologyClassBuilder.of("LOINC:79892-6", "Right eye Intraocular pressure");
        Value rightEyeValue = ValueBuilder.of(Unit.mmHg(), 15, ref);
        TimeElement age = TimeElements.age("P6M");

        Measurement leftEyeMeasurement = MeasurementBuilder.value(leftEyeIop, leftEyeValue).timeObserved(age).build();
        Measurement rightEyeMeasurement = MeasurementBuilder.value(rightEyeIop, rightEyeValue).timeObserved(age).build();
       //33728-7 Size.maximum dimension in Tumor
        //14 × 13 × 11 mm left eye tumor
        return List.of(leftEyeMeasurement, rightEyeMeasurement);
    }

    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
