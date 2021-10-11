package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.core.*;

import java.util.regex.*;

import static org.phenopackets.phenotools.builder.builders.Onset.late;
import static org.phenopackets.phenotools.builder.builders.Onset.middleAge;
import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.fromISO8601;

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

    private static final Pattern isoDuration = Pattern.compile("^P(?!$)(\\d+(?:\\.\\d+)?Y)?(\\d+(?:\\.\\d+)?M)?(\\d+(?:\\.\\d+)?W)?(\\d+(?:\\.\\d+)?D)?(T(?=\\d)(\\d+(?:\\.\\d+)?H)?(\\d+(?:\\.\\d+)?M)?(\\d+(?:\\.\\d+)?S)?)?$");

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

    private static Age iso8601Age(String iso8601duration) {
        Matcher matcher = isoDuration.matcher(iso8601duration);
        if (! matcher.matches()) {
            throw new PhenotoolsRuntimeException("Invalid iso8601 age (duration) string: \"" + iso8601duration + "\".");
        }
        return Age.newBuilder().setIso8601Duration(iso8601duration).build();
    }


    public static TimeElement age(String iso8601duration) {
        Age age = iso8601Age(iso8601duration);
        return TimeElement.newBuilder().setAge(age).build();
    }

    public static TimeElement ageRange(String iso8601start, String iso8601End) {
        Age start = iso8601Age(iso8601start);
        Age end = iso8601Age(iso8601End);
        return TimeElement.newBuilder().setAgeRange(AgeRange.newBuilder().setStart(start).setEnd(end)).build();
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

    public static TimeElement timestamp(String timestamp) {
        Timestamp time = fromISO8601(timestamp);
        return TimeElement.newBuilder().setTimestamp(time).build();
    }

    public static TimeElement interval(String timestampStart, String timestampEnd) {
        Timestamp start = fromISO8601(timestampStart);
        Timestamp end = fromISO8601(timestampEnd);
        return TimeElement.newBuilder().setInterval(TimeInterval.newBuilder().setStart(start).setEnd(end)).build();
    }

}
