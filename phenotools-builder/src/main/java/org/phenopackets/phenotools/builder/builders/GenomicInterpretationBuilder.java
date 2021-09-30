package org.phenopackets.phenotools.builder.builders;


import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.schema.v2.core.GenomicInterpretation;

import java.util.List;

public class GenomicInterpretationBuilder {

    private GenomicInterpretation.Builder builder;

    public GenomicInterpretationBuilder(String id, GenomicInterpretation.InterpretationStatus status) {
        builder = GenomicInterpretation.newBuilder().setSubjectOrBiosampleId(id).setInterpretationStatus(status);
    }

    // value_id 	string 	1..1 	Official identifier of the gene. REQUIRED.
    //symbol 	string 	1..1 	Official gene symbol. REQUIRED.
    //description 	string 	0..1 	A free-text description of the gene
    //alternate_ids 	list of string 	0..* 	Alternative identifier(s) of the gene
    //xrefs 	list of string 	0..* 	Related concept IDs (e.g. gene ortholog IDs) may be placed in xrefs
    //alternate_symbols 	list of string 	0..* 	Alternative symbol(s) of the gene

    GenomicInterpretationBuilder geneDescriptor(String identifier, String symbol) {
        GeneDescriptor geneDescriptor = GeneDescriptor.newBuilder().setValueId(identifier).setSymbol(symbol).build();
        builder = builder.mergeFrom(builder.build()).setGene(geneDescriptor);
        return this;
    }

    GenomicInterpretationBuilder geneDescriptor(String identifier, String symbol, String description) {
        GeneDescriptor geneDescriptor = GeneDescriptor.newBuilder()
                .setValueId(identifier)
                .setSymbol(symbol)
                .setDescription(description)
                .build();
        builder = builder.mergeFrom(builder.build()).setGene(geneDescriptor);
        return this;
    }

    GenomicInterpretationBuilder geneDescriptor(String identifier,
                                                String symbol,
                                                String description,
                                                List<String> alternateIds,
                                                List<String> xrefs,
                                                List<String> alternateSymbols) {

        GeneDescriptor geneDescriptor = GeneDescriptor.newBuilder()
                .setValueId(identifier)
                .setSymbol(symbol)
                .setDescription(description)
                .addAllAlternateIds(alternateIds)
                .addAllXrefs(xrefs)
                .addAllAlternateSymbols(alternateSymbols)
                .build();
        builder = builder.mergeFrom(builder.build()).setGene(geneDescriptor);
        return this;
    }




    public GenomicInterpretation build() {
        return builder.build();
    }

    public static GenomicInterpretationBuilder create(String identifier, GenomicInterpretation.InterpretationStatus status)  {
        return new GenomicInterpretationBuilder(identifier, status);
    }

    public static GenomicInterpretationBuilder rejected(String identifier) {
        return new GenomicInterpretationBuilder(identifier, GenomicInterpretation.InterpretationStatus.REJECTED);
    }

    public static GenomicInterpretationBuilder candidate(String identifier) {
        return new GenomicInterpretationBuilder(identifier, GenomicInterpretation.InterpretationStatus.CANDIDATE);
    }

    public static GenomicInterpretationBuilder contributory(String identifier) {
        return new GenomicInterpretationBuilder(identifier, GenomicInterpretation.InterpretationStatus.CONTRIBUTORY);
    }
    public static GenomicInterpretationBuilder causative(String identifier) {
        return new GenomicInterpretationBuilder(identifier, GenomicInterpretation.InterpretationStatus.CAUSATIVE);
    }


}
