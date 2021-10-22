package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceConverter {

    private ResourceConverter() {
    }

    public static List<Resource> toResources(List<org.phenopackets.schema.v1.core.Resource> resourcesList) {
        return resourcesList.stream().map(ResourceConverter::toResource).collect(Collectors.toUnmodifiableList());
    }

    public static Resource toResource(org.phenopackets.schema.v1.core.Resource resource) {
        return Resource.newBuilder()
                .setId(resource.getId())
                .setName(resource.getName())
                .setVersion(resource.getVersion())
                .setUrl(resource.getUrl())
                .setIriPrefix(resource.getIriPrefix())
                .setNamespacePrefix(resource.getNamespacePrefix())
                .build();
    }
}
