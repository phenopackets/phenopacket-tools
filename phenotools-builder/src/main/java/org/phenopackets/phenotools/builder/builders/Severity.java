package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;


public class Severity {

    private Severity() {
    }

    private static final OntologyClass SEVERE = ontologyClass("HP:0012828", "Severe");

    public static OntologyClass severe() {
        return SEVERE;
    }

}
