package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Disease;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.List;

import static org.phenopackets.phenopackettools.converter.converters.v2.AgeConverter.toAge;
import static org.phenopackets.phenopackettools.converter.converters.v2.AgeConverter.toAgeRange;
import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.*;

public class DiseaseConverter {

    private DiseaseConverter() {
    }

    public static List<Disease> toDiseases(List<org.phenopackets.schema.v1.core.Disease> diseasesList) {
        return diseasesList.stream()
                .map(DiseaseConverter::toDisease)
                .toList();
    }

    /**
     * Convert to V2 disease. Note that not all disease messageas have age of onset information.
     * If we pass empty lists for the TnmFinding or disease stage, this does not cause the V2 element
     * to show an empty list in this implementeation
     * @param v1Disease Disease message from the version 1 Phenopacket Schema
     * @return Disease message from the version 2 Phenopacket Schema
     */
    public static Disease toDisease(org.phenopackets.schema.v1.core.Disease v1Disease) {
        Disease.Builder builder = Disease.newBuilder()
                .setTerm(toOntologyClass(v1Disease.getTerm()))
                .addAllDiseaseStage(toOntologyClassList(v1Disease.getDiseaseStageList()))
                .addAllClinicalTnmFinding(toOntologyClassList(v1Disease.getTnmFindingList()));
        if (v1Disease.hasAgeOfOnset() || v1Disease.hasAgeRangeOfOnset() || v1Disease.hasClassOfOnset()) {
            builder.setOnset(toDiseaseOnset(v1Disease));
                    // no excluded or resolution in v1
        }
        return builder.build();
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
