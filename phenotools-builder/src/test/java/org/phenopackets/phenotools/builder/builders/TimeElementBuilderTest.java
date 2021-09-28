package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TimeElement;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.fromRFC3339;

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

    @Test
    public void testValidIso8601Age() {
        String iso8601 = "P31Y3M2D";
        TimeElement age = TimeElementBuilder.create().age(iso8601).build();
        assertTrue(age.hasAge());
        assertEquals(iso8601, age.getAge().getIso8601Duration());
    }

    @Test
    public void testInvalidIso8601Age() {
        // B instead of Y -- invalid, should throw exception
        String iso8601 = "P31B3M2D";
        Assertions.assertThrows(PhenotoolsRuntimeException.class, () -> {
            TimeElement age = TimeElementBuilder.create().age(iso8601).build();
        });
    }

    @Test
    public void testAgeRange() {
        String iso8601start = "P31Y3M2D";
        String iso8601end = "P32Y3M2D";
        TimeElement time = TimeElementBuilder.create().ageRange(iso8601start, iso8601end).build();
        assertTrue(time.hasAgeRange());
    }

    @Test
    public void testOntologyClass() {
        OntologyClass cls = OntologyClass.newBuilder().setId("ABC:12345").setLabel("made-up").build();
        TimeElement time = TimeElementBuilder.create().ontologyClass(cls).build();
        assertTrue(time.hasOntologyClass());
    }

    @Test
    public void testFetalOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0011461").setLabel("Fetal onset").build();
        TimeElement time = TimeElementBuilder.create().fetalOnset().build();
        assertTrue(time.hasOntologyClass());
        assertEquals(fetal, time.getOntologyClass());
    }

    @Test
    public void testMiddleAgeOnset() {
        OntologyClass middleAge = OntologyClass.newBuilder().setId("HP:0003596").setLabel("Middle age onset").build();
        TimeElement time = TimeElementBuilder.create().middleAgeOnset().build();
        assertTrue(time.hasOntologyClass());
        assertEquals(middleAge, time.getOntologyClass());
    }

    @Test
    public void testTimestamp() {
        String timeString = "2020-03-17T00:00:00Z";
        Timestamp timestamp = fromRFC3339(timeString);
        TimeElement time = TimeElementBuilder.create().timestamp("2020-03-17T00:00:00Z").build();
        assertTrue(time.hasTimestamp());
        assertEquals(timestamp, time.getTimestamp());
    }
    @Test
    public void testTimeInterval() {
        String time1 = "2020-03-17T00:00:00Z";
        String time2 = "2021-03-17T00:00:00Z";
        Timestamp timestamp1 = fromRFC3339(time1);
        Timestamp timestamp2 = fromRFC3339(time2);
        TimeElement time = TimeElementBuilder.create().interval(time1, time2).build();
        assertTrue(time.hasInterval());
        assertEquals(timestamp1, time.getInterval().getStart());
        assertEquals(timestamp2, time.getInterval().getEnd());
    }

    @Test
    public void testAttemptToAddTwoFields() {
        String timeString = "2020-03-17T00:00:00Z";
        // We do not allow a time Element to be constructed with more than one subelement
        Assertions.assertThrows(PhenotoolsRuntimeException.class, () -> {
            TimeElement time = TimeElementBuilder.create().timestamp(timeString).pediatricOnset().build();
        });
    }

    @Test
    public void testAttemptToCreateEmptyTimeElement() {
        // We do not allow a time Element to be constructed without any data
        Assertions.assertThrows(PhenotoolsRuntimeException.class, () -> {
            TimeElement time = TimeElementBuilder.create().build();
        });
    }


}
