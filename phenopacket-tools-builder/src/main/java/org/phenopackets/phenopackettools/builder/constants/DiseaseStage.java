package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class DiseaseStage {

  private static final OntologyClass STAGE_0 = OntologyClassBuilder.ontologyClass("NCIT:C28051", "Stage 0");
  private static final OntologyClass STAGE_I = OntologyClassBuilder.ontologyClass("NCIT:C27966", "Stage I");
  private static final OntologyClass STAGE_II = OntologyClassBuilder.ontologyClass("NCIT:C28054", "Stage II");
  private static final OntologyClass STAGE_III = OntologyClassBuilder.ontologyClass("NCIT:C27970", "Stage III");
  private static final OntologyClass STAGE_IV = OntologyClassBuilder.ontologyClass("NCIT:C27971", "Stage IV");
  private static final OntologyClass NYHA_I = OntologyClassBuilder.ontologyClass("NCIT:C66904", "New York Heart Association Class I");
  private static final OntologyClass NYHA_II = OntologyClassBuilder.ontologyClass("NCIT:C66905", "New York Heart Association Class II");
  private static final OntologyClass NYHA_III = OntologyClassBuilder.ontologyClass("NCIT:C66907", "New York Heart Association Class III");
  private static final OntologyClass NYHA_III_IV = OntologyClassBuilder.ontologyClass("NCIT:C7922", "New York Heart Association Class III/IV");
  private static final OntologyClass NYHA_IV = OntologyClassBuilder.ontologyClass("NCIT:C66908", "New York Heart Association Class IV");


  public static OntologyClass stage0() { return STAGE_0; }
  public static OntologyClass stageI() { return STAGE_I; }
  public static OntologyClass stageII() { return STAGE_II; }
  public static OntologyClass stageIII() { return STAGE_III; }
  public static OntologyClass stageIV() { return STAGE_IV; }
  public static OntologyClass nyhaClassI() { return NYHA_I; }
  public static OntologyClass nyhaClassII() { return NYHA_II; }
  public static OntologyClass nyhaClassIII() { return NYHA_III; }
  public static OntologyClass nyhaClassIII_or_IV() { return NYHA_III_IV; }
  public static OntologyClass nyhaClassIV() { return NYHA_IV; }

}
