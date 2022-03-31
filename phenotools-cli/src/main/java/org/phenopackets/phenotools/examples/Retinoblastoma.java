package org.phenopackets.phenotools.examples;

import org.ga4gh.vrsatile.v1.VariationDescriptor;
import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;
import static org.phenopackets.phenotools.builder.builders.Util.*;

public class Retinoblastoma {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final String BIOSAMPLE_ID = "biosample.1";
    private static final OntologyClass BIOPSY = ontologyClass("NCIT:C15189", "Biopsy");
    private static final OntologyClass retinoblastoma = ontologyClass("NCIT:C7541", "Retinoblastoma");

    private final OntologyClass leftEye = ontologyClass("UBERON:0004548", "left eye");

    private final Phenopacket phenopacket;
    public Retinoblastoma() {
        var metadata = MetaDataBuilder.create("2021-05-14T10:35:00Z", "anonymous biocurator")
                .resource(Resources.ncitVersion("21.05d"))
                .resource(Resources.efoVersion("3.34.0"))
                .resource(Resources.uberonVersion("2021-07-27"))
                .resource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();
        Individual proband = IndividualBuilder.create(PROBAND_ID).
                ageAtLastEncounter("P6M").
                female().
                XX().
                build();
        // VCF file with results of germline whole-genome sequencing
        File wgsFile = FileBuilder.file("file://data/germlineWgs.vcf.gz");

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
         DiagnosisBuilder dbuilder = DiagnosisBuilder.create(retinoblastoma);
         dbuilder.genomicInterpretation(somatic);
         ibuilder.diagnosis(dbuilder.build());
         return ibuilder.build();
    }


    /**
     * Variation ID:126824 (ClinGen pathogenic)
     * @return
     */
    GenomicInterpretation somaticRb1Missense() {
        AlleleBuilder abuilder = AlleleBuilder.create();
        abuilder.setSequenceId("ga4gh:VA.GuPzvZoansqNHPoXkQLXKo31VkTpDKsM");
        abuilder.startEnd( 48941647, 48941648);
        abuilder.setAltAllele("T");
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.create("rs121913300");
        vbuilder.variation(abuilder.buildVariation());
        vbuilder.genomic();
        vbuilder.heterozygous();

        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.create("interpretation.1");
        gbuilder.candidate();
        //gbuilder.variantInterpretation(vbuilder.build());

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
                .createWithUnknownParentId(family_id, mother_id)
                .female()
                .unaffected()
                .build();
        Pedigree.Person father = PersonBuilder
                .createWithUnknownParentId(family_id, father_id)
                .male()
                .unaffected()
                .build();
        Pedigree.Person child = PersonBuilder
                .create(family_id, PROBAND_ID, father_id, mother_id)
                .female()
                .affected()
                .build();
        PedigreeBuilder pbuilder = PedigreeBuilder.create();
        pbuilder.person(father);
        pbuilder.person(mother);
        pbuilder.person(child);
        return pbuilder.build();
    }


    Biosample enucleatedEye() {
        String biosampleId = "biosample.1";
        BiosampleBuilder builder = BiosampleBuilder.create(biosampleId);
        builder.sampledTissue(eye());
        //Retinoblastoma with tumor invading optic nerve past lamina cribrosa but not to surgical resection line and exhibiting massive choroidal invasion.
        builder.pathologicalTnmFinding(ontologyClass("NCIT:C88735",
                "Retinoblastoma pT3b TNM Finding v7"));
        //Retinoblastoma with no regional lymph node involvement.
        builder.pathologicalTnmFinding(ontologyClass("NCIT:C88741",
                "Retinoblastoma pN0 TNM Finding v7"));

        //
        OntologyClass fwRosette = ontologyClass("NCIT:C35941", "Flexner-Wintersteiner Rosette Formation");
        PhenotypicFeature pfRosette = PhenotypicFeatureBuilder.create(fwRosette).build();
        builder.phenotypicFeature(pfRosette);
        OntologyClass apoptosisNecrosis = ontologyClass("NCIT:C132485", "Apoptosis and Necrosis");
        PhenotypicFeature pfApoptosis = PhenotypicFeatureBuilder.create(apoptosisNecrosis).build();
        builder.phenotypicFeature(pfApoptosis);

        ProcedureBuilder pbuilder = ProcedureBuilder.create("NCIT:C48601", "Enucleation");
        TimeElement age = TimeElements.age("P8M2W");
        pbuilder.bodySite(leftEye).performed(age);
        builder.procedure(pbuilder.build());
        builder.tumorProgression(primaryNeoplasm());
        // VCF file with results of whole-genome sequencing on this tumor
        File wgsFile = FileBuilder.file("file://data/fileSomaticWgs.vcf.gz");
        builder.file(wgsFile);
        return builder.build();
    }



    MedicalAction melphalan() {
        OntologyClass melphalan = ontologyClass("DrugCentral:1678", "melphalan");
        OntologyClass administration = ontologyClass("NCIT:C38222",  "Intraarterial Route of Administration");
       //0.4 mg/kg (up to a starting dose of 5 mg)
        Quantity quantity = QuantityBuilder.quantity( mm_per_kg(), 0.4);
        TimeInterval interval = TimeIntervalBuilder.timeInterval("2020-09-02", "2020-09-02");
        OntologyClass once = ontologyClass("NCIT:C64576", "Once");

        DoseInterval doseInterval = DoseIntervalBuilder.doseInterval(quantity, once, interval);


        Treatment treatment = TreatmentBuilder.create(melphalan)
                .routeOfAdministration(administration)
                .doseInterval(doseInterval).build();

        return MedicalActionBuilder.create(treatment)
                .adverseEvent(ontologyClass("HP:0025637", "Vasospasm"))
                .treatmentTarget(ontologyClass("NCIT:C7541", "Retinoblastoma"))
                .treatmentIntent(ontologyClass("NCIT:C62220", "Cure"))
                .treatmentTerminationReason(ontologyClass("NCIT:C41331", "Adverse Event"))
                .build();
    }


