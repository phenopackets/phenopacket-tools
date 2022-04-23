package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Laterality;
import org.phenopackets.phenopackettools.builder.constants.Unit;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class Retinoblastoma implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final String BIOSAMPLE_ID = "biosample.1";
    private static final OntologyClass BIOPSY = ontologyClass("NCIT:C15189", "Biopsy");
    private static final OntologyClass RETINOBLASTOMA = ontologyClass("NCIT:C7541", "Retinoblastoma");
    private static final OntologyClass PRIMARY_NEOPLASM = ontologyClass("NCIT:C8509", "Primary Neoplasm");

    // Organs
    private static final OntologyClass EYE = ontologyClass("UBERON:0000970", "eye");
    private static final OntologyClass CURE = ontologyClass("NCIT:C62220", "Cure");
    private static final OntologyClass LEFT_EYE = ontologyClass("UBERON:0004548", "left eye");

    private final Phenopacket phenopacket;
    public Retinoblastoma() {
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.efoVersion("3.34.0"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addResource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();
        Individual proband = IndividualBuilder.builder(PROBAND_ID).
                ageAtLastEncounter("P6M").
                female().
                XX().
                build();
        // VCF file with results of germline whole-genome sequencing
        File wgsFile = FileBuilder.hg38vcf("file://data/germlineWgs.vcf.gz")
                .individualToFileIdentifier(PROBAND_ID, "sample1")
                .build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addAllMeasurements(getMeasurements())
                .addAllPhenotypicFeatures(getPhenotypicFeatures())
                .addDisease(getDisease())
                .addMedicalAction(melphalan())
                .addMedicalAction(chemoRegimen())
                .addMedicalAction(enucleation())
                .addBiosample(enucleatedEye())
                .addInterpretation(interpretation())
                .addFile(wgsFile)
                .build();
    }


    Interpretation interpretation() {
        Diagnosis diagnosis = DiagnosisBuilder.builder(RETINOBLASTOMA)
                .addGenomicInterpretation(germlineRb1Deletion())
                .addGenomicInterpretation(somaticRb1Missense())
                .build();
        return InterpretationBuilder.builder("interpretation.id").solved(diagnosis);
    }


    /**
     * Variation ID:126824 (ClinGen pathogenic)
     * @return Genomic interpretation related to a somatic missense mutation in the RB1 gene.
     */
    GenomicInterpretation somaticRb1Missense() {
        AlleleBuilder abuilder = AlleleBuilder.builder();
        abuilder.sequenceId("refseq:NC_000013.14");
        abuilder.startEnd( 48941647, 48941648);
        abuilder.altAllele("T");
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder("rs121913300")
                .variation(abuilder.buildVariation())
                .genomic()
                .heterozygous()
                .label("RB1 c.958C>T (p.Arg320Ter)")
                .transcript()
                .vcfHg38("NC_000013.11", 48367512, "C", "T")
                .alleleFrequency(25.0)
                .geneContext(GeneDescriptorBuilder.of("HGNC:9884", "RB1"))
                .addExpression(Expressions.hgvsCdna("NM_000321.2:c.958C>T"))
                .addExpression(Expressions.transcriptReference("NM_000321.2"));
        // wrap in VariantInterpretation
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.builder(vbuilder);
        vibuilder.pathogenic();
        vibuilder.actionable();

        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder(BIOSAMPLE_ID);
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);

        return gbuilder.build();
    }


    GenomicInterpretation germlineRb1Deletion() {
        CopyNumberBuilder abuilder = CopyNumberBuilder.builder();
        //abuilder.copyNumberId("ga4gh:VCN.AFfJws1M4Lg8w1O3XknmHYc9TU2hHYpp");
        // original coordinates in paper were given as 13q12.13q21.2(26,555,387–62,280,955 for hg19
        //chr13	25981249	61706822 -- lifted over to hg38

        abuilder.alleleLocation("refseq:NC_000013.14",25981249, 61706822);//VRS uses inter-residue coordinates
        abuilder.oneCopy();
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder();
        vbuilder.variation(abuilder.buildVariation());
        vbuilder.mosaicism(40.0);
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.builder(vbuilder);
        vibuilder.pathogenic();
        vibuilder.actionable();


        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder(PROBAND_ID);
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);

        return gbuilder.build();

    }

    /**
     * Can be used for the multisample VCF file for germline WGS in this family
     * @return a pedigree object in which just the proband is affected (de novo variant)
     */
    Pedigree pedigree() {
        String familyId = "family.1";
        String motherId = "maternal1";
        String fatherId = "paternal1";
        Pedigree.Person mother = PersonBuilder
                .builderWithParentsAsFounders(familyId, motherId)
                .female()
                .unaffected()
                .build();
        Pedigree.Person father = PersonBuilder
                .builderWithParentsAsFounders(familyId, fatherId)
                .male()
                .unaffected()
                .build();
        Pedigree.Person child = PersonBuilder
                .builder(familyId, PROBAND_ID, fatherId, motherId)
                .female()
                .affected()
                .build();
        PedigreeBuilder pbuilder = PedigreeBuilder.builder();
        pbuilder.addPerson(father);
        pbuilder.addPerson(mother);
        pbuilder.addPerson(child);
        return pbuilder.build();
    }

    Biosample enucleatedEye() {
        TimeElement age = TimeElements.age("P8M2W");
        BiosampleBuilder biosampleBuilder = BiosampleBuilder.builder(BIOSAMPLE_ID);
        biosampleBuilder.sampledTissue(EYE);
        //Retinoblastoma with tumor invading optic nerve past lamina cribrosa but not to surgical resection line and exhibiting massive choroidal invasion.
        biosampleBuilder.addPathologicalTnmFinding(ontologyClass("NCIT:C140720", "Retinoblastoma pT3 TNM Finding v8"));
        //Retinoblastoma with no regional lymph node involvement.
        biosampleBuilder.addPathologicalTnmFinding(ontologyClass("NCIT:C140711","Retinoblastoma pN0 TNM Finding v8"));

        biosampleBuilder.addPhenotypicFeature("NCIT:C35941", "Flexner-Wintersteiner Rosette Formation");
        biosampleBuilder.addPhenotypicFeature("NCIT:C132485", "Apoptosis and Necrosis");
        OntologyClass maxTumorSizeTest = OntologyClassBuilder.ontologyClass("LOINC:33728-7", "Size.maximum dimension in Tumor");
        Value maxTumorSize = ValueBuilder.of(Unit.mm(), 15);
        Measurement maxTumorSizeMeasurement = MeasurementBuilder.builder(maxTumorSizeTest, maxTumorSize).timeObserved(age).build();
        biosampleBuilder.addMeasurement(maxTumorSizeMeasurement);

        Procedure enucleation = ProcedureBuilder.builder("NCIT:C48601", "Enucleation")
                .bodySite(LEFT_EYE)
                .performed(age)
                .build();
        biosampleBuilder.procedure(enucleation);
        biosampleBuilder.tumorProgression(PRIMARY_NEOPLASM);
        // VCF file with results of whole-genome sequencing on this tumor
        File wgsFile = FileBuilder.hg38vcf("file://data/fileSomaticWgs.vcf.gz")
                        .individualToFileIdentifier(BIOSAMPLE_ID, "specimen.1")
                                .build();
        biosampleBuilder.addFile(wgsFile);
        return biosampleBuilder.build();
    }



    MedicalAction melphalan() {
        OntologyClass melphalan = ontologyClass("DrugCentral:1678", "melphalan");
        OntologyClass administration = ontologyClass("NCIT:C38222",  "Intraarterial Route of Administration");
       //0.4 mg/kg (up to a starting dose of 5 mg)
        Quantity quantity = QuantityBuilder.of(Unit.mgPerKg(), 0.4);
        TimeInterval interval = TimeIntervalBuilder.of("2020-09-02", "2020-09-02");
        OntologyClass once = ontologyClass("NCIT:C64576", "Once");

        DoseInterval doseInterval = DoseIntervalBuilder.of(quantity, once, interval);


        Treatment treatment = TreatmentBuilder.builder(melphalan)
                .routeOfAdministration(administration)
                .addDoseInterval(doseInterval).build();

        return MedicalActionBuilder.builder(treatment)
                .addAdverseEvent(ontologyClass("HP:0025637", "Vasospasm"))
                .treatmentTarget(RETINOBLASTOMA)
                .treatmentIntent(CURE)
                .treatmentTerminationReason(ontologyClass("NCIT:C41331", "Adverse Event"))
                .build();
    }


    MedicalAction enucleation() {
        ProcedureBuilder builder = ProcedureBuilder.builder("NCIT:C48601", "Enucleation");
        TimeElement age = TimeElements.age("P8M2W");
        builder.bodySite(LEFT_EYE).performed(age);
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
        OntologyClass stageE = ontologyClass("LOINC:LA24739-7", "Group E");
        OntologyClass noMetastasis = ontologyClass("NCIT:C140678", "Retinoblastoma cM0 TNM Finding v8");

        TimeElement age4m = TimeElements.age("P4M");
        return DiseaseBuilder.builder(RETINOBLASTOMA)
                .onset(age4m)
                .addDiseaseStage(stageE)
                .addClinicalTnmFinding(noMetastasis)
                .primarySite(LEFT_EYE)
                .build();
    }




    List<PhenotypicFeature> getPhenotypicFeatures() {
        TimeElement age3months = TimeElements.age("P3M");
        PhenotypicFeature clinodactyly = PhenotypicFeatureBuilder.
                builder("HP:0030084", "Clinodactyly").
                addModifier(Laterality.right()).
                onset(age3months).
                build();
        TimeElement age4months = TimeElements.age("P4M");
        PhenotypicFeature leukocoria = PhenotypicFeatureBuilder.
                builder("HP:0000555", "Leukocoria")
                .addModifier(Laterality.left())
                .onset(age4months)
                .build();
        TimeElement age5months = TimeElements.age("P5M15D");
        PhenotypicFeature strabismus = PhenotypicFeatureBuilder.
                builder("HP:0000486", "Strabismus")
                .addModifier(Laterality.left())
                .onset(age5months)
                .build();
        TimeElement age6months = TimeElements.age("P6M");
        PhenotypicFeature retinalDetachment = PhenotypicFeatureBuilder
                .builder("HP:0000541", "Retinal detachment")
                .addModifier(Laterality.left())
                .onset(age6months)
                .build();
        return List.of(clinodactyly, leukocoria, strabismus, retinalDetachment);
    }


    /*
    The intraocular pressure was 25 mmHg in the right eye and 15 mmHg in the left eye,
    measured with the Perkins tonometer.
     */
    List<Measurement> getMeasurements() {
        OntologyClass iop = ontologyClass("56844-4","Intraocular pressure of Eye");
        ReferenceRange ref = ReferenceRangeBuilder.of(iop, 10, 21);
        OntologyClass leftEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79893-4", "Left eye Intraocular pressure");
        Value leftEyeValue = ValueBuilder.of(Unit.mmHg(), 25, ref);
        OntologyClass rightEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79892-6", "Right eye Intraocular pressure");
        Value rightEyeValue = ValueBuilder.of(Unit.mmHg(), 15, ref);
        TimeElement age = TimeElements.age("P6M");

        Measurement leftEyeMeasurement = MeasurementBuilder.builder(leftEyeIop, leftEyeValue).timeObserved(age).build();
        Measurement rightEyeMeasurement = MeasurementBuilder.builder(rightEyeIop, rightEyeValue).timeObserved(age).build();
       //33728-7 Size.maximum dimension in Tumor
        //14 × 13 × 11 mm left eye tumor
        return List.of(leftEyeMeasurement, rightEyeMeasurement);
    }

    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
