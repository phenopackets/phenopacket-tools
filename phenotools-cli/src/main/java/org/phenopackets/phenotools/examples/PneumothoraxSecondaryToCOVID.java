package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.ArrayList;
import java.util.List;

public class PneumothoraxSecondaryToCOVID {

    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";

    // Organs

    private final Phenopacket phenopacket;

    public PneumothoraxSecondaryToCOVID() {

        //TODO: Fix ontology versions
        var metadata = MetaDataBuilder.builder("2022-04-21T10:35:00Z", "anonymous biocurator")
                .resource(Resources.ncitVersion("21.05d"))
                .resource(Resources.hpoVersion("2021-08-02"))
                .resource(Resources.efoVersion("3.34.0"))
                .resource(Resources.uberonVersion("2021-07-27"))
                .resource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();

        Individual proband = IndividualBuilder.builder(PROBAND_ID).
                ageAtLastEncounter("P36Y").
                male().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addAllPhenotypicFeatures(getMedicalHistory())
                .addAllPhenotypicFeatures(getLast3WeekHistory())
                .addAllPhenotypicFeatures(getSymptomsOnPresentation())
                .addAllMeasurements(getMeasurementsOnPresentation())
                .addAllPhenotypicFeatures(getChestRadiograph())
                .build();

    }

    /**
     * A 12-lead ECG showed a sinus tachycardia at 155 beats/min.
     *
     * The patient’s full blood count demonstrated a raised white cell count (13.64×109/L) with a neutrophilia (8.91×109/L), lymphocyte count (3.81×109/L), haemoglobin (146g/L), and platelets (1051×109/L); biochemical markers demonstrated a raised C-reactive protein (28.7mg/L) and alanine aminotransferase (107IU/L).
     * Diagnosis: tension pneumothorax, secondary to underlying COVID-19
     *
     * Treatment:
     * emergency needle decompression:
     * - 14-gauge cannula inserted in the second rib space and mid-clavicular line
     * - 12-French Seldinger chest drain inserted into the patients left axilla
     *
     * Outcome:
     * repeat chest radiograph => lung re-expansion
     * Within an hour the patient’s oxygen requirement reduced to 4L/min via nasal cannula
     * normalisation of respiratory and heart rate.
     */


    /**
     * Emergency portable chest radiograph
     * - large left-sided pneumothorax with mediastinal shift
     * - radiological signs of tension
     * - right lung compressed BUT widespread patchy consolidative changes
     */

    private List<PhenotypicFeature> getChestRadiograph() {
        // How to associate phenotypes with procedures?

        return new ArrayList<>();
    }

    /**
     * On presentation:                                         << How to specify the temporal element?
     * - SpO2 of 88% on 15L/min oxygen via non-rebreathe mask
     * - respiratory rate of 50 breaths per minute
     * - heart rate of 150 beats/min
     * - blood pressure of 110/65 mm Hg
     */
    private List<Measurement> getMeasurementsOnPresentation() {
        OntologyClass respiratoryRate = OntologyClassBuilder.ontologyClass("LOINC:9279-1", "Respiratory rate");

        // Couldn't find 'breaths per minute' as a measurement unit and UCUM has not been published.
        // This applies to all measurements below
        Value respiratoryRateValue = ValueBuilder.value("UCUM:{breaths}/min", "Breaths/minute", 50);

        // Time observed: on presentation? This applies to all measurements below
        Measurement respiratoryRateMeasurement = MeasurementBuilder.
                value(respiratoryRate, respiratoryRateValue).
                build();

        OntologyClass heartRate = OntologyClassBuilder.ontologyClass("LOINC:8867-4", "Heart rate");
        Value heartRateValue = ValueBuilder.value("UCUM:{beats}/min", "Beats/minute", 150);
        Measurement heartRateMeasurement = MeasurementBuilder.
                value(heartRate, heartRateValue).
                build();

        OntologyClass systBloodPressure = OntologyClassBuilder.ontologyClass("LOINC:8480-6", "Systolic blood pressure");
        Value systBloodPressureValue = ValueBuilder.value("UCUM:mm[Hg]", "mm[Hg]", 110);
        Measurement systBloodPressureMeasurement = MeasurementBuilder.
                value(systBloodPressure, systBloodPressureValue).
                build();

        OntologyClass diastBloodPressure = OntologyClassBuilder.ontologyClass("LOINC:8480-6", "Systolic blood pressure");
        Value diastBloodPressureValue = ValueBuilder.value("UCUM:mm[Hg]", "mm[Hg]", 65);
        Measurement diastBloodPressureMeasurement = MeasurementBuilder.
                value(diastBloodPressure, diastBloodPressureValue).
                build();


        // How to define the 'intervention' component SpO2 of 88%  << on 15L/min oxygen via non-rebreathe mask >>
        // It's not a procedure

        OntologyClass spO2 = OntologyClassBuilder.ontologyClass("LOINC:20564-1", "Oxygen saturation in Blood");
        Value spO2Value = ValueBuilder.value("UCUM:%", "%", 88);
        Measurement spO2Measurement = MeasurementBuilder.
                value(spO2, spO2Value).
                build();


        return List.of(respiratoryRateMeasurement,
                heartRateMeasurement,
                systBloodPressureMeasurement,
                diastBloodPressureMeasurement,
                spO2Measurement);
    }

