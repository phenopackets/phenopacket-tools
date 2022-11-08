package org.phenopackets.phenopackettools.io;

import com.google.protobuf.Message;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The implementors can serialize a top-level element of Phenopacket schema into provided {@link OutputStream}.
 */
public interface PhenopacketPrinter {

    void print(Message message, OutputStream os) throws IOException;

    default void print(Message message, Path output) throws IOException {
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(output))) {
            print(message, os);
        }
    }
}
