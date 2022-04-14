package org.phenopackets.phenopackettools.converter.converters;

import org.phenopackets.schema.v2.Phenopacket;

import java.util.List;
import java.util.stream.Collectors;

import static org.phenopackets.phenopackettools.converter.converters.v2.BiosampleConverter.toBiosamples;
import static org.phenopackets.phenopackettools.converter.converters.v2.DiseaseConverter.toDiseases;
import static org.phenopackets.phenopackettools.converter.converters.v2.FileConverter.toFiles;
import static org.phenopackets.phenopackettools.converter.converters.v2.IndividualConverter.toIndividual;
import static org.phenopackets.phenopackettools.converter.converters.v2.MetaDataConverter.toMetaData;
import static org.phenopackets.phenopackettools.converter.converters.v2.PhenotypicFeatureConverter.toPhenotypicFeatures;

public class PhenopacketConverter {

    private PhenopacketConverter() {
    }

    public static Phenopacket toV2Phenopacket(org.phenopackets.schema.v1.Phenopacket v1PhenoPacket) {
        Phenopacket.Builder builder = Phenopacket.newBuilder();

        if (v1PhenoPacket.hasMetaData()) {
            builder.setMetaData(toMetaData(v1PhenoPacket.getMetaData()));
        }
        if (!v1PhenoPacket.getId().isEmpty()) {
            builder.setId(v1PhenoPacket.getId());
        }
        if (v1PhenoPacket.hasSubject()) {
            builder.setSubject(toIndividual(v1PhenoPacket.getSubject()));
        }
        if (v1PhenoPacket.getPhenotypicFeaturesCount() > 0) {
            builder.addAllPhenotypicFeatures(toPhenotypicFeatures(v1PhenoPacket.getPhenotypicFeaturesList()));
        }
        if (v1PhenoPacket.getBiosamplesCount() > 0) {
            builder.addAllBiosamples(toBiosamples(v1PhenoPacket.getBiosamplesList()));
        }
        if (v1PhenoPacket.getDiseasesCount() > 0) {
            builder.addAllDiseases(toDiseases(v1PhenoPacket.getDiseasesList()));
        }
        if (v1PhenoPacket.getHtsFilesCount() > 0) {
            builder.addAllFiles(toFiles(v1PhenoPacket.getHtsFilesList()));
        }
        return builder.build();
    }

    public static List<Phenopacket> toV2Phenopackets(List<org.phenopackets.schema.v1.Phenopacket> v1Phenopackets) {
        return v1Phenopackets.stream().map(PhenopacketConverter::toV2Phenopacket).collect(Collectors.toUnmodifiableList());
    }
}
