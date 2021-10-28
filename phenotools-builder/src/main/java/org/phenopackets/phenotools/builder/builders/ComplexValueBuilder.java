package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.ComplexValue;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.TypedQuantity;

public class ComplexValueBuilder {

    private ComplexValueBuilder() {
    }

    public ComplexValue complexValue(TypedQuantity typedQuantity) {
        return ComplexValue.newBuilder().addTypedQuantities(typedQuantity).build();
    }

    public ComplexValue complexValue(OntologyClass type, Quantity quantity) {
        TypedQuantity typedQuantity = TypedQuantity.newBuilder().setType(type).setQuantity(quantity).build();
        return complexValue(typedQuantity);
    }
}
