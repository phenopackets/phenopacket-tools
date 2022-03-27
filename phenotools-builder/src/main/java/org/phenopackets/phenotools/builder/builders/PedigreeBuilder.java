package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Pedigree;

public class PedigreeBuilder {

    private final Pedigree.Builder builder;

    public PedigreeBuilder() {
        builder = Pedigree.newBuilder();
    }

    public PedigreeBuilder person(Pedigree.Person person) {
        builder.addPersons(person);
        return this;
    }

    public Pedigree build() {
        return builder.build();
    }

    public static PedigreeBuilder create() {
        return new PedigreeBuilder();
    }
}
