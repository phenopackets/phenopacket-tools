package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Unit {

  private static final OntologyClass DEGREE = OntologyClassBuilder.ontologyClass("UCUM:degree", "degree (plane angle)");
  private static final OntologyClass DIOPTER = OntologyClassBuilder.ontologyClass("UCUM:[diop]", "diopter");
  private static final OntologyClass GRAM = OntologyClassBuilder.ontologyClass("UCUM:g", "gram");
  private static final OntologyClass GRAM_PER_KG = OntologyClassBuilder.ontologyClass("UCUM:g/kg", "gram per kilogram");
  private static final OntologyClass KILIGRAM = OntologyClassBuilder.ontologyClass("UCUM:kg", "kiligram");
  private static final OntologyClass LITER = OntologyClassBuilder.ontologyClass("UCUM:L", "liter");
  private static final OntologyClass METER = OntologyClassBuilder.ontologyClass("UCUM:m", "meter");
  private static final OntologyClass MICROGRAM = OntologyClassBuilder.ontologyClass("UCUM:ug", "microgram");
  private static final OntologyClass MICROGRAM_PER_DECILITER = OntologyClassBuilder.ontologyClass("UCUM:ug/dL", "microgram per deciliter");
  private static final OntologyClass MICROGRAM_PER_LITER = OntologyClassBuilder.ontologyClass("UCUM:ug/L", "microgram per liter");
  private static final OntologyClass MICROLITER = OntologyClassBuilder.ontologyClass("UCUM:uL", "microliter");
  private static final OntologyClass MICROMETER = OntologyClassBuilder.ontologyClass("UCUM:um", "micrometer");
  private static final OntologyClass MILLIGRAM = OntologyClassBuilder.ontologyClass("UCUM:mg", "milligram");
  private static final OntologyClass MILLIGRAM_PER_LITER = OntologyClassBuilder.ontologyClass("UCUM:mg/L", "milligram per liter");
  private static final OntologyClass MILLIGRAM_PER_DL = OntologyClassBuilder.ontologyClass("UCUM:mg/dL", "milligram per deciliter");
  private static final OntologyClass MILLIGRAM_PER_KG = OntologyClassBuilder.ontologyClass("UCUM:mg.kg-1", "milligram per kilogram");
  private static final OntologyClass MILLILITER = OntologyClassBuilder.ontologyClass("UCUM:mL", "milliliter");
  private static final OntologyClass MILLIMETER = OntologyClassBuilder.ontologyClass("UCUM:mm", "millimeter");
  private static final OntologyClass MILLIMETRES_OF_MERCURY = OntologyClassBuilder.ontologyClass("UCUM:mm[Hg]", "millimetres of mercury");
  private static final OntologyClass MILLIMOLE = OntologyClassBuilder.ontologyClass("UCUM:mmol", "millimole");
  private static final OntologyClass MOLE = OntologyClassBuilder.ontologyClass("UCUM:mol", "mole");
  private static final OntologyClass MOLE_PER_LITER = OntologyClassBuilder.ontologyClass("UCUM:mol/L", "mole per liter");
  private static final OntologyClass MOLE_PER_MILLILITER = OntologyClassBuilder.ontologyClass("UCUM:mol/mL", "mole per milliliter");
  private static final OntologyClass ENZYME_UNIT_PER_LITER = OntologyClassBuilder.ontologyClass("UCUM:U/L", "enzyme unit per liter");


  public static OntologyClass degreeOfAngle() { return DEGREE; }
  public static OntologyClass diopter() { return DIOPTER; }
  public static OntologyClass gram() { return GRAM; }
  public static OntologyClass gramPerKilogram() { return GRAM_PER_KG; }
  public static OntologyClass kilogram() { return KILIGRAM; }
  public static OntologyClass liter() { return LITER; }
  public static OntologyClass meter() { return METER; }
  public static OntologyClass microgram() { return MICROGRAM; }
  public static OntologyClass microgramPerDeciliter() { return MICROGRAM_PER_DECILITER; }
  public static OntologyClass microgramPerLiter() { return MICROGRAM_PER_LITER; }
  public static OntologyClass microliter() { return MICROLITER; }
  public static OntologyClass micrometer() { return MICROMETER; }
  public static OntologyClass milligram() { return MILLIGRAM; }
  public static OntologyClass milligramPerLiter() { return MILLIGRAM_PER_LITER; }
  public static OntologyClass milligramPerDeciliter() { return MILLIGRAM_PER_DL; }
  public static OntologyClass mgPerKg() { return MILLIGRAM_PER_KG; }
  public static OntologyClass milliliter() { return MILLILITER; }
  public static OntologyClass millimeter() { return MILLIMETER; }
  public static OntologyClass mmHg() { return MILLIMETRES_OF_MERCURY; }
  public static OntologyClass millimole() { return MILLIMOLE; }
  public static OntologyClass mole() { return MOLE; }
  public static OntologyClass molePerLiter() { return MOLE_PER_LITER; }
  public static OntologyClass molePerMilliliter() { return MOLE_PER_MILLILITER; }
  public static OntologyClass enzymeUnitPerLiter() { return ENZYME_UNIT_PER_LITER; }

}
