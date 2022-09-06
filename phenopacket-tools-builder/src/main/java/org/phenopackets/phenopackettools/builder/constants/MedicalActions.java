package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class MedicalActions {

    // routes of administration
    private static final OntologyClass INTRA_ARTERIAL = ontologyClass("NCIT:C38222",  "Intraarterial Route of Administration");
//
    private static final OntologyClass ORAL_ADMINISTRATION = ontologyClass("NCIT:C38288", "Oral Route of Administration");


    // treatment termination
    private static final OntologyClass ADVERSE_EVENT = ontologyClass("NCIT:C41331", "Adverse Event");


    private static final  OntologyClass ONCE_DAILY = ontologyClass("NCIT:C64576", "Once");


    public static OntologyClass intraArterialAdministration() {
        return INTRA_ARTERIAL;
    }


    public static OntologyClass oralAdministration() {
        return ORAL_ADMINISTRATION;
    }


    public static OntologyClass adverseEvent() {
        return ADVERSE_EVENT;
    }


    public static OntologyClass onceDaily() {
        return ONCE_DAILY;
    }
}
