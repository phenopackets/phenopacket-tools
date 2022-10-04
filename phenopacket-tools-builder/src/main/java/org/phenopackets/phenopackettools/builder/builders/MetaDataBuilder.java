package org.phenopackets.phenopackettools.builder.builders;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.Update;

import java.time.Instant;

import static org.phenopackets.phenopackettools.builder.builders.TimestampBuilder.fromISO8601;

public class MetaDataBuilder {

    private static final String SCHEMA_VERSION = "2.0.0";

    private final MetaData.Builder builder;

    private MetaDataBuilder(String created, String createdBy) {
        builder = MetaData.newBuilder()
                .setCreated(fromISO8601(created))
                .setCreatedBy(createdBy)
                .setPhenopacketSchemaVersion(SCHEMA_VERSION); // only one option for schema version!
    }

    private MetaDataBuilder(Timestamp createdTimeStamp, String createdBy) {
        builder = MetaData.newBuilder()
                .setCreated(createdTimeStamp)
                .setCreatedBy(createdBy)
                .setPhenopacketSchemaVersion(SCHEMA_VERSION); // only one option for schema version!
    }

    public static MetaDataBuilder builder(String created, String createdBy) {
        return new MetaDataBuilder(created, createdBy);
    }

    public static MetaDataBuilder builder(Timestamp createdTimeStamp, String createdBy) {
        return new MetaDataBuilder(createdTimeStamp, createdBy);
    }

    /**
     * Create a new {@link MetaDataBuilder} with time set to now.
     */
    public static MetaDataBuilder builder(String createdBy) {
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        return new MetaDataBuilder(timestamp, createdBy);
    }

    public MetaDataBuilder submittedBy(String submitter) {
        builder.setSubmittedBy(submitter);
        return this;
    }

    public MetaDataBuilder addResource(Resource r) {
        builder.addResources(r);
        return this;
    }

    public MetaDataBuilder addUpdate(Update u) {
        builder.addUpdates(u);
        return this;
    }

    public MetaDataBuilder addExternalReference(ExternalReference er) {
        builder.addExternalReferences(er);
        return this;
    }

    public MetaDataBuilder addExternalReference(String id, String description) {
        ExternalReference er = ExternalReference.newBuilder().setId(id).setDescription(description).build();
        return addExternalReference(er);
    }

    public MetaData build() {
        return builder.build();
    }
}
