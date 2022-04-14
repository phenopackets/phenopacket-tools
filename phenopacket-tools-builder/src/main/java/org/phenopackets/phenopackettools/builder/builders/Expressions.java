package org.phenopackets.phenopackettools.builder.builders;

import org.ga4gh.vrsatile.v1.Expression;

public class Expressions {

    /**
     * An Human Genome Variation Society (HGVS) expression that denotes a DNA variant <a href="https://varnomen.hgvs.org/">https://varnomen.hgvs.org/</a>
     * @param hgvsExpression a String such as NM_004006.1:c.3>T
     */
    public static Expression hgvsCdna(String hgvsExpression) {
        return Expression.newBuilder().setSyntax("hgvs.c").setValue(hgvsExpression).build();
    }

    /**
     * The identifier of the transcript that is used as a reference for a variant
     * @param value a string such as "NM_000321.2"
     */
    public static Expression transcriptReference(String value) {
        return Expression.newBuilder().setSyntax("transcript_reference").setValue(value).build();
    }


}
