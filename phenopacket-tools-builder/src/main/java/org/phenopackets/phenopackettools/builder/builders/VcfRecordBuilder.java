package org.phenopackets.phenopackettools.builder.builders;

import org.ga4gh.vrsatile.v1.VcfRecord;

import java.util.ArrayList;
import java.util.List;

public class VcfRecordBuilder {

    private final VcfRecord.Builder builder;
    private boolean passIsSet = false;
    private List<String> filters = null;

    private VcfRecordBuilder(String assembly, String chromosome, int position, String ref, String alt) {
        builder = VcfRecord.newBuilder()
                .setGenomeAssembly(assembly)
                .setChrom(chromosome)
                .setPos(position)
                .setRef(ref)
                .setAlt(alt);
    }

    public static VcfRecord of(String assembly, String chromosome, int position, String ref, String alt) {
        return VcfRecord.newBuilder()
                .setGenomeAssembly(assembly)
                .setChrom(chromosome)
                .setPos(position)
                .setRef(ref)
                .setAlt(alt)
                .build();
    }

    public static VcfRecordBuilder builder(String assembly, String chromosome, int position, String ref, String alt) {
        return new VcfRecordBuilder(assembly, chromosome, position, ref, alt);
    }

    /**
     * Identifier: Semicolon-separated list of unique identifiers where available
     * If this is a dbSNP/dbVAR variant these rsID/nsvID(s) should be used.
     * @param id identifier for this variant
     */
    public VcfRecordBuilder id(String id) {
        builder.setId(id);
        return this;
    }

    /**
     * @param qual: Phred-scaled quality score for the assertion made in ALT.
     */
    public VcfRecordBuilder qual(String qual) {
        builder.setQual(qual);
        return this;
    }

    /**
     * If this method is called, the FILTER column is set to "PASS" and any other previously added filters are cleared.
     * Calling {@code pass()} is equivalent to calling <code>filter("PASS")</code>.
     */
    public synchronized VcfRecordBuilder pass() {
        passIsSet = true;
        if (filters != null)
            filters.clear();

        return this;
    }

    /**
     * Add a VCF filter field.
     * <p>
     * The field can be a single value (e.g. <code>q50</code>) or several values joined by <code>;</code> (e.g. <code>q10;q50</code>).
     * <p>
     * Calling <code>filter("PASS")</code> is equivalent to calling {@link #pass()}. As a side effect,
     * all previously added filter values are removed.
     *
     * @param filter add a FILTER field.
     * @see #pass()
     */
    public synchronized VcfRecordBuilder filter(String filter) {
        passIsSet = false;
        if (filters == null)
            filters = new ArrayList<>();

        if ("PASS".equalsIgnoreCase(filter))
            return pass();
        else {
            if (filter.contains(";")) {
                for (String field : filter.split(";")) {
                    String trimmed = field.trim();
                    if (!trimmed.isEmpty())
                        this.filter(field);
                }
            } else
                filters.add(filter);
        }

        return this;
    }

    /**
     * @param info Additional information: Semicolon-separated series of additional information fields
     */
    public VcfRecordBuilder info(String info) {
        builder.setInfo(info);
        return this;
    }

    public synchronized VcfRecord build() {
        if (passIsSet)
            builder.setFilter("PASS");

        else if (filters != null && !filters.isEmpty())
            builder.setFilter(String.join(";", filters));

        return builder.build();
    }
}
