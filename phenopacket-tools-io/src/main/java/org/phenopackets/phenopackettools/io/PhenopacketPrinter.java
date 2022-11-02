package org.phenopackets.phenopackettools.io;

import com.google.protobuf.MessageOrBuilder;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface PhenopacketPrinter<T extends MessageOrBuilder> {

    void print(T message, OutputStream os) throws IOException;

    default void print(T message, Path output) throws IOException {
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(output))) {
            print(message, os);
        }
    }
}
