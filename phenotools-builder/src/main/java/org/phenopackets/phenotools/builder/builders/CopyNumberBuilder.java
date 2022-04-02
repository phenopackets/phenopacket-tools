package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrs.v1.*;
import org.ga4gh.vrs.v1.Number;

public class CopyNumberBuilder {

    private final CopyNumber.Builder builder;
    private final SequenceLocation.Builder slbuilder;

    private CopyNumberBuilder() {
        this.builder = CopyNumber.newBuilder();

        slbuilder = SequenceLocation.newBuilder();
    }

    public CopyNumberBuilder copyNumberId(String id) {
        builder.setId(id);
        return this;
    }
    public CopyNumberBuilder alleleLocation(String contig, int startPos, int endPos) {
        SequenceInterval interval = SequenceInterval.newBuilder()
                .setStartNumber(Number.newBuilder().setValue(startPos))
                .setEndNumber(Number.newBuilder().setValue(endPos))
                .build();
        slbuilder.setSequenceInterval(interval);
        builder.setDefiniteRange(DefiniteRange.newBuilder().setMin(startPos).setMax(endPos).getDefaultInstanceForType());
        AlleleBuilder alleleBuilder = AlleleBuilder.create();
        alleleBuilder.startEnd(startPos, endPos);
        alleleBuilder.chromosomeLocation(contig);
        builder.setAllele(alleleBuilder.build());
        return this;
    }


    public CopyNumber build() {
        return builder.build();
    }

    public CopyNumberBuilder oneCopy() {
        builder.setNumber(Number.newBuilder().setValue(1));
        return this;
    }
    public CopyNumberBuilder twoCopies() {
        builder.setNumber(Number.newBuilder().setValue(2));
        return this;
    }
    public CopyNumberBuilder threeCopies() {
        builder.setNumber(Number.newBuilder().setValue(3));
        return this;
    }

    public CopyNumberBuilder nCopies(int n) {
        builder.setNumber(Number.newBuilder().setValue(n));
        return this;
    }

    /**
     * Wrap the Allele message in a Variation message
     * @return The constructed Variant message that contains the Allele and if provided SequenceLocation information.
     */
    public Variation buildVariation() {
        return Variation.newBuilder().setCopyNumber(builder).build();

    }
    public static CopyNumberBuilder create() {
        return new CopyNumberBuilder();
    }


}
