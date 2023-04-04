package org.phenopackets.phenopackettools.cli.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.IndividualBuilder;
import org.phenopackets.phenopackettools.builder.builders.MetaDataBuilder;
import org.phenopackets.phenopackettools.builder.builders.PhenotypicFeatureBuilder;
import org.phenopackets.phenopackettools.builder.builders.Resources;
import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.core.MetaData;

/**
 * An example of creating a Cohort message with two individuals.
 */
public class ExampleCohort {

    private ExampleCohort() {
    }

    /**
     * @return an example {@linkplain Cohort} with two individuals.
     */
    public static Cohort getCohort() {
        MetaData metaData = MetaDataBuilder.builder("2022-04-17T10:35:00Z", "biocurator")
                .addResource(Resources.hpoVersion("2022-06-11"))
                .build();
        return Cohort.newBuilder()
                .setId("example-cohort")
                .setDescription("An example cohort")
                .addMembers(PhenopacketBuilder.create("cohort-member-1", metaData)
                        .individual(IndividualBuilder.of("cohort-member-id-1"))
                        .addPhenotypicFeature(PhenotypicFeatureBuilder.of("HP:0031936", "Delayed ability to walk"))
                        .addPhenotypicFeature(PhenotypicFeatureBuilder.of("HP:0009046", "Difficulty running"))
                        .addPhenotypicFeature(PhenotypicFeatureBuilder.builder("HP:0003236", "Elevated circulating creatine kinase concentration")
                                .excluded()
                                .build())
                        .build())
                .addMembers(PhenopacketBuilder.create("cohort-member-2", metaData)
                        .individual(IndividualBuilder.of("cohort-member-id-2"))
                        .addPhenotypicFeature(PhenotypicFeatureBuilder.of("HP:0000518", "Cataract"))
                        .addPhenotypicFeature(PhenotypicFeatureBuilder.builder("HP:0001647", "Bicuspid aortic valve")
                                .excluded()
                                .build())
                        .build())
                .setMetaData(metaData)
                .build();
    }
}
