package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.*;

import java.util.List;

public class TreatmentBuilder {

    private final Treatment.Builder builder;

    private TreatmentBuilder(OntologyClass agent) {
        builder = Treatment.newBuilder().setAgent(agent);
    }

    public static Treatment of(OntologyClass agent) {
        return Treatment.newBuilder().setAgent(agent).build();
    }

    public static Treatment of(String agentId, String agentLabel) {
        return of(OntologyClassBuilder.ontologyClass(agentId, agentLabel));
    }

    public static TreatmentBuilder builder(OntologyClass agent) {
        return new TreatmentBuilder(agent);
    }

    public static TreatmentBuilder builder(String id, String label) {
        return new TreatmentBuilder(OntologyClassBuilder.ontologyClass(id, label));
    }

    public TreatmentBuilder routeOfAdministration(OntologyClass route) {
        builder.setRouteOfAdministration(route);
        return this;
    }

    public TreatmentBuilder addDoseInterval(DoseInterval interval) {
        builder.addDoseIntervals(interval);
        return this;
    }

    public TreatmentBuilder addAllDoseIntervals(List<DoseInterval> intervals) {
        builder.addAllDoseIntervals(intervals);
        return this;
    }

    public TreatmentBuilder prescription() {
        builder.setDrugType(DrugType.PRESCRIPTION);
        return this;
    }

    public TreatmentBuilder ehrMedicationList() {
        builder.setDrugType(DrugType.EHR_MEDICATION_LIST);
        return this;
    }


    public TreatmentBuilder procedureRelated() {
        builder.setDrugType(DrugType.ADMINISTRATION_RELATED_TO_PROCEDURE);
        return this;
    }

    public TreatmentBuilder cumulativeDose(Quantity quantity) {
        builder.setCumulativeDose(quantity);
        return this;
    }

    public Treatment build() {
        return builder.build();
    }
}
