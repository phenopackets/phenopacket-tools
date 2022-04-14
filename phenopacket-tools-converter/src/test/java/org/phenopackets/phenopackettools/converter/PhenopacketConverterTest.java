package org.phenopackets.phenopackettools.converter;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.converter.converters.PhenopacketConverter;

class PhenopacketConverterTest {

    @Test
    void name() throws InvalidProtocolBufferException {
        org.phenopackets.schema.v1.Phenopacket v1Phenopacket = BethlemMyopathyV1.proband();

        org.phenopackets.schema.v2.Phenopacket v2Phenopacket = PhenopacketConverter.toV2Phenopacket(v1Phenopacket);

//        System.out.println(JsonFormat.printer().print(v1Phenopacket));
//        System.out.println(JsonFormat.printer().print(v2Phenopacket));
    }
}