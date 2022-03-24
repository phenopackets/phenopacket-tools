package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TherapeuticRegimen;
import org.phenopackets.schema.v2.core.TimeElement;


public class TherapeuticRegimenBuilder {


    private final TherapeuticRegimen.Builder builder;


    public TherapeuticRegimenBuilder() {
        builder = TherapeuticRegimen.newBuilder();
    }

    public static TherapeuticRegimenBuilder create(String id, String label) {
        OntologyClass clazz = OntologyClassBuilder.ontologyClass(id, label);
        TherapeuticRegimenBuilder builder = new TherapeuticRegimenBuilder();
        builder.ontologyClass(clazz);
        return builder;
    }

    public TherapeuticRegimenBuilder ontologyClass(OntologyClass clz) {
        builder.setOntologyClass(clz);
        return this;
    }

    public TherapeuticRegimenBuilder startTime(TimeElement timeElement) {
        builder.setStartTime(timeElement);
        return this;
    }
    public TherapeuticRegimenBuilder endTime(TimeElement timeElement) {
        builder.setEndTime(timeElement);
        return this;
    }

    public TherapeuticRegimenBuilder started() {
        builder.setRegimenStatus(TherapeuticRegimen.RegimenStatus.STARTED);
        return this;
    }

    public TherapeuticRegimenBuilder completed() {
        builder.setRegimenStatus(TherapeuticRegimen.RegimenStatus.COMPLETED);
        return this;
    }
    public TherapeuticRegimenBuilder discontinued() {
        builder.setRegimenStatus(TherapeuticRegimen.RegimenStatus.DISCONTINUED);
        return this;
    }

    public TherapeuticRegimenBuilder unknown() {
        builder.setRegimenStatus(TherapeuticRegimen.RegimenStatus.UNKNOWN_STATUS);
        return this;
    }

    public TherapeuticRegimen build() {
        return builder.build();
    }


}
