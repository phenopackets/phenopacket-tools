package org.phenopackets.phenotools.builder;

import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

public class PhenopacketBuilder {

    private final Phenopacket.Builder builder;

    public PhenopacketBuilder(String id, MetaData metaData) {
        builder = Phenopacket.newBuilder().setId(id).setMetaData(metaData);
    }

    public PhenopacketBuilder individual(Individual subject) {
        builder.setSubject(subject);
        return this;
    }

    public PhenopacketBuilder phenotypicFeature(PhenotypicFeature feature) {
        builder.addPhenotypicFeatures(feature);
        return this;
    }

    public PhenopacketBuilder allPhenotypicFeatures(List<PhenotypicFeature> features) {
        builder.addAllPhenotypicFeatures(features);
        return this;
    }

    public PhenopacketBuilder measurement(Measurement measurement) {
        builder.addMeasurements(measurement);
        return this;
    }

    public PhenopacketBuilder allMeasurements(List<Measurement> measurements) {
        builder.addAllMeasurements(measurements);
        return this;
    }

    public PhenopacketBuilder biosample(Biosample biosample) {
        builder.addBiosamples(biosample);
        return this;
    }

    public PhenopacketBuilder interpretation(Interpretation interpretation) {
        builder.addInterpretations(interpretation);
        return this;
    }

    public PhenopacketBuilder disease(Disease disease) {
        builder.addDiseases(disease);
        return this;
    }

    public PhenopacketBuilder medicalAction(MedicalAction medicalAction) {
        builder.addMedicalActions(medicalAction);
        return this;
    }

    public PhenopacketBuilder file(File file) {
        builder.addFiles(file);
        return this;
    }

    public Phenopacket build() {
        return builder.build();
    }

    public static PhenopacketBuilder create(String id, MetaData metaData)  {
        return new PhenopacketBuilder(id, metaData);
    }
}
