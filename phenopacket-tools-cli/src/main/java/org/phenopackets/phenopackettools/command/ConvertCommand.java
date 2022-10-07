package org.phenopackets.phenopackettools.command;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.converter.converters.V1ToV2Converter;
import org.phenopackets.phenopackettools.util.format.FormatSniffException;
import org.phenopackets.phenopackettools.util.format.PhenopacketFormat;
import org.phenopackets.schema.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;

import java.io.*;

import static picocli.CommandLine.Option;

@Command(name = "convert",
        mixinStandardHelpOptions = true,
        description = "Convert a v1.0 phenopacket to a v2.0 phenopacket.",
        footer = "Beware this process could be lossy!")
public class ConvertCommand extends BasePTCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertCommand.class);

    @Option(names = {"--out-format"},
            description = "Output format (default: input format)")
    public PhenopacketFormat outputFormat = null;

    @Option(names = {"-ov", "--out-version"},
            description = "Version to convert to (default: ${DEFAULT-VALUE})")
    public String outVersion = "2.0";

    @Option(names = {"--convert-variants"},
            description = "Convert variant data (default: ${DEFAULT-VALUE})")
    public boolean convertVariants = false;


    @Override
    public Integer call() {
        // (0) Check the inputs.
        if (!outVersion.matches("2(\\.0)?(\\.0)?")) {
            System.err.printf("Conversion to %s is not supported%n", outVersion);
            return 1;
        }

        // (1) Read the input v1 message.
        Message message;
        try {
            message = readInputMessage();
        } catch (FormatSniffException e) {
            System.err.printf("Unable to detect input format of %s.\nConsider using the `--format` option.%n", input.toAbsolutePath());
            return 1;
        } catch (IOException e) {
            System.err.println("Unable to read input file, " + e.getMessage() + "\nPlease check the format of file " + input.toAbsolutePath());
            return 1;
        }

        // (2) Convert into v2 format
        if (convertVariants)
            LOGGER.debug("Converting variants");

        V1ToV2Converter converter = V1ToV2Converter.of(convertVariants);
        Message v2 = switch (element) {
            case PHENOPACKET -> converter.convertPhenopacket((Phenopacket) message);
            case FAMILY -> converter.convertFamily((Family) message);
            case COHORT -> converter.convertCohort((Cohort) message);
        };

        // (3) Write v2 into output using either the input format or the selected output format.
        if (outputFormat == null)
            outputFormat = format;
        try {
            writeV2Message(v2, outputFormat);
        } catch (IOException e) {
            System.err.println("Could not write v" + outVersion + " phenopacket to file " + output + " : " + e.getMessage());
            return 1;
        }

        // We're done!
        return 0;
    }

}
