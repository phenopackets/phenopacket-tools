package org.phenopackets.phenopackettools.validator.core.phenotype.base;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.Objects;

public abstract class BaseHpoValidator<T extends MessageOrBuilder> implements PhenopacketValidator<T> {

    protected final Ontology hpo;
    protected final String hpoVersion;

    protected BaseHpoValidator(Ontology hpo) {
        this.hpo = Objects.requireNonNull(hpo);
        // TODO - can be replaced by this.hpo.version() in the most recent phenol versions.
        this.hpoVersion = this.hpo.getMetaInfo().getOrDefault("data-version", "HPO");
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
