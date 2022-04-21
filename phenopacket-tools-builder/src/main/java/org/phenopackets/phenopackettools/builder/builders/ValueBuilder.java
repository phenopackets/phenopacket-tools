package org.phenopackets.phenopackettools.builder.builders;


import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.ReferenceRange;
import org.phenopackets.schema.v2.core.Value;


public class ValueBuilder {

    private ValueBuilder() {
    }

    public static Value of(String id, String label) {
        OntologyClass ontologyClass = OntologyClassBuilder.ontologyClass(id, label);
        return of(ontologyClass);
    }

    public static Value of(OntologyClass ontologyClass) {
        return Value.newBuilder().setOntologyClass(ontologyClass).build();
    }

    public static Value of(Quantity quantity) {
        return Value.newBuilder().setQuantity(quantity).build();
    }

    public static Value of(String id, String label, double value) {
        Quantity quantity = QuantityBuilder.of(id, label, value);
        return of(quantity);
    }

    public static Value of(OntologyClass ontologyClass, double value) {
        Quantity quantity = QuantityBuilder.of(ontologyClass, value);
        return of(quantity);
    }


    public static Value of(OntologyClass ontologyClass, double value, ReferenceRange ref) {
        Quantity quantity = QuantityBuilder.of(ontologyClass, value, ref);
        return of(quantity);
    }
}
