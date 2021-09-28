package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.Update;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.SCHEMA_VERSION;
import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.fromRFC3339;

public class MetaDataBuilder {



    private MetaData.Builder builder;


    public MetaDataBuilder(String created, String createdBy) {
        builder = MetaData.newBuilder()
                .setCreated(fromRFC3339(created))
                .setCreatedBy(createdBy)
                .setPhenopacketSchemaVersion(SCHEMA_VERSION); // only one option for schema version!
    }


    public MetaDataBuilder submittedBy(String submitter) {
        builder = builder.mergeFrom(builder.build()).setSubmittedBy(submitter);
        return this;
    }

    public MetaDataBuilder addResource(Resource r) {
        builder = builder.mergeFrom(builder.build()).addResources(r);
        return this;
    }

    public MetaDataBuilder addUpdate(Update u) {
        builder = builder.mergeFrom(builder.build()).addUpdates(u);
        return this;
    }

    public MetaDataBuilder addExternalReference(ExternalReference er) {
        builder = builder.mergeFrom(builder.build()).addExternalReferences(er);
        return this;
    }

    public MetaDataBuilder addExternalReference(String id, String description) {
        ExternalReference er = ExternalReference.newBuilder().setId(id).setDescription(description).build();
        builder = builder.mergeFrom(builder.build()).addExternalReferences(er);
        return this;
    }

    public MetaData build() {
        return builder.build();
    }



    public static MetaDataBuilder create(String created, String createdBy) {
        return new MetaDataBuilder(created, createdBy);
    }
}
