package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

public class Unit {

    private static final OntologyClass MILLIMETRES_OF_MERCURY = ontologyClass("UO:0000272", "millimetres of mercury");
    private static final OntologyClass MILLIMETER = ontologyClass("UO:0000016", "millimeter");
    private static final OntologyClass MILLILITER_PER_KILOGRAM = ontologyClass("UO:0000308", "milliliter per kilogram");

    private Unit() {
    }

    /**
     * @return class for mm Hg, needed for blood pressure and intraocular pressure
     */
    public static OntologyClass mmHg() {
        return MILLIMETRES_OF_MERCURY;
    }

    public static OntologyClass mm() {
        return MILLIMETER;
    }

    public static OntologyClass mlPerKg() {
        return MILLILITER_PER_KILOGRAM;
    }
}
