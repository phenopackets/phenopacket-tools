package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.Update;

import java.util.List;
import java.util.stream.Collectors;

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

    private static List<Resource> toResources(List<org.phenopackets.schema.v1.core.Resource> resourcesList) {
        return resourcesList.stream().map(MetaDataConverter::toResource).collect(Collectors.toUnmodifiableList());
    }

    private static Resource toResource(org.phenopackets.schema.v1.core.Resource resource) {
        return Resource.newBuilder()
                .setId(resource.getId())
                .setName(resource.getName())
                .setVersion(resource.getVersion())
                .setUrl(resource.getUrl())
                .setIriPrefix(resource.getIriPrefix())
                .setNamespacePrefix(resource.getNamespacePrefix())
                .build();
    }

    private static List<ExternalReference> toExternalReferences(List<org.phenopackets.schema.v1.core.ExternalReference> externalReferencesList) {
        return externalReferencesList.stream().map(ExternalReferenceConverter::toExternalReference).collect(Collectors.toUnmodifiableList());
    }



}
