package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;

class Marfan implements PhenopacketExample {

    private static final String PHENOPACKET_ID = "id-C";
    private static final String PROBAND_ID = "proband C";

    private final Phenopacket phenopacket;

    Marfan() {
        var marfan = DiseaseBuilder.disease("OMIM:154700 ", "Marfan syndrome");
        var individual = IndividualBuilder.create(PROBAND_ID).female().ageAtLastEncounter("P27Y").build();
        var losartan = ontologyClass("DrugCentral:1610", "losartan");
        var mg = ontologyClass("UO:0000022", "milligram");
        var aorticAneurysm =
                PhenotypicFeatureBuilder.phenotypicFeature("HP:0002616", "Aortic root aneurysm");
        var quantity = QuantityBuilder.quantity(mg, 30.0);
        var administration = ontologyClass("NCIT:C38288", "Oral Route of Administration");
        var bid = ontologyClass("NCIT:C64496", "Twice Daily");
        var interval = TimeIntervalBuilder.timeInterval("2019-03-20T00:00:00Z", "2021-03-20T00:00:00Z");
        var dosage = DoseIntervalBuilder.doseInterval(quantity, bid, interval);
        var losartanTreatment = TreatmentBuilder
                .create(losartan)
                .doseInterval(dosage)
                .routeOfAdministration(administration)
                .build();
        var medicalAction = MedicalActionBuilder.treatment(losartanTreatment);
        var metaData = MetaDataBuilder.create("2021-05-14T10:35:00Z", "anonymous biocurator")
                .resource(Resources.hpoVersion("2021-08-02"))
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
