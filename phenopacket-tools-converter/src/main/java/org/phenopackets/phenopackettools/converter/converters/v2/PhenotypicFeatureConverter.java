package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.converter.converters.v2.AgeConverter.toAge;
import static org.phenopackets.phenopackettools.converter.converters.v2.AgeConverter.toAgeRange;
import static org.phenopackets.phenopackettools.converter.converters.v2.EvidenceConverter.toEvidences;
import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClass;
import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClassList;

public class PhenotypicFeatureConverter {

    private PhenotypicFeatureConverter() {
    }

    public static List<PhenotypicFeature> toPhenotypicFeatures(List<org.phenopackets.schema.v1.core.PhenotypicFeature> v1PhenotypicFeaturesList) {
        return v1PhenotypicFeaturesList.stream()
                .map(PhenotypicFeatureConverter::toPhenotypicFeature)
                .toList();
    }

    public static PhenotypicFeature toPhenotypicFeature(org.phenopackets.schema.v1.core.PhenotypicFeature v1PhenotypicFeature) {
        PhenotypicFeature.Builder builder = PhenotypicFeature.newBuilder();
        builder.setType(toOntologyClass(v1PhenotypicFeature.getType()));
        builder.setExcluded(v1PhenotypicFeature.getNegated());
        if (v1PhenotypicFeature.hasSeverity()) {
            builder.setSeverity(toOntologyClass(v1PhenotypicFeature.getSeverity()));
        }
        if (v1PhenotypicFeature.getModifiersCount() > 0) {
            builder.addAllModifiers(toModifiers(v1PhenotypicFeature.getModifiersList()));
        }
        if (v1PhenotypicFeature.getEvidenceCount() > 0) {
            builder.addAllEvidence(toEvidences(v1PhenotypicFeature.getEvidenceList()));
        }
        if (v1PhenotypicFeature.hasAgeOfOnset() ||
                v1PhenotypicFeature.hasAgeRangeOfOnset() ||
                v1PhenotypicFeature.hasClassOfOnset()) {
            builder.setOnset(toPhenotypicFeatureOnset(v1PhenotypicFeature));
        }
        if (!v1PhenotypicFeature.getDescription().isEmpty()) {
            builder.setDescription(v1PhenotypicFeature.getDescription());
        }
        return builder.build();
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
