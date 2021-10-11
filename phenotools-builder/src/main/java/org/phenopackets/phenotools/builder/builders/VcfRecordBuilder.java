package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrsatile.v1.VcfRecord;

public class VcfRecordBuilder {

    private final VcfRecord.Builder builder;

    public VcfRecordBuilder(String assembly, String chromosome, int position, String ref, String alt) {
        builder = VcfRecord.newBuilder()
                .setGenomeAssembly(assembly)
                .setChrom(chromosome)
                .setPos(position)
                .setRef(ref)
                .setAlt(alt);
    }

    /**
     * Identifier: Semicolon-separated list of unique identifiers where available
     * If this is a dbSNP variant thes number(s) should be used.
     * @param id identifier for this variant
     */
    public VcfRecordBuilder id(String id) {
        builder.setId(id);
        return this;
    }

    /**
     * @param QUAL: Phred-scaled quality score for the assertion made in ALT.
     */
    public VcfRecordBuilder qual(String QUAL) {
        builder.setQual(QUAL);
        return this;
    }

    /**
     * If this method is called, "PASS" is added to the FILTER column
     */
    public VcfRecordBuilder pass() {
        builder.setFilter("PASS");
        return this;
    }

    /**
     * @param filter FILTER field of VCF. calling {@link #pass()} is equivant to calling filter("PASS)
     */
    public VcfRecordBuilder filter(String filter) {
        builder.setFilter(filter);
        return this;
    }

    /**
     * @param info Additional information: Semicolon-separated series of additional information fields
     */
    public VcfRecordBuilder info(String info) {
        builder.setInfo(info);
        return this;
    }

    public VcfRecord build() {
        return builder.build();
    }

    public static VcfRecordBuilder create(String assembly, String chromosome, int position, String ref, String alt) {
        return new VcfRecordBuilder(assembly, chromosome, position, ref, alt);
    }


}
