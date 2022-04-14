package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v1.core.HtsFile;
import org.phenopackets.schema.v2.core.File;

import java.util.List;
import java.util.stream.Collectors;

public class FileConverter {

    private FileConverter() {
    }

    public static List<File> toFiles(List<HtsFile> htsFilesList) {
        return htsFilesList.stream().map(FileConverter::toFile).collect(Collectors.toUnmodifiableList());
    }

    public static File toFile(HtsFile htsFile) {
        return File.newBuilder()
                .setUri(htsFile.getUri())
                .putFileAttributes("genomeAssembly", htsFile.getGenomeAssembly())
                .putFileAttributes("fileFormat", htsFile.getHtsFormat().name().toLowerCase())
                .putFileAttributes("description", htsFile.getDescription())
                .putAllIndividualToFileIdentifiers(htsFile.getIndividualToSampleIdentifiersMap())
                .build();
    }

}
