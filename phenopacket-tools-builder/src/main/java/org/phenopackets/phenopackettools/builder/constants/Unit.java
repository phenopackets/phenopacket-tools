package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Unit {

  private static final OntologyClass MILLIGRAM = OntologyClassBuilder.ontologyClass("UCUM:mg", "milligram");
  private static final OntologyClass GRAM = OntologyClassBuilder.ontologyClass("UCUM:m", "gram");
  private static final OntologyClass MICROGRAM = OntologyClassBuilder.ontologyClass("UCUM:ug", "microgram");
  private static final OntologyClass KILIGRAM = OntologyClassBuilder.ontologyClass("UCUM:kg", "kiligram");
  private static final OntologyClass MICROLITER = OntologyClassBuilder.ontologyClass("UCUM:uL", "microliter");
  private static final OntologyClass MILLILITER = OntologyClassBuilder.ontologyClass("UCUM:mL", "milliliter");
  private static final OntologyClass LITER = OntologyClassBuilder.ontologyClass("UCUM:L", "liter");
  private static final OntologyClass MICROMETER = OntologyClassBuilder.ontologyClass("UCUM:um", "micrometer");
  private static final OntologyClass MILLIMETER = OntologyClassBuilder.ontologyClass("UCUM:mm", "millimeter");
  private static final OntologyClass METER = OntologyClassBuilder.ontologyClass("UCUM:m", "meter");
  private static final OntologyClass MILLIGRAM_PER_KG = OntologyClassBuilder.ontologyClass("UCUM:mg.kg-1", "milligram per kilogram");
  private static final OntologyClass MILLIMETRES_OF_MERCURY = OntologyClassBuilder.ontologyClass("UO:0000272", "millimetres of mercury");
  private static final OntologyClass DIOPTER = OntologyClassBuilder.ontologyClass("NCIT:C100899", "Diopter");


  public static OntologyClass milligram() { return MILLIGRAM; }
  public static OntologyClass gram() { return GRAM; }
  public static OntologyClass microgram() { return MICROGRAM; }
  public static OntologyClass kilogram() { return KILIGRAM; }
  public static OntologyClass microliter() { return MICROLITER; }
  public static OntologyClass milliliter() { return MILLILITER; }
  public static OntologyClass liter() { return LITER; }
  public static OntologyClass micrometer() { return MICROMETER; }
  public static OntologyClass millimeter() { return MILLIMETER; }
  public static OntologyClass meter() { return METER; }
  public static OntologyClass mgPerKg() { return MILLIGRAM_PER_KG; }
  public static OntologyClass mmHg() { return MILLIMETRES_OF_MERCURY; }
  public static OntologyClass diopter() { return DIOPTER; }

}
