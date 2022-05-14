package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.MetaDataBuilder;
import org.phenopackets.phenopackettools.builder.builders.Resources;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Laterality;
import org.phenopackets.phenopackettools.builder.constants.Unit;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;


public class GlaucomaSurgery implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final String BIOSAMPLE_ID = "biosample.1";

    // Organs
    private static final OntologyClass EYE = ontologyClass("UBERON:0000970", "eye");
    private static final OntologyClass CURE = ontologyClass("NCIT:C62220", "Cure");
    private static final OntologyClass LEFT_EYE = ontologyClass("UBERON:0004548", "left eye");



    private final Phenopacket phenopacket;


    public GlaucomaSurgery() {
        // hallo
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
        return List.of(leftEyeMeasurement, rightEyeMeasurement);
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

}

