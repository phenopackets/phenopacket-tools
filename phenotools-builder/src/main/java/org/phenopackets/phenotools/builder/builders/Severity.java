package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;


public class Severity {

    private Severity() {
    }

    private static final OntologyClass BORDERLINE = ontologyClass("HP:0012827", "Borderline");
    private static final OntologyClass MILD = ontologyClass("HP:0012825", "Mild");
    private static final OntologyClass MODERATE = ontologyClass("HP:0012826", "Moderate");
    private static final OntologyClass SEVERE = ontologyClass("HP:0012828", "Severe");
    private static final OntologyClass PROFOUND = ontologyClass("HP:0012829", "Profound");

    /**
     * Having a minor degree of severity that is considered to be on the boundary between the normal and the abnormal ranges. For quantitative traits, a deviation of that is less than two standard deviations from the appropriate population mean.
     */
    public static OntologyClass borderline() {
        return BORDERLINE;
    }

    /**
     * Having a relatively minor degree of severity. For quantitative traits, a deviation of between two and three standard deviations from the appropriate population mean.
     */
    public static OntologyClass mild() {
        return MILD;
    }

    /**
     * Having a medium degree of severity. For quantitative traits, a deviation of between three and four standard deviations from the appropriate population mean.
     */
    public static OntologyClass moderate() {
        return MODERATE;
    }

    /**
     * Having a high degree of severity. For quantitative traits, a deviation of between four and five standard deviations from the appropriate population mean.
     */
    public static OntologyClass severe() {
        return SEVERE;
    }

    /**
     * Having an extremely high degree of severity. For quantitative traits, a deviation of more than five standard deviations from the appropriate population mean.
     */
    public static OntologyClass profound() {
        return PROFOUND;
    }

}
