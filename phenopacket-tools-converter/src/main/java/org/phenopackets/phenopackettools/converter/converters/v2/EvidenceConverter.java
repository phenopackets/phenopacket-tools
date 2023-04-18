package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Evidence;
import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.List;
import java.util.Optional;

import static org.phenopackets.phenopackettools.converter.converters.v2.ExternalReferenceConverter.toExternalReference;
import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClass;

class EvidenceConverter {

    private EvidenceConverter() {
    }

    static List<Evidence> toEvidences(List<org.phenopackets.schema.v1.core.Evidence> evidenceList) {
        return evidenceList.stream()
                .map(EvidenceConverter::toEvidence)
                .flatMap(Optional::stream)
                .toList();
    }

    static Optional<Evidence> toEvidence(org.phenopackets.schema.v1.core.Evidence v1Evidence) {
        if (v1Evidence.equals(org.phenopackets.schema.v1.core.Evidence.getDefaultInstance()))
            return Optional.empty();

        Optional<OntologyClass> evidenceCode = toOntologyClass(v1Evidence.getEvidenceCode());
        Optional<ExternalReference> externalReference = toExternalReference(v1Evidence.getReference());

        if (evidenceCode.isEmpty() && externalReference.isEmpty())
            return Optional.empty();

        return Optional.of(Evidence.newBuilder()
                .setEvidenceCode(evidenceCode.orElse(OntologyClass.getDefaultInstance()))
                .setReference(externalReference.orElse(ExternalReference.getDefaultInstance()))
                .build());
    }
}
