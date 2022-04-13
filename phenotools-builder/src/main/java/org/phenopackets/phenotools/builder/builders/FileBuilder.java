package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.File;

public class FileBuilder {

    private final File.Builder builder;

    private FileBuilder(String uri) {
        builder = File.newBuilder().setUri(uri);
    }

    public static File file(String uri) {
        return File.newBuilder().setUri(uri).build();
    }

    public static FileBuilder builder(String uri) {
        return new FileBuilder(uri);
    }

    public FileBuilder addFileAttribute(String k, String v) {
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


    public FileBuilder description(String description) {
        builder.putFileAttributes("description", description);
        return this;
    }
}
