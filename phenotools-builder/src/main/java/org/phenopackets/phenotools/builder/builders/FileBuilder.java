package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.File;

public class FileBuilder {

    private final File.Builder builder;

    public FileBuilder(String uri) {
        builder = File.newBuilder().setUri(uri);
    }

    public FileBuilder addFileAttribute(String k, String v) {
        builder.mergeFrom(builder.build()).putFileAttributes(k, v);
        return this;
    }

    public FileBuilder addIndividualToFileIdentifiers(String individual, String fileIdentifier) {
        builder.mergeFrom(builder.build()).putIndividualToFileIdentifiers(individual, fileIdentifier);
        return this;
    }

    public File build() {
        return builder.build();
    }

    public static FileBuilder create(String uri) {
        return new FileBuilder(uri);
    }

    public static FileBuilder hg38vcf(String uri) {
        FileBuilder fb = new FileBuilder(uri);
        fb.addFileAttribute("genomeAssembly", "GRCh38");
        fb.addFileAttribute("fileFormat", "VCF");
        return fb;
    }

    public static FileBuilder hg37vcf(String uri) {
        FileBuilder fb = new FileBuilder(uri);
        fb.addFileAttribute("genomeAssembly", "GRCh37");
        fb.addFileAttribute("fileFormat", "VCF");
        return fb;
    }
}
