package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

class Thrombocytopenia2 implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "id-C";
    private static final String INTERPRETATION_ID = "arbitrary interpretation id";
    private static final String PROBAND_ID = "family 10 proband";

    private final Phenopacket phenopacket;

    Thrombocytopenia2() {
        var authorAssertion = EvidenceBuilder.authorStatementEvidence("PMID:21211618", "Mutations in the 5' UTR of ANKRD26, the ankirin repeat domain 26 gene, cause an autosomal-dominant form of inherited thrombocytopenia, THC2");
        var thrombocytopenia2 = ontologyClass("OMIM:188000", "Thrombocytopenia 2");
        var individual = IndividualBuilder.builder(PROBAND_ID).female().ageAtLastEncounter("P20Y").build();
        var metaData = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .resource(Resources.hpoVersion("2021-08-02"))
                .resource(Resources.genoVersion("2020-03-08"))
                .externalReference(authorAssertion.getReference())
                .build();
        var variationDescriptor =
                VariationDescriptorBuilder.builder("variant id")
                        .heterozygous()
                        .hgvs("NM_014915.2:c.-128G>A")
                        .build();
        var col6a1VariantInterpretation = VariantInterpretationBuilder.variantInterpretation(variationDescriptor, Status.pathogenic());
        var genomicInterpretation =
                GenomicInterpretationBuilder.builder("genomic interpretation id")
                    .causative()
                        .variantInterpretation(col6a1VariantInterpretation)
                        .build();
        var diagnosis = DiagnosisBuilder.builder(thrombocytopenia2).genomicInterpretation(genomicInterpretation).build();
        var interpretation = InterpretationBuilder.builder(INTERPRETATION_ID, Status.completed())
                .diagnosis(diagnosis).build();
        var excludedAbnormalPlateletSize =
                PhenotypicFeatureBuilder.builder("HP:0011876", "Abnormal platelet volume")
                        .excluded()
                        .evidence(authorAssertion)
                        .build();
        var brusing =
                PhenotypicFeatureBuilder.builder("HP:0000978", "Bruising susceptibility")
                        .evidence(authorAssertion)
                        .build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metaData)
                .individual(individual)
                .addPhenotypicFeature(excludedAbnormalPlateletSize)
                .addPhenotypicFeature(brusing)
                .addInterpretation(interpretation)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
