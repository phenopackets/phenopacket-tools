package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.*;

public class MeasurementBuilder {

    private final Measurement.Builder builder;

    public MeasurementBuilder(OntologyClass assay, Value value) {
        builder = Measurement.newBuilder().setAssay(assay).setValue(value);
    }

    public MeasurementBuilder(OntologyClass assay, ComplexValue complexValue) {
        builder = Measurement.newBuilder().setAssay(assay).setComplexValue(complexValue);
    }

    public MeasurementBuilder description(String desc) {
        builder.setDescription(desc);
        return this;
    }

    public MeasurementBuilder timeObserved(TimeElement time) {
        builder.setTimeObserved(time);
        return this;
    }

    public MeasurementBuilder procedure(Procedure procedure) {
        builder.setProcedure(procedure);
        return this;
    }

    public Measurement build() {
        return builder.build();
    }

    public static MeasurementBuilder value(OntologyClass assay, Value value) {
        return new MeasurementBuilder(assay, value);
    }

    public static MeasurementBuilder complexValue(OntologyClass assay, ComplexValue complexValue) {
        return new MeasurementBuilder(assay, complexValue);
    }

}
