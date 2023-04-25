package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.*;

import java.util.List;
import java.util.Optional;

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
                .flatMap(Optional::stream)
                .toList();
    }

    static Optional<PhenotypicFeature> toPhenotypicFeature(org.phenopackets.schema.v1.core.PhenotypicFeature v1PhenotypicFeature) {
        boolean isDefault = true;

        PhenotypicFeature.Builder builder = PhenotypicFeature.newBuilder();

        Optional<OntologyClass> type = toOntologyClass(v1PhenotypicFeature.getType());
        if (type.isPresent()) {
            isDefault = false;
            builder.setType(type.get());
        }

        if (v1PhenotypicFeature.getNegated()) {
            isDefault = false;
            builder.setExcluded(true);
        }

        Optional<OntologyClass> severity = toOntologyClass(v1PhenotypicFeature.getSeverity());
        if (v1PhenotypicFeature.hasSeverity() && severity.isPresent()) {
            isDefault = false;
            builder.setSeverity(severity.get());
        }

        if (v1PhenotypicFeature.getModifiersCount() > 0) {
            List<OntologyClass> modifiers = toOntologyClassList(v1PhenotypicFeature.getModifiersList());
            if (!modifiers.isEmpty()) {
                isDefault = false;
                builder.addAllModifiers(modifiers);
            }
        }

        if (v1PhenotypicFeature.getEvidenceCount() > 0) {
            List<Evidence> evidences = toEvidences(v1PhenotypicFeature.getEvidenceList());
            if (!evidences.isEmpty()) {
                isDefault = false;
                builder.addAllEvidence(evidences);
            }
        }

        Optional<TimeElement> onset = toPhenotypicFeatureOnset(v1PhenotypicFeature);
        if (onset.isPresent()) {
            isDefault = false;
            builder.setOnset(onset.get());
        }

        if (!v1PhenotypicFeature.getDescription().isEmpty()) {
            isDefault = false;
            builder.setDescription(v1PhenotypicFeature.getDescription());
        }

        return isDefault
                ? Optional.empty()
                : Optional.of(builder.build());
    }

    private static Optional<TimeElement> toPhenotypicFeatureOnset(org.phenopackets.schema.v1.core.PhenotypicFeature v1PhenotypicFeature) {
        if (v1PhenotypicFeature.equals(TimeElement.getDefaultInstance()))
            return Optional.empty();

        boolean isDefault = true;
        TimeElement.Builder builder = TimeElement.newBuilder();
        if (v1PhenotypicFeature.hasClassOfOnset()) {
            Optional<OntologyClass> onsetClass = toOntologyClass(v1PhenotypicFeature.getClassOfOnset());
            if (onsetClass.isPresent()) {
                isDefault = false;
                builder.setOntologyClass(onsetClass.get());
            }
        } else if (v1PhenotypicFeature.hasAgeOfOnset()) {
            Optional<Age> age = toAge(v1PhenotypicFeature.getAgeOfOnset());
            if (age.isPresent()) {
                isDefault = false;
                builder.setAge(age.get());
            }
        } else if (v1PhenotypicFeature.hasAgeRangeOfOnset()) {
            Optional<AgeRange> ageRange = toAgeRange(v1PhenotypicFeature.getAgeRangeOfOnset());
            if (ageRange.isPresent()) {
                isDefault = false;
                builder.setAgeRange(ageRange.get());
            }
        }

        return isDefault
                ? Optional.empty()
                : Optional.of(builder.build());
    }

}
