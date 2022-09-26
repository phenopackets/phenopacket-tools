package org.phenopackets.phenopackettools.validator.core.convert;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.validator.core.PhenopacketFormatConverter;

abstract class BaseConverter<T extends MessageOrBuilder> implements PhenopacketFormatConverter<T> {

    protected final JsonFormat.Parser parser = JsonFormat.parser();
    protected final JsonFormat.Printer printer = JsonFormat.printer();

    @Override
    public String toJson(T item) {
        try {
            return printer.print(item);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

}
