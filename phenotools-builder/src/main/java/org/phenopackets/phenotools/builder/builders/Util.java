package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.of;

/**
 * A convenience class for generating commonly used messages
 */
public class Util {

    /**
     * @return class for mm Hg, needed for blood pressure and intraocular pressure
     */
    public static OntologyClass mmHg() {
        return of("UO:0000272", "millimetres of mercury");
    }

    public static OntologyClass mm() {
        return of("UO:0000016", "millimeter");
    }

    public static OntologyClass mm_per_kg() {
        return of("UO:0000308", "milliliter per kilogram");
    }


    public static OntologyClass biopsy() {
        return of("NCIT:C15189", "Biopsy");
    }

    public static OntologyClass right() {
        return of("HP:0012834", "Right");
    }

    public static OntologyClass left() {
        return of("HP:0012835", "Left");
    }

    public static OntologyClass unilateral() {
        return of("HP:0012833", "Unilateral");
    }

    public static OntologyClass bilateral() {
        return of("HP:0012832", "Bilateral");
    }

    // Organs
    public static OntologyClass eye() {
        return of("UBERON:0000970", "eye");
    }


    public static OntologyClass primaryNeoplasm() {
        return of("NCIT:C8509", "Primary Neoplasm");
    }

}

