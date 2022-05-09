package org.phenopackets.phenopackettools.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.MetaDataBuilder;
import org.phenopackets.phenopackettools.builder.builders.Resources;
import org.phenopackets.schema.v2.Phenopacket;

public class GlaucomaSurgery implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";

    private final Phenopacket phenopacket;


    public GlaucomaSurgery() {
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.efoVersion("3.34.0"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addResource(Resources.ncbiTaxonVersion("2021-06-10"))
                .build();

        PhenopacketBuilder builder = PhenopacketBuilder.create(PHENOPACKET_ID, metadata);

        phenopacket = builder.build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
