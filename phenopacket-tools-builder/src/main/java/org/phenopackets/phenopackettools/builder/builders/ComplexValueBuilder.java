package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.ComplexValue;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.TypedQuantity;

import java.util.List;

public class ComplexValueBuilder {

    private ComplexValueBuilder() {
    }

    public static ComplexValue complexValue(TypedQuantity typedQuantity) {
        return ComplexValue.newBuilder().addTypedQuantities(typedQuantity).build();
    }

    public static ComplexValue complexValue(TypedQuantity... typedQuantities) {
        return ComplexValue.newBuilder().addAllTypedQuantities(List.of(typedQuantities)).build();
    }

    public static ComplexValue complexValue(List<TypedQuantity> typedQuantities) {
        return ComplexValue.newBuilder().addAllTypedQuantities(typedQuantities).build();
    }

    public static ComplexValue complexValue(OntologyClass type, Quantity quantity) {
        TypedQuantity typedQuantity = TypedQuantityBuilder.typedQuantity(type, quantity);
        return complexValue(typedQuantity);
    }
}
