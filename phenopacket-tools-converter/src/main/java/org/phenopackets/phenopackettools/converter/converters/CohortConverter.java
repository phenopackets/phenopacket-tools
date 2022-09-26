package org.phenopackets.phenopackettools.converter.converters;

import org.phenopackets.schema.v2.Cohort;

import static org.phenopackets.phenopackettools.converter.converters.PhenopacketConverter.toV2Phenopackets;
import static org.phenopackets.phenopackettools.converter.converters.v2.FileConverter.toFiles;
import static org.phenopackets.phenopackettools.converter.converters.v2.MetaDataConverter.toMetaData;

public class CohortConverter {

    private CohortConverter() {
    }

    public static Cohort toV2Cohort(org.phenopackets.schema.v1.Cohort v1Cohort) {
        Cohort.Builder builder = Cohort.newBuilder();

        if (v1Cohort.hasMetaData()) {
            builder.setMetaData(toMetaData(v1Cohort.getMetaData()));
        }
        if (!v1Cohort.getId().isEmpty()) {
            builder.setId(v1Cohort.getId());
        }
        if (!v1Cohort.getDescription().isEmpty()) {
            builder.setDescription(v1Cohort.getDescription());
        }
        if (v1Cohort.getMembersCount() > 0) {
            builder.addAllMembers(toV2Phenopackets(v1Cohort.getMembersList()));
        }
        if (v1Cohort.getHtsFilesCount() > 0) {
            builder.addAllFiles(toFiles(v1Cohort.getHtsFilesList()));
        }

        return builder.build();
    }

}
