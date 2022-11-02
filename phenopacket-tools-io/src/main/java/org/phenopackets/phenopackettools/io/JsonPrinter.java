package org.phenopackets.phenopackettools.io;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

class JsonPrinter<T extends Message> implements PhenopacketPrinter<T> {

    private static final JsonFormat.Printer PRINTER = JsonFormat.printer();

    private static final JsonPrinter<?> INSTANCE = new JsonPrinter<>();

    static <T extends Message> JsonPrinter<T> getInstance() {
        // We know that JsonFormat can serialize ANY Message, hence the unchecked cast is safe.

        //noinspection unchecked
        return (JsonPrinter<T>) INSTANCE;
    }
    private JsonPrinter() {
    }
    @Override
    public void print(T message, OutputStream os) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        PRINTER.appendTo(message, writer);
        writer.flush();
    }
}
