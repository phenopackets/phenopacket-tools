package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.DoseInterval;
import org.phenopackets.schema.v2.core.TimeInterval;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

public class DoseIntervalBuilderTest {

    @Test
    public void testDoseInterval1() {
        var mg = ontologyClass("UO:0000022", "milligram");
        var quantity = QuantityBuilder.quantity(mg, 30.0);
        var administration = ontologyClass("NCIT:C38288", "Oral Route of Administration");
        var bid = ontologyClass("NCIT:C64496", "Twice Daily");
        var interval = TimeIntervalBuilder.timeInterval("2019-03-20", "2021-03-20");
        DoseInterval dosage = DoseIntervalBuilder.doseInterval(quantity, bid, interval);
        assertEquals(30.0, dosage.getQuantity().getValue());
        TimeInterval timeInterval = dosage.getInterval();
        Timestamp start = timeInterval.getStart();
        Timestamp end = timeInterval.getEnd();
        assertTrue(start.getSeconds() < end.getSeconds());
    }
}
