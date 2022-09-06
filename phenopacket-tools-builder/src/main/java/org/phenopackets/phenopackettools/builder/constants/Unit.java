package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class Unit {

    private static final OntologyClass MILLIMETRES_OF_MERCURY = ontologyClass("UO:0000272", "millimetres of mercury");
    private static final OntologyClass MILLIMETER = ontologyClass("UO:0000016", "millimeter");


    private static final OntologyClass MILLIGRAM = ontologyClass("uom:mg", "milligram");
    private static final OntologyClass MILLIGRAM_PER_KILOGRAM = ontologyClass("UO:0000308", "milligram per kilogram");
    private static final OntologyClass DIOPTER = ontologyClass("NCIT:C100899", "Diopter");

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

    // mass
    public static OntologyClass milligram() { return MILLIGRAM; }

    public static OntologyClass mgPerKg() {
        return MILLIGRAM_PER_KILOGRAM;
    }

    //A unit of measurement of the optical power of a curved mirror or lens represented by the inverse of the focal length in meters.
    public static OntologyClass diop() {return DIOPTER; }
}
