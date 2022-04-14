package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Biosample;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.List;
import java.util.stream.Collectors;

import static org.phenopackets.phenopackettools.converter.converters.v2.FileConverter.toFiles;
import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClass;
import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClassList;
import static org.phenopackets.phenopackettools.converter.converters.v2.PhenotypicFeatureConverter.*;
import static org.phenopackets.phenopackettools.converter.converters.v2.ProcedureConverter.toProcedure;

public class BiosampleConverter {

    private static final OntologyClass referenceSample = OntologyClass.newBuilder().setId("EFO:0009654").setLabel("reference sample").build();
    private static final  OntologyClass abnormalSample = OntologyClass.newBuilder().setId("EFO:0009655").setLabel("abnormal sample").build();

    private BiosampleConverter() {
    }

    public static List<Biosample> toBiosamples(List<org.phenopackets.schema.v1.core.Biosample> biosamples) {
        return biosamples.stream().map(BiosampleConverter::toBiosample).collect(Collectors.toUnmodifiableList());
    }

    public static Biosample toBiosample(org.phenopackets.schema.v1.core.Biosample biosample) {
        Biosample.Builder builder = Biosample.newBuilder();
        if (biosample.hasSampledTissue()) {
            builder.setSampledTissue(toOntologyClass(biosample.getSampledTissue()));
        }
        if (biosample.hasTaxonomy()) {
            builder.setTaxonomy(toOntologyClass(biosample.getTaxonomy()));
        }
        if (biosample.hasAgeOfIndividualAtCollection() || biosample.hasAgeRangeOfIndividualAtCollection()) {
            builder.setTimeOfCollection(toTimeOfCollection(biosample));
        }
        if (biosample.hasHistologicalDiagnosis()) {
            builder.setHistologicalDiagnosis(toOntologyClass(biosample.getHistologicalDiagnosis()));
        }
        if (biosample.hasTumorProgression()) {
            builder.setTumorProgression(toOntologyClass(biosample.getTumorProgression()));

        }
        if (biosample.hasTumorGrade()) {
            builder.setTumorGrade(toOntologyClass(biosample.getTumorGrade()));
        }
        if (biosample.hasProcedure()) {
            builder.setProcedure(toProcedure(biosample.getProcedure()));
        }
        if (biosample.getVariantsCount() != 0) {
            // TODO: create an interpretation along with any genes
        }
        if (biosample.getIsControlSample()) {
            builder.setMaterialSample(referenceSample);
        } else if (biosample.hasTumorProgression() || biosample.hasTumorGrade()) {
            builder.setMaterialSample(abnormalSample);
        }

        return builder
                .setId(biosample.getId())
                .setIndividualId(biosample.getIndividualId())
                .setDescription(biosample.getDescription())
                .addAllPhenotypicFeatures(toPhenotypicFeatures(biosample.getPhenotypicFeaturesList()))
                .addAllDiagnosticMarkers(toOntologyClassList(biosample.getDiagnosticMarkersList()))
                .addAllFiles(toFiles(biosample.getHtsFilesList()))
                .build();
    }

    private static TimeElement toTimeOfCollection(org.phenopackets.schema.v1.core.Biosample biosample) {
        if (biosample.hasAgeOfIndividualAtCollection()) {
            return TimeElement.newBuilder().setAge(AgeConverter.toAge(biosample.getAgeOfIndividualAtCollection())).build();
        } else if (biosample.hasAgeRangeOfIndividualAtCollection()) {
            return TimeElement.newBuilder().setAgeRange(AgeConverter.toAgeRange(biosample.getAgeRangeOfIndividualAtCollection())).build();
        }
        return TimeElement.getDefaultInstance();
    }
}
