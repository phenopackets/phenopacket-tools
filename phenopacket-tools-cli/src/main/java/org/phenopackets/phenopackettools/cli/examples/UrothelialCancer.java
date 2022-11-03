package org.phenopackets.phenopackettools.cli.examples;


import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Severity;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class UrothelialCancer implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "patient1";
    private static final TimeElement AGE_AT_BIOPSY = TimeElements.age("P52Y2M");
    private static final OntologyClass BIOPSY = ontologyClass("NCIT:C15189", "Biopsy");

    private final Phenopacket phenopacket;

    public UrothelialCancer() {
        var individual = IndividualBuilder.builder(PROBAND_ID).male().dateOfBirth("1964-03-15T00:00:00Z").build();
        var hematuria = PhenotypicFeatureBuilder.of("HP:0000790","Hematuria");
        var dsyuria = PhenotypicFeatureBuilder.builder("HP:0100518", "Dysuria")
                .severity(Severity.severe())
                .build();
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.efoVersion("3.34.0"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addResource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(individual)
                .addDisease(infiltratingUrothelialCarcinoma())
                .addPhenotypicFeature(dsyuria)
                .addPhenotypicFeature(hematuria)
                .addBiosample(prostateBiosample())
                .addBiosample(rightUreterBiosample())
                .addBiosample(leftUreterBiosample())
                .addBiosample(bladderBiosample())
                .addBiosample(pelvicLymphNodeBiosample())
                .addFile(normalGermlineHtsFile())
                .build();
    }

    private File normalGermlineHtsFile() {
        // first create a File
        // We are imagining there is a reference to a VCF file for a normal germline genome seqeunce
        return FileBuilder.builder("file://data/genomes/germline_wgs.vcf.gz")
                .individualToFileIdentifier("example case", "NA12345")
                .addFileAttribute("genomeAssembly", "GRCh38")
                .addFileAttribute("fileFormat", "vcf")
                .addFileAttribute("description", "Matched normal germline sample")
                .build();
    }

    private Disease infiltratingUrothelialCarcinoma() {
        return DiseaseBuilder.builder("NCIT:C39853", "Infiltrating Urothelial Carcinoma")
                // Disease stage here is calculated based on the TMN findings
                .addDiseaseStage(ontologyClass("NCIT:C27971", "Stage IV"))
                // The tumor was staged as pT2b, meaning infiltration into the outer muscle layer of the bladder wall
                // pT2b Stage Finding (Code C48766)
                .addClinicalTnmFinding(ontologyClass("NCIT:C48766", "pT2b Stage Finding"))
                //pN2 Stage Finding (Code C48750)
                // cancer has spread to 2 or more lymph nodes in the true pelvis (N2)
                .addClinicalTnmFinding(ontologyClass("NCIT:C48750", "pN2 Stage Finding"))
                // M1 Stage Finding
                // the tumour has spread from the original site (Metastatic Neoplasm in lymph node - sample5)
                .addClinicalTnmFinding(ontologyClass("NCIT:C48700", "M1 Stage Finding"))
                .build();
    }

    private Biosample prostateBiosample() {
        OntologyClass prostateGland = ontologyClass("UBERON:0002367", "prostate gland");
        OntologyClass prostateAcinarAdenocarcinoma = ontologyClass("NCIT:C5596", "Prostate Acinar Adenocarcinoma");
        return BiosampleBuilder.builder("prostate biosample ID")
                .sampledTissue(prostateGland)
                .individualId(PROBAND_ID)
                .timeOfCollection(AGE_AT_BIOPSY)
                .histologicalDiagnosis(prostateAcinarAdenocarcinoma)
                .tumorProgression(ontologyClass("NCIT:C95606", "Second Primary Malignant Neoplasm"))
                .tumorGrade(ontologyClass("NCIT:C28091", "Gleason Score 7"))
                .procedure(ProcedureBuilder.builder(BIOPSY).build())
                .build();
    }

    private Biosample leftUreterBiosample() {
        OntologyClass leftUreter = ontologyClass("UBERON:0001223", "left ureter");
        return BiosampleBuilder.builder("left ureter biosample ID")
                .sampledTissue(leftUreter)
                .individualId(PROBAND_ID)
                .timeOfCollection(AGE_AT_BIOPSY)
                .histologicalDiagnosis(ontologyClass("NCIT:C38757", "Negative Finding"))
                .procedure(ProcedureBuilder.builder(BIOPSY).build())
                .build();
    }

    private Biosample rightUreterBiosample() {

        OntologyClass rightUreter = ontologyClass("UBERON:0001222", "right ureter");
        return BiosampleBuilder.builder("right ureter biosample ID")
                .sampledTissue(rightUreter)
                .individualId(PROBAND_ID)
                .timeOfCollection(AGE_AT_BIOPSY)
                .histologicalDiagnosis(ontologyClass("NCIT:C38757", "Negative Finding"))
                .procedure(ProcedureBuilder.builder(BIOPSY).build())
                .build();
    }

    private Biosample pelvicLymphNodeBiosample() {
        OntologyClass pelvicLymphNode = ontologyClass("UBERON:0015876", "pelvic lymph node");
        return BiosampleBuilder.builder("pelvic lymph node biosample ID")
                .sampledTissue(pelvicLymphNode)
                .individualId(PROBAND_ID)
                .timeOfCollection(AGE_AT_BIOPSY)
                .tumorProgression(ontologyClass("NCIT:C3261", "Metastatic Neoplasm"))
                .addFile(metastasisHtsFile())
                .procedure(ProcedureBuilder.builder(BIOPSY).build())
                .build();
    }

    private Biosample bladderBiosample() {
        OntologyClass bladderWall = ontologyClass("UBERON_0001256", "wall of urinary bladder");
        return BiosampleBuilder.builder("bladder biopsy id")
                .sampledTissue(bladderWall)
                .individualId(PROBAND_ID)
                .timeOfCollection(AGE_AT_BIOPSY)
                .histologicalDiagnosis(ontologyClass("NCIT:C39853", "Infiltrating Urothelial Carcinoma"))
                .tumorProgression(ontologyClass("NCIT:C84509", "Primary Malignant Neoplasm"))
                .addFile(somaticHtsFile())
                .procedure(ProcedureBuilder.builder("NCIT:C5189", "Radical Cystoprostatectomy").build())
                .build();
    }

    public File somaticHtsFile() {
        // first create a File
        // We are imagining there is a reference to a VCF file for a normal germline genome seqeunce
        // Now create a File object
        return FileBuilder.builder("file://data/genomes/urothelial_ca_wgs.vcf.gz")
                .individualToFileIdentifier("sample1", "BS342730")
                .addFileAttribute("genomeAssembly", "GRCh38")
                .addFileAttribute("fileFormat", "vcf")
                .addFileAttribute("description", "Urothelial carcinoma sample")
                .build();
    }

    public File metastasisHtsFile() {
        // first create a File
        // We are imagining there is a reference to a VCF file for a normal germline genome seqeunce
        return FileBuilder.builder("file://data/genomes/metastasis_wgs.vcf.gz")
                .individualToFileIdentifier("sample5", "BS730275")
                .addFileAttribute("genomeAssembly", "GRCh38")
                .addFileAttribute("fileFormat", "vcf")
                .addFileAttribute("description", "lymph node metastasis sample")
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
