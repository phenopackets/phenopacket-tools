package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TimeElement;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.phenopackets.phenotools.builder.builders.TimestampBuilder.fromISO8601;

public class TimeElementsTest {

    @Test
    public void testGestationalAge() {
        int weeks = 20;
        int days = 2;
        TimeElement time = TimeElements.gestationalAge(weeks, days);
        assertTrue(time.hasGestationalAge());
        assertEquals(weeks, time.getGestationalAge().getWeeks());
        assertEquals(days, time.getGestationalAge().getDays());
    }

    @Test
    public void testValidIso8601Age() {
        String iso8601 = "P31Y3M2D";
        TimeElement age = TimeElements.age(iso8601);
        assertTrue(age.hasAge());
        assertEquals(iso8601, age.getAge().getIso8601Duration());
    }

    @Test
    public void testInvalidIso8601Age() {
        // B instead of Y -- invalid, should throw exception
        String iso8601 = "P31B3M2D";
        Assertions.assertThrows(PhenotoolsRuntimeException.class, () -> {
            TimeElement age = TimeElements.age(iso8601);
        });
    }

    @Test
    public void testAgeRange() {
        String iso8601start = "P31Y3M2D";
        String iso8601end = "P32Y3M2D";
        TimeElement time = TimeElements.ageRange(iso8601start, iso8601end);
        assertTrue(time.hasAgeRange());
    }

    @Test
    public void testOntologyClass() {
        OntologyClass cls = OntologyClass.newBuilder().setId("ABC:12345").setLabel("made-up").build();
        TimeElement time = TimeElements.ontologyClass(cls);
        assertTrue(time.hasOntologyClass());
    }

    @Test
    public void testFetalOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0011461").setLabel("Fetal onset").build();
        TimeElement time = TimeElements.fetalOnset();
        assertTrue(time.hasOntologyClass());
        assertEquals(fetal, time.getOntologyClass());
    }

    @Test
    public void testMiddleAgeOnset() {
        OntologyClass middleAge = OntologyClass.newBuilder().setId("HP:0003596").setLabel("Middle age onset").build();
        TimeElement time = TimeElements.middleAgeOnset();
        assertTrue(time.hasOntologyClass());
        assertEquals(middleAge, time.getOntologyClass());
    }

    @Test
    public void testTimestamp() {
        String timeString = "2020-03-17T00:00:00Z";
        Timestamp timestamp = fromISO8601(timeString);
        TimeElement time = TimeElements.timestamp("2020-03-17T00:00:00Z");
        assertTrue(time.hasTimestamp());
        assertEquals(timestamp, time.getTimestamp());
    }
    @Test
    public void testTimeInterval() {
        String time1 = "2020-03-17T00:00:00Z";
        String time2 = "2021-03-17T00:00:00Z";
        Timestamp timestamp1 = fromISO8601(time1);
        Timestamp timestamp2 = fromISO8601(time2);
        TimeElement time = TimeElements.interval(time1, time2);
        assertTrue(time.hasInterval());
        assertEquals(timestamp1, time.getInterval().getStart());
        assertEquals(timestamp2, time.getInterval().getEnd());
    }

}
