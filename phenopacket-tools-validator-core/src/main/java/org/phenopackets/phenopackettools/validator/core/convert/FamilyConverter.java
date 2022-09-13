package org.phenopackets.phenopackettools.validator.core.convert;

import com.google.protobuf.InvalidProtocolBufferException;
import org.phenopackets.phenopackettools.validator.core.ConversionException;
import org.phenopackets.schema.v2.*;

public class FamilyConverter extends BaseConverter<FamilyOrBuilder> {

    private static final FamilyConverter INSTANCE = new FamilyConverter();

    public static FamilyConverter getInstance() {
        return INSTANCE;
    }

    private FamilyConverter() {
        // private no-op
    }

    @Override
    public FamilyOrBuilder toItem(byte[] payload) throws ConversionException {
        try {
            return Family.parseFrom(payload);
        } catch (InvalidProtocolBufferException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public FamilyOrBuilder toItem(String payload) throws ConversionException {
        Family.Builder builder = Family.newBuilder();
        try {
            parser.merge(payload, builder);
        } catch (InvalidProtocolBufferException e) {
            throw new ConversionException(e);
        }
        return builder;
    }
}
