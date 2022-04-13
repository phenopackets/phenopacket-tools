package org.phenopackets.phenotools.builder;

import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.File;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Pedigree;

import java.util.List;

public class FamilyBuilder {

    private final Family.Builder builder;

    private FamilyBuilder(String id) {
        builder = Family.newBuilder().setId(id);
    }


    public FamilyBuilder phenopacket(Phenopacket phenopacket) {
        builder.setProband(phenopacket);
        return this;
    }

    public FamilyBuilder relatives(List<Phenopacket> relativeList) {
        builder.addAllRelatives(relativeList);
        return this;
    }

    public FamilyBuilder pedigree(Pedigree pedigree) {
        builder.setPedigree(pedigree);
        return this;
    }

    public FamilyBuilder files(List<File> files) {
        builder.addAllFiles(files);
        return this;
    }

    public FamilyBuilder file(File file) {
        builder.addFiles(file);
        return this;
    }


    public FamilyBuilder metaData(MetaData metaData) {
        builder.setMetaData(metaData);
        return this;
    }

    public static FamilyBuilder create(String familyId) {
        return new FamilyBuilder(familyId);
    }



    public Family build() {
        if (! builder.hasMetaData()) {
            throw new PhenotoolsRuntimeException("MetaData element missing from Family");
        } else if (! builder.hasPedigree()) {
            throw new PhenotoolsRuntimeException("Pedigree element missing from Family");
        } else if (! builder.hasProband()) {
            throw new PhenotoolsRuntimeException("Proband Phenopacket element missing from Family");
        }

        return builder.build();
    }


}
