package org.phenopackets.phenotools.validator.testdatagen;

import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.File;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.phenotools.validator.testdatagen.PhenopacketUtil.PhenotypicFeatureBuilder;
import static org.phenopackets.phenotools.validator.testdatagen.PhenopacketUtil.*;

public class RareDiseasePhenopacket {


    private final Phenopacket phenopacket;

    public RareDiseasePhenopacket() {

        Individual proband = IndividualBuilder.create("patient 1")
            .dateOfBirth("1998-01-01T00:00:00Z")
            .timeAtLastEncounter("P3Y")
            .male()
            .build();
        var syndactyly = PhenotypicFeatureBuilder.create("HP:0001159", "Syndactyly")
            .congenitalOnset().build();
        var pneumonia = PhenotypicFeatureBuilder.create("HP:0002090", "Pneumonia")
            .childhoodOnset().build();
        var cryptorchidism = PhenotypicFeatureBuilder.create("HP:0000028", "Cryptorchidism")
        .congenitalOnset().build();
        var sinusitis = PhenotypicFeatureBuilder.create("HP:0011109", "Chronic sinusitis")
            .severe().adultOnset().build();

        Resource hpo = hpoResource("2021-08-02");
        Resource mondo = mondoResource("2021-09-01");
        MetaData meta = MetaDataBuilder.create("2021-07-01T19:32:35Z", "anonymous biocurator")
            .submittedBy("anonymous submitter")
            .addResource(hpo)
            .addResource(mondo)
            .addExternalReference("PMID:20842687",
             "Severe dystonic encephalopathy without hyperphenylalaninemia associated with an 18-bp deletion within the proximal GCH1 promoter")
             .build();
        File vcf = FileBuilder.hg38vcf("file://data/file.vcf.gz")
            .addIndividualToFileIdentifiers("kindred 1A", "SAME000234")
            .build();

        this.phenopacket = Phenopacket.newBuilder()
            .setId("phenopacket-id-1")
            .setSubject(proband)
            .addPhenotypicFeatures(syndactyly)
            .addPhenotypicFeatures(pneumonia)
            .addPhenotypicFeatures(cryptorchidism)
            .addPhenotypicFeatures(sinusitis)
            .addFiles(vcf)
            .setMetaData(meta)
            .build();

    }

    public Phenopacket getPhenopacket() {
        return phenopacket;
    }


    
}
