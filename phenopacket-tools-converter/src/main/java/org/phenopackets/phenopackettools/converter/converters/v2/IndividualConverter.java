package org.phenopackets.phenopackettools.converter.converters.v2;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v2.core.*;

import java.util.Optional;

import static org.phenopackets.phenopackettools.converter.converters.v2.AgeConverter.toAge;
import static org.phenopackets.phenopackettools.converter.converters.v2.AgeConverter.toAgeRange;
import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClass;

public class IndividualConverter {

    private IndividualConverter() {
    }

    public static Optional<Individual> toIndividual(org.phenopackets.schema.v1.core.Individual v1Individual) {
        if (v1Individual.equals(org.phenopackets.schema.v1.core.Individual.getDefaultInstance()))
            return Optional.empty();

        boolean isDefault = true;
        Individual.Builder builder = Individual.newBuilder();

        String id = v1Individual.getId();
        if (!id.isEmpty()) {
            isDefault = false;
            builder.setId(id);
        }

        if (v1Individual.getAlternateIdsCount() != 0) {
            isDefault = false;
            builder.addAllAlternateIds(v1Individual.getAlternateIdsList());
        }

        Timestamp dateOfBirth = v1Individual.getDateOfBirth();
        if (!dateOfBirth.equals(Timestamp.getDefaultInstance())) {
            isDefault = false;
            builder.setDateOfBirth(dateOfBirth);
        }

        Sex sex = toSex(v1Individual.getSex());
        if (!sex.equals(Sex.UNKNOWN_SEX) && !sex.equals(Sex.UNRECOGNIZED)) {
            isDefault = false;
            builder.setSex(sex);
        }

        KaryotypicSex karyotypicSex = toKaryotypicSex(v1Individual.getKaryotypicSex());
        if (!karyotypicSex.equals(KaryotypicSex.UNKNOWN_KARYOTYPE) && !karyotypicSex.equals(KaryotypicSex.UNRECOGNIZED)) {
            isDefault = false;
            builder.setKaryotypicSex(karyotypicSex);
        }

        Optional<OntologyClass> taxonomy = toOntologyClass(v1Individual.getTaxonomy());
        if (taxonomy.isPresent()) {
            isDefault = false;
            builder.setTaxonomy(taxonomy.get());
        }

        Optional<TimeElement> lastEncounter = toIndividualTimeAtLastEncounter(v1Individual);
        if (lastEncounter.isPresent()) {
            isDefault = false;
            builder.setTimeAtLastEncounter(lastEncounter.get());
        }

        if (isDefault)
            return Optional.empty();
        else
            // no vital status or gender
            return Optional.of(builder.build());
    }

    private static KaryotypicSex toKaryotypicSex(org.phenopackets.schema.v1.core.KaryotypicSex karyotypicSex) {
        return switch (karyotypicSex) {
            case XX -> KaryotypicSex.XX;
            case XY -> KaryotypicSex.XY;
            case XO -> KaryotypicSex.XO;
            case XXY -> KaryotypicSex.XXY;
            case XXX -> KaryotypicSex.XXX;
            case XXYY -> KaryotypicSex.XXYY;
            case XXXY -> KaryotypicSex.XXXY;
            case XXXX -> KaryotypicSex.XXXX;
            case XYY -> KaryotypicSex.XYY;
            case OTHER_KARYOTYPE -> KaryotypicSex.OTHER_KARYOTYPE;
            case UNRECOGNIZED -> KaryotypicSex.UNRECOGNIZED;
            case UNKNOWN_KARYOTYPE -> KaryotypicSex.UNKNOWN_KARYOTYPE;
        };
    }

    static Sex toSex(org.phenopackets.schema.v1.core.Sex sex) {
        return switch (sex) {
            case FEMALE -> Sex.FEMALE;
            case MALE -> Sex.MALE;
            case OTHER_SEX -> Sex.OTHER_SEX;
            case UNRECOGNIZED -> Sex.UNRECOGNIZED;
            case UNKNOWN_SEX -> Sex.UNKNOWN_SEX;
        };
    }

    private static Optional<TimeElement> toIndividualTimeAtLastEncounter(org.phenopackets.schema.v1.core.Individual v1Individual) {
        if (v1Individual.hasAgeAtCollection()) {
            return toAge(v1Individual.getAgeAtCollection())
                    .map(age -> TimeElement.newBuilder()
                            .setAge(age)
                            .build());
        } else if (v1Individual.hasAgeRangeAtCollection())
           return toAgeRange(v1Individual.getAgeRangeAtCollection())
                    .map(ageRange -> TimeElement.newBuilder()
                            .setAgeRange(ageRange)
                            .build());
        else
            return Optional.empty();
    }
}
