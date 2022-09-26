package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Onset {

  private static final OntologyClass ANTENATAL_ONSET = OntologyClassBuilder.ontologyClass("HP:0030674", "Antenatal onset");
  private static final OntologyClass EMBRYONAL_ONSET = OntologyClassBuilder.ontologyClass("HP:0011460", "Embryonal onset");
  private static final OntologyClass FETAL_ONSET = OntologyClassBuilder.ontologyClass("HP:0011461", "Fetal onset");
  private static final OntologyClass LATE_FIRST_TRIMESTER_ONSET = OntologyClassBuilder.ontologyClass("HP:0034199", "Late first trimester onset");
  private static final OntologyClass SECOND_TRIMESTER_ONSET = OntologyClassBuilder.ontologyClass("HP:0034198", "Second trimester onset");
  private static final OntologyClass THIRD_TRIMESTER_ONSET = OntologyClassBuilder.ontologyClass("HP:0034197", "Third trimester onset");
  private static final OntologyClass CONGENITAL_ONSET = OntologyClassBuilder.ontologyClass("HP:0003577", "Congenital onset");
  private static final OntologyClass NEONATAL_ONSET = OntologyClassBuilder.ontologyClass("HP:0003623", "Neonatal onset");
  private static final OntologyClass INFANTILE_ONSET = OntologyClassBuilder.ontologyClass("HP:0003593", "Infantile onset");
  private static final OntologyClass CHILDHOOD_ONSET = OntologyClassBuilder.ontologyClass("HP:0011463", "Childhood onset");
  private static final OntologyClass JUVENILE_ONSET = OntologyClassBuilder.ontologyClass("HP:0003621", "Juvenile onset");
  private static final OntologyClass ADULT_ONSET = OntologyClassBuilder.ontologyClass("HP:0003581", "Adult onset");
  private static final OntologyClass YOUNG_ADULT_ONSET = OntologyClassBuilder.ontologyClass("HP:0011462", "Young adult onset");
  private static final OntologyClass EARLY_YOUNG_ADULT_ONSET = OntologyClassBuilder.ontologyClass("HP:0025708", "Early young adult onset");
  private static final OntologyClass INTERMEDIATE_YOUNG_ADULT_ONSET = OntologyClassBuilder.ontologyClass("HP:0025709", "Intermediate young adult onset");
  private static final OntologyClass LATE_YOUNG_ADULT_ONSET = OntologyClassBuilder.ontologyClass("HP:0025710", "Late young adult onset");
  private static final OntologyClass MIDDLE_AGE_ONSET = OntologyClassBuilder.ontologyClass("HP:0003596", "Middle age onset");
  private static final OntologyClass LATE_ONSET = OntologyClassBuilder.ontologyClass("HP:0003584", "Late onset");


  public static OntologyClass antenatalOnset() { return ANTENATAL_ONSET; }
  public static OntologyClass embryonalOnset() { return EMBRYONAL_ONSET; }
  public static OntologyClass fetalOnset() { return FETAL_ONSET; }
  public static OntologyClass lateFirstTrimesterOnset() { return LATE_FIRST_TRIMESTER_ONSET; }
  public static OntologyClass secondTrimesterOnset() { return SECOND_TRIMESTER_ONSET; }
  public static OntologyClass thirdTrimesterOnset() { return THIRD_TRIMESTER_ONSET; }
  public static OntologyClass congenitalOnset() { return CONGENITAL_ONSET; }
  public static OntologyClass neonatalOnset() { return NEONATAL_ONSET; }
  public static OntologyClass infantileOnset() { return INFANTILE_ONSET; }
  public static OntologyClass childhoodOnset() { return CHILDHOOD_ONSET; }
  public static OntologyClass juvenileOnset() { return JUVENILE_ONSET; }
  public static OntologyClass adultOnset() { return ADULT_ONSET; }
  public static OntologyClass youngAdultOnset() { return YOUNG_ADULT_ONSET; }
  public static OntologyClass earlyYoungAdultOnset() { return EARLY_YOUNG_ADULT_ONSET; }
  public static OntologyClass intermediateYoungAdultOnset() { return INTERMEDIATE_YOUNG_ADULT_ONSET; }
  public static OntologyClass lateYoungAdultOnset() { return LATE_YOUNG_ADULT_ONSET; }
  public static OntologyClass middleAgeOnset() { return MIDDLE_AGE_ONSET; }
  public static OntologyClass lateOnset() { return LATE_ONSET; }

}
