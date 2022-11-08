package org.phenopackets.phenopackettools.builder.builders;

import org.ga4gh.vrsatile.v1.GeneDescriptor;

import java.util.List;

public class GeneDescriptorBuilder {

    private final GeneDescriptor.Builder builder;

    private GeneDescriptorBuilder(String valueId, String symbol) {
        builder = GeneDescriptor.newBuilder().setValueId(valueId).setSymbol(symbol);
    }

    /**
     * @param valueId Official identifier of the gene, e.g., HGNC:3603
     * @param symbol Official gene symbol, e.g., FBN1
     * @return completely built {@link GeneDescriptor} object
     */
    public static GeneDescriptor of(String valueId, String symbol) {
        return GeneDescriptor.newBuilder().setValueId(valueId).setSymbol(symbol).build();
    }


    /**
     * @param valueId Official identifier of the gene, e.g., HGNC:3603
     * @param symbol Official gene symbol, e.g., FBN1
     * @return GeneDescriptorBuilder that can be used to set additional field values
     */
    public static GeneDescriptorBuilder builder(String valueId, String symbol) {
        return new GeneDescriptorBuilder(valueId, symbol);
    }

    public GeneDescriptorBuilder description(String desc) {
        builder.setDescription(desc);
        return this;
    }

    public GeneDescriptorBuilder addAlternateId(String altId) {
        builder.addAlternateIds(altId);
        return this;
    }

    public GeneDescriptorBuilder addAllAlternateIds(List<String> altIds) {
        builder.addAllAlternateIds(altIds);
        return this;
    }

    public GeneDescriptorBuilder addXref(String xref) {
        builder.addXrefs(xref);
        return this;
    }

    public GeneDescriptorBuilder addAllXrefs(List<String> xrefs) {
        builder.addAllXrefs(xrefs);
        return this;
    }

    public GeneDescriptorBuilder addAlternateSymbol(String altSymbol) {
        builder.addAlternateSymbols(altSymbol);
        return this;
    }

    public GeneDescriptorBuilder addAllAlternateSymbols(List<String> altSymbols) {
        builder.addAllAlternateSymbols(altSymbols);
        return this;
    }

    public GeneDescriptor build() {
        return builder.build();
    }
}
