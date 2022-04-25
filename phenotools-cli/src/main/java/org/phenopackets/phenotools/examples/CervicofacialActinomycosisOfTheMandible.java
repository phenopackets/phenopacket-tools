package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.Interpretation;
import org.phenopackets.schema.v2.core.MedicalAction;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.List;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

public class CervicofacialActinomycosisOfTheMandible {


    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String INDIVIDUAL = "individual A";

    private final Phenopacket phenopacket;

    public CervicofacialActinomycosisOfTheMandible() {

        var externalRef = ExternalReferenceBuilder.builder()
                .id("DOI:10.1136/bcr-2019-233681")
                .builder("PMID:32467116")
                .description("Cervicofacial actinomycosis of the mandible in a paediatric patient")
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
                ageAtLastEncounter("P10Y").
                female().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addAllPhenotypicFeatures(getMedicalHistory())
                .addAllPhenotypicFeatures(getLast8Monthsistory())
                .addMedicalAction(neckCT())
                .addMedicalAction(mri())
                .addPhenotypicFeature(examination())
                .addInterpretation(interpretation())
                .addMedicalAction(biopsy())
                .addMedicalAction(frozenSection())
                .addMedicalAction(tissueCultures())
                .addMedicalAction(anaerobicCultures())
                .addMedicalAction(treatment())
                .build();
    }

    /**
     * - Expanded medullary spaces with oedema and vascular proliferation
     * - No cytological atypia
     * - negative for D2-40 and PROX1
     */
    private MedicalAction frozenSection() {
        return null;
    }

    /**
     * Negative for fungal and acid-fast bacilli
     */
    private MedicalAction tissueCultures() {
        return null;
    }

    /**
     * Anaerobic cultures: Actinomyces odontolyticus
     */
    private MedicalAction anaerobicCultures() {
        return null;
    }

    /**
     * - Incisional biopsy culture under general anaesthetic
     * - Subperiosteal dissection exposing the ascending ramus
     * - No granules encountered
     */
    private MedicalAction biopsy() {
        return null;
    }

    /**
     * - clinically healthy periodontium with mild tenderness on palpation
     */
    private PhenotypicFeature examination() {
        return null;
    }

    /**
     * Contrast-enhanced MRI
     *
     * - hyperintense T2 signal abnormality in the left mandibular bone marrow with periosteal thickening
     * - no drainable fluid collection
     */
    private MedicalAction mri() {
        return null;
    }

    /**
     * Contrast-enhanced neck CT
     * - bony expansion of the left mandible
     * - overlying soft tissue swelling
     */
    private MedicalAction neckCT() {
        return null;
    }

    /**
     * Last 8 months:
     *
     * - waxing and waning left sided mandible pain and swelling
     * - denied erythema or drainage
     * - no history of facial trauma
     * - no history of dental procedures
     * - no history of dental caries
     * - intermittent fevers
     *
     * - Oral steroid courses - no improvement in facial swelling
     */
    private List<PhenotypicFeature> getLast8Monthsistory() {
        return null;
    }

    /**
     * - 6-month course of oral amoxicillin 1000mg two times per day
     */
    private MedicalAction treatment() {
        return null;
    }

    /**
     * Outcome:
     *
     * At 1 month:
     * - occasional mild pain in her mandible
     *
     * At 5 months:
     * - maxillofacial CT: resolving/chronic osteomyelitis
     * - extended antibiotic treatment to a 12-month course
     */

    /**
     * - no significant medical history
     */
    private List<PhenotypicFeature> getMedicalHistory() {
        return null;
    }

    /**
     * Diagnosis: cervicofacial actinomycosis
     */
    private Interpretation interpretation() {
        InterpretationBuilder ibuilder = InterpretationBuilder.solved("interpretation.id");
        DiagnosisBuilder dbuilder = DiagnosisBuilder.builder(ontologyClass("???", "Cervicofacial actinomycosis"));
        ibuilder.diagnosis(dbuilder.build());
        return ibuilder.build();
    }
}
