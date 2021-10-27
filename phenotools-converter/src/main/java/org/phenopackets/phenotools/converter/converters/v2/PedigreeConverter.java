package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Pedigree;

import java.util.List;
import java.util.stream.Collectors;

import static org.phenopackets.phenotools.converter.converters.v2.IndividualConverter.toSex;

public class PedigreeConverter {

    private PedigreeConverter() {
    }

    public static Pedigree toPedigree(org.phenopackets.schema.v1.core.Pedigree v1Pedigree) {
        Pedigree.Builder builder = Pedigree.newBuilder();

         if (v1Pedigree.getPersonsCount() > 0) {
            builder.addAllPersons(toPersons(v1Pedigree.getPersonsList()));
         }

        return builder.build();
    }

    private static List<Pedigree.Person> toPersons(List<org.phenopackets.schema.v1.core.Pedigree.Person> v1PersonsList) {
        return v1PersonsList.stream().map(PedigreeConverter::toPerson).collect(Collectors.toUnmodifiableList());
    }

    private static Pedigree.Person toPerson(org.phenopackets.schema.v1.core.Pedigree.Person v1Person) {
        Pedigree.Person.Builder builder = Pedigree.Person.newBuilder();

        builder.setIndividualId(v1Person.getIndividualId());
        builder.setSex(toSex(v1Person.getSex()));
        builder.setAffectedStatus(toAffectedStatus(v1Person.getAffectedStatus()));

        if (!v1Person.getFamilyId().isEmpty()) {
            builder.setFamilyId(v1Person.getFamilyId());
        }
        if (!v1Person.getMaternalId().isEmpty()) {
            builder.setMaternalId(v1Person.getMaternalId());
        }
        if (!v1Person.getPaternalId().isEmpty()) {
            builder.setPaternalId(v1Person.getPaternalId());
        }

        return builder.build();
    }

    private static Pedigree.Person.AffectedStatus toAffectedStatus(org.phenopackets.schema.v1.core.Pedigree.Person.AffectedStatus v1AffectedStatus) {
        switch (v1AffectedStatus) {
            case UNAFFECTED:
                return Pedigree.Person.AffectedStatus.UNAFFECTED;
            case AFFECTED:
                return Pedigree.Person.AffectedStatus.AFFECTED;
            case UNRECOGNIZED:
                return Pedigree.Person.AffectedStatus.UNRECOGNIZED;
            case MISSING:
            default:
                return Pedigree.Person.AffectedStatus.MISSING;
        }
    }
}
