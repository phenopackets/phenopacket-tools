package org.phenopackets.phenotools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.ComplexValue;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TypedQuantity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.of;

class ComplexValueBuilderTest {

    /**
     * https://phenopacket-schema.readthedocs.io/en/v2/complex-value.html#example
     */
    @Test
    void testComplexValue() {
        OntologyClass millimeterOfMercury = of("NCIT:C49670", "Millimeter of Mercury");
        TypedQuantity systolicBloodPressure = TypedQuantityBuilder.of(of("NCIT:C25298", "Systolic Blood Pressure"), QuantityBuilder.of(millimeterOfMercury, 120));
        TypedQuantity diastolicBloodPressure = TypedQuantityBuilder.of(of("NCIT:C25299", "Diastolic Blood Pressure"), QuantityBuilder.of(millimeterOfMercury, 70));
        ComplexValue complexValue = ComplexValueBuilder.of(systolicBloodPressure, diastolicBloodPressure);

        assertThat(complexValue.getTypedQuantitiesList(), equalTo(List.of(systolicBloodPressure, diastolicBloodPressure)));
    }
}