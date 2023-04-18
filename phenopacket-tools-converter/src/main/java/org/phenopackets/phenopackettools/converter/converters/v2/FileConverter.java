package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v1.core.HtsFile;
import org.phenopackets.schema.v2.core.File;

import java.util.List;
import java.util.Optional;

public class FileConverter {

    private FileConverter() {
    }

    public static List<File> toFiles(List<HtsFile> htsFilesList) {
        return htsFilesList.stream()
                .map(FileConverter::toFile)
                .flatMap(Optional::stream)
                .toList();
    }

    static Optional<File> toFile(HtsFile htsFile) {
        if (htsFile.equals(HtsFile.getDefaultInstance()))
            return Optional.empty();

        boolean isDefault = true;
        File.Builder builder = File.newBuilder();

        if (!htsFile.getUri().isEmpty()) {
            isDefault = false;
            builder.setUri(htsFile.getUri());
        }

        String assembly = htsFile.getGenomeAssembly();
        if (!assembly.isEmpty()) {
            isDefault = false;
            builder.putFileAttributes("genomeAssembly", assembly);
        }

        HtsFile.HtsFormat htsFormat = htsFile.getHtsFormat();
        if (!htsFormat.equals(HtsFile.HtsFormat.UNKNOWN) && !htsFormat.equals(HtsFile.HtsFormat.UNRECOGNIZED)) {
            isDefault = false;
            builder.putFileAttributes("fileFormat", htsFormat.name().toLowerCase());
        }

        if (!htsFile.getDescription().isEmpty()) {
            isDefault = false;
            builder.putFileAttributes("description", htsFile.getDescription());
        }

        if (htsFile.getIndividualToSampleIdentifiersCount() != 0) {
            isDefault = false;
            builder.putAllIndividualToFileIdentifiers(htsFile.getIndividualToSampleIdentifiersMap());
        }

        return isDefault
                ? Optional.empty()
                : Optional.of(builder.build());
    }

}
