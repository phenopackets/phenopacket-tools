package org.phenopackets.phenopackettools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.Age;
import org.phenopackets.schema.v2.core.AgeRange;
import org.phenopackets.schema.v2.core.GestationalAge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AgeBuilderTest {

    /**
     * <a href="https://phenopacket-schema.readthedocs.io/en/v2/age.html#example">https://phenopacket-schema.readthedocs.io/en/v2/age.html#example</a>
     */
    @Test
    public void testAge() {
        Age age = Ages.age("P25Y3M2D");
        assertThat(age.getIso8601Duration(), equalTo("P25Y3M2D"));
    }

    /**
     * <a href="https://phenopacket-schema.readthedocs.io/en/v2/age.html#id3">https://phenopacket-schema.readthedocs.io/en/v2/age.html#id3</a>
     */
    @Test
    public void testAgeRange() {
        AgeRange ageRange = Ages.ageRange("P45Y", "P49Y");
        assertThat(ageRange.getStart(), equalTo(Ages.age("P45Y")));
        assertThat(ageRange.getEnd(), equalTo(Ages.age("P49Y")));
    }

    /**
     * <a href="https://phenopacket-schema.readthedocs.io/en/v2/gestational-age.html">https://phenopacket-schema.readthedocs.io/en/v2/gestational-age.html</a>
     */
    @Test
    public void testGestationalAge() {
        GestationalAge gestationalAge = Ages.gestationalAge(33, 2);
        assertThat(gestationalAge.getWeeks(), equalTo(33));
        assertThat(gestationalAge.getDays(), equalTo(2));
    }
}