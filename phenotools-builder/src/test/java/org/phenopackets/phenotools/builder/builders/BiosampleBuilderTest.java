package org.phenopackets.phenotools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.Biosample;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.of;

class BiosampleBuilderTest {

    @Test
    void biosampleTest() {
        Biosample biosample = BiosampleBuilder.biosample("sample1");
        assertThat(biosample.getId(), equalTo("sample1"));
    }

    /**
     * https://phenopacket-schema.readthedocs.io/en/v2/biosample.html#example
     */
    @Test
    void biosampleBuilderTest() {
        Biosample biosample = BiosampleBuilder.builder("sample1")
                .individualId("patient1")
                .description("Additional information can go here")
                .sampledTissue(of("UBERON_0001256", "wall of urinary bladder"))
                .histologicalDiagnosis(of("NCIT:C39853", "Infiltrating Urothelial Carcinoma"))
                .tumorProgression(of("NCIT:C84509", "Primary Malignant Neoplasm"))
                .tumorGrade(of("NCIT:C36136", "Grade 2 Lesion"))
                .procedure(ProcedureBuilder.procedure("NCIT:C5189", "Radical Cystoprostatectomy"))
                .file(FileBuilder.builder("file:///data/genomes/urothelial_ca_wgs.vcf.gz")
                        .individualToFileIdentifier("patient1", "NA12345")
                        .fileAttribute("genomeAssembly", "GRCh38")
                        .fileAttribute("fileFormat", "VCF")
                        .build())
                .materialSample(of("EFO:0009655", "abnormal sample"))
                .timeOfCollection(TimeElements.age("P52Y2M"))
                .pathologicalStage(of("NCIT:C28054", "Stage II"))
                .pathologicalTnmFinding(of("NCIT:C48726", "T2b Stage Finding"))
                .pathologicalTnmFinding(of("NCIT:C48705", "N0 Stage Finding"))
                .pathologicalTnmFinding(of("NCIT:C48699", "M0 Stage Finding"))
                .build();
        assertThat(biosample.getId(), equalTo("sample1"));
        assertThat(biosample.getDescription(), equalTo("Additional information can go here"));
        assertThat(biosample.getSampledTissue(), equalTo(of("UBERON_0001256", "wall of urinary bladder")));
        assertThat(biosample.getHistologicalDiagnosis(), equalTo(of("NCIT:C39853", "Infiltrating Urothelial Carcinoma")));
        assertThat(biosample.getTumorProgression(), equalTo(of("NCIT:C84509", "Primary Malignant Neoplasm")));
        assertThat(biosample.getTumorGrade(), equalTo(of("NCIT:C36136", "Grade 2 Lesion")));
        assertThat(biosample.getProcedure(), equalTo(ProcedureBuilder.procedure("NCIT:C5189", "Radical Cystoprostatectomy")));
        assertThat(biosample.getFilesList(), equalTo(List.of(FileBuilder.builder("file:///data/genomes/urothelial_ca_wgs.vcf.gz")
                .individualToFileIdentifier("patient1", "NA12345")
                .fileAttribute("genomeAssembly", "GRCh38")
                .fileAttribute("fileFormat", "VCF")
                .build())));
        assertThat(biosample.getMaterialSample(), equalTo(of("EFO:0009655", "abnormal sample")));
        assertThat(biosample.getTimeOfCollection(), equalTo(TimeElements.age("P52Y2M")));
        assertThat(biosample.getPathologicalStage(), equalTo(of("NCIT:C28054", "Stage II")));
        assertThat(biosample.getPathologicalTnmFindingList(), equalTo(List.of(
                of("NCIT:C48726", "T2b Stage Finding"),
                of("NCIT:C48705", "N0 Stage Finding"),
                of("NCIT:C48699", "M0 Stage Finding")
        )));
    }
}