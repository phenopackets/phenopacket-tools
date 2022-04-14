package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.TypedQuantity;

public class TypedQuantityBuilder {

    private TypedQuantityBuilder() {
    }

    public static TypedQuantity typedQuantity(OntologyClass type, Quantity quantity) {
        return TypedQuantity.newBuilder().setType(type).setQuantity(quantity).build();
    }

}
