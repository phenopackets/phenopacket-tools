package org.phenopackets.phenotools.examples;

import org.phenopackets.phenotools.builder.PhenopacketBuilder;
import org.phenopackets.phenotools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.of;

class Marfan implements PhenopacketExample {

    private static final String PHENOPACKET_ID = "id-C";
    private static final String PROBAND_ID = "proband C";

    private final Phenopacket phenopacket;

    Marfan() {
        var marfan = DiseaseBuilder.disease("OMIM:154700 ", "Marfan syndrome");
        var individual = IndividualBuilder.builder(PROBAND_ID).female().ageAtLastEncounter("P27Y").build();
        var losartan = of("DrugCentral:1610", "losartan");
        var mg = of("UO:0000022", "milligram");
        var aorticAneurysm =
                PhenotypicFeatureBuilder.of("HP:0002616", "Aortic root aneurysm");
        var quantity = QuantityBuilder.of(mg, 30.0);
        var administration = of("NCIT:C38288", "Oral Route of Administration");
        var bid = of("NCIT:C64496", "Twice Daily");
        var interval = TimeIntervalBuilder.of("2019-03-20T00:00:00Z", "2021-03-20T00:00:00Z");
        var dosage = DoseIntervalBuilder.of(quantity, bid, interval);
        var losartanTreatment = TreatmentBuilder
                .builder(losartan)
                .doseInterval(dosage)
                .routeOfAdministration(administration)
                .build();
        var medicalAction = MedicalActionBuilder.treatment(losartanTreatment);
        var metaData = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
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
