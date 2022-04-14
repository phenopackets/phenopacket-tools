package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Procedure;

import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClass;

public class ProcedureConverter {

    private ProcedureConverter() {
    }

    public static Procedure toProcedure(org.phenopackets.schema.v1.core.Procedure procedure) {
        if (org.phenopackets.schema.v1.core.Procedure.getDefaultInstance().equals(procedure)) {
            return Procedure.getDefaultInstance();
        }

        Procedure.Builder builder = Procedure.newBuilder();
        if (procedure.hasBodySite()) {
            builder.setBodySite(toOntologyClass(procedure.getBodySite()));
        }
        return builder
                .setCode(toOntologyClass(procedure.getCode()))
                .build();
    }
}
