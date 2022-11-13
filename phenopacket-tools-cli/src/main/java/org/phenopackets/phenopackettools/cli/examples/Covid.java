package org.phenopackets.phenopackettools.cli.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.ArrayList;
import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class Covid implements PhenopacketExample {

    private static final String ONSET_OF_COVID = "2021-02-01T05:00:00Z";

    private static final String RETURN_TO_HOSPITAL_TIME = "2020-03-20T00:00:00Z";

    private static final OntologyClass stage3kidney = ontologyClass("HP:0012625", "Stage 3 chronic kidney disease");
    private static final OntologyClass obesity = ontologyClass("HP:0001513", "Obesity");
    private static final OntologyClass giBleeding = ontologyClass("HP:0002239", "Gastrointestinal hemorrhage");
    private static final OntologyClass vtach = ontologyClass("HP:0004756", "Ventricular tachycardia");
    private static final OntologyClass rvFailure = ontologyClass("HP:0001708", "Right ventricular failure");
    private static final Disease notDiabetesMellitus = Disease.newBuilder()
            .setTerm(ontologyClass("MONDO:0005015", "diabetes mellitus"))
            .setExcluded(true)
            .build();

    private static final OntologyClass CONTINUOUS = ontologyClass("PATO:0000689", "continuous");

    private final Phenopacket phenopacket;


    public Covid() {
        Individual patient = IndividualBuilder.builder("P123542")
                .male()
                .ageAtLastEncounter("P70Y")
                .vitalStatus(VitalStatusBuilder.deceased().causeOfDeath("MONDO:0100096", "COVID-19").build())
                .build();

        Disease cardiomyopathy = DiseaseBuilder.of("MONDO:0004994", "cardiomyopathy");
        Disease covid = DiseaseBuilder
                .builder("MONDO:0100096", "COVID-19")
                .onset(TimeElements.timestamp("2020-03-17T00:00:00Z"))
                .build();
        var bloodGroupA = PhenotypicFeatureBuilder.of("HP:0032370", "Blood group A");
        var rhesusPositive = PhenotypicFeatureBuilder.of("NCIT:C76251", "Rh Positive Blood Group");
        var obesityPhenotype = PhenotypicFeatureBuilder.of(obesity);
        var externalRef = ExternalReferenceBuilder.reference()
                .id("DOI:10.1016/j.jaccas.2020.04.001")
                .reference("PMID:32292915")
                .description("The Imperfect Cytokine Storm: Severe COVID-19 With ARDS in a Patient on Durable LVAD Support")
                .build();
        var metaData = MetaDataBuilder.builder("2021-08-17T00:00:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("2019-11-26"))
                .addResource(Resources.mondoVersion("2021-11-26"))
                .addExternalReference(externalRef)
                .build();


        phenopacket = PhenopacketBuilder.create("arbitrary.phenopacket.id", metaData)
                .individual(patient)
                .addPhenotypicFeatures(getAllPhenotypicFeatures())
                .addMeasurements(getAllMeasurements())
                // .addMeasurements(initialBloodLymphocyteCount)
                // .addMeasurements(hoD0bloodLymphocyteCount)
                .addMedicalAction(lvadImplant())
                .addMedicalAction(nasalOxygenAdministered())
                .addMedicalAction(dexamethasone())
                .addMedicalAction(trachealIntubation())
                .addMedicalAction(peepOxygenAdministered())
                .addMedicalAction(tocilizumabAdministered())
                .addDisease(notDiabetesMellitus)
                .addDisease(cardiomyopathy)
                .addDisease(covid)
                .build();

    }

    private List<PhenotypicFeature> getAllPhenotypicFeatures() {
        // 70-year-old male with a destination therapy HeartMate 3 (Abbott Laboratory, Lake Bluff, Illinois)
        // left ventricular assist device (LVAD) implanted in 2016 who developed fever, flank pain, and hematuria 3 days
        // after attending a party
        // His blood group was type A positive.

        var fever = PhenotypicFeatureBuilder.builder("HP:0001945", "Fever ")
                .onset(TimeElements.timestamp(ONSET_OF_COVID))
                .build();
        var flankPain = PhenotypicFeatureBuilder.builder("HP:0030157", "Flank pain")
                .onset(TimeElements.timestamp(ONSET_OF_COVID))
                .build();
        var hematuria = PhenotypicFeatureBuilder.builder("HP:0000790", "Hematuria")
                .onset(TimeElements.timestamp(ONSET_OF_COVID))
                .build();
        var renalFailureStage3 = PhenotypicFeatureBuilder.builder(stage3kidney)
                .onset(TimeElements.timestamp(ONSET_OF_COVID))
                .build();

        // He was tested for coronavirus disease 2019 (COVID-19), but he left against medical advice.
        // In the ensuing days, he continued to have fever, new onset myalgia, diarrhea, and dyspnea.
        TimeElement preHospitalisationDateRange =
                TimeElements.interval("2020-03-18T00:00:00Z", "2020-03-20T00:00:00Z");
        var myalgia = PhenotypicFeatureBuilder.builder("HP:0003326", "Myalgia")
                .onset(preHospitalisationDateRange)
                .build();
        var diarrhea = PhenotypicFeatureBuilder.builder("HP:0002014", "Diarrhea")
                .onset(preHospitalisationDateRange)
                .build();
        var dyspnea = PhenotypicFeatureBuilder.builder("HP:0002094", "Dyspnea")
                .onset(preHospitalisationDateRange)
                .build();

        var ARDS =
                PhenotypicFeatureBuilder.builder("HP:0033677", "Acute respiratory distress syndrome")
                        .onset(TimeElements.timestamp(RETURN_TO_HOSPITAL_TIME))
                        .build();

        List<PhenotypicFeature> features = new ArrayList<>();
        features.add(fever);
        features.add(flankPain);
        features.add(hematuria);
        features.add(renalFailureStage3);
        features.add(myalgia);
        features.add(diarrhea);
        features.add(dyspnea);
        features.add(ARDS);
        return features;
    }

    private List<Measurement> getAllMeasurements() {
        List<Measurement> measurements = new ArrayList<>();
        Value value = ValueBuilder.of(QuantityBuilder.of("NCIT:C67245", "Thousand Cells", 1.4));
        var assay = ontologyClass("LOINC:26474-7", "Lymphocytes [#/volume] in Blood");
        var initialBloodLymphocyteCount = MeasurementBuilder.builder(assay, value)
                .timeObserved(TimeElements.interval("2019-09-01T00:00:00Z", "2020-03-01T00:00:00Z"))
                .build();
        measurements.add(initialBloodLymphocyteCount);
        Value value2 = ValueBuilder.of(QuantityBuilder.of("NCIT:C67245", "Thousand Cells", 0.7));

        var hoD0bloodLymphocyteCount = MeasurementBuilder.builder(assay, value2)
                .timeObserved(TimeElements.timestamp(RETURN_TO_HOSPITAL_TIME))
                .build();
        measurements.add(hoD0bloodLymphocyteCount);
        return measurements;
    }

    private MedicalAction nasalOxygenAdministered() {
        Quantity twoLperMin = QuantityBuilder.of("NCIT:C67388", "Liter per Minute", 2);
        var interval1 = DoseIntervalBuilder.of(twoLperMin,
                CONTINUOUS,
                TimeIntervalBuilder.of("2021-02-01T18:58:43Z", "2021-02-02T08:22:42Z"));
        Quantity fiftyLperMin = QuantityBuilder.of("NCIT:C67388", "Liter per Minute", 50);
        var interval2 = DoseIntervalBuilder.of(fiftyLperMin,
                CONTINUOUS,
                TimeIntervalBuilder.of("2021-02-02T08:22:42Z", "2021-02-02T12:22:42Z"));
        Treatment nasalOxygen = TreatmentBuilder.builder("NCIT:C722", "Oxygen")
                .routeOfAdministration(ontologyClass("NCIT:C38284", "Nasal Route of Administration"))
                .addDoseInterval(interval1)
                .addDoseInterval(interval2)
                .build();
        return MedicalActionBuilder.treatment(nasalOxygen);
    }

    private MedicalAction lvadImplant() {
        Procedure proc = ProcedureBuilder.builder("NCIT:C80473", "Left Ventricular Assist Device")
                .performed(TimeElements.timestamp("2016-01-01T00:00:00Z")).build();
        return MedicalActionBuilder.procedure(proc);
    }

    private MedicalAction trachealIntubation() {
        Procedure intubation = ProcedureBuilder.builder("NCIT:C116648", "Tracheal Intubation")
                .performed(TimeElements.timestamp("2020-03-22T00:00:00Z")).build();
        return MedicalActionBuilder.procedure(intubation);
    }

    private MedicalAction peepOxygenAdministered() {
        Quantity quantity = QuantityBuilder.of("NCIT:C91060", "Centimeters of Water", 14);
        var doseInterval = DoseIntervalBuilder.of(quantity, CONTINUOUS, "2020-03-22", "2020-03-28");
        Treatment oxygen = TreatmentBuilder.builder(ontologyClass("NCIT:C722", "Oxygen"))
                .routeOfAdministration(ontologyClass("NCIT:C50254", "Positive end Expiratory Pressure Valve Device"))
                .addDoseInterval(doseInterval)
                .build();
        return MedicalActionBuilder.treatment(oxygen);
    }

    private MedicalAction tocilizumabAdministered() {
        Quantity quantity = QuantityBuilder.of("NCIT:C124458", "Milligram per Kilogram per Dose", 4);
        OntologyClass q4weeks = ontologyClass("NCIT:C64529", "Every Four Weeks");
        var doseInterval = DoseIntervalBuilder.of(quantity, q4weeks, "2020-03-24", "2020-03-28");
        var treatment = TreatmentBuilder.builder("NCIT:C84217", "Tocilizumab")
                .addDoseInterval(doseInterval)
                .build();
        return MedicalActionBuilder.treatment(treatment);
    }

    private MedicalAction dexamethasone() {
        // ten days, 6 mg once a day
        Quantity quantity = QuantityBuilder.of("UO:0000022", "milligram", 6);
        OntologyClass onceDaily = ontologyClass("NCIT:C125004", "Once Daily");
        var doseInterval = DoseIntervalBuilder.of(quantity, onceDaily, "2020-03-20", "2020-03-30");

        Treatment dexa = TreatmentBuilder.builder("CHEBI:41879", "dexamethasone")
                .addDoseInterval(doseInterval)
                .build();
        return MedicalActionBuilder.treatment(dexa);
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
