package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.List;
import java.util.stream.Collectors;

public class OntologyClassConverter {

    private OntologyClassConverter() {
    }

    public static List<OntologyClass> toOntologyClassList(List<org.phenopackets.schema.v1.core.OntologyClass> ontologyClasses) {
        return ontologyClasses.stream().map(OntologyClassConverter::toOntologyClass).collect(Collectors.toUnmodifiableList());
    }

    public static OntologyClass toOntologyClass(org.phenopackets.schema.v1.core.OntologyClass v1OntologyClass) {
        if (org.phenopackets.schema.v1.core.OntologyClass.getDefaultInstance().equals(v1OntologyClass)) {
            return OntologyClass.getDefaultInstance();
        }
        return OntologyClass.newBuilder()
                .setId(v1OntologyClass.getId())
                .setLabel(v1OntologyClass.getLabel())
                .build();
    }
}
