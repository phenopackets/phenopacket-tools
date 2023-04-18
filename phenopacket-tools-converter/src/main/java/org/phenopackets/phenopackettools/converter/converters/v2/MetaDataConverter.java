package org.phenopackets.phenopackettools.converter.converters.v2;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.Update;

import java.util.List;
import java.util.Optional;

import static org.phenopackets.phenopackettools.converter.converters.v2.ExternalReferenceConverter.toExternalReferences;
import static org.phenopackets.phenopackettools.converter.converters.v2.ResourceConverter.toResources;

public class MetaDataConverter {

    private static final String VERSION = "2.0.0";

    private MetaDataConverter() {
    }

    public static Optional<MetaData> toMetaData(org.phenopackets.schema.v1.core.MetaData metaData) {
        if (metaData.equals(org.phenopackets.schema.v1.core.MetaData.getDefaultInstance()))
            return Optional.empty();

        List<ExternalReference> externalReferences = toExternalReferences(metaData.getExternalReferencesList());
        List<Resource> resources = toResources(metaData.getResourcesList());
        List<Update> updates = toUpdates(metaData.getUpdatesList());

        if (metaData.getPhenopacketSchemaVersion().isEmpty()
                && metaData.getCreated().equals(Timestamp.getDefaultInstance())
                && metaData.getCreatedBy().isEmpty()
                && metaData.getSubmittedBy().isEmpty()
                && externalReferences.isEmpty()
                && resources.isEmpty()
                && updates.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(MetaData.newBuilder()
                    .setPhenopacketSchemaVersion(VERSION)
                    .setCreated(metaData.getCreated())
                    .setCreatedBy(metaData.getCreatedBy())
                    .setSubmittedBy(metaData.getSubmittedBy())
                    .addAllExternalReferences(externalReferences)
                    .addAllResources(resources)
                    .addAllUpdates(updates)
                    .build());
        }
    }

    private static List<Update> toUpdates(List<org.phenopackets.schema.v1.core.Update> updatesList) {
        return updatesList.stream()
                .map(MetaDataConverter::toUpdate)
                .flatMap(Optional::stream)
                .toList();
    }

    private static Optional<Update> toUpdate(org.phenopackets.schema.v1.core.Update update) {
        if (update.equals(org.phenopackets.schema.v1.core.Update.getDefaultInstance()))
            return Optional.empty();

        if (update.getUpdatedBy().isEmpty()
                && update.getTimestamp().equals(Timestamp.getDefaultInstance())
                && update.getComment().isEmpty())
            return Optional.empty();
        else
            return Optional.of(Update.newBuilder()
                .setUpdatedBy(update.getUpdatedBy())
                .setTimestamp(update.getTimestamp())
                .setComment(update.getComment())
                .build());
    }
}
