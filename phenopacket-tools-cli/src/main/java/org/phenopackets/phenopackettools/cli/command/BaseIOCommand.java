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
        @CommandLine.Option(names = {"-i", "--input"},
                arity = "0..*",
                description = "Input phenopacket(s).%nLeave empty for STDIN")
        public List<Path> inputs = null;

        // The format will be sniffed if it is not provided.
        @CommandLine.Option(names = {"-f", "--format"},
                description = {"Phenopacket format.",
                        "Choose from: {${COMPLETION-CANDIDATES}}"})
        public PhenopacketFormat format = null;

        @CommandLine.Option(names = {"-e", "--element"},
                description = {"Top-level element.",
                        "Choose from {${COMPLETION-CANDIDATES}}",
                        "Default: phenopacket"})
        public PhenopacketElement element = null;

    }
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
        if (inputSection.inputs == null) {
            // The user did not set `-i | --input` option, assuming a single input is coming from STDIN.
            InputStream is = System.in;
            try {
                setFormatAndElement(is, schemaVersion);
                Message message = parser.parse(inputSection.format, inputSection.element, is);
                return List.of(new MessageAndPath(message, null));
            } catch (SniffException e) {
                System.err.println("Unable to detect input format from STDIN.\nConsider using the `--format` option.");
            } catch (IOException e) {
                System.err.println("Unable to read STDIN: " + e.getMessage() + "\nPlease check the input format.");
            }
            System.exit(1);
        } else {
            // Assuming a one or more input are provided via `-i | --input`.
            //
            // Picocli should ensure that `input` is never an empty list. `input` is `null` if no `-i` was supplied.
            assert !inputSection.inputs.isEmpty();

            List<MessageAndPath> messages = new ArrayList<>();
            for (Path input : inputSection.inputs) {
                try (InputStream is = new BufferedInputStream(Files.newInputStream(input))) {
                    setFormatAndElement(is, schemaVersion);
                    Message message = parser.parse(inputSection.format, inputSection.element, is);
                    messages.add(new MessageAndPath(message, input));
                } catch (SniffException e) {
                    System.err.printf("Unable to detect input format of %s.\nConsider using the `--format` option.%n", input.toAbsolutePath());
                    System.exit(1);
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
     * and {@link InputSection#element} items
     *
     * @throws IOException if I/O error happens
     * @throws SniffException if we cannot sniff the format
     */
    private void setFormatAndElement(InputStream is, PhenopacketSchemaVersion schemaVersion) throws IOException, SniffException {
        // Set format.
        PhenopacketFormat fmt = FormatSniffer.sniff(is);
        if (inputSection.format == null) {
            LOGGER.info("Input format was not provided, making an educated guess..");
            LOGGER.info("The input looks like a {} file", fmt);
            inputSection.format = fmt;
        } else {
            if (!inputSection.format.equals(fmt))
                // This can happen e.g. if processing multiple files at once but one turns out to be a different format.
                // We emit warning because this is likely not what the user intended and the code will likely explode
                // further downstream.
                LOGGER.warn("Input format is set to {} but the current input looks like a {}", inputSection.format, fmt);
        }

        // Set element.
        PhenopacketElement element = ElementSniffer.sniff(is, schemaVersion, inputSection.format);
        if (inputSection.element == null) {
            LOGGER.info("Input element type (-e | --element) was not provided, making an educated guess..");
            LOGGER.info("The input looks like a {} ", element);
            inputSection.element = element;
        } else {
            if (!inputSection.element.equals(element))
                // Let's go an extra mile and check for the user.
                LOGGER.warn("Input element is set to {} but the current input looks like a {}", inputSection.element, element);
        }
    }

    protected record MessageAndPath(Message message, Path path) {}

}
