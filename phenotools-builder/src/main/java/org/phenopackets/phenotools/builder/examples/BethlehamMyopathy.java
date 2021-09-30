package org.phenopackets.phenotools.builder.examples;

import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.externalReference;

public class BethlehamMyopathy implements PhenopacketExample{
    private static final String PHENOPACKET_ID = "id-A";
    private static final String PROBAND_ID = "proband A";
    private static final String PMID = "PMID:30808312";
    private static final String publication = "COL6A1 mutation leading to Bethlem myopathy with recurrent hematuria: a case report";

    private final Phenopacket phenopacket;

    public BethlehamMyopathy() {
        Evidence authorAssertion = EvidenceBuilder.authorStatementEvidence(PMID, publication);
        Disease bethlehamMyopathy = DiseaseBuilder.create("OMIM:158810", "Bethlem myopathy 1").build();
        Individual individual = IndividualBuilder.create(PROBAND_ID).male().ageAtLastEncounter("P6Y3M").build();
        MetaData metaData = MetaDataBuilder.create("2021-05-14T10:35:00Z", "anonymous biocurator")
                .hpWithVersion("2021-08-02")
                .genoWithVersion("2020-03-08")
                .addExternalReference(authorAssertion.getReference())
                .build();
        String hgvsExpression = "NM_001848.2:c.877G>A";
        phenopacket = null;
    }





    @Override
    public String getJson() {
        return null;
    }

    @Override
    public Phenopacket getPhenopacket() {
        return null;
    }


    /*

    private static final OntologyClass disease = OntologyClassUtil.ontologyClassFactory
    private static final



    public RareDiseaseMCAHS1() {
        ExternalReference citation = ExternalReferenceUtil.externalReference(PMID, publication);
        MetaData metaData = MetaDataUtil.defaultRareDiseaseMetaData(citation);

       Interpretation interpretation = pathogenicHeterozygousHgvsInterpretation(disease, "interpretation.id.1", PROBAND_ID, hgvsExpression);
        phenopacket = Phenopacket.newBuilder()
                .setId(PHENOPACKET_ID)
                .setSubject(proband)
                .addPhenotypicFeatures(ventricularSeptalDefect())
                .addPhenotypicFeatures(coarseFacialFeatures())
                .addPhenotypicFeatures(bilateralCryptorchidism())
                .addPhenotypicFeatures(polyhydramnios())
                .addPhenotypicFeatures(micropenis())
                .addPhenotypicFeatures(anonychia())
                .addPhenotypicFeatures(cerebellarVermisHypoplasia())
                .addPhenotypicFeatures(cataract())
                .addPhenotypicFeatures(dilatedFourthVentricle())
                .addPhenotypicFeatures(unilateralCleftLip())
                .addInterpretations(interpretation)
                .setMetaData(metaData)
                .build();
    }

    private PhenotypicFeature ventricularSeptalDefect() {
        OntologyClass clz = ontologyClassFactory("HP:0001629","Ventricular septal defect");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }

    private PhenotypicFeature  coarseFacialFeatures() {
        OntologyClass clz = ontologyClassFactory("HP:0000280", "Coarse facial features");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }
    private PhenotypicFeature  bilateralCryptorchidism() {
        OntologyClass clz = ontologyClassFactory("HP:0008689", "Bilateral cryptorchidism");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }

    private PhenotypicFeature  polyhydramnios() {
        OntologyClass clz = ontologyClassFactory("HP:0001561", "Polyhydramnios");
        return fetalOnsetFeature(clz, authorAssertion);
    }

    private PhenotypicFeature  micropenis() {
        OntologyClass clz = ontologyClassFactory("HP:0000054","Micropenis");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }

    private PhenotypicFeature  anonychia() {
        OntologyClass clz = ontologyClassFactory("HP:0001798", "Anonychia");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }

    private PhenotypicFeature  cerebellarVermisHypoplasia() {
        OntologyClass clz = ontologyClassFactory("HP:0001320", "Cerebellar vermis hypoplasia");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }

    private PhenotypicFeature  cataract() {
        OntologyClass clz = ontologyClassFactory("HP:0000518", "Cataract");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }

    private PhenotypicFeature  dilatedFourthVentricle() {
        OntologyClass clz = ontologyClassFactory("HP:0002198", "Dilated fourth ventricle");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }

    private PhenotypicFeature  unilateralCleftLip() {
        OntologyClass clz = ontologyClassFactory("HP:0100333", "Unilateral cleft lip");
        return phenotypicFeatureWithEvidence(clz, authorAssertion);
    }



     */
}
