package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.ExternalReference;

import java.util.List;
import java.util.Optional;

class ExternalReferenceConverter {

    private ExternalReferenceConverter() {
    }

    static List<ExternalReference> toExternalReferences(List<org.phenopackets.schema.v1.core.ExternalReference> externalReferencesList) {
        return externalReferencesList.stream()
                .map(ExternalReferenceConverter::toExternalReference)
                .flatMap(Optional::stream)
                .toList();
    }

    static Optional<ExternalReference> toExternalReference(org.phenopackets.schema.v1.core.ExternalReference v1ExternalReference) {
        if (v1ExternalReference.equals(org.phenopackets.schema.v1.core.ExternalReference.getDefaultInstance()))
            return Optional.empty();

        return Optional.of(ExternalReference.newBuilder()
                .setId(v1ExternalReference.getId())
                .setDescription(v1ExternalReference.getDescription())
                // no v1 field for reference
                .build());
    }
}
