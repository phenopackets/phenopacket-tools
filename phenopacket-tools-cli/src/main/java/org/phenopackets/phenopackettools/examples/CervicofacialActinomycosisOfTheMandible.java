package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.MedicalAction;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class CervicofacialActinomycosisOfTheMandible {


    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String INDIVIDUAL = "individual A";

    private final Phenopacket phenopacket;

    public CervicofacialActinomycosisOfTheMandible() {

       var externalRef = ExternalReferenceBuilder.of("PMID:32467116",
               "Cervicofacial actinomycosis of the mandible in a paediatric patient");

        var metadata = MetaDataBuilder.builder("2022-04-21T10:35:00Z", "anonymous biocurator")
               .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.hpoVersion("2022-06-11"))
                .addResource(Resources.efoVersion("3.34.0"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addExternalReference(externalRef)
               .build();

       Individual proband = IndividualBuilder.builder(INDIVIDUAL).
               ageAtLastEncounter("P10Y").
               female().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
//                .addAllPhenotypicFeatures(getMedicalHistory())
//                .addAllPhenotypicFeatures(getLast8Monthsistory())
//                .addMedicalAction(neckCT())
//                .addMedicalAction(mri())
//                .addPhenotypicFeature(examination())
//                .addInterpretation(interpretation())
//                .addMedicalAction(biopsy())
//                .addMedicalAction(frozenSection())
//                .addMedicalAction(tissueCultures())
//                .addMedicalAction(anaerobicCultures())
//                .addMedicalAction(treatment())
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
        var age9y4m = TimeElements.age("P9Y4M");
        var mandiblePain = PhenotypicFeatureBuilder.builder("HP:0200025", "Mandibular pain")
                .onset(age9y4m).build();
        var fever = PhenotypicFeatureBuilder.builder("HP:0001954", "Recurrent fever")
                .onset(age9y4m).build();
        return List.of(mandiblePain, fever);
    }

    /**
     * Diagnosis: cervicofacial actinomycosis
     */
//    private Interpretation interpretation() {
//        InterpretationBuilder ibuilder = InterpretationBuilder.solved("interpretation.id");
//        DiagnosisBuilder dbuilder = DiagnosisBuilder.builder(ontologyClass("???", "Cervicofacial actinomycosis"));
//        ibuilder.diagnosis(dbuilder.build());
//        return ibuilder.build();
//    }
}
