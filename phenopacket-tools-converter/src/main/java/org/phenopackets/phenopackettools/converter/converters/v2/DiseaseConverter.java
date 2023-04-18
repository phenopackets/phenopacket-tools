package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Disease;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.List;
import java.util.Optional;

public class DiseaseConverter {

    private DiseaseConverter() {
    }

    public static List<Disease> toDiseases(List<org.phenopackets.schema.v1.core.Disease> diseasesList) {
        return diseasesList.stream()
                .map(DiseaseConverter::toDisease)
                .flatMap(Optional::stream)
                .toList();
    }

    static Optional<Disease> toDisease(org.phenopackets.schema.v1.core.Disease v1Disease) {
        if (v1Disease.equals(org.phenopackets.schema.v1.core.Disease.getDefaultInstance()))
            return Optional.empty();

        Optional<OntologyClass> term = OntologyClassConverter.toOntologyClass(v1Disease.getTerm());
        List<OntologyClass> stages = OntologyClassConverter.toOntologyClassList(v1Disease.getDiseaseStageList());
        List<OntologyClass> tnm = OntologyClassConverter.toOntologyClassList(v1Disease.getTnmFindingList());
        Optional<TimeElement> onset = toDiseaseOnset(v1Disease);

        if (term.isEmpty() && stages.isEmpty() && tnm.isEmpty() && onset.isEmpty())
            return Optional.empty();

        return Optional.of(Disease.newBuilder()
                .setTerm(term.orElse(OntologyClass.getDefaultInstance()))
                .addAllDiseaseStage(stages)
                .addAllClinicalTnmFinding(tnm)
                .setOnset(onset.orElse(TimeElement.getDefaultInstance()))
                .build());
    }

    static Optional<TimeElement> toDiseaseOnset(org.phenopackets.schema.v1.core.Disease v1Disease) {
        if (v1Disease.hasClassOfOnset())
            return OntologyClassConverter.toOntologyClass(v1Disease.getClassOfOnset())
                .map(onsetClass -> TimeElement.newBuilder().setOntologyClass(onsetClass).build());
        else if (v1Disease.hasAgeOfOnset())
            return AgeConverter.toAge(v1Disease.getAgeOfOnset())
                    .map(age -> TimeElement.newBuilder().setAge(age).build());
        else if (v1Disease.hasAgeRangeOfOnset())
            return AgeConverter.toAgeRange(v1Disease.getAgeRangeOfOnset())
                .map(ageRange -> TimeElement.newBuilder().setAgeRange(ageRange).build());
        else
            return Optional.empty();
    }
}
