package org.phenopackets.phenotools.builder.builders;


import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.Value;

public class ValueBuilder {

    private final Value.Builder builder;

    public ValueBuilder(OntologyClass clz) {
        builder = Value.newBuilder().setOntologyClass(clz);
    }

    public ValueBuilder(Quantity quantity) {
        builder = Value.newBuilder().setQuantity(quantity);
    }

    public Value build() {
        return builder.build();
    }

    public static ValueBuilder create(OntologyClass clz) {
        return new ValueBuilder(clz);
    }

    public static ValueBuilder create(Quantity quantity) {
        return new ValueBuilder(quantity);
    }


}
