package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.core.ComplexValue;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.TypedQuantity;

public class ComplexValueBuilder {


    private final ComplexValue.Builder builder;

    public ComplexValueBuilder() {
        builder = ComplexValue.newBuilder();
    }

    public ComplexValueBuilder typedQuantity(TypedQuantity typedQuantity) {
        builder.addTypedQuantities(typedQuantity);
        return this;
    }

    public ComplexValueBuilder typedQuantity(OntologyClass type, Quantity quantity) {
        TypedQuantity typedQuantity = TypedQuantity.newBuilder().setType(type).setQuantity(quantity).build();
        builder.addTypedQuantities(typedQuantity);
        return this;
    }

    public ComplexValue build() {
        if (builder.getTypedQuantitiesCount() == 0) {
            throw new PhenotoolsRuntimeException("at least one TypedQuanity required for ComplexValue");
        }
        return builder.build();
    }

    public static ComplexValueBuilder create() {
        return new ComplexValueBuilder();
    }


}
