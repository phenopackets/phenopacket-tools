package org.phenopackets.phenotools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.TimeElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeElementBuilderTest {

    @Test
    public void testGestationalAge() {
        int weeks = 20;
        int days = 2;
        TimeElement time = TimeElementBuilder.create().gestationalAge(weeks, days).build();
        assertTrue(time.hasGestationalAge());
        assertEquals(weeks, time.getGestationalAge().getWeeks());
        assertEquals(days, time.getGestationalAge().getDays());
    }

}
