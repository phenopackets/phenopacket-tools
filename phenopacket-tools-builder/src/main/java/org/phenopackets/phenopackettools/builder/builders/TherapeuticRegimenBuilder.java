package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TherapeuticRegimen;
import org.phenopackets.schema.v2.core.TimeElement;


public class TherapeuticRegimenBuilder {


    private final TherapeuticRegimen.Builder builder;


    private TherapeuticRegimenBuilder(OntologyClass clazz) {
        builder = TherapeuticRegimen.newBuilder()
                .setOntologyClass(clazz);
    }

    private TherapeuticRegimenBuilder(ExternalReference externalReference) {
        builder = TherapeuticRegimen.newBuilder()
                .setExternalReference(externalReference);
    }

    public static TherapeuticRegimenBuilder builder(String id, String label) {
        OntologyClass clazz = OntologyClassBuilder.ontologyClass(id, label);
        return new TherapeuticRegimenBuilder(clazz);
    }

    public static TherapeuticRegimenBuilder builder(ExternalReference reference) {
        return  new TherapeuticRegimenBuilder(reference);
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
