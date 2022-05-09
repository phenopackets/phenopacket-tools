package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

public class PneumothoraxSecondaryToCOVID {

    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String INDIVIDUAL = "individual A";

    private final Phenopacket phenopacket;

    public PneumothoraxSecondaryToCOVID() {

        var externalRef = ExternalReferenceBuilder.builder()
                .id("DOI:10.1136/bcr-2020-235861")
                .builder("PMID:32423911")
                .description("Tension pneumothorax in a patient with COVID-19")
                .build();

        //TODO: Fix ontology versions
        var metadata = MetaDataBuilder.builder("2022-04-21T10:35:00Z", "anonymous biocurator")
                .resource(Resources.ncitVersion("21.05d"))
                .resource(Resources.hpoVersion("2021-08-02"))
                .resource(Resources.efoVersion("3.34.0"))
                .resource(Resources.uberonVersion("2021-07-27"))
                .resource(Resources.ncbiTaxonVersion("2021-06-10"))
                .externalReference(externalRef)
                .build();

        Individual proband = IndividualBuilder.builder(INDIVIDUAL).
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
                .addInterpretation(interpretation())
                .addMedicalAction(decompression())
                .addMedicalAction(chestDrain())
                .build();
    }

    /**
     * TODO: How to represent outcomes?
     *
     * Outcome:
     * - Repeat chest radiograph => lung re-expansion
     * - Oxygen requirement reduced to 4L/min via nasal cannula
     * - normalisation of respiratory and heart rate
     */

    /**
     * TODO: Anatomical site improperly represented
     * TODO: Intent should be: lung decompression not 'Cure'
     * <p>
     * Intervention: 14-gauge cannula inserted in the second rib space and mid-clavicular line
     */
    private MedicalAction decompression() {
        ProcedureBuilder builder = ProcedureBuilder.builder("SCTID:42825003", "Cannulation (procedure)");
        builder.bodySite(ontologyClass("???", "space between second rib and mid-clavicular line"));
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(ontologyClass("SCTID:233645004", "Tension pneumothorax (disorder)"))
                .treatmentIntent(ontologyClass("NCIT:C62220", "Cure"));
        return mabuilder.build();
    }

    /**
     * Intervention: 12-French Seldinger chest drain inserted into the patients left axilla
     */
    private MedicalAction chestDrain() {
        ProcedureBuilder builder = ProcedureBuilder.builder("SCTID:264957007", "Insertion of pleural tube drain (procedure)");
        builder.bodySite(ontologyClass("FMA:45303", "Left axilla"));
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(ontologyClass("SCTID:233645004", "Tension pneumothorax (disorder)"))
                .treatmentIntent(ontologyClass("NCIT:C62220", "Cure"));
        return mabuilder.build();
    }

    /**
     * Diagnosis: tension pneumothorax, secondary to underlying COVID-19
     */
    private Interpretation interpretation() {
        InterpretationBuilder ibuilder = InterpretationBuilder.solved("interpretation.id");
        DiagnosisBuilder dbuilder = DiagnosisBuilder.builder(ontologyClass("SCTID:233645004", "Tension pneumothorax (disorder)"));
        ibuilder.diagnosis(dbuilder.build());
        return ibuilder.build();
    }

    /**
     * TODO: How to represent co-occurring phenotypes: pneumothorax WITH mediastinal shift?
     * TODO: How to associate these phenotypes with the procedure SCTID:399208008 'Plain chest X-ray'?
     * <p>
     * Emergency portable chest radiograph
     * - large left-sided pneumothorax with mediastinal shift
     * - radiological signs of tension
     * - right lung compressed BUT widespread patchy consolidative changes
     */

    private List<PhenotypicFeature> getChestRadiograph() {
        PhenotypicFeature pneumothorax = PhenotypicFeatureBuilder
                .builder("HP:0002107", "Pneumothorax")
                .modifier(OntologyClassBuilder.ontologyClass("PATO:0000600", "increased width"))
                .modifier(Laterality.left())
                .build();

        PhenotypicFeature rightLungNormal = PhenotypicFeatureBuilder
                .builder("HP:0031983", "Abnormal pulmonary thoracic imaging finding")
                .modifier(Laterality.right())
                .excluded()
                .build();

        PhenotypicFeature rightLungConsolidated = PhenotypicFeatureBuilder
                .builder("SCTID:95436008", "Lung consolidation (disorder)")     // Probably incorrect
                .modifier(Laterality.right())
                .modifier(OntologyClassBuilder.ontologyClass("PATO:0001630", "dispersed"))
                .modifier(OntologyClassBuilder.ontologyClass("PATO:0001608", "patchy"))
                .build();

        return List.of(pneumothorax, rightLungNormal, rightLungConsolidated);
    }

    /**
     * TODO: How to associate these phenotypes with the temporal aspect 'on presentation'?
     * TODO: Fix measurement units
     * <p>
     * On presentation:
     * - SpO2 of 88% on 15L/min oxygen via non-rebreather mask
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


        /**
         * TODO: How to associate SpO2 of 88% on 15L/min oxygen with the 'intervention' 'non rebreather mask oxygen delivery'?
         */

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
     * TODO: How to associate these phenotypes with the temporal aspect 'on presentation'?
     * <p>
     * On presentation:
     * - hypoxaemic
     * - tachycardic
     * - left-sided pleuritic chest pain
     * - trachea remained central
     * <p>
     * On ascultation:
     * - NO audible breath sounds of the left hemithorax                << Not sure how to code this
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


        /**
         * TODO: How to associate these phenotypes with the procedure SCTID:37931006 'Auscultation'?
         */
        PhenotypicFeature vocalFremitus = PhenotypicFeatureBuilder.
                builder("SCTID:301278006", "Vocal fremitus decreased").
                build();

        PhenotypicFeature asymmetricalChestExpansion = PhenotypicFeatureBuilder.
                builder("SCTID:271623001", "Chest movement unequal").
                build();

        return List.of(hypoxemia, tachycardia, chestPain, trachea, vocalFremitus, asymmetricalChestExpansion);
    }

    /**
     * medical history:
     * - Childhood asthma
     * - 10 pack-year history of smoking    << How to represent this !?
     * - NO history of trauma
     * - NO history of pneumothoraces
     */
    private List<PhenotypicFeature> getMedicalHistory() {
        PhenotypicFeature asthma = PhenotypicFeatureBuilder.
                builder("HP:0002099", "Asthma").
                childhoodOnset().
                build();

        /**
         * TODO: How to represent the difference between no HISTORY vs no PRESENT observation for a phenotype?
         */
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
     * TODO: How to represent relative time periods? (e.g., 3 weeks prior to diagnosis)
     * <p>
     * 3-week history:
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
