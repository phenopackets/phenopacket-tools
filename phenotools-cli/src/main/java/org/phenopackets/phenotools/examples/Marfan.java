package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.ontologyClass;

public class Marfan implements  PhenopacketExample {

    private static final String PHENOPACKET_ID = "id-C";
    private static final String PROBAND_ID = "proband C";

    private final Phenopacket phenopacket;

    public Marfan() {
        var marfan = DiseaseBuilder.create("OMIM:154700 ", "Marfan syndrome").build();
        var individual = IndividualBuilder.create(PROBAND_ID).female().ageAtLastEncounter("P27Y").build();
        var losartan = ontologyClass("DrugCentral:1610", "losartan");
        var mg = ontologyClass("UO:0000022", "milligram");
        var aorticAneurysm =
                PhenotypicFeatureBuilder.create("HP:0002616", "Aortic root aneurysm").build();
        var quantity = QuantityBuilder.create(mg, 30.0).build();
        var administration = ontologyClass("NCIT:C38288", "Oral Route of Administration");
        var bid = ontologyClass("NCIT:C64496", "Twice Daily");
        var interval = TimeIntervalCreator.create("2019-03-20", "2021-03-20");
        var dosage = DoseIntervalCreator.create(quantity, bid, interval);
        var losartanTreatment = TreatmentBuilder
                .create(losartan)
                .doseInterval(dosage)
                .routeOfAdministration(administration)
                .build();
        var medicalAction = MedicalActionBuilder.treatment(losartanTreatment).build();
        var metaData = MetaDataBuilder.create("2021-05-14T10:35:00Z", "anonymous biocurator")
                .hpWithVersion("2021-08-02")
                .build();
        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metaData)
                .individual(individual)
                .disease(marfan)
                .medicalAction(medicalAction)
                .build();
    }


    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
