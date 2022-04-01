package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrs.v1.*;
import org.ga4gh.vrs.v1.Number;


/**
 * Create a VRS Allele object such as
 *
 * {'_id': 'ga4gh:VA.GuPzvZoansqNHPoXkQLXKo31VkTpDKsM',
 *  'type': 'Allele',
 *  'location': {'type': 'SequenceLocation',
 *   'sequence_id': 'ga4gh:SQ.FOWokFmA__GgqWLtqFoWWDLuNEvvGwIJ',
 *   'interval': {'type': 'SequenceInterval',
 *    'start': {'type': 'Number', 'value': 48941647},
 *    'end': {'type': 'Number', 'value': 48941648}}},
 *  'state': {'type': 'LiteralSequenceExpression', 'sequence': 'T'}}
 *
 */
public class AlleleBuilder {

    private final Allele.Builder builder;

    private final SequenceLocation.Builder slbuilder;

    private AlleleBuilder() {
        builder = Allele.newBuilder();
        slbuilder = SequenceLocation.newBuilder();
    }

    public AlleleBuilder variantId(String id) {
        builder.setId(id);
        return this;
    }

    public AlleleBuilder startEnd(int start, int end) {
        SequenceInterval interval = SequenceInterval.newBuilder()
                .setStartNumber(Number.newBuilder().setValue(start))
                .setEndNumber(Number.newBuilder().setValue(end))
                .build();
        slbuilder.setSequenceInterval(interval);
        return this;
    }

    public AlleleBuilder setAltAllele(String alt) {
        builder.setLiteralSequenceExpression(LiteralSequenceExpression.newBuilder().setSequence(alt));
        return this;
    }

    public AlleleBuilder setSequenceId(String sequenceId) {
        slbuilder.setSequenceId(sequenceId);
        return this;
    }


    public static AlleleBuilder create() {
        return new AlleleBuilder();
    }

    public Allele build() {
        if (slbuilder.hasSequenceInterval()) {
            builder.setSequenceLocation(slbuilder);
        }
        return builder.build();
    }

    /**
     * Wrap the Allele message in a Variation message
     * @return
     */
    public Variation buildVariation() {
        if (slbuilder.hasSequenceInterval()) {
            builder.setSequenceLocation(slbuilder);
        }
        return Variation.newBuilder().setAllele(builder.build()).build();
    }
}
