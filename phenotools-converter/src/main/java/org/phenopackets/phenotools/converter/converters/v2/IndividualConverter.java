package org.phenopackets.phenotools.converter.converters.v2;

import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.KaryotypicSex;
import org.phenopackets.schema.v2.core.Sex;
import org.phenopackets.schema.v2.core.TimeElement;

import static org.phenopackets.phenotools.converter.converters.v2.AgeConverter.toAge;
import static org.phenopackets.phenotools.converter.converters.v2.AgeConverter.toAgeRange;
import static org.phenopackets.phenotools.converter.converters.v2.OntologyClassConverter.toOntologyClass;

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
        switch (karyotypicSex) {
            case XX:
                return KaryotypicSex.XX;
            case XY:
                return KaryotypicSex.XY;
            case XO:
                return KaryotypicSex.XO;
            case XXY:
                return KaryotypicSex.XXY;
            case XXX:
                return KaryotypicSex.XXX;
            case XXYY:
                return KaryotypicSex.XXYY;
            case XXXY:
                return KaryotypicSex.XXXY;
            case XXXX:
                return KaryotypicSex.XXXX;
            case XYY:
                return KaryotypicSex.XYY;
            case OTHER_KARYOTYPE:
                return KaryotypicSex.OTHER_KARYOTYPE;
            case UNRECOGNIZED:
                return KaryotypicSex.UNRECOGNIZED;
            case UNKNOWN_KARYOTYPE:
            default:
                return KaryotypicSex.UNKNOWN_KARYOTYPE;
        }
    }

    public static Sex toSex(org.phenopackets.schema.v1.core.Sex sex) {
        switch (sex) {
            case FEMALE:
                return Sex.FEMALE;
            case MALE:
                return Sex.MALE;
            case OTHER_SEX:
                return Sex.OTHER_SEX;
            case UNRECOGNIZED:
                return Sex.UNRECOGNIZED;
            case UNKNOWN_SEX:
            default:
                return Sex.UNKNOWN_SEX;
        }
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
