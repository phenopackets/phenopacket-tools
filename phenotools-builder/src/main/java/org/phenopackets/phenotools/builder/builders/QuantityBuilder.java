package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.ReferenceRange;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

public class QuantityBuilder {

    private final Quantity.Builder builder;

    private QuantityBuilder(OntologyClass unit, double value) {
        builder = Quantity.newBuilder().setUnit(unit).setValue(value);
    }

    public static Quantity quantity(OntologyClass unit, double value) {
        return Quantity.newBuilder().setUnit(unit).setValue(value).build();
    }

    public static Quantity quantity(String id, String label, double value) {
        return quantity(ontologyClass(id, label), value);
    }



    public static QuantityBuilder unitValue(OntologyClass unit, double value) {
        return new QuantityBuilder(unit, value);
    }

    public static QuantityBuilder unitValue(String id, String label, double value) {
        return new QuantityBuilder(ontologyClass(id, label), value);
    }

    public QuantityBuilder referenceRange(ReferenceRange range) {
        builder.setReferenceRange(range);
        return this;
    }

    public QuantityBuilder referenceRange(OntologyClass unit, double low, double high) {
        ReferenceRange range = ReferenceRangeBuilder.referenceRange(unit, low, high);
        builder.setReferenceRange(range);
        return this;
    }

    public QuantityBuilder referenceRange(String id, String label, double low, double high) {
        ReferenceRange range = ReferenceRangeBuilder.referenceRange(id, label, low, high);
        builder.setReferenceRange(range);
        return this;
    }

    public Quantity build() {
        return builder.build();
    }

}
