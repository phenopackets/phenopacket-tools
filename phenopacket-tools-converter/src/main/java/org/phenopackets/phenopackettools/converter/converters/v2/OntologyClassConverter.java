package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.List;
import java.util.Optional;

class OntologyClassConverter {

    private OntologyClassConverter() {
    }

    static List<OntologyClass> toOntologyClassList(List<org.phenopackets.schema.v1.core.OntologyClass> ontologyClasses) {
        // Returns non-default ontology classes.
        return ontologyClasses.stream()
                .map(OntologyClassConverter::toOntologyClass)
                .flatMap(Optional::stream)
                .toList();
    }

    static Optional<OntologyClass> toOntologyClass(org.phenopackets.schema.v1.core.OntologyClass v1OntologyClass) {
        if (v1OntologyClass.equals(org.phenopackets.schema.v1.core.OntologyClass.getDefaultInstance()))
            return Optional.empty();
        return Optional.of(OntologyClass.newBuilder()
                .setId(v1OntologyClass.getId())
                .setLabel(v1OntologyClass.getLabel())
                .build());
    }
}
