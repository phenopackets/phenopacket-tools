package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Severity {

  private static final OntologyClass BORDERLINE = OntologyClassBuilder.ontologyClass("HP:0012827", "Borderline");
  private static final OntologyClass MILD = OntologyClassBuilder.ontologyClass("HP:0012825", "Mild");
  private static final OntologyClass MODERATE = OntologyClassBuilder.ontologyClass("HP:0012826", "Moderate");
  private static final OntologyClass SEVERE = OntologyClassBuilder.ontologyClass("HP:0012828", "Severe");
  private static final OntologyClass PROFOUND = OntologyClassBuilder.ontologyClass("HP:0012829", "Profound");


  public static OntologyClass borderline() { return BORDERLINE; }
  public static OntologyClass mild() { return MILD; }
  public static OntologyClass moderate() { return MODERATE; }
  public static OntologyClass severe() { return SEVERE; }
  public static OntologyClass profound() { return PROFOUND; }

}
