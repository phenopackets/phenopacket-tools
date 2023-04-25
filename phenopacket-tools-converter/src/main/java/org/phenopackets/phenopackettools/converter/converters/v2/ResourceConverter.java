package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Resource;

import java.util.List;
import java.util.Optional;

class ResourceConverter {

    private ResourceConverter() {
    }

    static List<Resource> toResources(List<org.phenopackets.schema.v1.core.Resource> resourcesList) {
        return resourcesList.stream()
                .map(ResourceConverter::toResource)
                .flatMap(Optional::stream)
                .toList();
    }

    static Optional<Resource> toResource(org.phenopackets.schema.v1.core.Resource resource) {
        if (resource.equals(org.phenopackets.schema.v1.core.Resource.getDefaultInstance()))
            return Optional.empty();

        if (resource.getId().isEmpty()
                && resource.getName().isEmpty()
                && resource.getVersion().isEmpty()
                && resource.getUrl().isEmpty()
                && resource.getIriPrefix().isEmpty()
                && resource.getName().isEmpty()) {
            return Optional.empty();
        } else
            return Optional.of(Resource.newBuilder()
                    .setId(resource.getId())
                    .setName(resource.getName())
                    .setVersion(resource.getVersion())
                    .setUrl(resource.getUrl())
                    .setIriPrefix(resource.getIriPrefix())
                    .setNamespacePrefix(resource.getNamespacePrefix())
                    .build());
    }
}
