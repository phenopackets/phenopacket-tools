package org.phenopackets.phenopackettools.cli.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;
import static org.phenopackets.phenopackettools.builder.constants.MedicalActions.*;
import static org.phenopackets.phenopackettools.builder.constants.Response.favorableResponse;
import static org.phenopackets.phenopackettools.builder.constants.Unit.*;

/**
 * Phenopacket based on the case report in
 * Güngör C, Wieshmann UC.
 * Severe statin-induced autoimmune myopathy successfully treated with intravenous immunoglobulin.
 * BMJ Case Rep. 2020 May 21;13(5):e234805.
 * PMID: 32444443; PMCID: PMC7247403.
 */
public class SevereStatinInducedAutoimmuneMyopathy implements PhenopacketExample{


    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String INDIVIDUAL = "65-year-old man";

    private final Phenopacket phenopacket;

    public SevereStatinInducedAutoimmuneMyopathy() {
        var externalRef = ExternalReferenceBuilder.of("PMID:32444443",
                        "Severe statin-induced autoimmune myopathy successfully treated with intravenous immunoglobulin");

        var metadata = MetaDataBuilder.builder("2022-04-21T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.hpoVersion("2022-06-11"))
                .addResource(Resources.loincVersion("2.73"))
                .addResource(Resources.ucum())
                .addResource(Resources.mondoVersion("2022-04-04"))
                .addResource(Resources.drugCentralVersion("2022-08-22"))
                .addExternalReference(externalRef)
                .build();

        Individual proband = IndividualBuilder.builder(INDIVIDUAL).
                ageAtLastEncounter("P65Y").
                male().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addDiseases(getDiseases())
                .addMedicalActions(previousTreatments())
                .addPhenotypicFeatures(getSymptomsOnPresentation())
                .addMeasurements(getLabsOnPresentation())
               .addMedicalAction(treatment())
                .build();
    }


    /**
     * Returns the disease diagnoses that form the past medical history of our patient -- type 2 Disbetes mellitus and
     * hypertension.
     */
    List<Disease> getDiseases() {
        var age55y = TimeElements.age("P55Y");
        var niddm = DiseaseBuilder.builder("MONDO:0005148", "type 2 diabetes mellitus").onset(age55y).build();
        var htn = DiseaseBuilder.builder("MONDO:0001134", "essential hypertension").onset(age55y).build();

        return List.of(niddm, htn);
    }

    List<MedicalAction> previousTreatments() {
        TimeInterval interval = TimeIntervalBuilder.of("2010-09-02", "2020-09-02");
        var atorvastatin = ontologyClass("DrugCentral:257","atorvastatin");
        var tenMg = QuantityBuilder.builder(milligram(), 10).build();
        var atorvastatinAction = MedicalActionBuilder
                .oralAdministration(atorvastatin, tenMg, onceDaily(), interval)
                .addAdverseEvent(ontologyClass("HP:0003198","Myopathy"))
                .treatmentTerminationReason(adverseEvent())
                .build();
        var aspirin = ontologyClass("DrugCentral:74","acetylsalicylic acid");
        var seventyFiveMg = QuantityBuilder.builder(milligram(), 75).build();
        var aspirinAction = MedicalActionBuilder
                .oralAdministration(aspirin, seventyFiveMg, onceDaily(), interval)
                .build();
        var ramipril = ontologyClass("DrugCentral:2356", "ramipril");
        var ramiprilAction = MedicalActionBuilder
                .oralAdministration(ramipril, tenMg, onceDaily(), interval)
                .build();
        var metformin = ontologyClass( "DrugCentral:1725", "metformin");
        var fiveHundredMg = QuantityBuilder.builder(milligram(), 500).build();
        var metforminAction = MedicalActionBuilder
                .oralAdministration(metformin, fiveHundredMg, threeTimesDaily(), interval)
                .build();
        return List.of(atorvastatinAction, aspirinAction, ramiprilAction, metforminAction);
    }


    /**
     * Atorvastatin was stopped muscle weakness deteriorated in his legs
     *  spread to his arms making immune-modulating treatment necessary
     *  155 g IVIg, equivalent to 1.6 g/kg of body weight, was administered over 3 days
     *  55 g on the first day and 50 g on the second and third day
     *  developed a headache after first dose
     *  lack of significant improvement of the CK
     *  treatment was continued every 6 weeks slightly reducing the dose to 150 g
     *  After the third course of treatment, the CK in serum drastically fell to a mildly elevated level
     *  Following the fourth administration, CK was stable around 500 U/L and weakness in his limbs had been greatly improved.
     */
    private MedicalAction treatment() {
        var ivIg = ontologyClass("NCIT:C121331", "Intravenous Immunoglobulin Therapy");
        var everySixWeeks = ontologyClass("NCIT:C89788", "Every Six Weeks");
        var quantity = QuantityBuilder.builder(gramPerKilogram(), 1.6).build();
        TimeInterval interval = TimeIntervalBuilder.of("2020-09-02", "2021-03-02");
        return MedicalActionBuilder
                .intravenousAdministration(ivIg, quantity, everySixWeeks, interval)
                .responseToTreatment(favorableResponse())
                .build();
    }

    /**
     * creatine kinase (CK) level was raised up to 4292 U/L
     * alanine transaminase (ALT) was raised to 234 U/L
     * Alkaline phosphatase and bilirubin were normal
     * Autoantibodies against HMG-CoA reductase were positive.
     * Haemoglobin A1C was 51 mmol/mol.
     * Full blood count, thyroid function tests, renal profile, vitamin D and B12 were all normal.
     */
    private List<Measurement> getLabsOnPresentation() {
       var ckTest = ontologyClass("LOINC:2157-6",
               "Creatine kinase [Enzymatic activity/volume] in Serum or Plasma");
       var ckValue = ValueBuilder.of(enzymeUnitPerLiter(), 4292, 20, 200);
       Measurement ckMeasurement = MeasurementBuilder.builder(ckTest, ckValue).build();

       var altTest = ontologyClass("LOINC:1742-6",
               "Alanine aminotransferase [Enzymatic activity/volume] in Serum or Plasma");
       var altValue = ValueBuilder.of(enzymeUnitPerLiter(), 234, 4, 36);
        Measurement altMeasurement = MeasurementBuilder.builder(altTest, altValue).build();
        return List.of(ckMeasurement, altMeasurement);
    }


    /**
     * On examination fasciculations and wasting were noticed in both quadriceps muscles.
     * The muscular tone was normal. There was proximal weakness in both legs.
     * Trendelenburg's and Gower's signs were positive. The knee jerks were brisk.
     * Sensation was intact except loss of vibration sense up to the tibial plateau
     * bilaterally which had been known to the patient for many years.
     * Examination of the cranial nerves and the upper limbs was unremarkable.
     */
    private List<PhenotypicFeature> getSymptomsOnPresentation() {
        var age65y = TimeElements.age("P65Y");
        var proxLegWeakness = PhenotypicFeatureBuilder.builder("HP:0008994",
                "Proximal muscle weakness in lower limbs").onset(age65y).build();
        var fasciculations = PhenotypicFeatureBuilder
                .builder("HP:0007289", "Limb fasciculations").onset(age65y).build();
        var amyotrophy = PhenotypicFeatureBuilder.
        builder("HP:0008956", "Proximal lower limb amyotrophy").onset(age65y).build();
        var notAbnormalMuscleTone = PhenotypicFeatureBuilder
                .builder("HP:0003808", "Abnormal muscle tone")
                .onset(age65y)
                .excluded()
                .build();
        var gower = PhenotypicFeatureBuilder.builder("HP:0003391", "Gowers sign")
                .onset(age65y)
                .build();
        var stairs = PhenotypicFeatureBuilder
                .builder("HP:0003551", "Difficulty climbing stairs")
                .onset(age65y)
                .build();
        var vibration = PhenotypicFeatureBuilder
                .builder("HP:0006886", "Impaired distal vibration sensation")
                .onset(age65y)
                .build();
        return List.of(proxLegWeakness,
                fasciculations,
                amyotrophy,
                notAbnormalMuscleTone,
                gower,
                stairs,
                vibration);
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
