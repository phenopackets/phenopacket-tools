package org.phenopackets.phenopackettools.test;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v1.Cohort;
import org.phenopackets.schema.v1.Family;
import org.phenopackets.schema.v1.Phenopacket;
import org.phenopackets.schema.v1.core.*;

import java.util.List;

class V1 {

    public static final Age AGE = Age.newBuilder().setAge("P14Y").build();
    private static final String PHENOPACKET_ID = "comprehensive-phenopacket-id";
    private static final String FAMILY_ID = "comprehensive-family-id";
    private static final String COHORT_ID = "comprehensive-cohort-id";
    private static final String PROBAND_ID = "14 year-old boy";
    private static final List<String> ALTERNATE_PROBAND_IDS = List.of("boy", "patient", "proband");
    private static final String MOTHER_ID = "MOTHER";
    private static final String FATHER_ID = "FATHER";
    private static final ExternalReference EXTERNAL_REFERENCE = ExternalReference.newBuilder().
            setId("PMID:30808312").
            setDescription("COL6A1 mutation leading to Bethlem myopathy with recurrent hematuria: a case report.").
            build();
    private static final Disease CONGENITAL_PFEIFFER_SYNDROME = Disease.newBuilder()
            .setTerm(ontologyClass("OMIM:101600", "PFEIFFER SYNDROME"))
            .setClassOfOnset(ontologyClass("HP:0003577", "Congenital onset"))
            .build();
    private static final Gene FGFR1 = Gene.newBuilder()
            .setId("HGNC1:3688")
            .setSymbol("FGFR1")
            .build();
    private static final OntologyClass HOMO_SAPIENS = ontologyClass("NCBITaxon:9606", "homo sapiens");
    private static final Biosample BIOSAMPLE = Biosample.newBuilder()
            .setId("biosample-id")
            .setIndividualId(PROBAND_ID)
            .setDescription("Muscle biopsy of %s".formatted(PROBAND_ID))
            .setSampledTissue(ontologyClass("UBERON:0003403", "skin of forearm"))

            .setTaxonomy(HOMO_SAPIENS)
            .setAgeOfIndividualAtCollection(AGE)
            .setHistologicalDiagnosis(ontologyClass("NCIT:C38757", "Negative Finding"))
            .setTumorProgression(ontologyClass("NCIT:C3677", "Benign Neoplasm"))
            .setTumorGrade(ontologyClass("NCIT:C28076", "Disease Grade Qualifier")) // not a meaningful value, just something for testing
            .addDiagnosticMarkers(ontologyClass("NCIT:C68748", "HER2/Neu Positive"))
            .setIsControlSample(false)
            .build();
    ;

    private V1(){}

    static Phenopacket comprehensivePhenopacket() {
        Individual proband = Individual.newBuilder()
                .setId(PROBAND_ID)
                .addAllAlternateIds(ALTERNATE_PROBAND_IDS)
                .setDateOfBirth(Timestamp.newBuilder().setSeconds(123456).setNanos(100).build())
                .setAgeAtCollection(AGE)
                .setSex(Sex.MALE)
                .setKaryotypicSex(KaryotypicSex.XY)
                .setTaxonomy(HOMO_SAPIENS)
                .build();

        Evidence citation = Evidence.newBuilder().
                setReference(EXTERNAL_REFERENCE).
                setEvidenceCode(ontologyClass("ECO:0000033", "author statement supported by traceable reference"))
                .build();

        PhenotypicFeature decreasedFetalMovement = PhenotypicFeature.newBuilder()
                .setType(ontologyClass("HP:0001558", "Decreased fetal movement"))
                .setClassOfOnset(ontologyClass("HP:0011461", "Fetal onset"))
                .addEvidence(citation)
                .build();


        PhenotypicFeature absentCranialNerveAbnormality = PhenotypicFeature.newBuilder()
                .setType(ontologyClass("HP:0031910", "Abnormal cranial nerve physiology"))
                .setNegated(true)
                .addEvidence(citation)
                .build();

        PhenotypicFeature motorDelay = PhenotypicFeature.newBuilder()
                .setType(ontologyClass("HP:0001270","Motor delay"))
                .setClassOfOnset(ontologyClass("HP:0011463","Childhood onset"))
                .setSeverity(ontologyClass("HP:0012825", "Mild"))
                .build();

        PhenotypicFeature hematuria = PhenotypicFeature.newBuilder()
                .setType(ontologyClass("HP:0011463", "Macroscopic hematuria"))
                .setAgeOfOnset(AGE)
                .addModifiers(ontologyClass("HP:0031796","Recurrent"))
                .addEvidence(citation)
                .build();

        // Allele
        HgvsAllele c_877G_to_A = HgvsAllele.newBuilder()
                .setHgvs("NM_001848.2:c.877G>A")
                .build();
        // Corresponding variant
        Variant heterozygousCOL6A1Variant = Variant.newBuilder()
                .setHgvsAllele(c_877G_to_A)
                .setZygosity(ontologyClass("GENO:0000135", "heterozygous"))
                .build();

        return Phenopacket.newBuilder()
                .setId(PHENOPACKET_ID)
                .setSubject(proband)
                .addPhenotypicFeatures(decreasedFetalMovement)
                .addPhenotypicFeatures(absentCranialNerveAbnormality)
                .addPhenotypicFeatures(hematuria)
                .addPhenotypicFeatures(motorDelay)

                .addBiosamples(BIOSAMPLE)

                .addGenes(FGFR1)
                .addVariants(heterozygousCOL6A1Variant)
                .addDiseases(CONGENITAL_PFEIFFER_SYNDROME)
                .addHtsFiles(probandGenomeSequencingVcf())
                .setMetaData(metaData())
                .build();
    }

