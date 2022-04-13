package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrs.v1.Allele;
import org.ga4gh.vrs.v1.Variation;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class AlleleBuilderTest {

    @Test
    void testBuild() {
        Variation variation = AlleleBuilder.builder()
                .setSequenceId("NC_000003.12")
                .startEnd(42686219, 42686220)
                .chromosomeLocation("chr3")
                .setAltAllele("A")
                .buildVariation();
        assertThat(variation.hasAllele(), equalTo(true));

        Allele allele = variation.getAllele();
        assertThat(allele.getSequenceLocation().getSequenceId(), equalTo("NC_000003.12"));
        assertThat(allele.getSequenceLocation().getSequenceInterval().getStartNumber().getValue(), equalTo(42686219L));
        assertThat(allele.getSequenceLocation().getSequenceInterval().getEndNumber().getValue(), equalTo(42686220L));
        assertThat(allele.getLiteralSequenceExpression().getSequence(), equalTo("A"));
    }
}