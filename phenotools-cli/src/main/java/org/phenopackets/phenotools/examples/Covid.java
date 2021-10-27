package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.ArrayList;
import java.util.List;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

class Covid implements PhenopacketExample {

    private static final String ONSET_OF_COVID = "2021-02-01T05:00:00Z";

    private static final String RETURN_TO_HOSPITAL_TIME = "2020-03-20";

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

    private static final Evidence authorAssertion = EvidenceBuilder.create("PMID: 32292915", "The Imperfect Cytokine Storm: Severe COVID-19 With ARDS in a Patient on Durable LVAD Support").build();

    private final Phenopacket phenopacket;


    Covid() {
        Individual patient = IndividualBuilder.create("P123542")
                .male()
                .ageAtLastEncounter("P70Y")
                .vitalStatus(VitalStatusBuilder.deceased().causeOfDeath("MONDO:0100096", "COVID-19").build())
                .build();

        Disease cardiomyopathy = DiseaseBuilder.create("MONDO:0004994", "cardiomyopathy").build();
        Disease covid = DiseaseBuilder
                .create("MONDO:0100096", "COVID-19")
                .onset(TimeElements.timestamp("2020-03-17"))
                .build();
        var bloodGroupA = PhenotypicFeatureBuilder.create("HP:0032370", "Blood group A");
        var rhesusPositive = PhenotypicFeatureBuilder.create("NCIT:C76251", "Rh Positive Blood Group");
        var obesityPhenotype = PhenotypicFeatureBuilder.create(obesity).build();
        var externalRef = ExternalReferenceBuilder.create()
                .id("DOI:10.1016/j.jaccas.2020.04.001")
                .reference("PMID:32292915")
                .description("The Imperfect Cytokine Storm: Severe COVID-19 With ARDS in a Patient on Durable LVAD Support")
                .build();
        var metaData = MetaDataBuilder.create("2021-08-17", "anonymous biocurator")
                .resource(Resources.ncitVersion("2019-11-26"))
                .resource(Resources.mondoVersion("2021-11-26"))
                .externalReference(externalRef)
                .build();


        phenopacket = PhenopacketBuilder.create("arbitrary.phenopacket.id", metaData)
                .individual(patient)
                .allPhenotypicFeatures(getAllPhenotypicFeatures())
                .allMeasurements(getAllMeasurements())
                // .addMeasurements(initialBloodLymphocyteCount)
                // .addMeasurements(hoD0bloodLymphocyteCount)
                .medicalAction(lvadImplant())
                .medicalAction(nasalOxygenAdministered())
                .medicalAction(dexamethasone())
                .medicalAction(trachealIntubation())
                .medicalAction(peepOxygenAdministered())
                .medicalAction(tocilizumabAdministered())
                .disease(notDiabetesMellitus)
                .disease(cardiomyopathy)
                .disease(covid)
                .build();

    }

    private List<PhenotypicFeature> getAllPhenotypicFeatures() {
        // 70-year-old male with a destination therapy HeartMate 3 (Abbott Laboratory, Lake Bluff, Illinois)
        // left ventricular assist device (LVAD) implanted in 2016 who developed fever, flank pain, and hematuria 3 days
        // after attending a party
        // His blood group was type A positive.

        var fever = PhenotypicFeatureBuilder.create("HP:0001945", "Fever ")
                .onset(TimeElements.timestamp(ONSET_OF_COVID))
                .build();
        var flankPain = PhenotypicFeatureBuilder.create("HP:0030157", "Flank pain")
                .onset(TimeElements.timestamp(ONSET_OF_COVID))
                .build();
        var hematuria = PhenotypicFeatureBuilder.create("HP:0000790", "Hematuria")
                .onset(TimeElements.timestamp(ONSET_OF_COVID))
                .build();
        var renalFailureStage3 = PhenotypicFeatureBuilder.create(stage3kidney)
                .onset(TimeElements.timestamp(ONSET_OF_COVID))
                .build();

        // He was tested for coronavirus disease 2019 (COVID-19), but he left against medical advice.
        // In the ensuing days, he continued to have fever, new onset myalgia, diarrhea, and dyspnea.
        TimeElement preHospitalisationDateRange =
                TimeElements.interval("2020-03-18", "2020-03-20");
        var myalgia = PhenotypicFeatureBuilder.create("HP:0003326", "Myalgia")
                .onset(preHospitalisationDateRange)
                .build();
        var diarrhea = PhenotypicFeatureBuilder.create("HP:0002014", "Diarrhea")
                .onset(preHospitalisationDateRange)
                .build();
        var dyspnea = PhenotypicFeatureBuilder.create("HP:0002094", "Dyspnea")
                .onset(preHospitalisationDateRange)
                .build();

        var ARDS =
                PhenotypicFeatureBuilder.create("HP:0033677", "Acute respiratory distress syndrome")
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
        Value value = ValueBuilder.create(QuantityBuilder.create("NCIT:C67245", "Thousand Cells", 1.4).build()).build();
        var assay = ontologyClass("LOINC:26474-7", "Lymphocytes [#/volume] in Blood");
        var initialBloodLymphocyteCount = MeasurementBuilder.value(assay, value)
                .timeObserved(TimeElements.interval("2019-09-01", "2020-03-01"))
                .build();
        measurements.add(initialBloodLymphocyteCount);
        Value value2 = ValueBuilder.create(QuantityBuilder.create("NCIT:C67245", "Thousand Cells", 0.7).build()).build();

        var hoD0bloodLymphocyteCount = MeasurementBuilder.value(assay, value2)
                .timeObserved(TimeElements.timestamp(RETURN_TO_HOSPITAL_TIME))
                .build();
        measurements.add(hoD0bloodLymphocyteCount);
        return measurements;
    }

