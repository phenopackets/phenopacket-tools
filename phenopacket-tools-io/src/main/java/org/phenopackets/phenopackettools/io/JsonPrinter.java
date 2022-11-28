package org.phenopackets.phenopackettools.io;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.util.print.PhenopacketPrintUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

class JsonPrinter implements PhenopacketPrinter {

    private static final JsonPrinter INSTANCE = new JsonPrinter();

    static JsonPrinter getInstance() {
        return  INSTANCE;
    }

    private JsonPrinter() {
    }

    @Override
    public void print(Message message, OutputStream os) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        PhenopacketPrintUtil.getPrinter().appendTo(message, writer);
        writer.flush();
    }
}
