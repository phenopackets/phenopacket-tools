package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

class SquamousCellCancer implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final OntologyClass BIOPSY = ontologyClass("NCIT:C15189", "Biopsy");

    private final Phenopacket phenopacket;

    SquamousCellCancer() {
        Individual proband = IndividualBuilder.create(PROBAND_ID).male().ageAtLastEncounter("P38Y").build();
        var metadata = MetaDataBuilder.create("2021-05-14T10:35:00Z", "anonymous biocurator")
                .resource(Resources.ncitVersion("21.05d"))
                .resource(Resources.efoVersion("3.34.0"))
                .resource(Resources.uberonVersion("2021-07-27"))
                .resource(Resources.ncbiTaxonVersion(" 2021-06-10"))
                .build();
        var esophagealSCC = ontologyClass("NCIT:C4024", "Esophageal Squamous Cell Carcinoma");
        var disease = DiseaseBuilder.create(esophagealSCC)
                .clinicalTnmFinding(ontologyClass("NCIT:C48724", "T2 Stage Finding"))
                .clinicalTnmFinding(ontologyClass("NCIT:C48706", "N1 Stage Finding"))
                .clinicalTnmFinding(ontologyClass("NCIT:C48699", "M0 Stage Finding"))
                .build();
        var esophagusBiopsy = BiosampleBuilder.create("biosample 1")
                .individualId(PROBAND_ID)
                .timeOfCollection(TimeElements.age("P49Y2M"))
                .sampledTissue((ontologyClass("NCIT:C12389", "Esophagus")))
                .tumorProgression(ontologyClass("NCIT:C4813", "Recurrent Malignant Neoplasm"))
                .procedure(ProcedureBuilder.create(BIOPSY).build())
                .build();
        var lymphNodeBiopsy = BiosampleBuilder.create("biosample 2")
                .individualId(PROBAND_ID)
                .timeOfCollection(TimeElements.age("P48Y3M"))
                .sampledTissue(ontologyClass("NCIT:C139196", "Esophageal Lymph Node"))
                .tumorProgression(ontologyClass("NCIT:C84509", "Primary Malignant Neoplasm"))
                .histologicalDiagnosis(ontologyClass("NCIT:C4024", "Esophageal Squamous Cell Carcinoma"))
                .diagnosticMarker(ontologyClass("NCIT:C131711", "Human Papillomavirus-18 Positive"))
                .procedure(ProcedureBuilder.create(BIOPSY).build())
                .build();
        var lungBiopsy = BiosampleBuilder.create("biosample 3")
                .individualId(PROBAND_ID)
                .timeOfCollection(TimeElements.age("P50Y7M"))
                .sampledTissue(ontologyClass("NCIT:C12468", "Lung"))
                .tumorProgression(ontologyClass("NCIT:C3261", "Metastatic Neoplasm"))
                .procedure(ProcedureBuilder.create(BIOPSY).build())
                .build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .biosample(esophagusBiopsy)
                .biosample(lymphNodeBiopsy)
                .biosample(lungBiopsy)
                .disease(disease)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
