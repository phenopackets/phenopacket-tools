package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.*;

public class MeasurementBuilder {

    private final Measurement.Builder builder;

    private MeasurementBuilder(OntologyClass assay, Value value) {
        builder = Measurement.newBuilder().setAssay(assay).setValue(value);
    }

    private MeasurementBuilder(OntologyClass assay, ComplexValue complexValue) {
        builder = Measurement.newBuilder().setAssay(assay).setComplexValue(complexValue);
    }

    public static Measurement of(OntologyClass assay, Value value) {
        return Measurement.newBuilder().setAssay(assay).setValue(value).build();
    }

    public static Measurement of(OntologyClass assay, ComplexValue complexValue) {
        return Measurement.newBuilder().setAssay(assay).setComplexValue(complexValue).build();
    }

    public static MeasurementBuilder builder(OntologyClass assay, Value value) {
        return new MeasurementBuilder(assay, value);
    }

    public static MeasurementBuilder builder(OntologyClass assay, ComplexValue complexValue) {
        return new MeasurementBuilder(assay, complexValue);
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
}
