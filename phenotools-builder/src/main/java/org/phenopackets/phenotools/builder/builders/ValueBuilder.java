package org.phenopackets.phenotools.builder.builders;


import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.Value;
import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;


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

    public static ValueBuilder create(String id, String label) {
        OntologyClass clz = ontologyClass(id, label);
        return new ValueBuilder(clz);
    }

    public static ValueBuilder create(Quantity quantity) {
        return new ValueBuilder(quantity);
    }


}
