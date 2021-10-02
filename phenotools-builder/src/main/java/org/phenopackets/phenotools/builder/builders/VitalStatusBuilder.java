package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TimeElement;
import org.phenopackets.schema.v2.core.VitalStatus;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.ontologyClass;

public class VitalStatusBuilder {

    private final VitalStatus.Builder builder;

    public VitalStatusBuilder(VitalStatus.Status status) {
        builder = VitalStatus.newBuilder().setStatus(status);
    }

    public VitalStatusBuilder time_of_death(TimeElement time) {
        builder.setTimeOfDeath(time);
        return this;
    }

    public VitalStatusBuilder causeOfDeath(OntologyClass cause) {
        builder.setCauseOfDeath(cause);
        return this;
    }

    public VitalStatusBuilder causeOfDeath(String id, String label) {
        builder.setCauseOfDeath(ontologyClass(id, label));
        return this;
    }

    public  VitalStatusBuilder survivalTimeInDays(int days) {
        builder.setSurvivalTimeInDays(days);
        return this;
    }

    public VitalStatus build() {
        return builder.build();
    }

    public static VitalStatusBuilder deceased() {
        return new VitalStatusBuilder(VitalStatus.Status.DECEASED);
    }
    public static VitalStatusBuilder alive() {
        return new VitalStatusBuilder(VitalStatus.Status.ALIVE);
    }

}
