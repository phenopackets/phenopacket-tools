package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Assays {

  private static final OntologyClass CREATINE_KINASE = OntologyClassBuilder.ontologyClass("LOINC:2157-6", "Creatine kinase [Enzymatic activity/volume] in Serum or Plasma");


  public static OntologyClass creatineKinaseActivity() { return CREATINE_KINASE; }

}
