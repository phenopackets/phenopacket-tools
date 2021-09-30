package org.phenopackets.phenotools.builder;

import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

public class PhenopacketBuilder {

    private Phenopacket.Builder builder;

    public PhenopacketBuilder(String id, MetaData metaData) {
        builder = Phenopacket.newBuilder().setId(id).setMetaData(metaData);
    }

    public PhenopacketBuilder individual(Individual subject) {
        builder = builder.mergeFrom(builder.build()).setSubject(subject);
        return this;
    }

    public PhenopacketBuilder phenotypicFeature(PhenotypicFeature feature) {
        builder = builder.mergeFrom(builder.build()).addPhenotypicFeatures(feature);
        return this;
    }

    public PhenopacketBuilder addAllPhenotypicFeatures(List<PhenotypicFeature> features) {
        builder = builder.mergeFrom(builder.build()).addAllPhenotypicFeatures(features);
        return this;
    }

    public PhenopacketBuilder measurement(Measurement measurement) {
        builder = builder.mergeFrom(builder.build()).addMeasurements(measurement);
        return this;
    }

    public PhenopacketBuilder addAllMeasurements(List<Measurement> measurements) {
        builder = builder.mergeFrom(builder.build()).addAllMeasurements(measurements);
        return this;
    }

    public PhenopacketBuilder biosample(Biosample biosample) {
        builder = builder.mergeFrom(builder.build()).addBiosamples(biosample);
        return this;
    }

    public PhenopacketBuilder interpretation(Interpretation interpretation) {
        builder = builder.mergeFrom(builder.build()).addInterpretations(interpretation);
        return this;
    }

    public PhenopacketBuilder disease(Disease disease) {
        builder = builder.mergeFrom(builder.build()).addDiseases(disease);
        return this;
    }

    public PhenopacketBuilder medicalAction(MedicalAction medicalAction) {
        builder = builder.mergeFrom(builder.build()).addMedicalActions(medicalAction);
        return this;
    }

    public PhenopacketBuilder file(File file) {
        builder = builder.mergeFrom(builder.build()).addFiles(file);
        return this;
    }

    public Phenopacket build() {
        return builder.build();
    }

    public static PhenopacketBuilder create(String id, MetaData metaData)  {
        return new PhenopacketBuilder(id, metaData);
    }
}
