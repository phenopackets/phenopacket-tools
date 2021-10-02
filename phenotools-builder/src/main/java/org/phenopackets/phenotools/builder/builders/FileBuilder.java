package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.File;

public class FileBuilder {

    private final File.Builder builder;

    public FileBuilder(String uri) {
        builder = File.newBuilder().setUri(uri);
    }

    public FileBuilder fileAttribute(String k, String v) {
        builder.putFileAttributes(k, v);
        return this;
    }

    public FileBuilder individualToFileIdentifier(String individual, String fileIdentifier) {
        builder.putIndividualToFileIdentifiers(individual, fileIdentifier);
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
        fb.fileAttribute("genomeAssembly", "GRCh38");
        fb.fileAttribute("fileFormat", "VCF");
        return fb;
    }

    public static FileBuilder hg37vcf(String uri) {
        FileBuilder fb = new FileBuilder(uri);
        fb.fileAttribute("genomeAssembly", "GRCh37");
        fb.fileAttribute("fileFormat", "VCF");
        return fb;
    }
}
