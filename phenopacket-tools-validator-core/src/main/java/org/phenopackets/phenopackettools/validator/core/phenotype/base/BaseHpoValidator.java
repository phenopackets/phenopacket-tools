package org.phenopackets.phenopackettools.validator.core.phenotype.base;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.Objects;

public abstract class BaseHpoValidator<T extends MessageOrBuilder> implements PhenopacketValidator<T> {

    protected static final String UNKNOWN = "UNKNOWN_NAME";

    protected final MinimalOntology hpo;
    protected final String hpoVersion;

    protected BaseHpoValidator(MinimalOntology hpo) {
        this.hpo = Objects.requireNonNull(hpo);
        this.hpoVersion = hpo.version().orElse("UNKNOWN");
    }

    protected static String summarizePhenopacketAndIndividualId(PhenopacketOrBuilder phenopacket) {
        // Build a string like <phenopacket-id>/<subject-id> but only if one/other are present.
        StringBuilder builder = new StringBuilder();
        String phenopacketId = phenopacket.getId();
        String individualId = phenopacket.getSubject().getId();
        if (!phenopacketId.isBlank() || !individualId.isBlank()) {
            builder.append(" in ");
            if (!phenopacketId.isBlank())
                builder.append(phenopacketId);

            if (!individualId.isBlank()) {
                if (!phenopacketId.isBlank())
                    builder.append("/");
                builder.append(individualId);
            }
        }
        return builder.toString();
    }
}
