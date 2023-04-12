package org.phenopackets.phenopackettools.util.format;

import org.phenopackets.phenopackettools.core.PhenopacketFormat;

import java.io.IOException;
import java.io.InputStream;

/**
 * Make an educated guess of the format of a top-level element of Phenopacket schema.
 */
public class FormatSniffer {

    /**
     * The number of bytes used for format sniffing.
     */
    // The longest field name as of now is phenotypicFeatures which is 18 bytes,
    // hence 32 bytes should be enough for format sniffing.
    static final int BUFFER_SIZE = 32;

    private FormatSniffer() {
    }

    /**
     * Make an educated guess of {@link PhenopacketFormat} based on given {@code payload}.
     *
     * @param payload buffer with a certain number of bytes from the front end of the input.
     * @return the sniffed {@link PhenopacketFormat}.
     */
    public static PhenopacketFormat sniff(byte[] payload) {
        if (Util.looksLikeJson(payload)) {
            return PhenopacketFormat.JSON;
        } else if (Util.looksLikeYaml(payload)) {
            return PhenopacketFormat.YAML;
        } else {
            // No JSON, no YAML, it is likely protobuf bytes or some other bytes.
            // Trying to interpret the bytes as a protobuf message downstream is the best we can do.
            // TODO - implement the best possible guessing based on
            //  https://developers.google.com/protocol-buffers/docs/encoding
            return PhenopacketFormat.PROTOBUF;
        }
    }

    /**
     * Make an educated guess of {@link PhenopacketFormat} present in given {@code input}.
     *
     * @param input an {@link InputStream} that supports {@link InputStream#mark(int)}.
     * @return the sniffed {@link PhenopacketFormat}.
     * @throws IOException in case an error occurs while reading the {@code input}.
     * @throws SniffException if the {@code input} does not support {@link InputStream#mark(int)}.
     */
    public static PhenopacketFormat sniff(InputStream input) throws IOException, SniffException {
        return sniff(Util.getAtMostNFirstBytesAndReset(input, BUFFER_SIZE));
    }
}
