package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class MaterialSample {

  private static final OntologyClass ABNORMAL_SAMPLE = OntologyClassBuilder.ontologyClass("EFO:0009655", "abnormal sample");
  private static final OntologyClass REFERENCE_SAMPLE = OntologyClassBuilder.ontologyClass("EFO:0009654", "reference sample");


  public static OntologyClass abnormalSample() { return ABNORMAL_SAMPLE; }
  public static OntologyClass referenceSample() { return REFERENCE_SAMPLE; }

}
