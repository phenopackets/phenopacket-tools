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

        boolean isDefault = true;
        MetaData.Builder builder = MetaData.newBuilder().setPhenopacketSchemaVersion(VERSION);

        Timestamp created = metaData.getCreated();
        if (!created.equals(Timestamp.getDefaultInstance())) {
            isDefault = false;
            builder.setCreated(created);
        }

        String createdBy = metaData.getCreatedBy();
        if (!createdBy.isEmpty()) {
            isDefault = false;
            builder.setCreatedBy(createdBy);
        }

        String submittedBy = metaData.getSubmittedBy();
        if (!submittedBy.isEmpty()) {
            isDefault = false;
            builder.setSubmittedBy(submittedBy);
        }

        List<Resource> resources = toResources(metaData.getResourcesList());
        if (!resources.isEmpty()) {
            isDefault = false;
            builder.addAllResources(resources);
        }

        List<Update> updates = toUpdates(metaData.getUpdatesList());
        if (!updates.isEmpty()) {
            isDefault = false;
            builder.addAllUpdates(updates);
        }

        List<ExternalReference> externalReferences = toExternalReferences(metaData.getExternalReferencesList());
        if (!externalReferences.isEmpty()) {
            isDefault = false;
            builder.addAllExternalReferences(externalReferences);
        }

        return isDefault
                ? Optional.empty()
                : Optional.of(builder.build());
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
