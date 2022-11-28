package org.phenopackets.phenopackettools.builder.builders;

import org.ga4gh.vrs.v1.*;
import org.ga4gh.vrs.v1.Number;
import org.phenopackets.phenopackettools.core.PhenopacketToolsRuntimeException;

public class CopyNumberBuilder {

    private final CopyNumber.Builder builder;

    private CopyNumberBuilder() {
        this.builder = CopyNumber.newBuilder();
    }

    public CopyNumberBuilder copyNumberId(String id) {
        builder.setId(id);
        return this;
    }

    /**
     *  Sequence ranges use an interbase coordinate system, which involves
     *  Two integers that define the start and end positions of a range of
     *  residues, possibly with length zero, and specified using “0-start, half-open” coordinates.
     */
    public CopyNumberBuilder alleleLocation(String contig, int interbaseStartPos, int interbaseEndPos) {
        builder.setDerivedSequenceExpression(DerivedSequenceExpression.newBuilder()
                .setLocation(SequenceLocation.newBuilder()
                        .setSequenceId(contig)
                        .setSequenceInterval(SequenceInterval.newBuilder()
                                .setStartNumber(Number.newBuilder().setValue(interbaseStartPos))
                                .setEndNumber(Number.newBuilder().setValue(interbaseEndPos)))));
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
        if (n < 0) {
            throw new PhenopacketToolsRuntimeException("Negative copy numbers are not allowed");
        }
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
    public static CopyNumberBuilder builder() {
        return new CopyNumberBuilder();
    }


}
