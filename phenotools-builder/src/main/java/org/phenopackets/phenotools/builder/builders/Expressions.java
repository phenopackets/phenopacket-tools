package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrsatile.v1.Expression;

public class Expressions {

    public static Expression hgvsCdna(String value) {
        return Expression.newBuilder().setSyntax("hgvs.c").setValue(value).build();
    }

    public static Expression transcriptReference(String value) {
        return Expression.newBuilder().setSyntax("transcript_reference").setValue(value).build();
    }


}
