package org.phenopackets.phenopackettools.validator.core.convert;

import com.google.protobuf.InvalidProtocolBufferException;
import org.phenopackets.phenopackettools.validator.core.ConversionException;
import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.CohortOrBuilder;

public class CohortConverter extends BaseConverter<CohortOrBuilder> {

    private static final CohortConverter INSTANCE = new CohortConverter();

    public static CohortConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public CohortOrBuilder toItem(byte[] payload) throws ConversionException {
        try {
            return Cohort.parseFrom(payload);
        } catch (InvalidProtocolBufferException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public CohortOrBuilder toItem(String payload) throws ConversionException {
        Cohort.Builder builder = Cohort.newBuilder();
        try {
            parser.merge(payload, builder);
        } catch (InvalidProtocolBufferException e) {
            throw new ConversionException(e);
        }
        return builder;
    }

}
