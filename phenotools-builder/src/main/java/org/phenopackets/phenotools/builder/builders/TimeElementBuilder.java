package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.core.*;

import java.util.regex.*;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.fromRFC3339;

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
public class TimeElementBuilder {

    private static final Pattern isoDuration = Pattern.compile("^P(?!$)(\\d+(?:\\.\\d+)?Y)?(\\d+(?:\\.\\d+)?M)?(\\d+(?:\\.\\d+)?W)?(\\d+(?:\\.\\d+)?D)?(T(?=\\d)(\\d+(?:\\.\\d+)?H)?(\\d+(?:\\.\\d+)?M)?(\\d+(?:\\.\\d+)?S)?)?$");

    private TimeElement.Builder builder;

    public TimeElementBuilder() {
        builder = null;
    }

    public TimeElementBuilder gestationalAge(int weeks, int days) {
        if (builder != null) {
            throw new PhenotoolsRuntimeException("Not allowed to add two subelements to TimeElement");
        }
        this.builder= TimeElement.newBuilder().setGestationalAge(GestationalAge.newBuilder().setWeeks(weeks).setDays(days));
        return this;
    }

    public TimeElementBuilder gestationalAge(int weeks) {
        if (builder != null) {
            throw new PhenotoolsRuntimeException("Not allowed to add two subelements to TimeElement");
        }
        this.builder= TimeElement.newBuilder().setGestationalAge(GestationalAge.newBuilder().setWeeks(weeks));
        return this;
    }

    private Age getIso8601Age(String iso8601duration) {
        Matcher matcher = isoDuration.matcher(iso8601duration);
        if (! matcher.matches()) {
            throw new PhenotoolsRuntimeException("Invalid iso8601 age (duration) string: \"" + iso8601duration + "\".");
        }
        return Age.newBuilder().setIso8601Duration(iso8601duration).build();
    }


    public TimeElementBuilder age(String iso8601duration) {
        if (builder != null) {
            throw new PhenotoolsRuntimeException("Not allowed to add two subelements to TimeElement");
        }
        Age age = getIso8601Age(iso8601duration);
        this.builder= TimeElement.newBuilder().setAge(age);
        return this;
    }

    public TimeElementBuilder ageRange(String iso8601start, String iso8601End) {
        if (builder != null) {
            throw new PhenotoolsRuntimeException("Not allowed to add two subelements to TimeElement");
        }
        Age start = getIso8601Age(iso8601start);
        Age end = getIso8601Age(iso8601End);
        this.builder= TimeElement.newBuilder().setAgeRange(AgeRange.newBuilder().setStart(start).setEnd(end));
        return this;
    }

    public TimeElementBuilder ontologyClass(OntologyClass clz) {
        if (builder != null) {
            throw new PhenotoolsRuntimeException("Not allowed to add two subelements to TimeElement");
        }
        this.builder= TimeElement.newBuilder().setOntologyClass(clz);
        return this;
    }

    public TimeElementBuilder fetalOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0011461").setLabel("Fetal onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder embryonalOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0011460").setLabel("Embryonal onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder antenatalOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0030674").setLabel("Antenatal onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder congenitalOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0003577").setLabel("Congenital onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder neonatalOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0003623").setLabel("Neonatal onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder pediatricOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0410280").setLabel("Pediatric onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder childhoodOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0011463").setLabel("Childhood onset").build();
        return ontologyClass(fetal);
    }
    public TimeElementBuilder juvenileOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0003621").setLabel("Juvenile onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder infantileOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0003593").setLabel("Infantile onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder adultOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0003581").setLabel("Adult onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder lateOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0003584").setLabel("Late onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder middleAgeOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0003596").setLabel("Middle age onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder youngAdultOnset() {
        OntologyClass fetal = OntologyClass.newBuilder().setId("HP:0011462").setLabel("Young adult onset").build();
        return ontologyClass(fetal);
    }

    public TimeElementBuilder timestamp(String timestamp) {
        if (builder != null) {
            throw new PhenotoolsRuntimeException("Not allowed to add two subelements to TimeElement");
        }
        Timestamp time = fromRFC3339(timestamp);
        this.builder= TimeElement.newBuilder().setTimestamp(time);
        return this;
    }

    public TimeElementBuilder interval(String timestampStart, String timestampEnd) {
        if (builder != null) {
            throw new PhenotoolsRuntimeException("Not allowed to add two subelements to TimeElement");
        }
        Timestamp start = fromRFC3339(timestampStart);
        Timestamp end = fromRFC3339(timestampEnd);
        this.builder= TimeElement.newBuilder().setInterval(TimeInterval.newBuilder().setStart(start).setEnd(end));
        return this;
    }

    public TimeElement build() {
        if (builder == null) {
            throw new PhenotoolsRuntimeException("Attempt to construct a TImeElement with no data");
        }
        return builder.build();
    }

    public static TimeElementBuilder create() {
        return new TimeElementBuilder();
    }


}
