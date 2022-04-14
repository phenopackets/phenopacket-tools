package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrsatile.v1.GeneDescriptor;

import java.util.List;

public class GeneDescriptorBuilder {

    private final GeneDescriptor.Builder builder;

    private GeneDescriptorBuilder(String identifier, String symbol) {
        builder = GeneDescriptor.newBuilder().setValueId(identifier).setSymbol(symbol);
    }

    public static GeneDescriptor geneDescriptor(String identifier, String symbol) {
        return GeneDescriptor.newBuilder().setValueId(identifier).setSymbol(symbol).build();
    }

    public static GeneDescriptorBuilder builder(String identifier, String symbol) {
        return new GeneDescriptorBuilder(identifier, symbol);
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
