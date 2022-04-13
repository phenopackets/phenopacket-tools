package org.phenopackets.phenotools.builder.builders;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TimeElement;
import org.phenopackets.schema.v2.core.VitalStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.phenopackets.phenotools.builder.builders.IndividualBuilder.HOMO_SAPIENS;
import static org.phenopackets.phenotools.builder.builders.TimestampBuilder.fromISO8601;


public class IndividualBuilderTest {


    @Test
    public void testId() {
        String id = "test";
        Individual individual = IndividualBuilder.builder(id).build();
        assertEquals(id, individual.getId());
    }

    @Test
    public void testDateOfBirth() {
        String dob = "1998-01-01T00:00:00Z";
        // The builder converts the RFC3339 time string to a Google Timestamp
        Individual individual = IndividualBuilder.builder("some.id")
                .dateOfBirth(dob)
                .build();
        Timestamp dobStamp = fromISO8601(dob);
        assertEquals(dobStamp, individual.getDateOfBirth());
    }

    @Test
    public void testTimeAtLastEncounter() {
        String lastExamTime = "2020-01-01T00:00:00Z";
        // The builder converts the RFC3339 time string to a Google Timestamp
        Individual individual = IndividualBuilder.builder("some.id")
                .timestampAtLastEncounter(lastExamTime)
                .build();
        Timestamp encounterStamp = fromISO8601(lastExamTime);
        TimeElement lastEncounter = individual.getTimeAtLastEncounter();
        assertTrue(lastEncounter.hasTimestamp());
        assertEquals(encounterStamp, lastEncounter.getTimestamp());
    }


    @Test
    public void testTaxons() {
        OntologyClass musMusculus = OntologyClass.newBuilder()
                .setId("NCBI:txid10090")
                .setLabel("Mus musculus")
                .build();
        Individual individual = IndividualBuilder.builder("mickey").taxonomy(musMusculus).build();
        assertEquals(musMusculus, individual.getTaxonomy());

        Individual individual2 = IndividualBuilder.builder("human").homoSapiens().build();
        assertEquals(HOMO_SAPIENS, individual2.getTaxonomy());
        assertNotEquals(HOMO_SAPIENS, individual.getTaxonomy());
    }

    @Test
    public void testVitalStatus() {
        Individual individual = IndividualBuilder.builder("id").deceased().build();
        assertTrue(individual.hasVitalStatus());
        assertEquals(VitalStatus.Status.DECEASED, individual.getVitalStatus().getStatus());
        Individual individual2 = IndividualBuilder.builder("id").alive().build();
        assertTrue(individual2.hasVitalStatus());
        assertEquals(VitalStatus.Status.ALIVE, individual2.getVitalStatus().getStatus());
    }



}
