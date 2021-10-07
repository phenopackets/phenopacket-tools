package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Evidence;
import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;


public class EvidenceBuilder {

    private final Evidence.Builder builder;

    public EvidenceBuilder(String id, String label) {
        OntologyClass evidenceCode = ontologyClass(id, label);
        builder = Evidence.newBuilder().setEvidenceCode(evidenceCode);
    }

    public EvidenceBuilder(OntologyClass evidenceCode) {
        builder = Evidence.newBuilder().setEvidenceCode(evidenceCode);
    }

    public EvidenceBuilder reference(ExternalReference externalReference) {
        builder.setReference(externalReference);
        return this;
    }

    public Evidence build() {
        return builder.build();
    }

    public static EvidenceBuilder create(String id, String label) {
        return new EvidenceBuilder(id, label);
    }

    public static Evidence authorStatementEvidence(String pmid, String title) {
        String id = "ECO:0000033";
        String label = "author statement supported by traceable reference";
        OntologyClass evidenceCode = ontologyClass(id, label);
        ExternalReference externalReference = PhenoBuilder.externalReference(pmid, title);
        return new EvidenceBuilder(evidenceCode).reference(externalReference).build();
    }


}
