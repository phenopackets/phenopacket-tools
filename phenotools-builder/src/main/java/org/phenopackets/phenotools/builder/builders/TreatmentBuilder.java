package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.*;

import java.util.List;

public class TreatmentBuilder {
    private final Treatment.Builder builder;

    public TreatmentBuilder(OntologyClass agent) {
        builder = Treatment.newBuilder().setAgent(agent);
    }

    public TreatmentBuilder routeOfAdministration(OntologyClass route) {
        builder.setRouteOfAdministration(route);
        return this;
    }

    public TreatmentBuilder doseInterval(DoseInterval interval) {
        builder.addDoseIntervals(interval);
        return this;
    }

    public TreatmentBuilder allDoseIntervals(List<DoseInterval> intervals) {
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

    public TreatmentBuilder cumulative_dose(Quantity quantity) {
        builder.setCumulativeDose(quantity);
        return this;
    }

   public Treatment build() {
        return builder.build();
   }

   public static TreatmentBuilder create(OntologyClass agent) {
        return new TreatmentBuilder(agent);
   }
}
