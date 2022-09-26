package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class MedicalActions {

  private static final OntologyClass ADVERSE_EVENT = OntologyClassBuilder.ontologyClass("NCIT:C41331", "Adverse Event");
  private static final OntologyClass FOUR_TIMES_DAILY = OntologyClassBuilder.ontologyClass("NCIT:C64530", "Four Times Daily");
  private static final OntologyClass INTRA_ARTERIAL = OntologyClassBuilder.ontologyClass("NCIT:C38222", "Intraarterial Route of Administration");
  private static final OntologyClass IV_ADMINISTRATION = OntologyClassBuilder.ontologyClass("NCIT:C38276", "Intravenous Route of Administration");
  private static final OntologyClass ORAL_ADMINISTRATION = OntologyClassBuilder.ontologyClass("NCIT:C38288", "Oral Route of Administration");
  private static final OntologyClass ONCE = OntologyClassBuilder.ontologyClass("NCIT:C64576", "Once");
  private static final OntologyClass ONCE_DAILY = OntologyClassBuilder.ontologyClass("NCIT:C125004", "Once Daily");
  private static final OntologyClass THREE_TIMES_DAILY = OntologyClassBuilder.ontologyClass("NCIT:C64527", "Three Times Daily");
  private static final OntologyClass TWICE_DAILY = OntologyClassBuilder.ontologyClass("NCIT:C64496", "Twice Daily");


  public static OntologyClass adverseEvent() { return ADVERSE_EVENT; }
  public static OntologyClass fourtimesDaily() { return FOUR_TIMES_DAILY; }
  public static OntologyClass intraArterialAdministration() { return INTRA_ARTERIAL; }
  public static OntologyClass intravenousAdministration() { return IV_ADMINISTRATION; }
  public static OntologyClass oralAdministration() { return ORAL_ADMINISTRATION; }
  public static OntologyClass once() { return ONCE; }
  public static OntologyClass onceDaily() { return ONCE_DAILY; }
  public static OntologyClass threetimesDaily() { return THREE_TIMES_DAILY; }
  public static OntologyClass twiceDaily() { return TWICE_DAILY; }

}
