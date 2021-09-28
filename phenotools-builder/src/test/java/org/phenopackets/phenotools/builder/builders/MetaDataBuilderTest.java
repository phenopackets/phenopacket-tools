package org.phenopackets.phenotools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.MetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetaDataBuilderTest {


    @Test
    public void testCreation() {
        String creationDate = "2020-03-17T00:00:00Z";
        String creator = "anonymous biocurator";
        MetaData metaData = MetaDataBuilder.create(creationDate, creator).build();
        assertTrue(metaData.hasCreated());
        assertEquals(creator, metaData.getCreatedBy());
    }
}
