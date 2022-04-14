package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;

public class OntologyClassBuilder {

    private OntologyClassBuilder() {
    }

    public static OntologyClass ontologyClass(String id, String label) {
        return OntologyClass.newBuilder().setId(id).setLabel(label).build();
    }




}