    /**
     * On presentation:                                     << How to represent this from a temporal perspective?
     * - hypoxaemic
     * - tachycardic
     * - left-sided pleuritic chest pain
     * - trachea remained central
     * <p>
     * On ascultation:
     * - no audible breath sounds of the left hemithorax
     * - reduced vocal fremitus
     * - asymmetrical chest expansion
     */
    private List<PhenotypicFeature> getSymptomsOnPresentation() {
        PhenotypicFeature hypoxemia = PhenotypicFeatureBuilder.
                builder("HP:0012418", "Hypoxemia").
                build();

        PhenotypicFeature tachycardia = PhenotypicFeatureBuilder.
                builder("HP:0001649", "Tachycardia").
                build();

        PhenotypicFeature chestPain = PhenotypicFeatureBuilder.
                builder("HP:0033771", "Pleuritic chest pain").
                modifier(Laterality.left()).
                build();

        PhenotypicFeature trachea = PhenotypicFeatureBuilder.
                builder("HP:0002778", "Abnormal tracheal morphology").
                excluded().
                build();

        // How to combine a procedure < ascultation > with phenotypic features?

        return List.of(hypoxemia, tachycardia, chestPain, trachea);
    }

    /**
     * medical history:
     * - Childhood asthma
     * - 10 pack-year history of smoking    << How to represent this !?
     * - NO history of trauma               << Did I represent correct the absence of the phenotype?
     * - NO history of pneumothoraces
     */
    private List<PhenotypicFeature> getMedicalHistory() {
        PhenotypicFeature asthma = PhenotypicFeatureBuilder.
                builder("HP:0002099", "Asthma").
                childhoodOnset().
                build();
        PhenotypicFeature trauma = PhenotypicFeatureBuilder.
                builder("SCTID:417746004", "Trauma").
                excluded().
                build();
        PhenotypicFeature pneumothorax = PhenotypicFeatureBuilder.
                builder("HP:0002107", "Pneumothorax").
                excluded().
                build();

        return List.of(asthma, trauma, pneumothorax);
    }

    /**
     * 3-week history: << How to represent relative time periods?
     * - cough
     * - fevers
     * - shortness of breath
     */
    private List<PhenotypicFeature> getLast3WeekHistory() {
        PhenotypicFeature cough = PhenotypicFeatureBuilder.
                builder("HP:0012735", "Cough").
                build();

        PhenotypicFeature fever = PhenotypicFeatureBuilder.
                builder("HP:0001945", "Fever").
                build();

        PhenotypicFeature dyspnea = PhenotypicFeatureBuilder.
                builder("HP:0002094", "Dyspnea").
                build();

        return List.of(cough, fever, dyspnea);
    }

}
