package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Procedure;
import org.phenopackets.schema.v2.core.TimeElement;

public class ProcedureBuilder {

    private final Procedure.Builder builder;

    public ProcedureBuilder(OntologyClass procedure) {
        builder = Procedure.newBuilder().setCode(procedure);
    }

    public ProcedureBuilder bodySite(OntologyClass site) {
        builder.setBodySite(site);
        return this;
    }

    public ProcedureBuilder performed(TimeElement timeElement) {
        builder.setPerformed(timeElement);
        return this;
    }

    public Procedure build() {
        return builder.build();
    }

    public static ProcedureBuilder create(OntologyClass procedure) {
        return new ProcedureBuilder(procedure);
    }
}
