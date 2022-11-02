package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class Evidence {

  private static final OntologyClass AUTHOR_STATEMENT_FROM_PCS = OntologyClassBuilder.ontologyClass("ECO:0006016", "author statement from published clinical study");
  private static final OntologyClass AUTHOR_STATEMENT_FROM_PCS_AUTOMATIC = OntologyClassBuilder.ontologyClass("ECO:0007539", "author statement from published clinical study used in automatic assertion");
  private static final OntologyClass AUTHOR_STATEMENT_FROM_PCS_MANUAL = OntologyClassBuilder.ontologyClass("ECO:0006017", "author statement from published clinical study used in manual assertion");
  private static final OntologyClass AUTHOR_STATEMENT_TRACEABLE_REFERENCE = OntologyClassBuilder.ontologyClass("ECO:0000033", "author statement supported by traceable reference");
  private static final OntologyClass SELF_REPORTED_PATIENT_STATEMENT_EVIDENCE = OntologyClassBuilder.ontologyClass("ECO:0006154", "self-reported patient statement evidence");


  public static OntologyClass authorStatementFromPublishedClinicalStudy() { return AUTHOR_STATEMENT_FROM_PCS; }
  public static OntologyClass authorStatementFromPublishedClinicalStudyAutomaticAssertion() { return AUTHOR_STATEMENT_FROM_PCS_AUTOMATIC; }
  public static OntologyClass authorStatementFromPublishedClinicalStudyManualAssertion() { return AUTHOR_STATEMENT_FROM_PCS_MANUAL; }
  public static OntologyClass authorStatementSupportedByTraceableReference() { return AUTHOR_STATEMENT_TRACEABLE_REFERENCE; }
  public static OntologyClass selfReportedPatientStatementEvidence() { return SELF_REPORTED_PATIENT_STATEMENT_EVIDENCE; }

}
