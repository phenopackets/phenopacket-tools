package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Pedigree;
import org.phenopackets.schema.v2.core.Sex;

import java.util.List;
import java.util.Optional;

import static org.phenopackets.phenopackettools.converter.converters.v2.IndividualConverter.toSex;

public class PedigreeConverter {

    private PedigreeConverter() {
    }

    public static Optional<Pedigree> toPedigree(org.phenopackets.schema.v1.core.Pedigree v1Pedigree) {
        if (v1Pedigree.equals(org.phenopackets.schema.v1.core.Pedigree.getDefaultInstance()))
            return Optional.empty();

        boolean isDefault = true;
        Pedigree.Builder builder = Pedigree.newBuilder();

        List<Pedigree.Person> persons = toPersons(v1Pedigree.getPersonsList());
        if (!persons.isEmpty()) {
            isDefault = false;
            builder.addAllPersons(persons);
         }

        if (isDefault)
            return Optional.empty();
        else
            return Optional.of(builder.build());
    }

    private static List<Pedigree.Person> toPersons(List<org.phenopackets.schema.v1.core.Pedigree.Person> v1PersonsList) {
        return v1PersonsList.stream()
                .map(PedigreeConverter::toPerson)
                .flatMap(Optional::stream)
                .toList();
    }

    private static Optional<Pedigree.Person> toPerson(org.phenopackets.schema.v1.core.Pedigree.Person v1Person) {
        if (v1Person.equals(org.phenopackets.schema.v1.core.Pedigree.Person.getDefaultInstance()))
            return Optional.empty();

        boolean isDefault = true;
        Pedigree.Person.Builder builder = Pedigree.Person.newBuilder();

        String individualId = v1Person.getIndividualId();
        if (!individualId.isEmpty()) {
            isDefault = false;
            builder.setIndividualId(individualId);
        }

        Sex sex = toSex(v1Person.getSex());
        if (!sex.equals(Sex.UNRECOGNIZED) && !sex.equals(Sex.UNKNOWN_SEX)) {
            isDefault = false;
            builder.setSex(sex);
        }

        Pedigree.Person.AffectedStatus affectedStatus = toAffectedStatus(v1Person.getAffectedStatus());
        if (!affectedStatus.equals(Pedigree.Person.AffectedStatus.UNRECOGNIZED) && !affectedStatus.equals(Pedigree.Person.AffectedStatus.MISSING)) {
            isDefault = false;
            builder.setAffectedStatus(affectedStatus);
        }

        if (!v1Person.getFamilyId().isEmpty()) {
            isDefault = false;
            builder.setFamilyId(v1Person.getFamilyId());
        }
        if (!v1Person.getMaternalId().isEmpty()) {
            isDefault = false;
            builder.setMaternalId(v1Person.getMaternalId());
        }

        if (!v1Person.getPaternalId().isEmpty()) {
            isDefault = false;
            builder.setPaternalId(v1Person.getPaternalId());
        }

        if (isDefault)
            return Optional.empty();
        else
            return Optional.of(builder.build());
    }

    private static Pedigree.Person.AffectedStatus toAffectedStatus(org.phenopackets.schema.v1.core.Pedigree.Person.AffectedStatus v1AffectedStatus) {
        return switch (v1AffectedStatus) {
            case UNAFFECTED -> Pedigree.Person.AffectedStatus.UNAFFECTED;
            case AFFECTED -> Pedigree.Person.AffectedStatus.AFFECTED;
            case UNRECOGNIZED -> Pedigree.Person.AffectedStatus.UNRECOGNIZED;
            case MISSING -> Pedigree.Person.AffectedStatus.MISSING;
        };
    }
}
