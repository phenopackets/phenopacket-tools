package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.*;

import java.util.List;
import java.util.Optional;

import static org.phenopackets.phenopackettools.converter.converters.v2.PhenotypicFeatureConverter.*;

public class BiosampleConverter {

    private static final OntologyClass referenceSample = OntologyClassBuilder.ontologyClass("EFO:0009654", "reference sample");
    private static final OntologyClass abnormalSample = OntologyClassBuilder.ontologyClass("EFO:0009655", "abnormal sample");

    private BiosampleConverter() {
    }

    public static List<Biosample> toBiosamples(List<org.phenopackets.schema.v1.core.Biosample> biosamples) {
        return biosamples.stream()
                .map(BiosampleConverter::toBiosample)
                .flatMap(Optional::stream)
                .toList();
    }

    public static Optional<Biosample> toBiosample(org.phenopackets.schema.v1.core.Biosample biosample) {
        if (biosample.equals(org.phenopackets.schema.v1.core.Biosample.getDefaultInstance()))
            return Optional.empty();

        boolean isDefault = true;
        Biosample.Builder builder = Biosample.newBuilder();

        String id = biosample.getId();
        if (!id.isEmpty()) {
            isDefault = false;
            builder.setId(id);
        }

        String individualId = biosample.getIndividualId();
        if (!individualId.isEmpty()) {
            isDefault = false;
            builder.setIndividualId(individualId);
        }

        String description = biosample.getDescription();
        if (!description.isEmpty()) {
            isDefault = false;
            builder.setDescription(description);
        }

        Optional<OntologyClass> sampledTissue = OntologyClassConverter.toOntologyClass(biosample.getSampledTissue());
        if (sampledTissue.isPresent()) {
            isDefault = false;
            builder.setSampledTissue(sampledTissue.get());
        }

        List<PhenotypicFeature> phenotypicFeatures = toPhenotypicFeatures(biosample.getPhenotypicFeaturesList());
        if (!phenotypicFeatures.isEmpty()) {
            isDefault = false;
            builder.addAllPhenotypicFeatures(phenotypicFeatures);
        }

        Optional<OntologyClass> taxonomy = OntologyClassConverter.toOntologyClass(biosample.getTaxonomy());
        if (taxonomy.isPresent()) {
            isDefault = false;
            builder.setTaxonomy(taxonomy.get());
        }

        Optional<TimeElement> timeOfCollection = toTimeOfCollection(biosample);
        if (timeOfCollection.isPresent()) {
            isDefault = false;
            builder.setTimeOfCollection(timeOfCollection.get());
        }

        Optional<OntologyClass> histologicalDiagnosis = OntologyClassConverter.toOntologyClass(biosample.getHistologicalDiagnosis());
        if (histologicalDiagnosis.isPresent()) {
            isDefault = false;
            builder.setHistologicalDiagnosis(histologicalDiagnosis.get());
        }

        Optional<OntologyClass> tumorProgression = OntologyClassConverter.toOntologyClass(biosample.getTumorProgression());
        if (tumorProgression.isPresent()) {
            isDefault = false;
            builder.setTumorProgression(tumorProgression.get());
        }

        Optional<OntologyClass> tumorGrade = OntologyClassConverter.toOntologyClass(biosample.getTumorGrade());
        if (tumorGrade.isPresent()) {
            isDefault = false;
            builder.setTumorGrade(tumorGrade.get());
        }

        List<OntologyClass> diagnosticMarkers = OntologyClassConverter.toOntologyClassList(biosample.getDiagnosticMarkersList());
        if (!diagnosticMarkers.isEmpty()) {
            isDefault = false;
            builder.addAllDiagnosticMarkers(diagnosticMarkers);
        }

        Optional<Procedure> procedure = ProcedureConverter.toProcedure(biosample.getProcedure());
        if (procedure.isPresent()) {
            isDefault = false;
            builder.setProcedure(procedure.get());
        }

        List<File> files = FileConverter.toFiles(biosample.getHtsFilesList());
        if (!files.isEmpty()) {
            isDefault = false;
            builder.addAllFiles(files);
        }

        if (biosample.getVariantsCount() != 0) {
            // TODO: create an interpretation along with any genes
        }

        if (biosample.getIsControlSample())
            // (non)-defaultness does not apply here
            builder.setMaterialSample(referenceSample);
        else if (tumorProgression.isPresent() || tumorGrade.isPresent())
            builder.setMaterialSample(abnormalSample);


        if (isDefault)
            return Optional.empty();
        else
            return Optional.of(builder.build());
    }

    private static Optional<TimeElement> toTimeOfCollection(org.phenopackets.schema.v1.core.Biosample biosample) {
        if (biosample.hasAgeOfIndividualAtCollection()) {
            return AgeConverter.toAge(biosample.getAgeOfIndividualAtCollection())
                    .map(age -> TimeElement.newBuilder().setAge(age).build());
        } else if (biosample.hasAgeRangeOfIndividualAtCollection()) {
            return AgeConverter.toAgeRange(biosample.getAgeRangeOfIndividualAtCollection())
                    .map(ageRange -> TimeElement.newBuilder().setAgeRange(ageRange).build());
        }
        return Optional.empty();
    }
}
