package org.phenopackets.phenopackettools.builder.builders;

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

    public AlleleBuilder chromosomeLocation(String chrom) {
        ChromosomeLocation chromosomalLocation = ChromosomeLocation.newBuilder().setChr(chrom).build();
        builder.setChromosomeLocation(chromosomalLocation);
        return this;
    }

    /**
     * Sequence ranges use an interbase coordinate system, which involves
     * Two integers that define the start and end positions of a range of
     * residues, possibly with length zero, and specified using “0-start, half-open” coordinates.
     */

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


    public static AlleleBuilder builder() {
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
     * @return The constructed Variant message that contains the Allele and if provided SequenceLocation information.
     */
    public Variation buildVariation() {
        Allele allele = build();
        return Variation.newBuilder().setAllele(allele).build();
    }
}
