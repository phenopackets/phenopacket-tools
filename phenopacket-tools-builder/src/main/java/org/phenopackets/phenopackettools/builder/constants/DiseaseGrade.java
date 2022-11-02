package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class DiseaseGrade {

  private static final OntologyClass GRADE_1 = OntologyClassBuilder.ontologyClass("NCIT:C28077", "Grade 1");
  private static final OntologyClass GRADE_2 = OntologyClassBuilder.ontologyClass("NCIT:C28078", "Grade 2");
  private static final OntologyClass GRADE_3 = OntologyClassBuilder.ontologyClass("NCIT:C28079", "Grade 3");
  private static final OntologyClass GRADE_3A = OntologyClassBuilder.ontologyClass("NCIT:C28080", "Grade 3a");
  private static final OntologyClass GRADE_3B = OntologyClassBuilder.ontologyClass("NCIT:C28081", "Grade 3b");
  private static final OntologyClass GRADE_4 = OntologyClassBuilder.ontologyClass("NCIT:C28082", "Grade 4");


  public static OntologyClass grade1() { return GRADE_1; }
  public static OntologyClass grade2() { return GRADE_2; }
  public static OntologyClass grade3() { return GRADE_3; }
  public static OntologyClass grade3a() { return GRADE_3A; }
  public static OntologyClass grade3b() { return GRADE_3B; }
  public static OntologyClass grade4() { return GRADE_4; }

}
