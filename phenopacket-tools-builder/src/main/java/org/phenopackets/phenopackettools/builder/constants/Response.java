package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Response {

  private static final OntologyClass FAVORABLE = OntologyClassBuilder.ontologyClass("NCIT:C102560", "Favorable");
  private static final OntologyClass UNFAVORABLE = OntologyClassBuilder.ontologyClass("NCIT:C102561", "Unfavorable");


  public static OntologyClass favorable() { return FAVORABLE; }
  public static OntologyClass unfavorable() { return UNFAVORABLE; }

}
