package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Gender {

  private static final OntologyClass IDENTIFIES_AS_MALE = OntologyClassBuilder.ontologyClass("LOINC:LA22878-5", "Identifies as male");
  private static final OntologyClass IDENTIFIES_AS_FEMALE = OntologyClassBuilder.ontologyClass("LOINC:LA22879-3", "Identifies as female");
  private static final OntologyClass FEMALE_TO_MALE_TRANSSEXUAL = OntologyClassBuilder.ontologyClass("LOINC:LA22880-1", "Female-to-male transsexual");
  private static final OntologyClass MALE_TO_FEMALE_TRANSSEXUAL = OntologyClassBuilder.ontologyClass("LOINC:LA22881-9", "Male-to-female transsexual");
  private static final OntologyClass IDENTIFIES_AS_NON_CONFORMING = OntologyClassBuilder.ontologyClass("LOINC:LA22882-7", "Identifies as non-conforming");
  private static final OntologyClass OTHER_GENDER = OntologyClassBuilder.ontologyClass("LOINC:LA46-8", "other");
  private static final OntologyClass ASKED_BUT_UNKNOWN = OntologyClassBuilder.ontologyClass("LOINC:LA20384-6", "Asked but unknown");


  public static OntologyClass identifiesAsMale() { return IDENTIFIES_AS_MALE; }
  public static OntologyClass identifiesAsFemale() { return IDENTIFIES_AS_FEMALE; }
  public static OntologyClass femaleToMaleTranssexual() { return FEMALE_TO_MALE_TRANSSEXUAL; }
  public static OntologyClass maleToFemaleTranssexual() { return MALE_TO_FEMALE_TRANSSEXUAL; }
  public static OntologyClass identifiesAsNonConforming() { return IDENTIFIES_AS_NON_CONFORMING; }
  public static OntologyClass otherGender() { return OTHER_GENDER; }
  public static OntologyClass askedButUnknown() { return ASKED_BUT_UNKNOWN; }

}
