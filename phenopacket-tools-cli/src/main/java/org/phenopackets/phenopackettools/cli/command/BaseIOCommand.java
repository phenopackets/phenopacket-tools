package org.phenopackets.phenopackettools.cli.command;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.io.PhenopacketParser;
import org.phenopackets.phenopackettools.io.PhenopacketParserFactory;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;
import org.phenopackets.phenopackettools.util.format.ElementSniffer;
import org.phenopackets.phenopackettools.util.format.FormatSniffer;
import org.phenopackets.phenopackettools.util.format.SniffException;
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

    private final PhenopacketParserFactory parserFactory;

    @CommandLine.ArgGroup(validate = false, heading = "Inputs:%n")
    public InputSection inputSection = new InputSection();

    public static class InputSection {
        // The format will be sniffed if it is not provided.
        @CommandLine.Option(names = {"-f", "--format"},
                description = {"Phenopacket format.",
                        "Choose from: {${COMPLETION-CANDIDATES}}"})
        public PhenopacketFormat format = null;

        @CommandLine.Option(names = {"-e", "--element"},
                description = {"Top-level element.",
                        "Choose from {${COMPLETION-CANDIDATES}}",
                        "Default: an educated guess from the input"})
        public PhenopacketElement element = null;

    }

    @CommandLine.Parameters(
            paramLabel = "phenopacket file(s)",
            description = {
                    "Input phenopacket(s).",
                    "Leave empty for STDIN"
            }
    )
    public List<Path> inputs = null;

    protected BaseIOCommand() {
        parserFactory = PhenopacketParserFactory.getInstance();
    }

    /**
     * Attempt to read the input in the provided {@code schemaVersion} and exit upon any failure. As a side effect,
     * {@link org.phenopackets.phenopackettools.cli.command.BaseIOCommand.InputSection#format}
     * and {@link org.phenopackets.phenopackettools.cli.command.BaseIOCommand.InputSection#element}
     * fields are set after the function returns.
     * <p>
     * Note that the function does <em>not</em> return if reading fails.
     */
    protected List<MessageAndPath> readMessagesOrExit(PhenopacketSchemaVersion schemaVersion) {
        PhenopacketParser parser = parserFactory.forFormat(schemaVersion);
        if (inputs == null) {
            // The user did not provide positional parameters, assuming a single input is coming from STDIN.
            InputStream is = System.in;
            try {
                setFormatAndElement(is);
                Message message = parser.parse(inputSection.format, inputSection.element, is);
                return List.of(new MessageAndPath(message, null));
            } catch (IOException e) {
                System.err.println("Unable to read STDIN: " + e.getMessage() + "\nPlease check the input format.");
            }
            System.exit(1);
        } else {
            // Assuming a one or more inputs are provided via positional parameters.
            //
            // Picocli should ensure that `input` is never an empty list.
            // The `input` is `null` if no positional parameters were supplied.
            assert !inputs.isEmpty();

            List<MessageAndPath> messages = new ArrayList<>();
            for (Path input : inputs) {
                try (InputStream is = new BufferedInputStream(Files.newInputStream(input))) {
                    setFormatAndElement(is);
                    Message message = parser.parse(inputSection.format, inputSection.element, is);
                    messages.add(new MessageAndPath(message, input));
                } catch (IOException e) {
                    System.err.printf("Unable to read input file %s: %s\nPlease check the input format.%n", input.toAbsolutePath(), e.getMessage());
                    System.exit(1);
                }
            }
            return messages;
        }
        return null; // Cannot happen since System.exit() never returns, but to make the compiler happy...
    }

    /**
     * Peek into the provided {@link InputStream} {@code is} to set {@link InputSection#format}
     * and {@link InputSection#element} items.
     *
     * @throws IOException if the format/element sniffing fails and the user did not set the CLI fields or if an I/O error happens.
     */
    private void setFormatAndElement(InputStream is) throws IOException {
        SniffException se = null;

        // Set the format.
        PhenopacketFormat fmt = null;
        try {
            fmt = FormatSniffer.sniff(is);
        } catch (SniffException e) {
            se = e;
        }
        if (inputSection.format == null) {
            LOGGER.info("Input format was not provided, making an educated guess..");
            if (fmt == null)
                throw new IOException("Input format (-f | --format) was not provided and format sniffing failed", se);
            LOGGER.info("The input looks like a {} file", fmt);
            inputSection.format = fmt;
        } else {
            if (fmt != null && !inputSection.format.equals(fmt))
                // This can happen e.g. if processing multiple files at once but one turns out to be a different format.
                // We emit a warning because this is likely not what the user intended and the code will likely explode
                // further downstream.
                LOGGER.warn("Input format is set to {} but the current input looks like a {}", inputSection.format, fmt);
        }

        // Set the element.
        PhenopacketElement element = null;
        try {
            element = ElementSniffer.sniff(is, inputSection.format);
        } catch (SniffException e) {
            se = e;
        }
        if (inputSection.element == null) {
            LOGGER.info("Input element was not provided, making an educated guess..");
            if (element == null)
                throw new IOException("Input element (-e | --element) was not provided and element sniffing failed", se);
            LOGGER.info("The input looks like a {} ", element);
            inputSection.element = element;
        }
        else {
            if (element != null && !inputSection.element.equals(element))
                // Let's go an extra mile and check for the user.
                // Same as above, we emit a warning since the code will likely explode further downstream.
                LOGGER.warn("Input element is set to {} but the current input looks like a {}", inputSection.element, element);
        }
    }

    protected record MessageAndPath(Message message, Path path) {}

}
