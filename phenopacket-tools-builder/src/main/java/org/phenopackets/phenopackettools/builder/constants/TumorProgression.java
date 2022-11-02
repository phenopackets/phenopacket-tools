package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class TumorProgression {

  private static final OntologyClass PRIMARY_NEOPLASM = OntologyClassBuilder.ontologyClass("NCIT:C8509", "Primary Neoplasm");
  private static final OntologyClass METASTATIC_NEOPLASM = OntologyClassBuilder.ontologyClass("NCIT:C3261", "Metastatic Neoplasm");
  private static final OntologyClass RECURRENT_NEOPLASM = OntologyClassBuilder.ontologyClass("NCIT:C4798", "Recurrent Neoplasm");


  public static OntologyClass primaryNeoplasm() { return PRIMARY_NEOPLASM; }
  public static OntologyClass metastaticNeoplasm() { return METASTATIC_NEOPLASM; }
  public static OntologyClass recurrentNeoplasm() { return RECURRENT_NEOPLASM; }

}
