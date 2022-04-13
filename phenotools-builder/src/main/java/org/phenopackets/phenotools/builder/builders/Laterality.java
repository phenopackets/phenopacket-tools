package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;


public class Laterality {

    private static final OntologyClass RIGHT = OntologyClassBuilder.ontologyClass("HP:0012834", "Right");
    private static final OntologyClass LEFT = OntologyClassBuilder.ontologyClass("HP:0012835", "Left");
    private static final OntologyClass UNILATERAL = OntologyClassBuilder.ontologyClass("HP:0012833", "Unilateral");
    private static final OntologyClass BILATERAL = OntologyClassBuilder.ontologyClass("HP:0012832", "Bilateral");

    private Laterality() {
    }

    public static OntologyClass right() {
        return RIGHT;
    }

    public static OntologyClass left() {
        return LEFT;
    }

    public static OntologyClass unilateral() {
        return UNILATERAL;
    }

    public static OntologyClass bilateral() {
        return BILATERAL;
    }
}
