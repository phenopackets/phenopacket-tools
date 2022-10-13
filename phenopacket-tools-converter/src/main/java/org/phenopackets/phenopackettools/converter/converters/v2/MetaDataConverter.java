package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Update;

import java.util.List;

import static org.phenopackets.phenopackettools.converter.converters.v2.ExternalReferenceConverter.toExternalReferences;
import static org.phenopackets.phenopackettools.converter.converters.v2.ResourceConverter.toResources;

public class MetaDataConverter {

    private static final String VERSION = "2.0.0";

    private MetaDataConverter() {
    }

    public static MetaData toMetaData(org.phenopackets.schema.v1.core.MetaData metaData) {
        return MetaData.newBuilder()
                .setPhenopacketSchemaVersion(VERSION)
                .setCreated(metaData.getCreated())
                .setCreatedBy(metaData.getCreatedBy())
                .setSubmittedBy(metaData.getSubmittedBy())
                .addAllExternalReferences(toExternalReferences(metaData.getExternalReferencesList()))
                .addAllResources(toResources(metaData.getResourcesList()))
                .addAllUpdates(toUpdates(metaData.getUpdatesList()))
                .build();
    }

    private static List<Update> toUpdates(List<org.phenopackets.schema.v1.core.Update> updatesList) {
        return updatesList.stream().map(MetaDataConverter::toUpdate).toList();
    }

    private static Update toUpdate(org.phenopackets.schema.v1.core.Update update) {
        return Update.newBuilder()
                .setUpdatedBy(update.getUpdatedBy())
                .setTimestamp(update.getTimestamp())
                .setComment(update.getComment())
                .build();
    }
}
