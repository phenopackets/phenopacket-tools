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
    public CopyNumberBuilder startEnd(int startPos, int endPos) {
        SequenceInterval interval = SequenceInterval.newBuilder()
                .setStartNumber(Number.newBuilder().setValue(startPos))
                .setEndNumber(Number.newBuilder().setValue(endPos))
                .build();
        slbuilder.setSequenceInterval(interval);
        return this;
    }


    public CopyNumber build() {
        return builder.build();
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
