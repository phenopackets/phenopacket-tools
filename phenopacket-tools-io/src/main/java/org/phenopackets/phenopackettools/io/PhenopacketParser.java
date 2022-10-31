package org.phenopackets.phenopackettools.io;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.util.format.FormatSniffer;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.util.format.SniffException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface PhenopacketParser {

    Message parse(PhenopacketFormat format, PhenopacketElement element, InputStream is) throws IOException;

    default Message parse(PhenopacketFormat format, PhenopacketElement element, Path path) throws IOException {
        try (InputStream is = openInputStream(path)) {
            return parse(format, element, is);
        }
    }

    /* ******************************************* CONVENIENCE METHODS ******************************************* */

    // We need to detect the element.

    default Message parse(PhenopacketFormat format, InputStream is) throws IOException {
        PhenopacketElement element = sniffElement(is);
        return parse(format, element, is);
    }

    default Message parse(PhenopacketFormat format, Path path) throws IOException {
        try (InputStream is = openInputStream(path)) {
            return parse(format, is);
        }
    }

    // We need to detect the format.

    default Message parse(PhenopacketElement element, InputStream is) throws IOException, SniffException {
        PhenopacketFormat format = sniffFormat(is);
        return parse(format, element, is);
    }

    default Message parse(PhenopacketElement element, Path path) throws IOException, SniffException {
        try (InputStream is = openInputStream(path)) {
            return parse(element, is);
        }
    }

    // We need to detect both the format and the element.

    default Message parse(InputStream is) throws IOException, SniffException {
        PhenopacketFormat format = sniffFormat(is);
        return parse(format, is);
    }

    default Message parse(Path path) throws IOException, SniffException {
        try (InputStream is = openInputStream(path)) {
            return parse(is);
        }
    }

    /* ******************************************* UTILITY METHODS ******************************************* */

    private static PhenopacketElement sniffElement(InputStream is) {
        return PhenopacketElement.PHENOPACKET; // TODO - implement
    }

    private static PhenopacketFormat sniffFormat(InputStream is) throws SniffException, IOException {
        return FormatSniffer.sniff(is);
    }

    private static BufferedInputStream openInputStream(Path path) throws IOException {
        return new BufferedInputStream(Files.newInputStream(path));
    }
}
