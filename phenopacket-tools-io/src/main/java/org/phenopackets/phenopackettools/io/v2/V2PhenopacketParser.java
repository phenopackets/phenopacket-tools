package org.phenopackets.phenopackettools.io.v2;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.io.base.BasePhenopacketParser;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;

import java.io.IOException;
import java.io.InputStream;

public class V2PhenopacketParser extends BasePhenopacketParser {

    public static final V2PhenopacketParser INSTANCE = new V2PhenopacketParser();

    @Override
    protected Message readProtobufMessage(PhenopacketElement element, InputStream is) throws IOException {
        return switch (element) {
            case PHENOPACKET -> Phenopacket.parseFrom(is);
            case FAMILY -> Family.parseFrom(is);
            case COHORT -> Cohort.parseFrom(is);
        };
    }

    @Override
    protected Message readYamlMessage(PhenopacketElement element, InputStream is) throws IOException {
        throw new RuntimeException("Not yet implemented"); // TODO - implement
    }

    @Override
    protected Message.Builder prepareBuilder(PhenopacketElement element) {
        return switch (element) {
            case PHENOPACKET -> Phenopacket.newBuilder();
            case FAMILY -> Family.newBuilder();
            case COHORT -> Cohort.newBuilder();
        };
    }
}
