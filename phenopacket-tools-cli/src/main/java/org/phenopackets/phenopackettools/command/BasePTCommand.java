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
 * Base command that defines routines for reading/writing input/output
 * as well as {@link PhenopacketFormat}s and {@link PhenopacketElement}s for commands that process
 * a single top-level Phenopacket schema element.
 */
public abstract class BasePTCommand implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePTCommand.class);

    @CommandLine.Option(names = {"-i", "--input"},
            description = "Input phenopacket file (leave empty for STDIN)")
    public Path input = null;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Output file (leave empty for STDOUT)")
    public Path output = null;

    // If the format is uninitialized, it will be sniffed.
    @CommandLine.Option(names = {"-f", "--format"},
            description = "Phenopacket format (choose from {json,yaml,protobuf})")
    public PhenopacketFormat format = null;

    @CommandLine.Option(names = {"-e", "--element"},
            description = "Top-level element (default: ${DEFAULT-VALUE})")
    public PhenopacketElement element = PhenopacketElement.PHENOPACKET;

    protected Message readInputMessage() throws FormatSniffException, IOException {
        InputStream is = null;
        try {
            is = openInput();
            if (format == null)
                // Remember the provided or sniffed input format.
                format = parseFormat(is);

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

    protected void writeV2Message(Message message, PhenopacketFormat format) throws IOException {
        OutputStream os = null;
        try {
            os = openOutput();
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
        } finally {
            if (os != null && os != System.out)
                os.close();
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

    private OutputStream openOutput() throws IOException {
        if (output == null) {
            // Write to STDOUT
            return System.out;
        } else {
            Path parent = output.getParent();
            if (Files.isRegularFile(parent)) {
                System.err.printf("The parent %s is a file%n", parent.toAbsolutePath());
                System.exit(1);
            }

            if (!Files.isDirectory(parent))
                createParentDirectoriesOrExit(parent);

            LOGGER.info("Writing the output to {}", output.toAbsolutePath());
            return new BufferedOutputStream(Files.newOutputStream(output));
        }
    }

    private static void createParentDirectoriesOrExit(Path parent) {
        try {
            LOGGER.info("Creating non-existing parent directories..");
            Files.createDirectories(parent);
        } catch (IOException e) {
            System.err.printf("Tried to create non-existent parent directories for %s but failed: %s%n", parent.toAbsolutePath(), e.getMessage());
            System.exit(1);
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
