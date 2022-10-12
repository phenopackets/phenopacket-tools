package org.phenopackets.phenopackettools.command;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.converter.converters.V1ToV2Converter;
import org.phenopackets.phenopackettools.util.format.PhenopacketFormat;
import org.phenopackets.schema.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static picocli.CommandLine.Option;

@Command(name = "convert",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        description = "Convert a v1.0 phenopacket to a v2.0 phenopacket.",
        footer = "Beware this process could be lossy!")
public class ConvertCommand extends BaseIOCommand {

    /**
     * A pattern to match file prefix
     */
    private static final Pattern PATTERN = Pattern.compile("^(?<prefix>.*)\\.((pb)|(json)|(yaml))$");
    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertCommand.class);

    @Option(names = {"-o", "--output-format"},
            description = "Output format.%nDefault: input format")
    public PhenopacketFormat outputFormat = null;

    @Option(names = {"-O", "--output-directory"},
            description = "Path to output directory")
    public Path outputDirectory = null;

    @Option(names = {"--convert-variants"},
            description = "Convert variant data.%nDefault: ${DEFAULT-VALUE}")
    public boolean convertVariants = false;


    @Override
    public Integer call() {
        // (0) Print banner.
        printBanner();

        if (!checkInputArgumentsAreOk())
            return 1;

        // (1) Read the input v1 message(s).
        List<MessageAndPath> messages = readMessagesOrExit(PhenopacketSchemaVersion.V1);

        // (2) Convert into v2 format.
        if (convertVariants)
            LOGGER.debug("Converting variants");

        V1ToV2Converter converter = V1ToV2Converter.of(convertVariants);
        List<MessageAndPath> converted = new ArrayList<>(messages.size());
        for (MessageAndPath mp : messages) {
            Message message = mp.message();
            Message v2 = switch (element) {
                case PHENOPACKET -> converter.convertPhenopacket((Phenopacket) message);
                case FAMILY -> converter.convertFamily((Family) message);
                case COHORT -> converter.convertCohort((Cohort) message);
            };
            converted.add(new MessageAndPath(v2, mp.path()));
        }

        // (3) Set the output format if necessary.
        if (outputFormat == null) {
            LOGGER.info("Output format (-o | --output-format) not provided, writing data in the input format `{}`", format);
            outputFormat = format;
        }

        // (4) Write out the output(s).
        return writeOutConverted(converted);
    }

    /**
     * Return {@code true} if CLI argument combination makes sense or {@code false} if the app should abort.
     */
    private boolean checkInputArgumentsAreOk() {
        if (inputs == null) {
            if (outputDirectory != null)
                LOGGER.warn("Output directory was provided but the input is coming from STDIN. The output will be written to STDOUT");
        } else {
            if (inputs.isEmpty()) {
                throw new RuntimeException("Input list should never be empty!"); // A bug guard.
            } else {
                if (inputs.size() > 1) {
                    if (outputDirectory == null) {
                        LOGGER.error("Output directory (-O | --output-directory) must be provided when processing >1 inputs");
                        return false;
                    } else if (!Files.isDirectory(outputDirectory)) {
                        LOGGER.error("The `-O | --output-directory` argument {} is not a directory", outputDirectory.toAbsolutePath());
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int writeOutConverted(List<MessageAndPath> converted) {
        if (converted.size() == 1) {
            // Writing out item, either from STDIN or from one `-i` options.
            MessageAndPath mp = converted.get(0);
            OutputStream os = null;
            try {
                // the input must have come from STDIN
                if (mp.path() == null || outputDirectory == null) {
                    os = System.out;
                } else {
                    os = openOutputStream(mp.path());
                }
                writeMessage(mp.message(), outputFormat, os);
            } catch (IOException e) {
                LOGGER.error("Error while writing out a phenopacket: {}", e.getMessage(), e);
                return 1;
            } finally {
                if (os != null && os != System.out) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        LOGGER.warn("Error occurred while closing the output");
                    }
                }
            }
        } else {
            // Writing out >1 items provided by `-i` options.
            for (MessageAndPath mp : converted) {
                try (OutputStream os = openOutputStream(mp.path())) {
                    writeMessage(mp.message(), outputFormat, os);
                } catch (IOException e) {
                    LOGGER.error("Error while writing out a phenopacket: {}", e.getMessage(), e);
                    return 1;
                }
            }
        }
        return 0;
    }

    private BufferedOutputStream openOutputStream(Path inputPath) throws IOException {
        // remove suffix, add `v2` and add
        String fileName = inputPath.toFile().getName();
        Matcher matcher = PATTERN.matcher(fileName);

        String suffix = ".v2" + outputFormat.suffix();
        Path output;
        if (matcher.matches()) {
            // Remove the prefix from the input file and create a new file
            String prefix = matcher.group("prefix");
            output = outputDirectory.resolve(prefix + suffix);
        } else {
            // Just append the suffix.
            output = outputDirectory.resolve(fileName + suffix);
        }
        LOGGER.debug("Input path: {}, output path: {}", inputPath.toAbsolutePath(), output.toAbsolutePath());

        return new BufferedOutputStream(Files.newOutputStream(output));
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

}
