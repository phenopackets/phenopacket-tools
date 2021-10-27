package org.phenopackets.phenotools.converter.converters;

import org.phenopackets.schema.v2.Family;

import static org.phenopackets.phenotools.converter.converters.PhenopacketConverter.toV2Phenopacket;
import static org.phenopackets.phenotools.converter.converters.PhenopacketConverter.toV2Phenopackets;
import static org.phenopackets.phenotools.converter.converters.v2.FileConverter.toFiles;
import static org.phenopackets.phenotools.converter.converters.v2.MetaDataConverter.toMetaData;
import static org.phenopackets.phenotools.converter.converters.v2.PedigreeConverter.toPedigree;

public class FamilyConverter {

    private FamilyConverter() {
    }

    public static Family toV2Family(org.phenopackets.schema.v1.Family v1Family) {
        Family.Builder builder = Family.newBuilder();

        if (v1Family.hasMetaData()) {
            builder.setMetaData(toMetaData(v1Family.getMetaData()));
        }
        if (!v1Family.getId().isEmpty()) {
            builder.setId(v1Family.getId());
        }
        if (v1Family.hasPedigree()) {
            builder.setPedigree(toPedigree(v1Family.getPedigree()));
        }
        if (v1Family.hasProband()) {
            builder.setProband(toV2Phenopacket(v1Family.getProband()));
        }
        if (v1Family.getRelativesCount() > 0) {
            builder.addAllRelatives(toV2Phenopackets(v1Family.getRelativesList()));
        }
        if (v1Family.getHtsFilesCount() > 0) {
            builder.addAllFiles(toFiles(v1Family.getHtsFilesList()));
        }

        return builder.build();
    }
}
