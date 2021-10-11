package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;

public class OntologyClassBuilder {

    private final OntologyClass.Builder builder;

    public OntologyClassBuilder(String id, String label) {
        builder = OntologyClass.newBuilder().setId(id).setLabel(label);
    }

    public OntologyClass build() {
        return builder.build();
    }

    public static OntologyClassBuilder create(String id, String label) {
        return new OntologyClassBuilder(id, label);
    }

    public static OntologyClass ontologyClass(String id, String label) {
        return OntologyClass.newBuilder().setId(id).setLabel(label).build();
    }


}
