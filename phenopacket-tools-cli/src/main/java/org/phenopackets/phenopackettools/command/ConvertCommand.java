package org.phenopackets.phenopackettools.command;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.converter.converters.V1ToV2Converter;
import org.phenopackets.schema.v2.Phenopacket;
import picocli.CommandLine.Command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Option;
import static picocli.CommandLine.Parameters;

@Command(name = "convert",
        mixinStandardHelpOptions = true,
        description = "Convert a v1.0 phenopacket to a v2.0 phenopacket.",
        footer = "Beware this process could be lossy!")
public class ConvertCommand implements Callable<Integer> {

    @Parameters(index = "0", arity = "1", description = "Input phenopacket file")
    private Path input;

    @Option(names = {"-o", "--output"}, description = "Output file")
    public Path output = null;

    @Option(names = {"-ov","--out-version"}, description = "Version to convert to (default: ${DEFAULT-VALUE})")
    public String outVersion = "2.0";

    @Option(names = {"--convert-variants"}, description = "Convert variant data (default: ${DEFAULT-VALUE})")
    public boolean convertVariants = false;

    @Override
    public Integer call() {
        if (!Files.isRegularFile(input)) {
            System.err.println("Error! No such input file: " + input.toAbsolutePath());
            return 1;
        }
        var builder = org.phenopackets.schema.v1.Phenopacket.newBuilder();
        try (BufferedReader reader = Files.newBufferedReader(input)) {
            JsonFormat.parser().merge(reader, builder);
        } catch (IOException e) {
            System.err.println("Error! Unable to read input file, " + e.getMessage() + "\nPlease check the format of file " + input.toAbsolutePath());
            return 1;
        }
        var v1Phenopacket = builder.build();
        var inputFileVersion = v1Phenopacket.getMetaData().getPhenopacketSchemaVersion();
        if (!(inputFileVersion.equals("1.0") || inputFileVersion.equals("1.0.0"))) {
            System.err.println("Error! This script converts version 1.0 to version 2.0 but the input file has version \"" + inputFileVersion + "\".");
            return 1;
        }

        V1ToV2Converter converter = V1ToV2Converter.of(convertVariants);
        Phenopacket v2 = converter.convertPhenopacket(v1Phenopacket);

        String json;
        try {
            json = JsonFormat.printer().print(v2);
        } catch (InvalidProtocolBufferException ex) {
            System.err.println("Unable to convert v" + outVersion + " phenopacket to json. " + ex.getMessage());
            return 1;
        }
        if (output == null) {
            System.out.println(json);
        } else {
//            Path v2File = Objects.requireNonNullElseGet(output, () -> getV2FileName(input));
            try (BufferedWriter writer = Files.newBufferedWriter(output)) {
                writer.write(json);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Could not write v" + outVersion + " phenopacket to file " + output + " : " + e.getMessage());
                return 1;
            }
        }
        return 0;
    }

    /**
     * Add "-v2" to an appropriate place in the file name
     * @param input filename (possibly path) of v1 phenopacket
     * @return corresponding v2 filename (possibly path)
     */
    private Path getV2FileName(Path input) {
        String inputFileName = input.getFileName().toString();
        String v2FileName = inputFileName.contains(".") ? inputFileName.replace(".", "-v2.") : inputFileName + "-v2";
        return Path.of(v2FileName);
    }
}
