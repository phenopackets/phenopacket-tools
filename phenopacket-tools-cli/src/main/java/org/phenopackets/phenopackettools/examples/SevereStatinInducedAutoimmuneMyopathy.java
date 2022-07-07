package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;


public class SevereStatinInducedAutoimmuneMyopathy {


    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String INDIVIDUAL = "individual A";

    private final Phenopacket phenopacket;

    public SevereStatinInducedAutoimmuneMyopathy() {

        var externalRef = ExternalReferenceBuilder.reference()
                .id("DOI:10.1136/bcr-2020-234805")
               // .builder("PMID:32444443")
                .description("Severe statin-induced autoimmune myopathy successfully treated with intravenous immunoglobulin")
                .build();

        //TODO: Fix ontology versions
        var metadata = MetaDataBuilder.builder("2022-04-21T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.hpoVersion("2021-08-02"))
                .addResource(Resources.efoVersion("3.34.0"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addResource(Resources.ncbiTaxonVersion("2021-06-10"))
                .addExternalReference(externalRef)
                .build();

        Individual proband = IndividualBuilder.builder(INDIVIDUAL).
                ageAtLastEncounter("P65Y").
                male().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addAllPhenotypicFeatures(getMedicalHistory())
                .addMedicalAction(existingTreatment())
                .addAllPhenotypicFeatures(getLastMonthHistory())
                .addAllPhenotypicFeatures(getSymptomsOnPresentation())
                .addAllMeasurements(getLabsOnPresentation())
                .addAllPhenotypicFeatures(getEMG())
                .addInterpretation(interpretation())
                .addMedicalAction(treatment())
                .build();
    }

    /**
     * Outcome:
     *
     * 7weeks after the fourth round of IVIg, the patient nearly regained full strength in his legs and CK did not show any further increase.
     */

    /**
     * Atorvastatin was stopped
     *  muscle weakness deteriorated in his legs
     *  spread to his arms making immune-modulating treatment necessary
     *  155 g IVIg, equivalent to 1.6 g/kg of body weight, was administered over 3 days
     *  55 g on the first day and 50 g on the second and third day
     *  developed a headache after first dose
     *  lack of significant improvement of the CK
     *  treatment was continued every 6 weeks slightly reducing the dose to 150 g
     *  After the third course of treatment, the CK in serum drastically fell to a mildly elevated level
     *  Following the fourth administration, CK was stable around 500 U/L and weakness in his limbs had been greatly improved.
     */
    private MedicalAction treatment() {
        return null;
    }

    /**
     * active denervation
     * and fasciculation potentials along with chronic denervation/re-innervation motor unit patterns
     * from the craniobulbar, thoracic, paraspinal as well as upper and lower limb muscles.
     * @return
     */
    private List<PhenotypicFeature> getEMG() {
        return null;
    }

    /**
     * creatine kinase (CK) level was raised up to 4292 U/L
     * alanine transaminase (ALT) was raised to 234 U/L
     * Alkaline phosphatase and bilirubin were normal
     * Autoantibodies against HMG-CoA reductase were positive.
     * Haemoglobin A1C was 51 mmol/mol.
     * Full blood count, thyroid function tests, renal profile, vitamin D and B12 were all normal.
     */
    private List<Measurement> getLabsOnPresentation() {
        return null;
    }

    /**
     * On examination:
     *
     * fasciculations and wasting were noticed in both quadriceps muscles
     * muscular tone was normal
     * proximal weakness in both legs
     * Trendelenburg's and Gower's signs were positive
     * knee jerks were brisk
     * Sensation was intact
     * loss of vibration sense up to the tibial plateau bilaterally
     * Examination of the cranial nerves and the upper limbs was unremarkable.
     */
    private List<PhenotypicFeature> getSymptomsOnPresentation() {
        return null;
    }

    /**
     * Last month history:
     *
     * - weakness started about 2 months ago; got progressively worse
     * difficulties climbing stairs
     * getting up from the squatting position
     *  walking or even putting on his trousers
     *  started using a wheelchair
     *  denied having any associated pain, and upper limbs as well as speech and swallowing were uninvolved.
     *
     * longstanding mild sensory symptoms in both feet
     * lost around 6 lb (about 2.72 kg) in weight in the previous month
     */
    private List<PhenotypicFeature> getLastMonthHistory() {
        return null;
    }

    /**
     * Medical history:
     *
     * - hypertension diagnosed 10 years back
     * - type 2 diabetes mellitus diagnosed 10 years back
     */
    private List<PhenotypicFeature> getMedicalHistory() {
        return null;
    }

    /**
     * HIV Treatment background (started at 33Y old)
     *
     * - atorvastatin 10 mg and aspirin 75 mg alongside antihypertensive (ramipril and amlodipine) and anti-diabetic (metformin, dapagliflozin, sitagliptin and gliclazide) medication
     */
    private MedicalAction existingTreatment() {
        return null;
    }


    /**
     * Diagnosis: statin-associated autoimmune myopathy
     */
    private Interpretation interpretation() {
        InterpretationBuilder ibuilder = InterpretationBuilder.builder("interpretation.id");
        DiagnosisBuilder dbuilder = DiagnosisBuilder.builder(ontologyClass("????", "Statin-associated autoimmune myopathy"));
       // ibuilder.(dbuilder.build());
        return null; //ibuilder.
    }
}
