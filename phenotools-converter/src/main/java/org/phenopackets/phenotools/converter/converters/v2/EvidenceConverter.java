package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Evidence;

import java.util.List;
import java.util.stream.Collectors;

import static org.phenopackets.phenotools.converter.converters.v2.ExternalReferenceConverter.toExternalReference;
import static org.phenopackets.phenotools.converter.converters.v2.OntologyClassConverter.toOntologyClass;

public class EvidenceConverter {

    private EvidenceConverter() {
    }

    public static List<Evidence> toEvidences(List<org.phenopackets.schema.v1.core.Evidence> evidenceList) {
        return evidenceList.stream().map(EvidenceConverter::toEvidence).collect(Collectors.toUnmodifiableList());
    }

    public static Evidence toEvidence(org.phenopackets.schema.v1.core.Evidence v1Evidence) {
        if (org.phenopackets.schema.v1.core.Evidence.getDefaultInstance().equals(v1Evidence)) {
            return Evidence.getDefaultInstance();
        }
        return Evidence.newBuilder()
                .setEvidenceCode(toOntologyClass(v1Evidence.getEvidenceCode()))
                .setReference(toExternalReference(v1Evidence.getReference()))
                .build();
    }
}
