package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Procedure;
import org.phenopackets.schema.v2.core.TimeElement;

public class ProcedureBuilder {

    private final Procedure.Builder builder;

    private ProcedureBuilder(OntologyClass procedure) {
        builder = Procedure.newBuilder().setCode(procedure);
    }

    public static Procedure procedure(OntologyClass procedure) {
        return Procedure.newBuilder().setCode(procedure).build();
    }

    public static Procedure procedure(String id, String label) {
        return procedure(OntologyClassBuilder.of(id, label));
    }

    public static ProcedureBuilder builder(OntologyClass procedure) {
        return new ProcedureBuilder(procedure);
    }

    public static ProcedureBuilder builder(String id, String label) {
        return new ProcedureBuilder(OntologyClassBuilder.of(id, label));
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
}
