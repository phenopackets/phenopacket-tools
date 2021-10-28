package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Evidence;
import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;


public class EvidenceBuilder {

    private final Evidence.Builder builder;

    private EvidenceBuilder(OntologyClass evidenceCode) {
        builder = Evidence.newBuilder().setEvidenceCode(evidenceCode);
    }

    public static Evidence evidence(OntologyClass evidenceCode, ExternalReference externalReference) {
        return new EvidenceBuilder(evidenceCode).reference(externalReference).build();
    }

    public static EvidenceBuilder create(String id, String label) {
        OntologyClass evidenceCode = ontologyClass(id, label);
        return new EvidenceBuilder(evidenceCode);
    }

    public static Evidence authorStatementEvidence(String pmid, String title) {
        String id = "ECO:0000033";
        String label = "author statement supported by traceable reference";
        OntologyClass evidenceCode = ontologyClass(id, label);
        ExternalReference externalReference = ExternalReferenceBuilder.externalReference(pmid, title);
        return evidence(evidenceCode, externalReference);
    }

    public EvidenceBuilder reference(ExternalReference externalReference) {
        builder.setReference(externalReference);
        return this;
    }

    public Evidence build() {
        return builder.build();
    }
}
