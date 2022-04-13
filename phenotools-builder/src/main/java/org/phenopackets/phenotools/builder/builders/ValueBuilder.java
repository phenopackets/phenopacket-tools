package org.phenopackets.phenotools.builder.builders;


import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.ReferenceRange;
import org.phenopackets.schema.v2.core.Value;


public class ValueBuilder {

    private ValueBuilder() {
    }

    public static Value value(String id, String label) {
        OntologyClass ontologyClass = OntologyClassBuilder.ontologyClass(id, label);
        return value(ontologyClass);
    }

    public static Value value(OntologyClass ontologyClass) {
        return Value.newBuilder().setOntologyClass(ontologyClass).build();
    }

    public static Value value(Quantity quantity) {
        return Value.newBuilder().setQuantity(quantity).build();
    }

    public static Value value(String id, String label, double value) {
        Quantity quantity = QuantityBuilder.quantity(id, label, value);
        return value(quantity);
    }

    public static Value value(OntologyClass ontologyClass, double value) {
        Quantity quantity = QuantityBuilder.quantity(ontologyClass, value);
        return value(quantity);
    }


    public static Value value(OntologyClass ontologyClass, double value, ReferenceRange ref) {
        Quantity quantity = QuantityBuilder.quantity(ontologyClass, value, ref);
        return value(quantity);
    }
}
