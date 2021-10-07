package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.TimeElement;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;


public class Aml implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";

    private final Phenopacket phenopacket;

    public Aml() {
        var individual = IndividualBuilder.create(PROBAND_ID).male().ageAtLastEncounter("P8Y").build();
        var disease = DiseaseBuilder
                .create(ontologyClass("NCIT:C3171", "Acute Myeloid Leukemia"))
                .build();
        TimeElement age = TimeElementBuilder.create().age("P8Y").build();
        var biospy = BiosampleBuilder.create("SAMN05324082")
                .individualId("SAMN05324082-individual")
                .description("THP-1; 6 hours; DMSO; Replicate 1")
                .timeOfCollection(age)
                .taxonomy(ontologyClass("NCBITaxon:9606", "Homo sapiens"))
                .sampledTissue(ontologyClass("UBERON:0000178", "peripheral blood"))
                .histologicalDiagnosis(ontologyClass("EFO:0000221", "Acute Monocytic Leukemia"))
                .build();
        var metadata = MetaDataBuilder.create("2021-05-14T10:35:00Z", "anonymous biocurator")
                .ncitWithVersion("21.05d")
                .efoWithVersion("3.34.0")
                .uberonWithVersion("2021-07-27")
                .ncbiTaxonWithVersion(" 2021-06-10")
                .build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(individual)
                .biosample(biospy)
                .disease(disease)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
    /*








     */
}
