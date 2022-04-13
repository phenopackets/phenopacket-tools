package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.ComplexValue;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.TypedQuantity;

import java.util.List;

public class ComplexValueBuilder {

    private ComplexValueBuilder() {
    }

    public static ComplexValue of(TypedQuantity typedQuantity) {
        return ComplexValue.newBuilder().addTypedQuantities(typedQuantity).build();
    }

    public static ComplexValue of(TypedQuantity... typedQuantities) {
        return ComplexValue.newBuilder().addAllTypedQuantities(List.of(typedQuantities)).build();
    }

    public static ComplexValue of(List<TypedQuantity> typedQuantities) {
        return ComplexValue.newBuilder().addAllTypedQuantities(typedQuantities).build();
    }

    public static ComplexValue of(OntologyClass type, Quantity quantity) {
        TypedQuantity typedQuantity = TypedQuantityBuilder.of(type, quantity);
        return of(typedQuantity);
    }
}
