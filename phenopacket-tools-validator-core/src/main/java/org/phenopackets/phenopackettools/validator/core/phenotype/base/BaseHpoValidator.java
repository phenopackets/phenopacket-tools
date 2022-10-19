package org.phenopackets.phenopackettools.validator.core.phenotype.base;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;

import java.util.Objects;

public abstract class BaseHpoValidator<T extends MessageOrBuilder> implements PhenopacketValidator<T> {

    protected final Ontology hpo;
    protected final String hpoVersion;

    protected BaseHpoValidator(Ontology hpo) {
        this.hpo = Objects.requireNonNull(hpo);
        // TODO - can be replaced by this.hpo.version() in the most recent phenol versions.
        this.hpoVersion = this.hpo.getMetaInfo().getOrDefault("data-version", "HPO");
    }
}
