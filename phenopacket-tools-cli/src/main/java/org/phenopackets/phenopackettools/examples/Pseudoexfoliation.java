package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.MetaDataBuilder;
import org.phenopackets.phenopackettools.builder.builders.Resources;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Laterality;
import org.phenopackets.phenopackettools.builder.constants.Unit;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

/*
RA: Cataract and PEX -> Cataractsurgery -> Emmetropia -> 1J1M Myopia + elevated eyeIop -> Brimonidine -> Yag-IT -> normal Iop
LA: Cataract -> Cataractsurgery -> Emmetropia
RA/LA: Monovision
*/
 public class Pseudoexfoliation implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final String BIOSAMPLE_ID = "biosample.1";
    private static final OntologyClass Pseudoexfoliation = ontologyClass("HP:0012627", "Pseudoexfoliation");
    private static final OntologyClass Pseudophakia = ontologyClass("HP:0500081", "Pseudophakia");
    // Organs
    private static final OntologyClass EYE = ontologyClass("UBERON:0000970", "eye");
    private static final OntologyClass LEFT_EYE = ontologyClass("UBERON:0004548", "left eye");
    private static final OntologyClass RIGHT_EYE = ontologyClass("UBERON:0004549", "right eye");
    private static final OntologyClass Cataract = ontologyClass("HP:0000518", "cataract");

    private final Phenopacket phenopacket;

    public Pseudoexfoliation() {
        // Metadaten
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
                .addAllMeasurements(getMeasurements())
                .addAllPhenotypicFeatures(getPhenotypicFeatures())
                .addDisease(getDisease())
                .addMedicalAction(Cataractsurgery())
                .addMedicalAction(brimonidine())
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
        */
    MedicalAction cataractsurgery = () {
        ProcedureBuilder builder = ProcedureBuilder.builder("HP:0000518", "Cataractsurgery");
        TimeElement age = TimeElements.age("P70J");
        builder.bodySite(RIGHT_EYE).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(Cataract)
                .treatmentIntent(Pseudophakia);
        return mabuilder.build();
    }
    MedicalAction cataractsurgery = () {
        ProcedureBuilder builder = ProcedureBuilder.builder("HP:0000518", "Cataractsurgery");
        TimeElement age = TimeElements.age("P70J6W");
        builder.bodySite(LEFT_EYE).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(Cataract)
                .treatmentIntent(Pseudophakia);
        return mabuilder.build();

    /*
     The intraocular pressure was 29 mmHg in the right eye and 11 mmHg in the left eye
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

        OntologyClass visusPercent = ontologyClass("NCIT:C48570", "Percent Unit");
        // -0.25/-0.5/110 degreees
        TypedQuantity  visus100 = TypedQuantityBuilder.of(ontologyClass("NCIT:C87149", "Visual Acuity"),
                QuantityBuilder.of(visusPercent, 100));
        var visionAssessment = ontologyClass("NCIT:C156778", "Vision Assessment");
        Measurement visusMeasurement = MeasurementBuilder
                .builder(visionAssessment, ComplexValueBuilder.of(visus100)).build();
        // NCIT:C117889  Astigmatism Axis - A measurement of the location, in degrees, of the flatter principal meridian on a 180-degree scale, where 90 degrees designates the vertical meridian and 180 degrees designates the horizontal meridian. [ NCI ]


        return List.of(leftEyeMeasurement, rightEyeMeasurement, visusMeasurement);
    }


    List<PhenotypicFeature> getPhenotypicFeatures() {
            TimeElement age70years = TimeElements.age("P70J");
            PhenotypicFeature emmetropia = PhenotypicFeatureBuilder.
                    builder("HP:0000539", "Abnormality of refraction"). // Verneinung, NO Abnormaility...
                    addModifier(Laterality.right()).
                    onset(age70years).
                    build();
            TimeElement age71years = TimeElements.age("P71J");
            PhenotypicFeature Myopia = PhenotypicFeatureBuilder.
                    builder("HP:0000545", "Myopia").
                    addModifier(Laterality.right()).
                    onset(age71years).
                    build();
        TimeElement age70years = TimeElements.age("P70J");
        PhenotypicFeature glaucoma = PhenotypicFeatureBuilder.
                builder("HP:0012108", "Open angle glaucoma ").
                addModifier(Laterality.right()).
                onset(age70years).
                build();
        TimeElement age4months = TimeElements.age("P4M");
        PhenotypicFeature leukocoria = PhenotypicFeatureBuilder.
                builder("HP:0500081", "Pseudophakia")
                .addModifier(Laterality.right())
                .onset(age4months)
                .build();

        PhenotypicFeature excludedPhacodonesis = PhenotypicFeatureBuilder.
                builder("HP:0012629", "Phakodonesis")
                .excluded()
                .build();
        return List.of(clinodactyly, leukocoria, strabismus, retinalDetachment, excludedPhacodonesis);
    }

}

