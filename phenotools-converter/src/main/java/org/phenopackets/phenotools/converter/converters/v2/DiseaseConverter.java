package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Disease;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.List;
import java.util.stream.Collectors;

import static org.phenopackets.phenotools.converter.converters.v2.AgeConverter.toAge;
import static org.phenopackets.phenotools.converter.converters.v2.AgeConverter.toAgeRange;
import static org.phenopackets.phenotools.converter.converters.v2.OntologyClassConverter.*;

public class DiseaseConverter {

    private DiseaseConverter() {
    }

    public static List<Disease> toDiseases(List<org.phenopackets.schema.v1.core.Disease> diseasesList) {
        return diseasesList.stream().map(DiseaseConverter::toDisease).collect(Collectors.toUnmodifiableList());
    }

    public static Disease toDisease(org.phenopackets.schema.v1.core.Disease v1Disease) {
        return Disease.newBuilder()
                .setTerm(toOntologyClass(v1Disease.getTerm()))
                .addAllDiseaseStage(toOntologyClassList(v1Disease.getDiseaseStageList()))
                .addAllClinicalTnmFinding(toOntologyClassList(v1Disease.getTnmFindingList()))
                .setOnset(toDiseaseOnset(v1Disease))
                // no excluded or resolution in v1
                .build();
    }

    public static TimeElement toDiseaseOnset(org.phenopackets.schema.v1.core.Disease v1Disease) {
        if (v1Disease.hasClassOfOnset()) {
            return TimeElement.newBuilder().setOntologyClass(toOntologyClass(v1Disease.getClassOfOnset())).build();
        } else if (v1Disease.hasAgeOfOnset()) {
            return TimeElement.newBuilder().setAge(toAge(v1Disease.getAgeOfOnset())).build();
        } else if (v1Disease.hasAgeRangeOfOnset()) {
            return TimeElement.newBuilder().setAgeRange(toAgeRange(v1Disease.getAgeRangeOfOnset())).build();
        }
        return TimeElement.getDefaultInstance();
    }
}
