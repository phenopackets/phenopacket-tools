package org.phenopackets.phenopackettools.command;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.util.format.FormatSniffException;
import org.phenopackets.phenopackettools.util.format.FormatSniffer;
import org.phenopackets.phenopackettools.util.format.PhenopacketElement;
import org.phenopackets.phenopackettools.util.format.PhenopacketFormat;
import org.phenopackets.schema.v1.Cohort;
import org.phenopackets.schema.v1.Family;
import org.phenopackets.schema.v1.Phenopacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * A command that provides routines for reading/writing input/output
 * as well as {@link PhenopacketFormat}s and {@link PhenopacketElement}s
 * for processing of a single top-level Phenopacket schema element.
 */
public abstract class SingleItemProcessingCommand implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleItemProcessingCommand.class);

    @CommandLine.Option(names = {"-i", "--input"},
            description = "Input phenopacket.%nLeave empty for STDIN")
    public Path input = null;

    // If the format is uninitialized, it will be sniffed.
    @CommandLine.Option(names = {"-f", "--format"},
            description = "Phenopacket format.%nChoose from: {${COMPLETION-CANDIDATES}}")
    public PhenopacketFormat format = null;

    @CommandLine.Option(names = {"-e", "--element"},
            description = "Top-level element.%nChoose from {${COMPLETION-CANDIDATES}}%nDefault: phenopacket")
    public PhenopacketElement element = null;

    /**
     * Read the input {@link Message} either from the standard input or from the provided {@link #input}.
     * <p>
     * The method uses {@link #format} and {@link #element} to decode the input. In absence of the {@link #format},
     * we make an educated guess (sniff) and throw a {@link FormatSniffException} if the sniffing fails.
     *
     * @return the parsed {@link Message}.
     * @throws FormatSniffException if the format sniffing fails.
     * @throws IOException in case of I/O errors.
     */
    protected Message readInputMessage() throws FormatSniffException, IOException {
        InputStream is = null;
        try {
            is = openInput();
            if (format == null)
                // Remember the provided or sniffed input format.
                format = parseFormat(is);

            if (element == null) {
                LOGGER.info("Input element type was not provided, assuming phenopacket.. ");
                element = PhenopacketElement.PHENOPACKET;
            }

            return switch (format) {
                case PROTOBUF -> {
                    LOGGER.debug("Reading protobuf message");
                    yield switch (element) {
                        case PHENOPACKET -> Phenopacket.parseFrom(is);
                        case FAMILY -> Family.parseFrom(is);
                        case COHORT -> Cohort.parseFrom(is);
                    };
                }
                case JSON -> {
                    LOGGER.debug("Reading JSON message");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    Message.Builder builder = prepareBuilder(element);
                    JsonFormat.parser().merge(reader, builder);
                    yield builder.build();
                }
                case YAML -> {
                    // TODO - implement
                    throw new RuntimeException("YAML parser is not yet implemented");
                }
            };
        } finally {
            if (is != null && is != System.in)
                is.close();
        }
    }

    /**
     * Write the {@code message} in an appropriate {@code format} into the provided {@link OutputStream} {@code os}.
     * <p>
     * Uses {@link }
     * @param message message to be written out.
     * @param format format to write out
     * @param os where to write
     * @throws IOException in case of I/O errors during the output
     */
    protected static void writeMessage(Message message, PhenopacketFormat format, OutputStream os) throws IOException {
        switch (format) {
            case PROTOBUF -> {
                LOGGER.debug("Writing protobuf message");
                message.writeTo(os);
            }
            case JSON -> {
                LOGGER.debug("Writing JSON message");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                JsonFormat.printer().appendTo(message, writer);
                writer.flush();
            }
            case YAML -> {
                // TODO - implement
                throw new RuntimeException("YAML printer is not yet implemented");
            }
        }
    }

    private InputStream openInput() throws IOException {
        if (input == null) {
            return System.in;
        } else {
            if (!Files.isRegularFile(input)) {
                System.err.printf("The input file %s does not exist!%n", input.toAbsolutePath());
                System.exit(1);
            }
            LOGGER.info("Reading input from {}", input.toAbsolutePath());
            return new BufferedInputStream(Files.newInputStream(input));
        }
    }

    private PhenopacketFormat parseFormat(InputStream is) throws IOException, FormatSniffException {
        if (format == null) {
            LOGGER.info("Input format was not provided, making an educated guess..");
            PhenopacketFormat fmt = FormatSniffer.sniff(is);
            LOGGER.info("The input looks like a {} file", fmt);
            return fmt;
        }
        return format;
    }

    private static Message.Builder prepareBuilder(PhenopacketElement element) {
        return switch (element) {
            case PHENOPACKET -> Phenopacket.newBuilder();
            case FAMILY -> Family.newBuilder();
            case COHORT -> Cohort.newBuilder();
        };
    }
}
