package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Pedigree;
import org.phenopackets.schema.v2.core.Sex;

public class PersonBuilder {

    private final static String parental_id_not_available = "0";

    private final org.phenopackets.schema.v2.core.Pedigree.Person.Builder builder;

    private PersonBuilder(String family_id,
                         String individual_id,
                         String paternal_id,
                         String maternal_id) {
        builder = Pedigree.Person.newBuilder();
        builder.setFamilyId(family_id);
        builder.setIndividualId(individual_id);
        builder.setPaternalId(paternal_id);
        builder.setMaternalId(maternal_id);
    }

    public PersonBuilder male() {
        builder.setSex(Sex.MALE);
        return this;
    }

    public PersonBuilder female() {
        builder.setSex(Sex.FEMALE);
        return this;
    }

    public PersonBuilder unknownSex() {
        builder.setSex(Sex.UNKNOWN_SEX);
        return this;
    }

    public PersonBuilder affected() {
        builder.setAffectedStatus(Pedigree.Person.AffectedStatus.AFFECTED);
        return this;
    }

    public PersonBuilder unaffected() {
        builder.setAffectedStatus(Pedigree.Person.AffectedStatus.UNAFFECTED);
        return this;
    }

    public PersonBuilder missing() {
        builder.setAffectedStatus(Pedigree.Person.AffectedStatus.MISSING);
        return this;
    }


    public static PersonBuilder builder(String family_id,
                                        String individual_id,
                                        String paternal_id,
                                        String maternal_id) {
        return new PersonBuilder(family_id, individual_id, paternal_id, maternal_id);
    }

    /**
     * Founders are persons in a PED file whose parents are not included. These parents are
     * indicated by "0". This function creates a personBuilder with prepopulated father/mother ids of "0"
     */
    public static PersonBuilder builderWithParentsAsFounders(String family_id,
                                                             String individual_id) {
        return new PersonBuilder(family_id, individual_id,
                parental_id_not_available, parental_id_not_available);
    }

    public Pedigree.Person build() {
        return builder.build();
    }

}
