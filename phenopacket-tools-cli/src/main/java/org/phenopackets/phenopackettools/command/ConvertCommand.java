package org.phenopackets.phenopackettools.command;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.converter.converters.V1ToV2Converter;
import org.phenopackets.phenopackettools.util.format.PhenopacketFormat;
import org.phenopackets.schema.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;

import java.io.*;

import static picocli.CommandLine.Option;

@Command(name = "convert",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        description = "Convert a v1.0 phenopacket to a v2.0 phenopacket.",
        footer = "Beware this process could be lossy!")
public class ConvertCommand extends SingleItemIOCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertCommand.class);

    @Option(names = {"-o", "--output-format"},
            description = "Output format.%nDefault: input format")
    public PhenopacketFormat outputFormat = null;

    @Option(names = {"--convert-variants"},
            description = "Convert variant data.%nDefault: ${DEFAULT-VALUE}")
    public boolean convertVariants = false;


    @Override
    public Integer call() {
        // (0) Print banner.
        printBanner();

        // (1) Read the input v1 message.
        Message message = readMessageOrExit(PhenopacketSchemaVersion.V1);

        // (2) Convert into v2 format
        if (convertVariants)
            LOGGER.debug("Converting variants");

        V1ToV2Converter converter = V1ToV2Converter.of(convertVariants);
        Message v2 = switch (element) {
            case PHENOPACKET -> converter.convertPhenopacket((Phenopacket) message);
            case FAMILY -> converter.convertFamily((Family) message);
            case COHORT -> converter.convertCohort((Cohort) message);
        };

        // (3) Write v2 into STDOUT using either the input format or the selected output format.
        OutputStream alwaysTheStandardOutput = System.out;
        if (outputFormat == null) {
            LOGGER.info("Output format (-o | --output-format) not provided, writing data in the input format `{}`", format);
            outputFormat = format;
        }
        try {
            writeMessage(v2, outputFormat, alwaysTheStandardOutput);
        } catch (IOException e) {
            System.err.println("Could not write phenopacket: " + e.getMessage());
            return 1;
        }

        // We're done!
        return 0;
    }

}
