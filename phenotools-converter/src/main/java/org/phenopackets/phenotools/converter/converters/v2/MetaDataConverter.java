package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.Update;

import java.util.List;
import java.util.stream.Collectors;

import static org.phenopackets.phenotools.converter.converters.v2.ExternalReferenceConverter.toExternalReferences;
import static org.phenopackets.phenotools.converter.converters.v2.ResourceConverter.toResources;

public class MetaDataConverter {

    private MetaDataConverter() {
    }

    public static MetaData toMetaData(org.phenopackets.schema.v1.core.MetaData metaData) {
        return MetaData.newBuilder()
                .setPhenopacketSchemaVersion("2.0")
                .setCreated(metaData.getCreated())
                .setCreatedBy(metaData.getCreatedBy())
                .setSubmittedBy(metaData.getSubmittedBy())
                .addAllExternalReferences(toExternalReferences(metaData.getExternalReferencesList()))
                .addAllResources(toResources(metaData.getResourcesList()))
                .addAllUpdates(toUpdates(metaData.getUpdatesList()))
                .build();
    }

    private static List<Update> toUpdates(List<org.phenopackets.schema.v1.core.Update> updatesList) {
        return updatesList.stream().map(MetaDataConverter::toUpdate).collect(Collectors.toUnmodifiableList());
    }

    private static Update toUpdate(org.phenopackets.schema.v1.core.Update update) {
        return Update.newBuilder()
                .setUpdatedBy(update.getUpdatedBy())
                .setTimestamp(update.getTimestamp())
                .setComment(update.getComment())
                .build();
    }
}
