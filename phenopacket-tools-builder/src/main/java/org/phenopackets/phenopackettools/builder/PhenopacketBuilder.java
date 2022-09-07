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

    public PhenopacketBuilder addPhenotypicFeatures(List<PhenotypicFeature> features) {
        features.forEach(builder::addPhenotypicFeatures);
        return this;
    }

    public PhenopacketBuilder addMeasurement(Measurement measurement) {
        builder.addMeasurements(measurement);
        return this;
    }

    public PhenopacketBuilder addMeasurements(List<Measurement> measurementList) {
        measurementList.forEach(builder::addMeasurements);
        return this;
    }

    public PhenopacketBuilder addBiosample(Biosample biosample) {
        builder.addBiosamples(biosample);
        return this;
    }

    public PhenopacketBuilder addAllBiosamples(List<Biosample> biosampleList) {
        biosampleList.forEach(builder::addBiosamples);
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

    public PhenopacketBuilder addDiseases(List<Disease> diseaseList) {
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

    public PhenopacketBuilder addMedicalActions(List<MedicalAction> previousTreatments) {
        previousTreatments.forEach(builder::addMedicalActions);
        return this;
    }
}
