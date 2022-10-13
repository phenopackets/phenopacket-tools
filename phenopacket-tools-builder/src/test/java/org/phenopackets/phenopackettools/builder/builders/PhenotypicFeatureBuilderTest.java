package org.phenopackets.phenopackettools.builder.builders;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.Age;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.phenopackets.schema.v2.core.TimeElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.phenopackets.phenopackettools.builder.constants.Laterality.left;
import static org.phenopackets.phenopackettools.builder.constants.Severity.severe;

public class PhenotypicFeatureBuilderTest {


    private static PhenotypicFeature phenotypicFeatureByProtobuf() {
        OntologyClass triceps = OntologyClass
                .newBuilder()
                .setId("HP:0031108")
                .setLabel("Triceps weakness")
                .build();
        OntologyClass left = OntologyClass
                .newBuilder()
                .setId("HP:0012835")
                .setLabel("Left")
                .build();
        OntologyClass severe = OntologyClass
                .newBuilder()
                .setId("HP:0012828")
                .setLabel("Severe")
                .build();
        Age iso8601duration= Age.newBuilder()
                .setIso8601Duration("P11Y4M").build();
        TimeElement ageElement = TimeElement
                .newBuilder()
                .setAge(iso8601duration)
                .build();
        return PhenotypicFeature
                .newBuilder()
                .setType(triceps)
                .setOnset(ageElement)
                .setSeverity(severe)
                .addModifiers(left)
                .build();
    }


    PhenotypicFeature phenotypicFeatureByBuilder() {
        return  PhenotypicFeatureBuilder
                .builder("HP:0031108","Triceps weakness")
                .severity(severe())
                .addModifier(left())
                .onset(TimeElements.age("P11Y4M"))
                .build();

    }

    @Test
    public void testEquality() throws InvalidProtocolBufferException {
        var pf1 = phenotypicFeatureByProtobuf();
        var pf2 = phenotypicFeatureByBuilder();
        String json = JsonFormat.printer().print(pf1);
        System.out.println(json);
        String json2 = JsonFormat.printer().print(pf2);
        System.out.println(json2);
        assertEquals(pf1, pf2);

    }


}
