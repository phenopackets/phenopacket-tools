package org.phenopackets.phenotools.converter.converters;

import org.phenopackets.schema.v2.Phenopacket;

import static org.phenopackets.phenotools.converter.converters.v2.DiseaseConverter.toDiseases;
import static org.phenopackets.phenotools.converter.converters.v2.FileConverter.toFiles;
import static org.phenopackets.phenotools.converter.converters.v2.IndividualConverter.toIndividual;
import static org.phenopackets.phenotools.converter.converters.v2.MetaDataConverter.toMetaData;
import static org.phenopackets.phenotools.converter.converters.v2.PhenotypicFeatureConverter.toPhenotypicFeatures;

public class PhenopacketConverter {

    private PhenopacketConverter() {
    }

    public static Phenopacket convertToV2(org.phenopackets.schema.v1.Phenopacket v1PhenoPacket) {
        return Phenopacket.newBuilder()
                .setMetaData(toMetaData(v1PhenoPacket.getMetaData()))
                .setId(v1PhenoPacket.getId())
                .setSubject(toIndividual(v1PhenoPacket.getSubject()))
                .addAllPhenotypicFeatures(toPhenotypicFeatures(v1PhenoPacket.getPhenotypicFeaturesList()))
                .addAllDiseases(toDiseases(v1PhenoPacket.getDiseasesList()))
                .addAllFiles(toFiles(v1PhenoPacket.getHtsFilesList()))
                .build();
    }

}
