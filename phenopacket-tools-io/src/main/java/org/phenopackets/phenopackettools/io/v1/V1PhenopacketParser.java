package org.phenopackets.phenopackettools.io.v1;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.io.base.BasePhenopacketParser;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.schema.v1.Cohort;
import org.phenopackets.schema.v1.Family;
import org.phenopackets.schema.v1.Phenopacket;

import java.io.IOException;
import java.io.InputStream;

public class V1PhenopacketParser extends BasePhenopacketParser {

    public static final V1PhenopacketParser INSTANCE = new V1PhenopacketParser();

    @Override
    protected Message readProtobufMessage(PhenopacketElement element, InputStream is) throws IOException {
        return switch (element) {
            case PHENOPACKET -> Phenopacket.parseFrom(is);
            case FAMILY -> Family.parseFrom(is);
            case COHORT -> Cohort.parseFrom(is);
        };
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
