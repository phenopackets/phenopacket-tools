package org.phenopackets.phenotools.builder.builders.constants;

import org.phenopackets.schema.v2.core.OntologyClass;

public final class Onset {

    private static final OntologyClass EMBRYONAL = OntologyClass.newBuilder().setId("HP:0011460").setLabel("Embryonal onset").build();
    private static final OntologyClass FETAL = OntologyClass.newBuilder().setId("HP:0011461").setLabel("Fetal onset").build();
    private static final OntologyClass ANTENATAL = OntologyClass.newBuilder().setId("HP:0030674").setLabel("Antenatal onset").build();
    private static final OntologyClass CONGENITAL = OntologyClass.newBuilder().setId("HP:0003577").setLabel("Congenital onset").build();
    private static final OntologyClass NEONATAL = OntologyClass.newBuilder().setId("HP:0003623").setLabel("Neonatal onset").build();
    private static final OntologyClass PEDIATRIC = OntologyClass.newBuilder().setId("HP:0410280").setLabel("Pediatric onset").build();
    private static final OntologyClass INFANTILE = OntologyClass.newBuilder().setId("HP:0003593").setLabel("Infantile onset").build();
    private static final OntologyClass JUVENILE = OntologyClass.newBuilder().setId("HP:0003621").setLabel("Juvenile onset").build();
    private static final OntologyClass CHILDHOOD = OntologyClass.newBuilder().setId("HP:0011463").setLabel("Childhood onset").build();
    private static final OntologyClass YOUNG_ADULT = OntologyClass.newBuilder().setId("HP:0011462").setLabel("Young adult onset").build();
    private static final OntologyClass ADULT = OntologyClass.newBuilder().setId("HP:0003581").setLabel("Adult onset").build();
    private static final OntologyClass MIDDLE_AGE = OntologyClass.newBuilder().setId("HP:0003596").setLabel("Middle age onset").build();
    private static final OntologyClass LATE = OntologyClass.newBuilder().setId("HP:0003584").setLabel("Late onset").build();

    private Onset() {
    }

    public static OntologyClass embryonal() {
        return EMBRYONAL;
    }

    public static OntologyClass fetal() {
        return FETAL;
    }

    public static OntologyClass antenatal() {
        return ANTENATAL;
    }

    public static OntologyClass congenital() {
        return CONGENITAL;
    }

    public static OntologyClass neonatal() {
        return NEONATAL;
    }

    public static OntologyClass pediatric() {
        return PEDIATRIC;
    }

    public static OntologyClass infantile() {
        return INFANTILE;
    }

    public static OntologyClass juvenile() {
        return JUVENILE;
    }

    public static OntologyClass childhood() {
        return CHILDHOOD;
    }

    public static OntologyClass youngAdult() {
        return YOUNG_ADULT;
    }

    public static OntologyClass adult() {
        return ADULT;
    }

    public static OntologyClass middleAge() {
        return MIDDLE_AGE;
    }

    public static OntologyClass late() {
        return LATE;
    }


}
