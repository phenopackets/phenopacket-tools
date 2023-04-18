package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Procedure;

import java.util.Optional;

import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClass;

class ProcedureConverter {

    private ProcedureConverter() {
    }

    static Optional<Procedure> toProcedure(org.phenopackets.schema.v1.core.Procedure procedure) {
        if (procedure.equals(org.phenopackets.schema.v1.core.Procedure.getDefaultInstance()))
            return Optional.empty();

        boolean isDefault = true;
        Procedure.Builder builder = Procedure.newBuilder();

        Optional<OntologyClass> code = toOntologyClass(procedure.getCode());
        if (code.isPresent()) {
            isDefault = false;
            builder.setCode(code.get());
        }

        Optional<OntologyClass> bodySite = toOntologyClass(procedure.getBodySite());
        if (bodySite.isPresent()) {
            isDefault = false;
            builder.setBodySite(bodySite.get());
        }

        if (isDefault)
            return Optional.empty();
        else
            return Optional.of(builder.build());

    }
}
