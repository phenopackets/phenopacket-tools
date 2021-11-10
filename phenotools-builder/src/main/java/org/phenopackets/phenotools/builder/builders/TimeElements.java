package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v2.core.*;

import java.time.Instant;

import static org.phenopackets.phenotools.builder.builders.Onset.late;
import static org.phenopackets.phenotools.builder.builders.Onset.middleAge;

/**
 * The TimeElement is used in many places in the Phenopacket. It is defined as being one of the
 * following options.
 * <ol>
 *     <li>gestational_age (GestationalAge)</li>
 *     <li>age (Age)</li>
 *     <li>age_range (AgeRange)</li>
 *     <li>ontology_class (OntologyClass)</li>
 *     <li>timestamp (Timestamp)</li>
 *     <li>interval (TimeInterval)</li>
 * </ol>
 * @author Peter N Robinson
 */
public class TimeElements {

    private static final TimeElement FETAL_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.fetal()).build();
    private static final TimeElement EMBRYONAL_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.embryonal()).build();
    private static final TimeElement ANTENATAL_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.antenatal()).build();
    private static final TimeElement CONGENITAL_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.congenital()).build();
    private static final TimeElement NEONATAL_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.neonatal()).build();
    private static final TimeElement PEDIATRIC_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.pediatric()).build();
    private static final TimeElement CHILDHOOD_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.childhood()).build();
    private static final TimeElement JUVENILE_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.juvenile()).build();
    private static final TimeElement INFANTILE_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.infantile()).build();
    private static final TimeElement ADULT_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.adult()).build();
    private static final TimeElement LATE_ONSET = TimeElement.newBuilder().setOntologyClass(late()).build();
    private static final TimeElement MIDDLE_AGE_ONSET = TimeElement.newBuilder().setOntologyClass(middleAge()).build();
    private static final TimeElement YOUNG_ADULT_ONSET = TimeElement.newBuilder().setOntologyClass(Onset.youngAdult()).build();

    private TimeElements() {
    }

    public static TimeElement gestationalAge(int weeks, int days) {
        return TimeElement.newBuilder().setGestationalAge(GestationalAge.newBuilder().setWeeks(weeks).setDays(days)).build();
    }

    public static TimeElement gestationalAge(int weeks) {
        return TimeElement.newBuilder().setGestationalAge(GestationalAge.newBuilder().setWeeks(weeks)).build();
    }

    public static TimeElement age(String iso8601duration) {
        Age age = AgeBuilder.age(iso8601duration);
        return TimeElement.newBuilder().setAge(age).build();
    }

    public static TimeElement ageRange(String iso8601start, String iso8601End) {
        AgeRange ageRange = AgeBuilder.ageRange(iso8601start, iso8601End);
        return TimeElement.newBuilder().setAgeRange(ageRange).build();
    }

    public static TimeElement ontologyClass(OntologyClass clz) {
        return TimeElement.newBuilder().setOntologyClass(clz).build();
    }

    public static TimeElement fetalOnset() {
        return FETAL_ONSET;
    }

    public static TimeElement embryonalOnset() {
        return EMBRYONAL_ONSET;
    }

    public static TimeElement antenatalOnset() {
        return ANTENATAL_ONSET;
    }

    public static TimeElement congenitalOnset() {
        return CONGENITAL_ONSET;
    }

    public static TimeElement neonatalOnset() {
        return NEONATAL_ONSET;
    }

    public static TimeElement pediatricOnset() {
        return PEDIATRIC_ONSET;
    }

    public static TimeElement childhoodOnset() {
        return CHILDHOOD_ONSET;
    }

    public static TimeElement juvenileOnset() {
        return JUVENILE_ONSET;
    }

    public static TimeElement infantileOnset() {
        return INFANTILE_ONSET;
    }

    public static TimeElement adultOnset() {
        return ADULT_ONSET;
    }

    public static TimeElement lateOnset() {
        return LATE_ONSET;
    }

    public static TimeElement middleAgeOnset() {
        return MIDDLE_AGE_ONSET;
    }

    public static TimeElement youngAdultOnset() {
        return YOUNG_ADULT_ONSET;
    }

    public static TimeElement timestamp(Instant instant) {
        Timestamp time = TimestampBuilder.fromInstant(instant);
        return TimeElement.newBuilder().setTimestamp(time).build();
    }

    public static TimeElement timestamp(String timestamp) {
        Timestamp time = TimestampBuilder.fromISO8601(timestamp);
        return TimeElement.newBuilder().setTimestamp(time).build();
    }

    public static TimeElement interval(Instant startInstant, Instant endInstant) {
        Timestamp start = TimestampBuilder.fromInstant(startInstant);
        Timestamp end = TimestampBuilder.fromInstant(endInstant);
        return TimeElement.newBuilder().setInterval(TimeInterval.newBuilder().setStart(start).setEnd(end)).build();
    }

    public static TimeElement interval(String timestampStart, String timestampEnd) {
        Timestamp start = TimestampBuilder.fromISO8601(timestampStart);
        Timestamp end = TimestampBuilder.fromISO8601(timestampEnd);
        return TimeElement.newBuilder().setInterval(TimeInterval.newBuilder().setStart(start).setEnd(end)).build();
    }

}
