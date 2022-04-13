package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Pedigree;

import java.util.Arrays;
import java.util.Collection;

public class PedigreeBuilder {

    private final Pedigree.Builder builder;

    public PedigreeBuilder() {
        builder = Pedigree.newBuilder();
    }

    public PedigreeBuilder person(Pedigree.Person person) {
        builder.addPersons(person);
        return this;
    }

    public PedigreeBuilder addAllPersons(Pedigree.Person... persons) {
        builder.addAllPersons(Arrays.asList(persons));
        return this;
    }

    public PedigreeBuilder addAllPersons(Collection<Pedigree.Person> persons){
        builder.addAllPersons(persons);
        return this;
    }

    public Pedigree build() {
        return builder.build();
    }

    public static PedigreeBuilder builder() {
        return new PedigreeBuilder();
    }
}