    static Family comprehensiveFamily() {
        return Family.newBuilder()
                .setId(FAMILY_ID)
                .setProband(comprehensivePhenopacket())
                .addRelatives(unaffectedMother())
                .addRelatives(unaffectedFather())
                .setPedigree(pedigree())
                .addHtsFiles(familyGenomeSequencingVcf())
                .setMetaData(metaData())
                .build();
    }

    static Cohort comprehensiveCohort() {
        return Cohort.newBuilder()
                .setId(COHORT_ID)
                .setDescription("A description of the example cohort.")
                .addMembers(comprehensivePhenopacket())
                .addMembers(unaffectedMother())
                .addMembers(unaffectedFather())
                .addHtsFiles(familyGenomeSequencingVcf())
                .setMetaData(metaData())
                .build();
    }

    // *****************************************                        ********************************************* //

    private static MetaData metaData() {
        return MetaData.newBuilder()
                .setCreated(Timestamp.newBuilder()
                        .setSeconds(1_664_815_144)
                        .setNanos(123456)
                        .build())
                .setCreatedBy("Peter R.")
                .setSubmittedBy("PhenopacketLab")
                .addResources(Resource.newBuilder()
                        .setId("hp")
                        .setName("human phenotype ontology")
                        .setNamespacePrefix("HP")
                        .setIriPrefix("http://purl.obolibrary.org/obo/HP_")
                        .setUrl("http://purl.obolibrary.org/obo/hp.owl")
                        .setVersion("2018-03-08")
                        .build())
                .addResources(Resource.newBuilder()
                        .setId("geno")
                        .setName("Genotype Ontology")
                        .setNamespacePrefix("GENO")
                        .setIriPrefix("http://purl.obolibrary.org/obo/GENO_")
                        .setUrl("http://purl.obolibrary.org/obo/geno.owl")
                        .setVersion("19-03-2018")
                        .build())
                .addResources(Resource.newBuilder()
                        .setId("pubmed")
                        .setName("PubMed")
                        .setNamespacePrefix("PMID")
                        .setIriPrefix("https://www.ncbi.nlm.nih.gov/pubmed/")
                        .build())
                .addResources(Resource.newBuilder()
                        .setId("ncit")
                        .setName("NCI Thesaurus")
                        .setNamespacePrefix("NCIT")
                        .setUrl("http://purl.obolibrary.org/obo/ncit.owl")
                        .setVersion("20-03-2020")
                        .setIriPrefix("http://purl.obolibrary.org/obo/NCIT_"))
                .addExternalReferences(EXTERNAL_REFERENCE)
                .setPhenopacketSchemaVersion("1.0.0")

                .build();
    }

    private static HtsFile familyGenomeSequencingVcf() {
        return HtsFile.newBuilder()
                .setUri("file://data/genomes/FAM000001")
                .setDescription("Whole genome sequencing VCF output")
                .setHtsFormat(HtsFile.HtsFormat.VCF)
                .setGenomeAssembly("GRCh38.p13")
                .putIndividualToSampleIdentifiers(PROBAND_ID, "P000001C") // proband
                .putIndividualToSampleIdentifiers(MOTHER_ID, "P000001M") // mother
                .putIndividualToSampleIdentifiers(FATHER_ID, "P000001F") // father
                .build();
    }

    private static HtsFile probandGenomeSequencingVcf() {
        return HtsFile.newBuilder()
                .setUri("file://data/genomes/P000001C")
                .setDescription("Whole genome sequencing VCF output")
                .setHtsFormat(HtsFile.HtsFormat.VCF)
                .setGenomeAssembly("GRCh38.p13")
                .putIndividualToSampleIdentifiers(PROBAND_ID, "P000001C") // proband
                .build();
    }

    private static Phenopacket unaffectedMother() {
        Individual mother = Individual.newBuilder()
                .setSex(Sex.FEMALE)
                .setId(MOTHER_ID)
                .build();
        return Phenopacket.newBuilder()
                .setSubject(mother)
                .build();
    }

    private static Phenopacket unaffectedFather() {
        Individual father = Individual.newBuilder()
                .setSex(Sex.MALE)
                .setId(FATHER_ID)
                .build();
        return Phenopacket.newBuilder()
                .setSubject(father)
                .build();
    }

    private static Pedigree pedigree() {
        Pedigree.Person pedProband = Pedigree.Person.newBuilder()
                .setIndividualId(PROBAND_ID)
                .setSex(Sex.MALE)
                .setMaternalId(MOTHER_ID)
                .setPaternalId(FATHER_ID)
                .setAffectedStatus(Pedigree.Person.AffectedStatus.AFFECTED)
                .build();

        Pedigree.Person pedMother = Pedigree.Person.newBuilder()
                .setIndividualId(MOTHER_ID)
                .setSex(Sex.FEMALE)
                .setAffectedStatus(Pedigree.Person.AffectedStatus.UNAFFECTED)
                .build();

        Pedigree.Person pedFather = Pedigree.Person.newBuilder()
                .setIndividualId(FATHER_ID)
                .setSex(Sex.MALE)
                .setAffectedStatus(Pedigree.Person.AffectedStatus.UNAFFECTED)
                .build();

        return Pedigree.newBuilder()
                .addPersons(pedProband)
                .addPersons(pedMother)
                .addPersons(pedFather)
                .build();
    }

    private static OntologyClass ontologyClass(String id, String label) {
        return OntologyClass.newBuilder()
                .setId(id)
                .setLabel(label)
                .build();
    }
}
