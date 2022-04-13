package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.*;

import java.util.List;

public class BiosampleBuilder {

    private final Biosample.Builder builder;

    private BiosampleBuilder(String id) {
        builder = Biosample.newBuilder().setId(id);
    }

    public static BiosampleBuilder builder(String id) {
        return new BiosampleBuilder(id);
    }

    public BiosampleBuilder individualId(String id) {
        builder.setIndividualId(id);
        return this;
    }

    public BiosampleBuilder derivedFromId(String id) {
        builder.setDerivedFromId(id);
        return this;
    }

    public BiosampleBuilder description(String desc) {
        builder.setDescription(desc);
        return this;
    }

    public BiosampleBuilder sampledTissue(OntologyClass tissue) {
        builder.setSampledTissue(tissue);
        return this;
    }

    public BiosampleBuilder sampledType(OntologyClass tissue) {
        builder.setSampleType(tissue);
        return this;
    }

    public BiosampleBuilder phenotypicFeature(PhenotypicFeature feature) {
        builder.addPhenotypicFeatures(feature);
        return this;
    }
    public BiosampleBuilder allPhenotypicFeatureS(List<PhenotypicFeature> features) {
        builder.addAllPhenotypicFeatures(features);
        return this;
    }

    public BiosampleBuilder measurement(Measurement measurement) {
        builder.addMeasurements(measurement);
        return this;
    }

    public BiosampleBuilder allMeasurements(List<Measurement> measurements) {
        builder.addAllMeasurements(measurements);
        return this;
    }
    public BiosampleBuilder taxonomy(OntologyClass taxon) {
        builder.setTaxonomy(taxon);
        return this;
    }
    public BiosampleBuilder timeOfCollection(TimeElement time) {
        builder.setTimeOfCollection(time);
        return this;
    }

    public BiosampleBuilder histologicalDiagnosis(OntologyClass dx) {
        builder.setHistologicalDiagnosis(dx);
        return this;
    }

    public BiosampleBuilder tumorProgression(OntologyClass progression) {
        builder.setTumorProgression(progression);
        return this;
    }

    public BiosampleBuilder tumorGrade(OntologyClass grade) {
        builder.setTumorGrade(grade);
        return this;
    }

    public BiosampleBuilder pathologicalStage(OntologyClass stage) {
        builder.setPathologicalStage(stage);
        return this;
    }
    public BiosampleBuilder pathologicalTnmFinding(OntologyClass tnmFinding) {
        builder.addPathologicalTnmFinding(tnmFinding);
        return this;
    }

    public BiosampleBuilder allPathologicalTnmFindings(List<OntologyClass> tnmFindings) {
        builder.addAllPathologicalTnmFinding(tnmFindings);
        return this;
    }
    public BiosampleBuilder diagnosticMarker(OntologyClass marker) {
        builder.addDiagnosticMarkers(marker);
        return this;
    }
    public BiosampleBuilder allDiagnosticMarker(List<OntologyClass> markers) {
        builder.addAllDiagnosticMarkers(markers);
        return this;
    }

    public BiosampleBuilder procedure(Procedure procedure) {
        builder.setProcedure(procedure);
        return this;
    }

    public BiosampleBuilder file(File file) {
        builder.addFiles(file);
        return this;
    }

    public BiosampleBuilder allFile(List<File> files) {
        builder.addAllFiles(files);
        return this;
    }

    public BiosampleBuilder materialSample(OntologyClass material) {
        builder.setMaterialSample(material);
        return this;
    }

    public BiosampleBuilder sampleProcessing(OntologyClass processing) {
        builder.setSampleProcessing(processing);
        return this;
    }
    public BiosampleBuilder sampleStorage(OntologyClass storage) {
        builder.setSampleStorage(storage);
        return this;
    }

   public Biosample build() {
       return builder.build();
   }
}
