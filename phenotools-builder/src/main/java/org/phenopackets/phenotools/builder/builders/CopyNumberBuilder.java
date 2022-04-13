package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrs.v1.*;
import org.ga4gh.vrs.v1.Number;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;

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
     * Variation variation = AlleleBuilder.create()
     *             .setSequenceId("refseq:NC_000003.12") // This is how and where the 'chromosome' should be set, although I think they suggest using a VRS CURIE such as ga4gh:SQ:dfsjfhgasriwyu38w3
     *             .startEnd(42686219, 42686220)
     * //            .chromosomeLocation("chr3")
     *             .setAltAllele("A")
     *             .buildVariation();
     * @param contig
     * @param startPos
     * @param endPos
     * @return
     */
    public CopyNumberBuilder alleleLocation(String contig, int startPos, int endPos) {
        AlleleBuilder abuilder = AlleleBuilder.builder()
                        .setSequenceId(contig)
                        .startEnd(startPos, endPos);
        builder.setAllele(abuilder.build());
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
            throw new PhenotoolsRuntimeException("Negative copy numbers are not allowed");
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
