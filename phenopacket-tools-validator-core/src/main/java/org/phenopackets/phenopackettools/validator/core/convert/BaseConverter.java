package org.phenopackets.phenopackettools.validator.core.convert;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.util.print.PhenopacketPrintUtil;
import org.phenopackets.phenopackettools.validator.core.PhenopacketFormatConverter;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorRuntimeException;

abstract class BaseConverter<T extends MessageOrBuilder> implements PhenopacketFormatConverter<T> {

    protected static final JsonFormat.Parser parser = PhenopacketPrintUtil.getParser();

    @Override
    public String toJson(T item) {
        try {
            return PhenopacketPrintUtil.getPrinter().print(item);
        } catch (InvalidProtocolBufferException e) {
            throw new PhenopacketValidatorRuntimeException(e);
        }
    }

}
