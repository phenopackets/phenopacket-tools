package org.phenopackets.phenopackettools.util.format;

import org.phenopackets.phenopackettools.core.PhenopacketElement;

import java.io.IOException;
import java.io.InputStream;

/**
 * Make an educated guess regarding which top-level element of Phenopacket schema is represented in the provided
 * {@code byte[]} or {@link  InputStream}.
 */
public class ElementSniffer {

    /**
     * The number of bytes used for element sniffing.
     */
    static final int BUFFER_SIZE = 32;

    private ElementSniffer() {
    }

    /**
     * Make an educated guess of {@link PhenopacketElement} present in given {@code input}.
     *
     * @param input an {@link InputStream} that supports {@link InputStream#mark(int)}.
     * @return the sniffed {@link PhenopacketElement}.
     * @throws IOException    in case an error occurs while reading the {@code input}.
     * @throws SniffException if there are not enough bytes available in the {@code input} of if the {@code input} does not
     *                        support {@link InputStream#mark(int)}.
     */
    public static PhenopacketElement sniff(InputStream input) throws IOException, SniffException {
        return sniff(Util.getFirstBytesAndReset(input, BUFFER_SIZE));
    }

    /**
     * Make an educated guess of {@link PhenopacketElement} based on given {@code payload}.
     *
     * @param payload buffer with at least the first {@link #BUFFER_SIZE} bytes of the input.
     * @return the sniffed {@link PhenopacketElement}.
     * @throws ElementSniffException if {@code payload} contains less than {@link #BUFFER_SIZE} bytes.
     */
    public static PhenopacketElement sniff(byte[] payload) throws ElementSniffException {
        if (payload.length < BUFFER_SIZE)
            throw new ElementSniffException("Need at least %d bytes to sniff but got %d".formatted(BUFFER_SIZE, payload.length));
        // TODO - implement
        return PhenopacketElement.PHENOPACKET;
    }
}
