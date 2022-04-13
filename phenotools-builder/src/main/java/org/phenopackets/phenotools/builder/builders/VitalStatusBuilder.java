package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TimeElement;
import org.phenopackets.schema.v2.core.VitalStatus;

public class VitalStatusBuilder {

    private final VitalStatus.Builder builder;

    private VitalStatusBuilder(VitalStatus.Status status) {
        builder = VitalStatus.newBuilder().setStatus(status);
    }

    public static VitalStatus of(VitalStatus.Status status) {
        return VitalStatus.newBuilder().setStatus(status).build();
    }

    public static VitalStatusBuilder alive() {
        return new VitalStatusBuilder(VitalStatus.Status.ALIVE);
    }

    public static VitalStatusBuilder deceased() {
        return new VitalStatusBuilder(VitalStatus.Status.DECEASED);
    }

    public VitalStatusBuilder timeOfDeath(TimeElement time) {
        builder.setTimeOfDeath(time);
        return this;
    }

    public VitalStatusBuilder causeOfDeath(OntologyClass cause) {
        builder.setCauseOfDeath(cause);
        return this;
    }

    public VitalStatusBuilder causeOfDeath(String id, String label) {
        builder.setCauseOfDeath(OntologyClassBuilder.of(id, label));
        return this;
    }

    public VitalStatusBuilder survivalTimeInDays(int days) {
        builder.setSurvivalTimeInDays(days);
        return this;
    }

    public VitalStatus build() {
        return builder.build();
    }
}
