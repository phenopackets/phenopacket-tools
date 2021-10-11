package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.ExternalReference;

public class ExternalReferenceConverter {

    private ExternalReferenceConverter() {
    }

    public static ExternalReference toExternalReference(org.phenopackets.schema.v1.core.ExternalReference v1ExternalReference) {
        if (v1ExternalReference.equals(org.phenopackets.schema.v1.core.ExternalReference.getDefaultInstance())) {
            return ExternalReference.getDefaultInstance();
        }
        return ExternalReference.newBuilder()
                .setId(v1ExternalReference.getId())
                .setDescription(v1ExternalReference.getDescription())
                // no v1 field for reference
                .build();
    }
}
