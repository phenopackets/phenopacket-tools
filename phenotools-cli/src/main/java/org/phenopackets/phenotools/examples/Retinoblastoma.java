package org.phenopackets.phenotools.examples;

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
    private static final OntologyClass BIOPSY = ontologyClass("NCIT:C15189", "Biopsy");

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


        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .allMeasurements(getMeasurements())
                .allPhenotypicFeatures(getPhenotypicFeatures())
                .disease(getDisease())
                .medicalAction(melphalan())
//                .biosample(esophagusBiopsy)
//                .biosample(lymphNodeBiopsy)
//                .biosample(lungBiopsy)
//                .disease(disease)
                .build();
    }


    MedicalAction melphalan() {
        OntologyClass melphalan = ontologyClass("DrugCentral:1678", "melphalan");
        OntologyClass administration = ontologyClass("NCIT:C38222",  "Intraarterial Route of Administration");
       //0.4 mg/kg (up to a starting dose of 5 mg)
        Quantity quantity = QuantityBuilder.quantity( mm_per_kg(), 0.4);
        TimeInterval interval = TimeIntervalBuilder.timeInterval("P6M1W", "P6M1W");
        DoseInterval doseInterval = DoseIntervalBuilder.doseInterval(quantity, administration, interval);

        Treatment treatment = TreatmentBuilder.create(melphalan)
                .routeOfAdministration(administration)
                .doseInterval(doseInterval).build();

        MedicalAction action = MedicalActionBuilder.create(treatment)
                .adverseEvent(ontologyClass("HP:0025637", "Vasospasm"))
                .treatmentTarget(ontologyClass("NCIT:C7541", "Retinoblastoma"))
                .treatmentIntent(ontologyClass("NCIT:C62220", "Cure"))
                .treatmentTerminationReason(ontologyClass("NCIT:C41331", "Adverse Event"))
                .build();

        return action;
    }

    List<MedicalAction> medicalActions() {

        return List.of(melphalan());
    }

    /*
     agent:
            id:
        routeOfAdministration:
            id:

     */



    Disease getDisease() {
        // NCIT:C27980
        // Stage E
        //  Group E = LOINC:LA24739-7
        // Retinoblastoma ,  NCIT:C7541
        OntologyClass stageE = ontologyClass("LOINC:LA24739-7", "Group E");
        OntologyClass retinoblastoma = ontologyClass("NCIT:C7541", "Retinoblastoma");
        OntologyClass leftEye = ontologyClass("UBERON:0004548", "left eye");
        TimeElement age4m = TimeElements.age("P4M");
        return DiseaseBuilder.create(retinoblastoma)
                .onset(age4m)
                .diseaseStage(stageE)
                .primarySite(leftEye)
                .build();
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
