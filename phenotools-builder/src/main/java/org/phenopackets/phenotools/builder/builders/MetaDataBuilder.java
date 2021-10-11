package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.Update;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.*;

public class MetaDataBuilder {

    private final MetaData.Builder builder;

    public MetaDataBuilder(String created, String createdBy) {
        builder = MetaData.newBuilder()
                .setCreated(fromISO8601(created))
                .setCreatedBy(createdBy)
                .setPhenopacketSchemaVersion(SCHEMA_VERSION); // only one option for schema version!
    }

    public MetaDataBuilder submittedBy(String submitter) {
        builder.setSubmittedBy(submitter);
        return this;
    }

    public MetaDataBuilder resource(Resource r) {
        builder.addResources(r);
        return this;
    }

    public MetaDataBuilder update(Update u) {
        builder.addUpdates(u);
        return this;
    }

    public MetaDataBuilder externalReference(ExternalReference er) {
        builder.addExternalReferences(er);
        return this;
    }

    public MetaDataBuilder externalReference(String id, String description) {
        ExternalReference er = ExternalReference.newBuilder().setId(id).setDescription(description).build();
        builder.addExternalReferences(er);
        return this;
    }

    public MetaData build() {
        return builder.build();
    }

    public static MetaDataBuilder create(String created, String createdBy) {
        return new MetaDataBuilder(created, createdBy);
    }
}
