package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Laterality;
import org.phenopackets.phenopackettools.builder.constants.Unit;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;


public class Pseudoexfoliation implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final String BIOSAMPLE_ID = "biosample.1";

    // Organs
    private static final OntologyClass EYE = ontologyClass("UBERON:0000970", "eye");
    private static final OntologyClass CURE = ontologyClass("NCIT:C62220", "Cure");
    private static final OntologyClass LEFT_EYE = ontologyClass("UBERON:0004548", "left eye");



    private final Phenopacket phenopacket;


    public Pseudoexfoliation() {
        // hallo
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.efoVersion("3.34.0"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addResource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();
        Individual proband = IndividualBuilder.builder(PROBAND_ID).
                ageAtLastEncounter("P70Y").
                female().
                XX().
                build();




        PhenopacketBuilder builder = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addAllMeasurements(getMeasurements())
                .addAllPhenotypicFeatures(getPhenotypicFeatures())
                .addDisease(getDisease());
        //  (Genom-Sequenz) .addInterpretation(interpretation())


        phenopacket = builder.build();
    }
    Disease getDisease() {
        // NCIT:C27980
        // Stage E
        //  Group E = LOINC:LA24739-7
        // Retinoblastoma ,  NCIT:C7541
        // No metastasis is NCIT:C140678, Retinoblastoma cM0 TNM Finding v8
        // Retinoblastoma with no signs or symptoms of intracranial or distant metastasis. (from AJCC 8th Ed.) [ NCI ]
        // OntologyClass stageE = ontologyClass("LOINC:LA24739-7", "Group E");
        // OntologyClass noMetastasis = ontologyClass("NCIT:C140678", "Retinoblastoma cM0 TNM Finding v8");
        OntologyClass exfoliationSyndrome = ontologyClass("MONDO:0008327", "exfoliation syndrome");
        // TimeElement age4m = TimeElements.age("P4M");
        TimeElement adult = TimeElements.adultOnset();
        return DiseaseBuilder.builder(exfoliationSyndrome)
                .onset(adult)
                // .addDiseaseStage(stageE)
                // .addClinicalTnmFinding(noMetastasis)

                // .primarySite(LEFT_EYE)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
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


    /*
Pseudophakia HP:0500081
Cataract HP:0000518
Shallow anterior chamber HP:0000594
Pseudophakia HP:0500081
Asymmetry of intraocular pressure HP:0012633
Ocular hypertension HP:0007906
PseudoexfoliationHP:0012627
GlaucomaOMIT:0007096
Pseudoexfoliation glaucoma (disorder)111514006
http://snomed.info/id/111514006
Pseudoexfoliation glaucoma of left eye (disorder)338481000119100
http://snomed.info/id/338481000119100
Pseudoexfoliation glaucoma of bilateral eyes (disorder)344251000119103
http://snomed.info/id/344251000119103
Pseudoexfoliation glaucoma of right eye (disorder)332871000119103
http://snomed.info/id/332871000119103
*/


    List<PhenotypicFeature> getPhenotypicFeatures() {
        TimeElement age70years = TimeElements.age("P70J");
        PhenotypicFeature clinodactyly = PhenotypicFeatureBuilder.
                builder("HP:0012108", "Open angle glaucoma ").
                addModifier(Laterality.right()).
                onset(age70years).
                build();
        TimeElement age4months = TimeElements.age("P4M");
        PhenotypicFeature leukocoria = PhenotypicFeatureBuilder.
                builder("HP:0500081", "Pseudophakia")
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
        PhenotypicFeature excludedPhacodonesis = PhenotypicFeatureBuilder.
                builder("HP:0012629", "Phakodonesis")
                .excluded()
                .build();
        return List.of(clinodactyly, leukocoria, strabismus, retinalDetachment, excludedPhacodonesis);
    }

}

