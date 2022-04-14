package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.ExternalReference;

public class ExternalReferenceBuilder {

    private final ExternalReference.Builder builder;

    private ExternalReferenceBuilder() {
        builder = ExternalReference.newBuilder();
    }

    public static ExternalReference externalReference(String id, String description) {
        return ExternalReference.newBuilder().setId(id).setDescription(description).build();
    }

    public static ExternalReferenceBuilder builder() {
        return new ExternalReferenceBuilder();
    }

    public ExternalReferenceBuilder id(String id) {
        builder.setId(id);
        return this;
    }

    public ExternalReferenceBuilder builder(String ref) {
        builder.setReference(ref);
        return this;
    }

    public ExternalReferenceBuilder description(String desc) {
        builder.setDescription(desc);
        return this;
    }

    public ExternalReference build() {
        return builder.build();
    }
}
