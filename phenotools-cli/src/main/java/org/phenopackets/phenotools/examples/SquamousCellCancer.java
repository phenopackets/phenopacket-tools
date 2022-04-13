package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.of;

class SquamousCellCancer implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";
    private static final OntologyClass BIOPSY = of("NCIT:C15189", "Biopsy");

    private final Phenopacket phenopacket;

    SquamousCellCancer() {
        Individual proband = IndividualBuilder.builder(PROBAND_ID).male().ageAtLastEncounter("P38Y").build();
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .resource(Resources.ncitVersion("21.05d"))
                .resource(Resources.efoVersion("3.34.0"))
                .resource(Resources.uberonVersion("2021-07-27"))
                .resource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();
        var esophagealSCC = of("NCIT:C4024", "Esophageal Squamous Cell Carcinoma");
        var disease = DiseaseBuilder.builder(esophagealSCC)
                .clinicalTnmFinding(of("NCIT:C48724", "T2 Stage Finding"))
                .clinicalTnmFinding(of("NCIT:C48706", "N1 Stage Finding"))
                .clinicalTnmFinding(of("NCIT:C48699", "M0 Stage Finding"))
                .build();
        var esophagusBiopsy = BiosampleBuilder.builder("biosample 1")
                .individualId(PROBAND_ID)
                .timeOfCollection(TimeElements.age("P49Y2M"))
                .sampledTissue((of("NCIT:C12389", "Esophagus")))
                .tumorProgression(of("NCIT:C4813", "Recurrent Malignant Neoplasm"))
                .procedure(ProcedureBuilder.builder(BIOPSY).build())
                .build();
        var lymphNodeBiopsy = BiosampleBuilder.builder("biosample 2")
                .individualId(PROBAND_ID)
                .timeOfCollection(TimeElements.age("P48Y3M"))
                .sampledTissue(of("NCIT:C139196", "Esophageal Lymph Node"))
                .tumorProgression(of("NCIT:C84509", "Primary Malignant Neoplasm"))
                .histologicalDiagnosis(of("NCIT:C4024", "Esophageal Squamous Cell Carcinoma"))
                .diagnosticMarker(of("NCIT:C131711", "Human Papillomavirus-18 Positive"))
                .procedure(ProcedureBuilder.builder(BIOPSY).build())
                .build();
        var lungBiopsy = BiosampleBuilder.builder("biosample 3")
                .individualId(PROBAND_ID)
                .timeOfCollection(TimeElements.age("P50Y7M"))
                .sampledTissue(of("NCIT:C12468", "Lung"))
                .tumorProgression(of("NCIT:C3261", "Metastatic Neoplasm"))
                .procedure(ProcedureBuilder.builder(BIOPSY).build())
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
