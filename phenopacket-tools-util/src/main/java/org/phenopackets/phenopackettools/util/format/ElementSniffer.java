package org.phenopackets.phenopackettools.util.format;

import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Make an educated guess regarding which top-level element of Phenopacket schema is represented in the provided
 * {@code byte[]} or {@link  InputStream}.
 */
public class ElementSniffer {

    // Remove SLF4J from module-info if we omit logging.
    private static final Logger LOGGER = LoggerFactory.getLogger(ElementSniffer.class);

    /**
     * The number of bytes used for element sniffing.
     */
    static final int BUFFER_SIZE = 32;

    private ElementSniffer() {
    }

    /**
     * Make an educated guess of {@link PhenopacketElement} present in given {@code input}.
     *
     * @param input  an {@link InputStream} that supports {@link InputStream#mark(int)}.
     * @param format the {@code payload} format
     * @return the sniffed {@link PhenopacketElement}.
     * @throws IOException    in case an error occurs while reading the {@code input}.
     * @throws SniffException if there are not enough bytes available in the {@code input} of if the {@code input} does not
     *                        support {@link InputStream#mark(int)}.
     */
    public static PhenopacketElement sniff(InputStream input,
                                           PhenopacketSchemaVersion schemaVersion,
                                           PhenopacketFormat format) throws IOException, SniffException {
        return sniff(Util.getFirstBytesAndReset(input, BUFFER_SIZE), schemaVersion, format);
    }

    /**
     * Make an educated guess of {@link PhenopacketElement} based on given {@code payload}.
     *
     * @param payload buffer with at least the first {@link #BUFFER_SIZE} bytes of the input.
     * @param format  the {@code payload} format
     * @return the sniffed {@link PhenopacketElement}.
     * @throws ElementSniffException if {@code payload} contains less than {@link #BUFFER_SIZE} bytes.
     */
    public static PhenopacketElement sniff(byte[] payload,
                                           PhenopacketSchemaVersion schemaVersion,
                                           PhenopacketFormat format) throws ElementSniffException {
        if (payload.length < BUFFER_SIZE)
            throw new ElementSniffException("Need at least %d bytes to sniff but got %d".formatted(BUFFER_SIZE, payload.length));

        return switch (format) {
            case PROTOBUF -> sniffProtobuf(payload, schemaVersion);
            case JSON -> sniffJson(payload, schemaVersion);
            case YAML -> sniffYaml(payload, schemaVersion);
        };
    }

    private static PhenopacketElement sniffProtobuf(byte[] payload, PhenopacketSchemaVersion schemaVersion) {
        // TODO - implement
        LOGGER.debug("Sniffing is not yet implemented, assuming {}", PhenopacketElement.PHENOPACKET);
        return PhenopacketElement.PHENOPACKET;
    }

    private static PhenopacketElement sniffJson(byte[] payload, PhenopacketSchemaVersion schemaVersion) {
        // TODO - implement
        // TODO - reconsider the sniffing workflow. In case of loosely defined formats like JSON and YAML,
        //  the fields can be in any order and we may not get enough information.
        //  Is it OK to throw upon sniffing failure or an Optional is enough?
        LOGGER.debug("Sniffing is not yet implemented, assuming {}", PhenopacketElement.PHENOPACKET);
        return PhenopacketElement.PHENOPACKET;
    }

    private static PhenopacketElement sniffYaml(byte[] payload, PhenopacketSchemaVersion schemaVersion) {
        // TODO - implement
        LOGGER.debug("Sniffing is not yet implemented, assuming {}", PhenopacketElement.PHENOPACKET);
        return PhenopacketElement.PHENOPACKET;
    }
}
