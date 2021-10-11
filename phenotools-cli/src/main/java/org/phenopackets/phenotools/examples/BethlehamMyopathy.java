package org.phenopackets.phenotools.examples;


import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;
public class BethlehamMyopathy implements PhenopacketExample{
    private static final String PHENOPACKET_ID = "arbitrary proband id";
    private static final String INTERPRETATION_ID = "arbitrary interpretation id";
    private static final String PROBAND_ID = "proband A";

    private final Phenopacket phenopacket;

    public BethlehamMyopathy() {
        var authorAssertion = EvidenceBuilder.authorStatementEvidence("PMID:30808312", "COL6A1 mutation leading to Bethlem myopathy with recurrent hematuria: a case report");
        var bethlehamMyopathy = ontologyClass("OMIM:158810", "Bethlem myopathy 1");
        var individual = IndividualBuilder.create(PROBAND_ID).male().ageAtLastEncounter("P6Y3M").build();
        var metaData = MetaDataBuilder.create("2021-05-14T10:35:00Z", "anonymous biocurator")
                .hpWithVersion("2021-08-02")
                .genoWithVersion("2020-03-08")
                .externalReference(authorAssertion.getReference())
                .build();
        var variationDescriptor =
                VariationDescriptorBuilder.create("variant id")
                        .heterozygous()
                        .hgvs("NM_001848.2:c.877G>A")
                        .build();
        var col6a1VariantInterpretation =
                VariantInterpretationBuilder.create(variationDescriptor)
                        .pathogenic()
                        .build();
        var genomicInterpretation =
                GenomicInterpretationBuilder.causative(INTERPRETATION_ID)
                        .variantInterpretation(col6a1VariantInterpretation)
                        .build();
        var diagnosis = Diagnosis.newBuilder()
                .setDisease(bethlehamMyopathy).addGenomicInterpretations(genomicInterpretation).build();
        var interpretation = InterpretationBuilder.completed(INTERPRETATION_ID)
                .diagnosis(diagnosis).build();
        var VSD =
                PhenotypicFeatureBuilder.create("HP:0001629","Ventricular septal defect")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        var coarseFacial =
                PhenotypicFeatureBuilder.create("HP:0000280", "Coarse facial features")
                        .evidence(authorAssertion)
                        .build();
        var cryptorchidism =
                PhenotypicFeatureBuilder.create("HP:0008689", "Bilateral cryptorchidism")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        var polyhydramnios =
                PhenotypicFeatureBuilder.create("HP:0001561", "Polyhydramnios")
                        .fetalOnset()
                        .evidence(authorAssertion)
                        .build();
        var micropenis =
                PhenotypicFeatureBuilder.create("HP:0000054","Micropenis")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        var anonychia =
                PhenotypicFeatureBuilder.create("HP:0001798", "Anonychia")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        var vermisHypoplasia =
                PhenotypicFeatureBuilder.create("HP:0001320", "Cerebellar vermis hypoplasia")
                        .evidence(authorAssertion)
                        .build();
        var cataract =
                PhenotypicFeatureBuilder.create("HP:0000518", "Cataract")
                        .infantileOnset()
                        .evidence(authorAssertion)
                        .build();
        var dilatedFourthVentricle =
                PhenotypicFeatureBuilder.create("HP:0002198", "Dilated fourth ventricle")
                        .evidence(authorAssertion)
                        .build();
        var unilateralCleftLip =
                PhenotypicFeatureBuilder.create("HP:0100333", "Unilateral cleft lip")
                        .congenitalOnset()
                        .evidence(authorAssertion)
                        .build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metaData)
                .individual(individual)
                .phenotypicFeature(VSD)
                .phenotypicFeature(coarseFacial)
                .phenotypicFeature(cryptorchidism)
                .phenotypicFeature(polyhydramnios)
                .phenotypicFeature(micropenis)
                .phenotypicFeature(anonychia)
                .phenotypicFeature(vermisHypoplasia)
                .phenotypicFeature(cataract)
                .phenotypicFeature(dilatedFourthVentricle)
                .phenotypicFeature(unilateralCleftLip)
                .interpretation(interpretation)
                .build();

    }


    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }

}
