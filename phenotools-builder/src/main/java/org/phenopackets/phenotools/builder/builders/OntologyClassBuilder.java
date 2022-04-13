package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;

public class OntologyClassBuilder {

    private OntologyClassBuilder() {
    }

    public static OntologyClass of(String id, String label) {
        return OntologyClass.newBuilder().setId(id).setLabel(label).build();
    }




}
