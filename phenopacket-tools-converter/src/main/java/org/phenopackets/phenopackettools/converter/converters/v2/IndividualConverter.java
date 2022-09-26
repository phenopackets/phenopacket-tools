package org.phenopackets.phenopackettools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.KaryotypicSex;
import org.phenopackets.schema.v2.core.Sex;
import org.phenopackets.schema.v2.core.TimeElement;

import static org.phenopackets.phenopackettools.converter.converters.v2.AgeConverter.toAge;
import static org.phenopackets.phenopackettools.converter.converters.v2.AgeConverter.toAgeRange;
import static org.phenopackets.phenopackettools.converter.converters.v2.OntologyClassConverter.toOntologyClass;

public class IndividualConverter {

    private IndividualConverter() {
    }

    public static Individual toIndividual(org.phenopackets.schema.v1.core.Individual v1Indvidual) {
        return Individual.newBuilder()
                .setId(v1Indvidual.getId())
                .addAllAlternateIds(v1Indvidual.getAlternateIdsList())
                .setDateOfBirth(v1Indvidual.getDateOfBirth())
                .setSex(toSex(v1Indvidual.getSex()))
                .setKaryotypicSex(toKaryotypicSex(v1Indvidual.getKaryotypicSex()))
                .setTaxonomy(toOntologyClass(v1Indvidual.getTaxonomy()))
                .setTimeAtLastEncounter(toIndividualTimeAtLastEncounter(v1Indvidual))
                // no vital status or gender
                .build();
    }

    public static KaryotypicSex toKaryotypicSex(org.phenopackets.schema.v1.core.KaryotypicSex karyotypicSex) {
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

    public static Sex toSex(org.phenopackets.schema.v1.core.Sex sex) {
        return switch (sex) {
            case FEMALE -> Sex.FEMALE;
            case MALE -> Sex.MALE;
            case OTHER_SEX -> Sex.OTHER_SEX;
            case UNRECOGNIZED -> Sex.UNRECOGNIZED;
            case UNKNOWN_SEX -> Sex.UNKNOWN_SEX;
        };
    }

    public static TimeElement toIndividualTimeAtLastEncounter(org.phenopackets.schema.v1.core.Individual v1Individual) {
        if (v1Individual.hasAgeAtCollection()) {
            return TimeElement.newBuilder().setAge(toAge(v1Individual.getAgeAtCollection())).build();
        } else if (v1Individual.hasAgeRangeAtCollection()) {
            return TimeElement.newBuilder().setAgeRange(toAgeRange(v1Individual.getAgeRangeAtCollection())).build();
        }
        return TimeElement.getDefaultInstance();
    }
}
