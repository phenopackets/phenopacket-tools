package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrsatile.v1.GeneDescriptor;

import java.util.List;

public class GeneDescriptorBuilder {
    GeneDescriptor.Builder builder;

    public GeneDescriptorBuilder(String identifier, String symbol) {
        builder = GeneDescriptor.newBuilder().setValueId(identifier).setSymbol(symbol);
    }

    public GeneDescriptorBuilder description(String desc) {
        builder = builder.mergeFrom(builder.build()).setDescription(desc);
        return this;
    }

    public GeneDescriptorBuilder alternateId(String alt_id) {
        builder = builder.mergeFrom(builder.build()).addAlternateIds(alt_id);
        return this;
    }

    public GeneDescriptorBuilder addAllAlternateIds(List<String> alt_ids) {
        builder = builder.mergeFrom(builder.build()).addAllAlternateIds(alt_ids);
        return this;
    }
    public GeneDescriptorBuilder xref(String xref) {
        builder = builder.mergeFrom(builder.build()).addXrefs(xref);
        return this;
    }

    public GeneDescriptorBuilder addAllXrefs(List<String> xrefs) {
        builder = builder.mergeFrom(builder.build()).addAllXrefs(xrefs);
        return this;
    }

    public GeneDescriptorBuilder alternateSymbol(String altSymbol) {
        builder = builder.mergeFrom(builder.build()).addAlternateSymbols(altSymbol);
        return this;
    }
    public GeneDescriptorBuilder addAllAlternateSymbols(List<String>  altSymbols) {
        builder = builder.mergeFrom(builder.build()).addAllAlternateSymbols(altSymbols);
        return this;
    }

    public GeneDescriptor build() {
        return builder.build();
    }

    public static GeneDescriptorBuilder create(String identifier, String symbol) {
        return new GeneDescriptorBuilder(identifier, symbol);
    }

}