    MedicalAction enucleation() {
        ProcedureBuilder builder = ProcedureBuilder.create("NCIT:C48601", "Enucleation");
        TimeElement age = TimeElements.age("P8M2W");
        builder.bodySite(leftEye).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.create(builder.build())
                .treatmentTarget(ontologyClass("NCIT:C7541", "Retinoblastoma"))
                .treatmentIntent(ontologyClass("NCIT:C62220", "Cure"));
        return mabuilder.build();
    }

    /**
     * NCIT:C10894, Carboplatin/Etoposide/Vincristine
     * @return MedicalAction message representing treatment with a regimen of these three chemotherapeutics
     */
    MedicalAction chemoRegimen() {
        TherapeuticRegimenBuilder builder = TherapeuticRegimenBuilder.create("NCIT:C10894", "Carboplatin/Etoposide/Vincristine");
        builder.completed();
        TimeElement start = TimeElements.age("P7M");
        TimeElement end = TimeElements.age("P8M");
        builder.startTime(start).endTime(end);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.create(builder.build())
                .treatmentTarget(ontologyClass("NCIT:C7541", "Retinoblastoma"))
                .treatmentIntent(ontologyClass("NCIT:C62220", "Cure"));
        return mabuilder.build();
    }



    Disease getDisease() {
        // NCIT:C27980
        // Stage E
        //  Group E = LOINC:LA24739-7
        // Retinoblastoma ,  NCIT:C7541
        OntologyClass stageE = ontologyClass("LOINC:LA24739-7", "Group E");


        TimeElement age4m = TimeElements.age("P4M");
        return DiseaseBuilder.create(retinoblastoma)
                .onset(age4m)
                .diseaseStage(stageE)
                .primarySite(leftEye)
                .build();
    }



    Biosample getBiosample() {
        BiosampleBuilder builder = BiosampleBuilder.create(BIOSAMPLE_ID);
       // builder.

        return builder.build();
    }



    List<PhenotypicFeature> getPhenotypicFeatures() {
        OntologyClass clinodactyly = ontologyClass("HP:0030084", "Clinodactyly");
        TimeElement age3months = TimeElements.age("P3M");
        PhenotypicFeature clinodactylyPf = PhenotypicFeatureBuilder.
                create(clinodactyly).
                modifier(right()).
                onset(age3months).
                build();
        OntologyClass leukocoria  = ontologyClass("HP:0000555", "Leukocoria");
        TimeElement age4months = TimeElements.age("P4M");
        PhenotypicFeature leukocoriaPf = PhenotypicFeatureBuilder.
                create(leukocoria)
                .modifier(unilateral())
                .onset(age4months)
                .build();
        OntologyClass strabismus  = ontologyClass("HP:0000486", "Strabismus");
        TimeElement age5months = TimeElements.age("P5M15D");
        PhenotypicFeature strabismusPf = PhenotypicFeatureBuilder.
                create(strabismus)
                .modifier(unilateral())
                .onset(age5months)
                .build();
        OntologyClass retinalDetachment  = ontologyClass("HP:0000541", "Retinal detachment");
        TimeElement age6months = TimeElements.age("P6M");
        PhenotypicFeature retinalDetachmentPf = PhenotypicFeatureBuilder.
                create(retinalDetachment)
                .modifier(unilateral())
                .onset(age6months)
                .build();
        return List.of(clinodactylyPf, leukocoriaPf, strabismusPf, retinalDetachmentPf);
    }


    /*
    The intraocular pressure was 25 mmHg in the right eye and 15 mmHg in the left eye,
    measured with the Perkins tonometer.
     */
    List<Measurement> getMeasurements() {
        OntologyClass iop = ontologyClass("56844-4","Intraocular pressure of Eye");
        ReferenceRange ref = ReferenceRangeBuilder.referenceRange(iop, 10, 21);
        OntologyClass leftEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79893-4", "Left eye Intraocular pressure");
        Value leftEyeValue = ValueBuilder.value(Util.mmHg(), 25, ref);
        OntologyClass rightEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79892-6", "Right eye Intraocular pressure");
        Value rightEyeValue = ValueBuilder.value(Util.mmHg(), 15, ref);
        TimeElement age = TimeElements.age("P6M");

        Measurement leftEyeMeasurement = MeasurementBuilder.value(leftEyeIop, leftEyeValue).timeObserved(age).build();
        Measurement rightEyeMeasurement = MeasurementBuilder.value(rightEyeIop, rightEyeValue).timeObserved(age).build();
       //33728-7 Size.maximum dimension in Tumor
        //14 × 13 × 11 mm left eye tumor
        OntologyClass maxTumorSizeTest = OntologyClassBuilder.ontologyClass("LOINC:33728-7", "Size.maximum dimension in Tumor");
        Value maxTumorSize = ValueBuilder.value(mm(), 15);
        Measurement maxTumorSizeMeasurement = MeasurementBuilder.value(maxTumorSizeTest, maxTumorSize).timeObserved(age).build();

        return List.of(leftEyeMeasurement, rightEyeMeasurement, maxTumorSizeMeasurement);
    }

    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
