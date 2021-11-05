package org.phenopackets.phenotools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.ComplexValue;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TypedQuantity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

class ComplexValueBuilderTest {

    /**
     * https://phenopacket-schema.readthedocs.io/en/v2/complex-value.html#example
     */
    @Test
    void testComplexValue() {
        OntologyClass millimeterOfMercury = ontologyClass("NCIT:C49670", "Millimeter of Mercury");
        TypedQuantity systolicBloodPressure = TypedQuantityBuilder.typedQuantity(ontologyClass("NCIT:C25298", "Systolic Blood Pressure"), QuantityBuilder.quantity(millimeterOfMercury, 120));
        TypedQuantity diastolicBloodPressure = TypedQuantityBuilder.typedQuantity(ontologyClass("NCIT:C25299", "Diastolic Blood Pressure"), QuantityBuilder.quantity(millimeterOfMercury, 70));
        ComplexValue complexValue = ComplexValueBuilder.complexValue(systolicBloodPressure, diastolicBloodPressure);

        assertThat(complexValue.getTypedQuantitiesList(), equalTo(List.of(systolicBloodPressure, diastolicBloodPressure)));
    }
}