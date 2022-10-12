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

/**
 * A command that provides routines for reading as well as {@link PhenopacketFormat}s and {@link PhenopacketElement}s
 * for processing of a single top-level Phenopacket schema element.
 */
public abstract class SingleItemInputCommand extends BaseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleItemInputCommand.class);

    @CommandLine.Option(names = {"-i", "--input"},
            description = "Input phenopacket.%nLeave empty for STDIN")
    public Path input = null;

    // The format will be sniffed if it is uninitialized.
    @CommandLine.Option(names = {"-f", "--format"},
            description = "Phenopacket format.%nChoose from: {${COMPLETION-CANDIDATES}}")
    public PhenopacketFormat format = null;

    // TODO - is it too hard to implement element sniffing?
    @CommandLine.Option(names = {"-e", "--element"},
            description = "Top-level element.%nChoose from {${COMPLETION-CANDIDATES}}%nDefault: phenopacket")
    public PhenopacketElement element = null;

    /**
     * Attempt to read the input in the provided {@code schemaVersion} and exit upon any failure. As a side effect,
     * {@link #format} and {@link #element} fields are set after the function returns.
     * <p>
     * Note that the function does <em>not</em> return if reading fails.
     */
    protected Message readMessageOrExit(PhenopacketSchemaVersion schemaVersion) {
        try {
            return readInputMessage(schemaVersion);
        } catch (FormatSniffException e) {
            System.err.printf("Unable to detect input format of %s.\nConsider using the `--format` option.%n", input.toAbsolutePath());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unable to read input file, " + e.getMessage() + "\nPlease check the format of file " + input.toAbsolutePath());
            System.exit(1);
        }
        return null; // Cannot happen but to make the compiler happy..
    }

    /**
     * Read the input {@link Message} either from the standard input or from the provided {@link #input}.
     * <p>
     * The method uses {@link #format} and {@link #element} to decode the input. In absence of the {@link #format},
     * we make an educated guess (sniff) and throw a {@link FormatSniffException} if the sniffing fails.
     *
     * @return the parsed {@link Message}.
     * @throws FormatSniffException if the format sniffing fails.
     * @throws IOException          in case of I/O errors.
     */
    private Message readInputMessage(PhenopacketSchemaVersion schemaVersion) throws FormatSniffException, IOException {
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

            return parseMessage(schemaVersion, is);
        } finally {
            if (is != null && is != System.in)
                is.close();
        }
    }

    private Message parseMessage(PhenopacketSchemaVersion schemaVersion, InputStream is) throws IOException {
        return switch (format) {
            case PROTOBUF -> readProtobufMessage(schemaVersion, is);
            case JSON -> readJsonMessage(schemaVersion, is);
            // TODO - implement YAML parsing
            case YAML -> throw new RuntimeException("YAML parser is not yet implemented");
        };
    }

    private Message readProtobufMessage(PhenopacketSchemaVersion schemaVersion, InputStream is) throws IOException {
        LOGGER.debug("Reading protobuf message");
        return switch (schemaVersion) {
            case V1 -> switch (element) {
                case PHENOPACKET -> Phenopacket.parseFrom(is);
                case FAMILY -> Family.parseFrom(is);
                case COHORT -> Cohort.parseFrom(is);
            };
            case V2 -> switch (element) {

                case PHENOPACKET -> org.phenopackets.schema.v2.Phenopacket.parseFrom(is);
                case FAMILY -> org.phenopackets.schema.v2.Family.parseFrom(is);
                case COHORT -> org.phenopackets.schema.v2.Cohort.parseFrom(is);
            };
        };
    }

    private Message readJsonMessage(PhenopacketSchemaVersion schemaVersion, InputStream is) throws IOException {
        LOGGER.debug("Reading JSON message");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Message.Builder builder = prepareBuilder(schemaVersion, element);
        JsonFormat.parser().merge(reader, builder);
        return builder.build();
    }

    private static Message.Builder prepareBuilder(PhenopacketSchemaVersion schemaVersion, PhenopacketElement element) {
        return switch (schemaVersion) {
            case V1 -> switch (element) {
                case PHENOPACKET -> org.phenopackets.schema.v1.Phenopacket.newBuilder();
                case FAMILY -> org.phenopackets.schema.v1.Family.newBuilder();
                case COHORT -> org.phenopackets.schema.v1.Cohort.newBuilder();
            };
            case V2 -> switch (element) {
                case PHENOPACKET -> org.phenopackets.schema.v2.Phenopacket.newBuilder();
                case FAMILY -> org.phenopackets.schema.v2.Family.newBuilder();
                case COHORT -> org.phenopackets.schema.v2.Cohort.newBuilder();
            };
        };
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

    protected enum PhenopacketSchemaVersion {
        V1,
        V2;
    }
}
