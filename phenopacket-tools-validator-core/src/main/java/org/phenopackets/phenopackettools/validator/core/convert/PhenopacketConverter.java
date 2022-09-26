package org.phenopackets.phenopackettools.validator.core.convert;

import com.google.protobuf.InvalidProtocolBufferException;
import org.phenopackets.phenopackettools.validator.core.ConversionException;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

public class PhenopacketConverter extends BaseConverter<PhenopacketOrBuilder> {

    private static final PhenopacketConverter INSTANCE = new PhenopacketConverter();

    public static PhenopacketConverter getInstance() {
        return INSTANCE;
    }

    private PhenopacketConverter() {
        // private no-op
    }

    @Override
    public PhenopacketOrBuilder toItem(byte[] payload) throws ConversionException {
        try {
            return Phenopacket.parseFrom(payload);
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public PhenopacketOrBuilder toItem(String payload) throws ConversionException {
        Phenopacket.Builder builder = Phenopacket.newBuilder();
        try {
            parser.merge(payload, builder);
        } catch (InvalidProtocolBufferException e) {
            throw new ConversionException(e);
        }
        return builder;
    }

}
