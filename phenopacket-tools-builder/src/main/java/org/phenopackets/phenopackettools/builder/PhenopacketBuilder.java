package org.phenopackets.phenopackettools.builder;

import org.phenopackets.phenopackettools.builder.builders.PhenotypicFeatureBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

public class PhenopacketBuilder {

    private final Phenopacket.Builder builder;

    private PhenopacketBuilder(String id, MetaData metaData) {
        builder = Phenopacket.newBuilder().setId(id).setMetaData(metaData);
    }

    public static PhenopacketBuilder create(String id, MetaData metaData)  {
        return new PhenopacketBuilder(id, metaData);
    }

    public PhenopacketBuilder individual(Individual subject) {
        builder.setSubject(subject);
        return this;
    }

    public PhenopacketBuilder addPhenotypicFeature(String id, String label) {
        PhenotypicFeature phenotypicFeature = PhenotypicFeatureBuilder.of(id, label);
        return addPhenotypicFeature(phenotypicFeature);
    }

    public PhenopacketBuilder addPhenotypicFeature(PhenotypicFeature feature) {
        builder.addPhenotypicFeatures(feature);
        return this;
    }

    public PhenopacketBuilder addAllPhenotypicFeatures(List<PhenotypicFeature> features) {
        builder.addAllPhenotypicFeatures(features);
        return this;
    }

    public PhenopacketBuilder addMeasurement(Measurement measurement) {
        builder.addMeasurements(measurement);
        return this;
    }

    public PhenopacketBuilder addAllMeasurements(List<Measurement> measurements) {
        builder.addAllMeasurements(measurements);
        return this;
    }

    public PhenopacketBuilder addBiosample(Biosample biosample) {
        builder.addBiosamples(biosample);
        return this;
    }

    public PhenopacketBuilder addAllBiosamples(List<Biosample> biosample) {
        builder.addAllBiosamples(biosample);
        return this;
    }

    public PhenopacketBuilder addInterpretation(Interpretation interpretation) {
        builder.addInterpretations(interpretation);
        return this;
    }

    public PhenopacketBuilder addDisease(Disease disease) {
        builder.addDiseases(disease);
        return this;
    }

    public PhenopacketBuilder addAllDiseases(List<Disease> diseaseList) {
        builder.addAllDiseases(diseaseList);
        return this;
    }

    public PhenopacketBuilder addMedicalAction(MedicalAction medicalAction) {
        builder.addMedicalActions(medicalAction);
        return this;
    }

    public PhenopacketBuilder addFile(File file) {
        builder.addFiles(file);
        return this;
    }

    public Phenopacket build() {
        return builder.build();
    }
}
