package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Laterality;
import org.phenopackets.phenopackettools.builder.constants.Unit;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

/*
Late-Onset Anterior Dislocation  of a Posterior Chamber
Intraocular Lens in a Patient with Pseudoexfoliation
Syndrome Case Rep Ophthalmol 2011;2:1–4  DOI: 10.1159/000323861
RA: Cataract and PEX -> Cataractsurgery -> Emmetropia -> 1J1M Myopia + elevated eyeIop -> Brimonidine -> Yag-IT -> normal Iop
LA: Cataract -> Cataractsurgery -> Emmetropia
Result: RA/LA: Monovision
Vorderkammertiefe!!!!!!!!!!
 */
public class Pseudoexfoliation implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";

    private static final OntologyClass Pseudoexfoliation = ontologyClass("HP:0012627", "Pseudoexfoliation");
    private static final OntologyClass Pseudophakia = ontologyClass("HP:0500081", "Pseudophakia");

    // Organs
    private static final OntologyClass EYE = ontologyClass("UBERON:0000970", "eye");
    private static final OntologyClass LEFT_EYE = ontologyClass("UBERON:0004548", "left eye");
    private static final OntologyClass RIGHT_EYE = ontologyClass("UBERON:0004549", "right eye");
    private static final OntologyClass Cataract = ontologyClass("HP:0000518", "cataract");
    private static final OntologyClass visusPercent = ontologyClass("NCIT:C48570", "Percent Unit");
    ;
    private static final OntologyClass ocularHypertension = ontologyClass("HP:0007906", "Ocular hypertension");


    //
    private Phenopacket phenopacket;

    public Pseudoexfoliation() {
        // Metadaten
        var authorAssertion = EvidenceBuilder.authorStatementEvidence("PMID:21532993", "Late-Onset Anterior Dislocation  of a Posterior Chamber Intraocular Lens in a Patient Pseudoexfoliation Syndrome");
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addResource(Resources.hpoVersion("2021-08-02"))
                .build();
        Individual proband = IndividualBuilder.builder(PROBAND_ID).
                ageAtLastEncounter("P70Y").
                male().
                XY().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addMeasurements(getMeasurements())
                .addPhenotypicFeatures(getPhenotypicFeatures())
                .addDisease(getDisease())
                .addMedicalAction(cataractsurgeryRight())
                .addMedicalAction(cataractsurgeryLeft())
                //.addMedicalAction(brimonidine())
                //.addMedicalAction(nd_yag_iridotomy)
                .build();

    }

    Disease getDisease() {

        OntologyClass exfoliationSyndrome = ontologyClass("MONDO:0008327", "exfoliation syndrome");
        TimeElement adult = TimeElements.adultOnset();
        return DiseaseBuilder.builder(exfoliationSyndrome)
                .onset(adult)
                .primarySite(RIGHT_EYE)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }

    /*
       uneventful clear-cornea phacoemulsification with PC/IOL
implantation in the right eye (OD) in January 2006.
// TODO find code for cat op
        */
    MedicalAction cataractsurgeryRight() {
        ProcedureBuilder builder = ProcedureBuilder.builder("NCIT:C157809", "Cataract Surgery");
        TimeElement age = TimeElements.age("P70Y");
        builder.bodySite(RIGHT_EYE).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(Cataract)
                .treatmentIntent(Pseudophakia);
        return mabuilder.build();
    }

    /*
      "Six weeks later (Feb./Marc 2006, the patient was subjected to an uncomplicated cataract surgery OS."
      No other event on the left eye
     */
    MedicalAction cataractsurgeryLeft() {
        ProcedureBuilder builder = ProcedureBuilder.builder("NCIT:C157809", "Cataract Surgery");
        TimeElement age = TimeElements.age("P70Y6W");
        builder.bodySite(LEFT_EYE).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(Cataract)
                .treatmentIntent(Pseudophakia);
        return mabuilder.build();
    }


    List<Measurement> getMeasurements2() {

        // visual acuity 1,0 right eye one week after Cataractsurgery

        TypedQuantity visus100 = TypedQuantityBuilder.of(ontologyClass("NCIT:C87149", "Visual Acuity"),
                QuantityBuilder.of(visusPercent, 100));
        TimeElement age = TimeElements.age("P70Y7W");// One week added to the patient's age. Is that how it works?
        var visionAssessment = ontologyClass("NCIT:C156778", "Vision Assessment");
        Measurement visusMeasurement = MeasurementBuilder
                .builder(visionAssessment, ComplexValueBuilder.of(visus100)).build();

        // Refraction after 1 week right eye -0.25/-0.5/110 degrees

        OntologyClass sphericalrefraction = ontologyClass("LOINC:79895-9", "Subjective refraction method");
        ReferenceRange ref = ReferenceRangeBuilder.of(sphericalrefraction, -30, 30);
        OntologyClass rightEyesphericalrefraction =
                OntologyClassBuilder.ontologyClass("LOINC:79850-4", "Right eye spharical refraction");
        Value rightEyeValue = ValueBuilder.of(Unit.diopter(), -0.25, ref);
        OntologyClass rightEyecylindricalrefraction =
                OntologyClassBuilder.ontologyClass("LOINC:79846-2", "Right eye cylindrical refraction");
        Value rightEyeValueCylinder = ValueBuilder.of(Unit.diopter(), -0.5, ref);
        //	Right eye Axis: LOINC 9829-8
        TimeElement age70years = TimeElements.age("P70Y"); //Druckerhöhung 1J1M nach Cataractsurgery
//leftEyeMeasurement, rightEyeMeasurement,  TODO -- add to list of returned items
        return List.of(visusMeasurement);
    }
   /*
    ONE YEAR AFTER: Refraction after 1 year right eye –3.75/–0.5/110°.  degrees

    OntologyClass sphericalrefraction = ontologyClass("LOINC:79895-9","Subjective refraction method");
    ReferenceRange ref = ReferenceRangeBuilder.of(sphericalrefraction, -30, 30);
    OntologyClass rightEyesphericalrefraction =
            OntologyClassBuilder.ontologyClass("LOINC:79850-4", "Right eye spharical refraction");
    Value rightEyeValue = ValueBuilder.of(Unit.diop(), -3.75, ref);
    OntologyClass rightEyecylindricalrefraction =
            OntologyClassBuilder.ontologyClass("LOINC:79846-2", "Right eye cylindrical refraction");
    Value rightEyeValueCylinder = ValueBuilder.of(Unit.diop(), -0.5, ref);
    //	Right eye Axis: LOINC 9829-8
    TimeElement age = TimeElements.age("P71J1M");

    */

    /*
       ONE YEAR AFTER: The intraocular pressure was 29 mmHg in the right eye and 11 mmHg in the left eye one
        */
    List<Measurement> getMeasurementsYeasr2() {
        OntologyClass iop = ontologyClass("LOINC:56844-4", "Intraocular pressure of Eye");
        ReferenceRange ref = ReferenceRangeBuilder.of(iop, 10, 21);
        OntologyClass leftEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79893-4", "Left eye Intraocular pressure");
        Value leftEyeValue = ValueBuilder.of(Unit.mmHg(), 11, ref);
        OntologyClass rightEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79892-6", "Right eye Intraocular pressure");
        Value rightEyeValue = ValueBuilder.of(Unit.mmHg(), 29, ref);
        TimeElement age = TimeElements.age("P71Y1M"); //Druckerhöhung 1J1M nach Cataractsurgery

        Measurement leftEyeMeasurement = MeasurementBuilder.builder(leftEyeIop, leftEyeValue).timeObserved(age).build();
        Measurement rightEyeMeasurement = MeasurementBuilder.builder(rightEyeIop, rightEyeValue).timeObserved(age).build();


        /* Medical action brimonidine: IOP elevation resisted to topical therapy with α-2 receptors agonists (brimonidine eye drops) */
        /*Product containing precisely brimonidine tartrate 2 milligram/1 milliliter conventional release eye drops (clinical drug)
        SCTID: 330731001*/
        return List.of(leftEyeMeasurement, rightEyeMeasurement);

    }

    MedicalAction brimonidine() {
        OntologyClass brimonidine = ontologyClass("DrugCentral:395", "brimonidine");
        OntologyClass administration = ontologyClass("NCIT:C29302", "Ophthalmic Solution"); //Eye drop
        Quantity quantity = QuantityBuilder.of(Unit.mgPerKg(), 0.002);// quantity of eye drop?
        TimeInterval interval = TimeIntervalBuilder.of("2022-07-07", "2022-07-07"); //omit?
        TimeElement age = TimeElements.age("P71Y1M");
        OntologyClass once = ontologyClass("NCIT:C64576", "Once");

        DoseInterval doseInterval = DoseIntervalBuilder.of(quantity, once, interval);

        Treatment treatment = TreatmentBuilder.builder(brimonidine)
                .routeOfAdministration(administration)
                .addDoseInterval(doseInterval).build();

        return MedicalActionBuilder.builder(treatment)
                .addAdverseEvent(ontologyClass("HP:0025637", "Vasospasm"))
                .treatmentTarget(ocularHypertension)
                .treatmentTerminationReason(ontologyClass("NCIT:C41331", "Adverse Event"))
                .build();
    }

    /* Medical action Nd:YAG iridotomy (one year after initial Cataractsurgery)*/

    MedicalAction nd_yag_iridotomy() {
        ProcedureBuilder builder = ProcedureBuilder.builder("LOINC:29031-2", "Right eye YAG mode");
        TimeElement age = TimeElements.age("P71Y1M");
        builder.bodySite(RIGHT_EYE).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(Cataract)
                .treatmentIntent(Pseudophakia);
        return mabuilder.build();
    }


    // IOP was successfully regulated OD after Nd:YAG iridotomy (direct postoperative IOP: 14 mm Hg).

    List<Measurement> getMeasurements() {
        OntologyClass iop = ontologyClass("LOINC:56844-4", "Intraocular pressure of Eye");
        ReferenceRange ref = ReferenceRangeBuilder.of(iop, 10, 21);

        OntologyClass rightEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79892-6", "Right eye Intraocular pressure");
        Value rightEyeValue = ValueBuilder.of(Unit.mmHg(), 14, ref);// after Nd:YAG iridotomy
        TimeElement age = TimeElements.age("P71Y1M");

// Anterior chamber depth was 3.93 mm. The PC/IOL was sitting in the capsular bag OS, and the anterior chamber depth was 5.21 mm
        OntologyClass acdod = ontologyClass("SCTID: 397312009", "Intraocular lens anterior chamber depth");
        ReferenceRange ref2 = ReferenceRangeBuilder.of(acdod, 0, 10);

        OntologyClass rightEyeacdod =
                OntologyClassBuilder.ontologyClass("SCTID: 397312009", "Intraocular lens anterior chamber depth");
        Value rightEyeValue2 = ValueBuilder.of(Unit.millimeter(), 3.93, ref);//

        // Measurement leftEyeMeasurement = MeasurementBuilder.builder(leftEyeIop, leftEyeValue).timeObserved(age).build();
        Measurement rightEyeMeasurement = MeasurementBuilder.builder(rightEyeIop, rightEyeValue).timeObserved(age).build();


        // ONE YEAR AFTER: visual acuity 1,0 right eye

        TypedQuantity visus100 = TypedQuantityBuilder.of(ontologyClass("NCIT:C87149", "Visual Acuity"),
                QuantityBuilder.of(visusPercent, 100));
        var visionAssessment = ontologyClass("NCIT:C156778", "Vision Assessment");
        Measurement visusMeasurement = MeasurementBuilder
                .builder(visionAssessment, ComplexValueBuilder.of(visus100)).build();
        return List.of(rightEyeMeasurement);
    }

