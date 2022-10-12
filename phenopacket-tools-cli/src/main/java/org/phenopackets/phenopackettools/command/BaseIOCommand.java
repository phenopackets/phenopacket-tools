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
import java.util.ArrayList;
import java.util.List;

/**
 * A command that provides routines for reading as well as {@link PhenopacketFormat}s and {@link PhenopacketElement}s
 * for processing of a single top-level Phenopacket schema element.
 */
public abstract class BaseIOCommand extends BaseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseIOCommand.class);

    @CommandLine.Option(names = {"-i", "--input"},
            arity = "0..*",
            description = "Input phenopacket(s).%nLeave empty for STDIN")
    public List<Path> inputs = null;

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
    protected List<MessageAndPath> readMessagesOrExit(PhenopacketSchemaVersion schemaVersion) {
        if (inputs == null) {
            // Assuming a single input is coming from STDIN
            InputStream is = System.in;
            try {
                setFormatAndElement(is);
                return List.of(new MessageAndPath(parseMessage(schemaVersion, is), null));
            } catch (FormatSniffException e) {
                System.err.println("Unable to detect input format from STDIN.\nConsider using the `--format` option.");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to read STDIN: " + e.getMessage() + "\nPlease check the input format.");
                System.exit(1);
            }
        } else {
            // Assuming a one or more input are provided via `-i | --input`.

            // Picocli should ensure that `input` is never an empty list. `input` is `null` if no `-i` was supplied.
            assert !inputs.isEmpty();

            List<MessageAndPath> messages = new ArrayList<>();
            for (Path input : inputs) {
                try (InputStream is = new BufferedInputStream(Files.newInputStream(input))) {
                    setFormatAndElement(is);
                    Message message = parseMessage(schemaVersion, is);
                    messages.add(new MessageAndPath(message, input));
                } catch (FormatSniffException e) {
                    System.err.printf("Unable to detect input format of %s.\nConsider using the `--format` option.%n", input.toAbsolutePath());
                    System.exit(1);
                } catch (IOException e) {
                    System.err.printf("Unable to read input file %s: %s\nPlease check the input format.%n", input.toAbsolutePath(), e.getMessage());
                    System.exit(1);
                }
            }
            return messages;
        }
        return null; // Cannot happen but to make the compiler happy...
    }

    private void setFormatAndElement(InputStream is) throws IOException, FormatSniffException {
        PhenopacketFormat sniffed = parseFormat(is);
        if (format == null) {
            format = sniffed;
        } else {
            if (!format.equals(sniffed))
                // This can happen e.g. if processing multiple files at once but one turns out to be a different format.
                // We emit warning because this is likely not what the user intended and the code will likely explode
                // further downstream.
                LOGGER.warn("Input format is set to {} but the current input looks like {}", format, sniffed);
        }

        if (element == null) {
            LOGGER.info("Input element type (-e | --element) was not provided, assuming phenopacket..");
            element = PhenopacketElement.PHENOPACKET;
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

    private PhenopacketFormat parseFormat(InputStream is) throws IOException, FormatSniffException {
        if (format == null) {
            LOGGER.info("Input format was not provided, making an educated guess..");
            PhenopacketFormat fmt = FormatSniffer.sniff(is);
            LOGGER.info("The input looks like a {} file", fmt);
            return fmt;
        }
        return format;
    }

    protected record MessageAndPath(Message message, Path path) {}

    protected enum PhenopacketSchemaVersion {
        V1,
        V2;
    }
}
