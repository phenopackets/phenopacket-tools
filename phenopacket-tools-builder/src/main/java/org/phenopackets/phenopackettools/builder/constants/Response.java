package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Response {

  private static final OntologyClass FAVORABLE_RESPONSE = OntologyClassBuilder.ontologyClass("NCIT:C123584", "Favorable Response");
  private static final OntologyClass UNFAVORABLE_RESPONSE = OntologyClassBuilder.ontologyClass("NCIT:C123617", "Unfavorable Response");
  private static final OntologyClass NO_RESPONSE = OntologyClassBuilder.ontologyClass("NCIT:C123600", "No Response");
  private static final OntologyClass STRINGENT_COMPLETE_RESPONSE = OntologyClassBuilder.ontologyClass("NCIT:C123614", "Stringent Complete Response");
  private static final OntologyClass MINIMAL_RESPONSE = OntologyClassBuilder.ontologyClass("NCIT:C123598", "Minimal Response");
  private static final OntologyClass COMPLETE_REMISSION = OntologyClassBuilder.ontologyClass("NCIT:C4870", "Complete Remission");
  private static final OntologyClass PARTIAL_REMISSION = OntologyClassBuilder.ontologyClass("NCIT:C18058", "Partial Remission");
  private static final OntologyClass PRIMARY_REFRACTORY = OntologyClassBuilder.ontologyClass("NCIT:C70604", "Primary Refractory");
  private static final OntologyClass iRECIST_COMPLETE_RESPONSE = OntologyClassBuilder.ontologyClass("NCIT:C142357", "iRECIST Complete Response");
  private static final OntologyClass iRECIST_CONFIRMED_PROGRESSIVE_DISEASE = OntologyClassBuilder.ontologyClass("NCIT:C142356", "iRECIST Confirmed Progressive Disease");
  private static final OntologyClass iRECIST_PARTIAL_RESPONSE = OntologyClassBuilder.ontologyClass("NCIT:C142358", "iRECIST Partial Response");
  private static final OntologyClass iRECIST_STABLE_DISEASE = OntologyClassBuilder.ontologyClass("NCIT:C142359", "iRECIST Stable Disease");
  private static final OntologyClass iRECIST_UNCONFIRMED_PROGRESSIVE_DISEASE = OntologyClassBuilder.ontologyClass("NCIT:C142360", "iRECIST Unconfirmed Progressive Disease");


  public static OntologyClass favorableResponse() { return FAVORABLE_RESPONSE; }
  public static OntologyClass unfavorableResponse() { return UNFAVORABLE_RESPONSE; }
  public static OntologyClass noResponse() { return NO_RESPONSE; }
  public static OntologyClass stringentCompleteResponse() { return STRINGENT_COMPLETE_RESPONSE; }
  public static OntologyClass minimalResponse() { return MINIMAL_RESPONSE; }
  public static OntologyClass completeRemission() { return COMPLETE_REMISSION; }
  public static OntologyClass partialRemission() { return PARTIAL_REMISSION; }
  public static OntologyClass primaryRefractory() { return PRIMARY_REFRACTORY; }
  public static OntologyClass iRECISTCompleteResponse() { return iRECIST_COMPLETE_RESPONSE; }
  public static OntologyClass iRECISTConfirmedProgressiveDisease() { return iRECIST_CONFIRMED_PROGRESSIVE_DISEASE; }
  public static OntologyClass iRECISTPartialResponse() { return iRECIST_PARTIAL_RESPONSE; }
  public static OntologyClass iRECISTStableDisease() { return iRECIST_STABLE_DISEASE; }
  public static OntologyClass iRECISTUnconfirmedProgressiveDisease() { return iRECIST_UNCONFIRMED_PROGRESSIVE_DISEASE; }

}
