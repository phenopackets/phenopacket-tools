package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

public class CushingSyndromeWithMultifocalOsteonecrosis {


    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String INDIVIDUAL = "individual A";

    private final Phenopacket phenopacket;

    public CushingSyndromeWithMultifocalOsteonecrosis() {

        var externalRef = ExternalReferenceBuilder.builder()
                .id("DOI:10.1136/bcr-2019-233712")
                .builder("PMID:32467117")
                .description("Iatrogenic Cushing syndrome and multifocal osteonecrosis caused by the interaction between inhaled fluticasone and ritonavir")
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
                ageAtLastEncounter("P40Y").
                male().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addAllPhenotypicFeatures(getMedicalHistory())
                .addAllPhenotypicFeatures(getMedicalBackground())
                .addMedicalAction(existingTreatment())
                .addAllPhenotypicFeatures(getSymptomsOnPresentation())
                .addAllMeasurements(getMeasurementsOnPresentation())
                .addInterpretation(interpretation())
                .addMedicalAction(treatment())
                .build();
    }

    /**
     * Outcomes:
     *
     * 10 months after:
     *
     * - less ‘full moon’ facies
     * - no oedema
     * - less purpuric striae
     * - 6kg weight loss
     * - BMI of 33kg/m2
     * - improvement of erectile dysfunction
     * - Serum and urinary cortisol almost normal
     * - ACTH not suppressed
     */

    /**
     * Treatment:
     *
     * First 4 months:
     * - antihypertensive
     * - corticosteroids at a halved dose
     * - statin therapy
     *
     * At 7 months:
     * - fluticasone ceased
     * - indacaterol 150µg one time a day
     */
    private MedicalAction treatment() {
        return null;
    }

    /**
     * Labs:
     *
     * - Low serum and urinary cortisol
     * - low serum adrenocorticotrophic hormone (ACTH) concentrations
     * => Indicates: suppression of the HPA axis
     */
    private List<Measurement> getMeasurementsOnPresentation() {
        return null;
    }

    /**
     * Medical history:
     *
     * - well controlled HIV1 infection
     * - CD4+ T cell count of 518 cells/mm3
     * - undetectable HIV1 RNA
     *
     * At 36Y old:
     * - avascular necrosis of the right femoral head
     * - total hip arthroplasty
     */
    private List<PhenotypicFeature> getMedicalHistory() {
        return null;
    }

    /**
     * Medical background:
     *
     * - COPD
     * - alcohol & tobacco abuse
     * - intravenous heroin abuse
     * - HIV1 infection diagnosed at 25Y old
     */
    private List<PhenotypicFeature> getMedicalBackground() {
        return null;
    }

    /**
     * HIV Treatment background (started at 33Y old)
     *
     * - lopinavir/ritonavir 400mg/100mg two times per day
     * - saquinavir 1000mg two times per day
     * - tenofovir 300mg one time a day
     *
     * COPD Treatment background (started at 35Y old)
     *
     * - inhaled therapy with fluticasone/salmeterol 250µg/50µg two times per day
     * - tiotropium bromide 18µg one time a day
     */
    private MedicalAction existingTreatment() {
        return null;
    }

    /**
     * On presentation:
     *
     * - gradual increase in abdominal and cervical volume
     * - 15kg weight gain over the previous 6months
     * - abdominal and limb striae
     * - gynecomastia
     * - ankle oedema
     * - erectile dysfunction
     * - humoral lability
     */
    private List<PhenotypicFeature> getSymptomsOnPresentation() {
        return null;
    }


    /**
     * Diagnosis: exogenous/iatrogenic Cushing syndrome secondary to inhaled fluticasone
     */
    private Interpretation interpretation() {
        InterpretationBuilder ibuilder = InterpretationBuilder.solved("interpretation.id");
        DiagnosisBuilder dbuilder = DiagnosisBuilder.builder(ontologyClass("???", "Exogenous/iatrogenic Cushing syndrome"));
        ibuilder.diagnosis(dbuilder.build());
        return ibuilder.build();
    }
}
