package org.phenopackets.phenotools.examples;


import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

class BethlehamMyopathy implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary proband id";
    private static final String INTERPRETATION_ID = "arbitrary interpretation id";
    private static final String PROBAND_ID = "proband A";

    private final Phenopacket phenopacket;

    BethlehamMyopathy() {
        var authorAssertion = EvidenceBuilder.authorStatementEvidence("PMID:30808312", "COL6A1 mutation leading to Bethlem myopathy with recurrent hematuria: a case report");
        var bethlehamMyopathy = ontologyClass("OMIM:158810", "Bethlem myopathy 1");
        var individual = IndividualBuilder.builder(PROBAND_ID).male().ageAtLastEncounter("P6Y3M").build();
        var metaData = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .resource(Resources.hpoVersion("2021-08-02"))
                .resource(Resources.genoVersion("2020-03-08"))
                .externalReference(authorAssertion.getReference())
                .build();
        var variationDescriptor =
                VariationDescriptorBuilder.builder("variant id")
                        .heterozygous()
                        .hgvs("NM_001848.2:c.877G>A")
                        .build();
        var col6a1VariantInterpretation =
                VariantInterpretationBuilder.variantInterpretation(variationDescriptor, Status.pathogenic());
        var genomicInterpretation =
                GenomicInterpretationBuilder.builder(INTERPRETATION_ID)
                        .causative()
                        .variantInterpretation(col6a1VariantInterpretation).build();
        var diagnosis = Diagnosis.newBuilder()
                .setDisease(bethlehamMyopathy).addGenomicInterpretations(genomicInterpretation).build();
        var interpretation = InterpretationBuilder.builder(INTERPRETATION_ID, Status.completed())
                .diagnosis(diagnosis).build();
        var ventricularSeptalDefect =
                PhenotypicFeatureBuilder.builder("HP:0001629", "Ventricular septal defect")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        var coarseFacial =
                PhenotypicFeatureBuilder.builder("HP:0000280", "Coarse facial features")
                        .evidence(authorAssertion)
                        .build();
        var cryptorchidism =
                PhenotypicFeatureBuilder.builder("HP:0008689", "Bilateral cryptorchidism")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        var polyhydramnios =
                PhenotypicFeatureBuilder.builder("HP:0001561", "Polyhydramnios")
                        .fetalOnset()
                        .evidence(authorAssertion)
                        .build();
        var micropenis =
                PhenotypicFeatureBuilder.builder("HP:0000054", "Micropenis")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        var anonychia =
                PhenotypicFeatureBuilder.builder("HP:0001798", "Anonychia")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        var vermisHypoplasia =
                PhenotypicFeatureBuilder.builder("HP:0001320", "Cerebellar vermis hypoplasia")
                        .evidence(authorAssertion)
                        .build();
        var cataract =
                PhenotypicFeatureBuilder.builder("HP:0000518", "Cataract")
                        .infantileOnset()
                        .evidence(authorAssertion)
                        .build();
        var dilatedFourthVentricle =
                PhenotypicFeatureBuilder.builder("HP:0002198", "Dilated fourth ventricle")
                        .evidence(authorAssertion)
                        .build();
        var unilateralCleftLip =
                PhenotypicFeatureBuilder.builder("HP:0100333", "Unilateral cleft lip")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metaData)
                .individual(individual)
                .addPhenotypicFeature(ventricularSeptalDefect)
                .addPhenotypicFeature(coarseFacial)
                .addPhenotypicFeature(cryptorchidism)
                .addPhenotypicFeature(polyhydramnios)
                .addPhenotypicFeature(micropenis)
                .addPhenotypicFeature(anonychia)
                .addPhenotypicFeature(vermisHypoplasia)
                .addPhenotypicFeature(cataract)
                .addPhenotypicFeature(dilatedFourthVentricle)
                .addPhenotypicFeature(unilateralCleftLip)
                .addInterpretation(interpretation)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
