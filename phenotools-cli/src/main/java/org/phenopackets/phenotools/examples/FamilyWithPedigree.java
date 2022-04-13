package org.phenopackets.phenotools.examples;


import org.phenopackets.phenotools.builder.FamilyBuilder;
import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

/**
 * An example of creating a Family message with pedigree.
 *
 */
public class FamilyWithPedigree {

    private final String FAMILY_ID = "family.1";
    private final String PATERNAL_ID = "father.1";
    private final String MATERNAL_ID = "mother.1";
    private final String DAUGHTER1_ID = "daughter.1";
    private final String SON_ID = "son.1";
    private final String DAUGHTER2_ID = "daughter.2";
    private final String FILE_LOCATION = "/data/samples/vcf/example_000001215.vcf.gz";


    private final Family family;

    public FamilyWithPedigree() {
        FamilyBuilder builder = FamilyBuilder.create(FAMILY_ID);
        var metadata = MetaDataBuilder.builder("2022-04-17T10:35:00Z", "biocurator")
                .build();
        builder.metaData(metadata);
        builder.pedigree(pedigree());
        builder.phenopacket(proband());
        builder.file(file());
        family = builder.build();
    }


    public Family getFamily() {
        return family;
    }


    public Phenopacket proband() {
        String phenopacketId = "phenopacket.id.1";
        var metadata = MetaDataBuilder.builder("2022-04-17T10:35:00Z", "biocurator")
                .resource(Resources.hpoVersion("2022-04-15"))
                .build();
        Individual proband = IndividualBuilder.builder(SON_ID).
                ageAtLastEncounter("P10Y2M4D").
                male().
                build();
        TimeElement congenital = TimeElements.congenitalOnset();
        PhenotypicFeature hearingImpairment =
                PhenotypicFeatureBuilder.builder("HP:0000407", "Sensorineural hearing impairment ")
                        .onset(congenital)
                        .build();
        PhenopacketBuilder phpBuilder = PhenopacketBuilder.create(phenopacketId, metadata)
                .addPhenotypicFeature(hearingImpairment)
                .individual(proband);
        return phpBuilder.build();
    }


    public File file() {
        FileBuilder fbuilder = FileBuilder.hg38vcf(FILE_LOCATION);
        fbuilder.individualToFileIdentifier(PATERNAL_ID, "sample.1");
        fbuilder.individualToFileIdentifier(MATERNAL_ID, "sample.2");
        fbuilder.individualToFileIdentifier(DAUGHTER1_ID, "sample.3");
        fbuilder.individualToFileIdentifier(SON_ID, "sample.4");
        fbuilder.individualToFileIdentifier(DAUGHTER2_ID, "sample.5");
        fbuilder.description("multi-sample VCF file for " + FAMILY_ID);
        return fbuilder.build();
    }


    public Pedigree pedigree() {
        PedigreeBuilder pbuilder = PedigreeBuilder.builder();
        Pedigree.Person father = PersonBuilder.builderWithParentsAsFounders(FAMILY_ID, PATERNAL_ID).male().unaffected().build();
        Pedigree.Person mother = PersonBuilder.builderWithParentsAsFounders(FAMILY_ID, MATERNAL_ID).female().unaffected().build();
        Pedigree.Person daughter1 = PersonBuilder.builder(FAMILY_ID, DAUGHTER1_ID, PATERNAL_ID, MATERNAL_ID).female().unaffected().build();
        Pedigree.Person son = PersonBuilder.builder(FAMILY_ID, SON_ID, PATERNAL_ID, MATERNAL_ID).male().affected().build();
        Pedigree.Person daughter2 = PersonBuilder.builder(FAMILY_ID, DAUGHTER2_ID, PATERNAL_ID, MATERNAL_ID).female().unaffected().build();
        return pbuilder.person(father).person(mother).person(daughter1).person(son).person(daughter2).build();
    }




}
