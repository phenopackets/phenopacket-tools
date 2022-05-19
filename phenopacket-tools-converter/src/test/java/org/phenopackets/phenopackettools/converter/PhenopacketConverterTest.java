package org.phenopackets.phenopackettools.converter;

import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.converter.converters.PhenopacketConverter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PhenopacketConverterTest {
    /**
     * To output: System.out.println(JsonFormat.printer().print(v2Phenopacket));
     */
    @Test
    public void name() {
        org.phenopackets.schema.v1.Phenopacket v1Phenopacket = BethlemMyopathyV1.proband();
        org.phenopackets.schema.v2.Phenopacket v2Phenopacket = PhenopacketConverter.toV2Phenopacket(v1Phenopacket);
        assertNotNull(v2Phenopacket);
    }
}