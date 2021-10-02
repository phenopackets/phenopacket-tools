package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.*;

import java.util.List;

public class MedicalActionBuilder {

    private final MedicalAction.Builder builder;

    public MedicalActionBuilder(Procedure procedure) {
        builder = MedicalAction.newBuilder().setProcedure(procedure);
    }

    public MedicalActionBuilder(Treatment treatment) {
        builder = MedicalAction.newBuilder().setTreatment(treatment);
    }

    public MedicalActionBuilder(RadiationTherapy rxTherapy) {
        builder = MedicalAction.newBuilder().setRadiationTherapy(rxTherapy);
    }

    public MedicalActionBuilder(TherapeuticRegimen regimen) {
        builder = MedicalAction.newBuilder().setTherapeuticRegimen(regimen);
    }

    public MedicalActionBuilder treatmentTarget(OntologyClass target) {
        builder.setTreatmentTarget(target);
        return this;
    }

    public MedicalActionBuilder treatmentIntent(OntologyClass intent) {
        builder.setTreatmentIntent(intent);
        return this;
    }

    public MedicalActionBuilder responseToTreatment(OntologyClass response) {
        builder.setResponseToTreatment(response);
        return this;
    }

    public MedicalActionBuilder adverseEvent(OntologyClass event) {
        builder.addAdverseEvents(event);
        return this;
    }

    public MedicalActionBuilder allAdverseEvents(List<OntologyClass> events) {
        builder.addAllAdverseEvents(events);
        return this;
    }

    public MedicalActionBuilder treatmentTerminationReason(OntologyClass reason) {
        builder.setTreatmentTerminationReason(reason);
        return this;
    }

    public MedicalAction build() {
        return builder.build();
    }

    public static MedicalActionBuilder procedure(Procedure procedure) {
        return new MedicalActionBuilder(procedure);
    }



    public static MedicalActionBuilder treatment(Treatment treatment) {
        return new MedicalActionBuilder(treatment);
    }

    public static MedicalActionBuilder radiationTherapy(RadiationTherapy rxTheraoy) {
        return new MedicalActionBuilder(rxTheraoy);
    }

    public static MedicalActionBuilder therapeuticRegimen(TherapeuticRegimen regimen) {
        return new MedicalActionBuilder(regimen);
    }


}