    private MedicalAction nasalOxygenAdministered() {
        Quantity twoLperMin = QuantityBuilder.create("NCIT:C67388", "Liter per Minute", 2).build();
        var interval1 = DoseIntervalCreator.create(twoLperMin,
                CONTINUOUS,
                TimeIntervalCreator.create("2021-02-01T18:58:43Z", "2021-02-02T08:22:42Z"));
        Quantity fiftyLperMin = QuantityBuilder.create("NCIT:C67388", "Liter per Minute", 50).build();
        var interval2 = DoseIntervalCreator.create(fiftyLperMin,
                CONTINUOUS,
                TimeIntervalCreator.create("2021-02-02T08:22:42Z", "2021-02-02T12:22:42Z"));
        Treatment nasalOxygen = TreatmentBuilder.create("NCIT:C722", "Oxygen")
                .routeOfAdministration(ontologyClass("NCIT:C38284", "Nasal Route of Administration"))
                .doseInterval(interval1)
                .doseInterval(interval2)
                .build();
        return MedicalActionBuilder.treatment(nasalOxygen).build();
    }

    private MedicalAction lvadImplant() {
        Procedure proc = ProcedureBuilder.create("NCIT:C80473", "Left Ventricular Assist Device")
                .performed(TimeElements.timestamp("2016-01-01")).build();
        return MedicalActionBuilder.procedure(proc).build();
    }

    private MedicalAction trachealIntubation() {
        Procedure intubation = ProcedureBuilder.create("NCIT:C116648", "Tracheal Intubation")
                .performed(TimeElements.timestamp("2020-03-22")).build();
        return MedicalActionBuilder.procedure(intubation).build();
    }

    private MedicalAction peepOxygenAdministered() {
        Quantity quantity = QuantityBuilder.create("NCIT:C91060", "Centimeters of Water", 14).build();
        var doseInterval = DoseIntervalCreator.create(quantity, CONTINUOUS, "2020-03-22", "2020-03-28");
        Treatment oxygen = TreatmentBuilder.create(ontologyClass("NCIT:C722", "Oxygen"))
                .routeOfAdministration(ontologyClass("NCIT:C50254", "Positive end Expiratory Pressure Valve Device"))
                .doseInterval(doseInterval)
                .build();
        return MedicalActionBuilder.treatment(oxygen).build();
    }

    private MedicalAction tocilizumabAdministered() {
        Quantity quantity = QuantityBuilder.create("NCIT:C124458", "Milligram per Kilogram per Dose", 4).build();
        OntologyClass q4weeks = ontologyClass("NCIT:C64529", "Every Four Weeks");
        var doseInterval = DoseIntervalCreator.create(quantity, q4weeks, "2020-03-24", "2020-03-28");
        var treatment = TreatmentBuilder.create("NCIT:C84217", "Tocilizumab")
                .doseInterval(doseInterval)
                .build();
        return MedicalActionBuilder.treatment(treatment).build();
    }

    private MedicalAction dexamethasone() {
        // ten days, 6 mg once a day
        Quantity quantity = QuantityBuilder.create("UO:0000022", "milligram", 6).build();
        OntologyClass onceDaily = ontologyClass("NCIT:C125004", "Once Daily");
        var doseInterval = DoseIntervalCreator.create(quantity, onceDaily, "2020-03-20", "2020-03-30");

        Treatment dexa = TreatmentBuilder.create("CHEBI:41879", "dexamethasone")
                .doseInterval(doseInterval)
                .build();
        return MedicalActionBuilder.treatment(dexa).build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
