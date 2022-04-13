package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.of;

public class Unit {

    private static final OntologyClass MILLIMETRES_OF_MERCURY = of("UO:0000272", "millimetres of mercury");
    private static final OntologyClass MILLIMETER = of("UO:0000016", "millimeter");
    private static final OntologyClass MILLIGRAM_PER_KILOGRAM = of("UO:0000308", "milligram per kilogram");

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

    public static OntologyClass mgPerKg() {
        return MILLIGRAM_PER_KILOGRAM;
    }
}
