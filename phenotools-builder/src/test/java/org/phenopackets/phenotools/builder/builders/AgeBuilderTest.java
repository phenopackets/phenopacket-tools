package org.phenopackets.phenotools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.Age;
import org.phenopackets.schema.v2.core.AgeRange;
import org.phenopackets.schema.v2.core.GestationalAge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class AgeBuilderTest {

    /**
     * https://phenopacket-schema.readthedocs.io/en/v2/age.html#example
     */
    @Test
    void testAge() {
        Age age = AgeBuilder.age("P25Y3M2D");
        assertThat(age.getIso8601Duration(), equalTo("P25Y3M2D"));
    }

    /**
     * https://phenopacket-schema.readthedocs.io/en/v2/age.html#id3
     */
    @Test
    void testAgeRange() {
        AgeRange ageRange = AgeBuilder.ageRange("P45Y", "P49Y");
        assertThat(ageRange.getStart(), equalTo(AgeBuilder.age("P45Y")));
        assertThat(ageRange.getEnd(), equalTo(AgeBuilder.age("P49Y")));
    }

    /**
     * https://phenopacket-schema.readthedocs.io/en/v2/gestational-age.html
     */
    @Test
    void testGestationalAge() {
        GestationalAge gestationalAge = AgeBuilder.gestationalAge(33, 2);
        assertThat(gestationalAge.getWeeks(), equalTo(33));
        assertThat(gestationalAge.getDays(), equalTo(2));
    }
}