/* Result: Monovision Although  the myopic shift OD was not eliminated, the patient was satisfied with the monovision,
which was achieved unintentionally and, therefore, we did not proceed to an exchange surgery of the PC/IOL.
 */


    List<PhenotypicFeature> getPhenotypicFeatures() {
        TimeElement age70years = TimeElements.age("P70Y");
        PhenotypicFeature emmetropia = PhenotypicFeatureBuilder.
                builder("HP:0000539", "Abnormality of refraction"). // Verneinung, NO Abnormaility...
                        addModifier(Laterality.right()).
                onset(age70years).
                build();
        TimeElement age71years = TimeElements.age("P71Y");
        PhenotypicFeature myopia = PhenotypicFeatureBuilder.
                builder("HP:0000545", "Myopia")
                .addModifier(Laterality.right())
                .onset(age71years)
                .build();

        PhenotypicFeature iopi = PhenotypicFeatureBuilder. // iopi = Intraocular Pressure Increased
                builder("NCIT:C50618", "Intraocular Pressure Increased")
                .addModifier(Laterality.right())
                .onset(age71years)
                .build();
        // Anterior chamber depth was 3.93 mm. The PC/IOL was sitting in the capsular bag OS, and the anterior chamber depth was 5.21 mm

        PhenotypicFeature acdod = PhenotypicFeatureBuilder. //acdod: anterior chamber depth oculus dexter
                builder("SCTID:397312009", "Intraocular lens anterior chamber depth")//NCIT:C12667 Anterior Chamber of the Eye
                .addModifier(Laterality.right())
                .onset(age71years)
                .build();
        PhenotypicFeature excludedPhacodonesis = PhenotypicFeatureBuilder.
                builder("HP:0012629", "Phakodonesis")
                .excluded()
                .build();
        PhenotypicFeature excludedpupilabnormality = PhenotypicFeatureBuilder.
                builder(" HP:0007686", "Abnormal pupillary function")
                .excluded()
                .build();
        PhenotypicFeature monovision = PhenotypicFeatureBuilder.
                builder(" SCTID: 414775001", "monovision")// alternative to snomed?
                .excluded()
                .build();
        return List.of(emmetropia, myopia, iopi, excludedpupilabnormality, excludedPhacodonesis);
    }

}

