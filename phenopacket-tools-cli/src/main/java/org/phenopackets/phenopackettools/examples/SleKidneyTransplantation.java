package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Disease;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

/**
 * Pavlakou P, et al. Case Report: Kidney Transplantation in a Patient With Acquired Agammaglobulinemia and SLE.
 * Issues and Challenges. Front Med (Lausanne). 2021 Mar 12;8:665475.
 * PMID: 33777986.
 */
public class SleKidneyTransplantation implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final String BIOSAMPLE_ID = "biosample.1";
    private static final OntologyClass BIOPSY = ontologyClass("NCIT:C15189", "Biopsy");
    private static final OntologyClass SYSTEMIC_LUPUS = ontologyClass("MONDO:0007915", "systemic lupus erythematosus");
    private static final OntologyClass PRIMARY_NEOPLASM = ontologyClass("NCIT:C8509", "Primary Neoplasm");

    // Organs
    private static final OntologyClass EYE = ontologyClass("UBERON:0000970", "eye");
    private static final OntologyClass CURE = ontologyClass("NCIT:C62220", "Cure");
    private static final OntologyClass LEFT_EYE = ontologyClass("UBERON:0004548", "left eye");

    private final Phenopacket phenopacket;

    public SleKidneyTransplantation() {
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.efoVersion("3.34.0"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addResource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();
        Individual proband = IndividualBuilder.builder(PROBAND_ID).
                ageAtLastEncounter("P39Y").
                female().
                build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addDisease(sle())
                .build();
    }


    /**
     * A 39-year-old female patient was referred to our Nephrology Department during late 2009.
     * She was newly diagnosed with SLE after a spontaneous miscarriage during the second trimester of pregnancy.
     * @return
     */
    Disease sle() {
        return DiseaseBuilder.builder(SYSTEMIC_LUPUS)
                .onset(TimeElements.age("P39Y"))
                .build();
    }


    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
