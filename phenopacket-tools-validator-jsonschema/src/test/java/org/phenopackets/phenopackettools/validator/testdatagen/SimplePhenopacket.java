package org.phenopackets.phenopackettools.validator.testdatagen;

import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;

import static org.phenopackets.phenopackettools.validator.testdatagen.PhenopacketUtil.hpoResource;


/**
 * Build the simplest possible phenopacket for validation
 */
public class SimplePhenopacket {

    private final Phenopacket phenopacket;

    public SimplePhenopacket() {
        Resource hpo = hpoResource("2021-08-02");
        MetaData meta = PhenopacketUtil.MetaDataBuilder.create("2021-07-01T19:32:35Z", "anonymous biocurator")
                .submittedBy("anonymous submitter")
                .addResource(hpo)
                .build();
        phenopacket = Phenopacket.newBuilder()
        .setId("hello world")
        .setMetaData(meta)
        .build();
    }

   
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }


}
