package org.phenopackets.phenopackettools.cli.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Unit;
import org.phenopackets.schema.v2.Phenopacket;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

public class Marfan implements PhenopacketExample {

    private static final String PHENOPACKET_ID = "id-C";
    private static final String PROBAND_ID = "proband C";

    private final Phenopacket phenopacket;

    public Marfan() {
        var marfan = DiseaseBuilder.of("OMIM:154700 ", "Marfan syndrome");
        var individual = IndividualBuilder.builder(PROBAND_ID).female().ageAtLastEncounter("P27Y").build();
        var losartan = ontologyClass("DrugCentral:1610", "losartan");
        var aorticAneurysm =
                PhenotypicFeatureBuilder.of("HP:0002616", "Aortic root aneurysm");
        var quantity = QuantityBuilder.of(Unit.milligram(), 30.0);
        var administration = ontologyClass("NCIT:C38288", "Oral Route of Administration");
        var bid = ontologyClass("NCIT:C64496", "Twice Daily");
        var interval = TimeIntervalBuilder.of("2019-03-20T00:00:00Z", "2021-03-20T00:00:00Z");
        var dosage = DoseIntervalBuilder.of(quantity, bid, interval);
        var losartanTreatment = TreatmentBuilder
                .builder(losartan)
                .addDoseInterval(dosage)
                .routeOfAdministration(administration)
                .build();
        var medicalAction = MedicalActionBuilder.treatment(losartanTreatment);
        var metaData = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.hpoVersion("2021-08-02"))
                .addResource(Resources.omimVersion("2022-11-23"))
                .addResource(Resources.drugCentralVersion("2022-08-22"))
                .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.ucum())
                .build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metaData)
                .individual(individual)
                .addDisease(marfan)
                .addPhenotypicFeature(aorticAneurysm)
                .addMedicalAction(medicalAction)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
