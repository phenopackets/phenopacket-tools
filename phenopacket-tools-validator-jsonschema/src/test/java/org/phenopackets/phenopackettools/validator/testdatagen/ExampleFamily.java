package org.phenopackets.phenopackettools.validator.testdatagen;

import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.File;
import org.phenopackets.schema.v2.core.Pedigree;
import org.phenopackets.schema.v2.core.Sex;

public class ExampleFamily {

    private static final Phenopacket RARE_DISEASE_PHENOPACKET = new RareDiseasePhenopacket().getPhenopacket();
    private static final String FAMILY_ID = "abcd";

    private ExampleFamily() {
        // no-op
    }

    public static Family getExampleFamily() {
        return Family.newBuilder()
                .setId(FAMILY_ID)
                .setProband(RARE_DISEASE_PHENOPACKET)
                .setConsanguinousParents(true)
                .setPedigree(pedigree())
                .addFiles(aVcfFile())
                .setMetaData(RareDiseasePhenopacket.createMetaData())
                .build();
    }

    private static Pedigree pedigree() {
        return Pedigree.newBuilder()
                .addPersons(createPerson(RARE_DISEASE_PHENOPACKET.getSubject().getId(), "FATHER_ID", "MOTHER_ID", Sex.MALE, Pedigree.Person.AffectedStatus.AFFECTED))
                .addPersons(createPerson("FATHER_ID", "0", "0", Sex.MALE, Pedigree.Person.AffectedStatus.UNAFFECTED))
                .addPersons(createPerson("MOTHER_ID", "0", "0", Sex.FEMALE, Pedigree.Person.AffectedStatus.UNAFFECTED))
                .build();
    }

    private static Pedigree.Person createPerson(String id,
                                                String paternalId,
                                                String maternalId,
                                                Sex sex,
                                                Pedigree.Person.AffectedStatus status) {
        return Pedigree.Person.newBuilder()
                .setFamilyId(ExampleFamily.FAMILY_ID)
                .setIndividualId(id)
                .setPaternalId(paternalId)
                .setMaternalId(maternalId)
                .setSex(sex)
                .setAffectedStatus(status)
                .build();
    }

    private static File aVcfFile() {
        return File.newBuilder()
                .setUri("file://data/genomes/file1.vcf.gz")
                .putIndividualToFileIdentifiers("a", "b")
                .putFileAttributes("file_format", "vcf")
                .build();
    }

}
