package org.phenopackets.phenopackettools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.ComplexValue;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TypedQuantity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class ComplexValueBuilderTest {

    /**
     * <a href="https://phenopacket-schema.readthedocs.io/en/v2/complex-value.html#example">https://phenopacket-schema.readthedocs.io/en/v2/complex-value.html#example</a>
     */
    @Test
    public void testComplexValue() {
        OntologyClass millimeterOfMercury = ontologyClass("NCIT:C49670", "Millimeter of Mercury");
        TypedQuantity systolicBloodPressure = TypedQuantityBuilder.of(ontologyClass("NCIT:C25298", "Systolic Blood Pressure"), QuantityBuilder.of(millimeterOfMercury, 120));
        TypedQuantity diastolicBloodPressure = TypedQuantityBuilder.of(ontologyClass("NCIT:C25299", "Diastolic Blood Pressure"), QuantityBuilder.of(millimeterOfMercury, 70));
        ComplexValue complexValue = ComplexValueBuilder.of(systolicBloodPressure, diastolicBloodPressure);

        assertThat(complexValue.getTypedQuantitiesList(), equalTo(List.of(systolicBloodPressure, diastolicBloodPressure)));
    }
}