package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.ExternalReference;

public class ExternalReferenceBuilder {

    private final ExternalReference.Builder builder;

    public ExternalReferenceBuilder() {
        builder = ExternalReference.newBuilder();
    }
    public ExternalReferenceBuilder id(String id) {
        builder.setId(id);
        return this;
    }

    public ExternalReferenceBuilder reference(String ref) {
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

    public static ExternalReferenceBuilder create() {
        return new ExternalReferenceBuilder();
    }


}
