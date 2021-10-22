package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.phenopackets.phenotools.converter.converters.v2.AgeConverter.toAge;
import static org.phenopackets.phenotools.converter.converters.v2.AgeConverter.toAgeRange;
import static org.phenopackets.phenotools.converter.converters.v2.EvidenceConverter.toEvidences;
import static org.phenopackets.phenotools.converter.converters.v2.ExternalReferenceConverter.toExternalReference;
import static org.phenopackets.phenotools.converter.converters.v2.OntologyClassConverter.toOntologyClass;
import static org.phenopackets.phenotools.converter.converters.v2.OntologyClassConverter.toOntologyClassList;

public class PhenotypicFeatureConverter {

    private PhenotypicFeatureConverter() {
    }

    public static List<PhenotypicFeature> toPhenotypicFeatures(List<org.phenopackets.schema.v1.core.PhenotypicFeature> v1PhenotypicFeaturesList) {
        return v1PhenotypicFeaturesList.stream().map(PhenotypicFeatureConverter::toPhenotypicFeature).collect(Collectors.toUnmodifiableList());
    }

    public static PhenotypicFeature toPhenotypicFeature(org.phenopackets.schema.v1.core.PhenotypicFeature v1PhenotypicFeature) {
        PhenotypicFeature.Builder builder = PhenotypicFeature.newBuilder();
        // optional fields we don't want to set as empty
        if (v1PhenotypicFeature.hasSeverity()) {
            builder.setSeverity(toOntologyClass(v1PhenotypicFeature.getSeverity()));
        }
        if (v1PhenotypicFeature.hasClassOfOnset() || v1PhenotypicFeature.hasAgeOfOnset() || v1PhenotypicFeature.hasAgeRangeOfOnset()) {
            builder.setOnset(toPhenotypicFeatureOnset(v1PhenotypicFeature));
        }

        return builder
                .setType(toOntologyClass(v1PhenotypicFeature.getType()))
                .setExcluded(v1PhenotypicFeature.getNegated())
                .addAllModifiers(toModifiers(v1PhenotypicFeature.getModifiersList()))
                .addAllEvidence(toEvidences(v1PhenotypicFeature.getEvidenceList()))
                .setDescription(v1PhenotypicFeature.getDescription())
                .build();
    }

    public static TimeElement toPhenotypicFeatureOnset(org.phenopackets.schema.v1.core.PhenotypicFeature v1PhenotypicFeature) {
        if (v1PhenotypicFeature.hasClassOfOnset()) {
            return TimeElement.newBuilder().setOntologyClass(toOntologyClass(v1PhenotypicFeature.getClassOfOnset())).build();
        } else if (v1PhenotypicFeature.hasAgeOfOnset()) {
            return TimeElement.newBuilder().setAge(toAge(v1PhenotypicFeature.getAgeOfOnset())).build();
        } else if (v1PhenotypicFeature.hasAgeRangeOfOnset()) {
            return TimeElement.newBuilder().setAgeRange(toAgeRange(v1PhenotypicFeature.getAgeRangeOfOnset())).build();
        }
        return TimeElement.getDefaultInstance();
    }

    public static List<OntologyClass> toModifiers(List<org.phenopackets.schema.v1.core.OntologyClass> modifiersList) {
        return toOntologyClassList(modifiersList);
    }
}
