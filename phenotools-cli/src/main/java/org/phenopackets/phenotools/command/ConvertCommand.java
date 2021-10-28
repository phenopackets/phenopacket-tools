package org.phenopackets.phenotools.command;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenotools.converter.converters.PhenopacketConverter;
import org.phenopackets.schema.v2.Phenopacket;
import picocli.CommandLine.Command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Option;
import static picocli.CommandLine.Parameters;

@Command(name = "convert", aliases = {"c"},
        mixinStandardHelpOptions = true,
        description = "convert phenopacket version")
public class ConvertCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "input phenopacket file")
    private Path input;

    @Option(names = {"-o", "--output"}, description = "output file")
    private Path output = null;

    @Option(names = {"-pv","--phenopackets_version"}, description = "version to convert to (defaults to 2.0)")
    private String version = "2.0";

    @Override
    public Integer call() throws Exception {
        if (input == null) {
            System.err.println("Error! No input file provided");
            return 1;
        }
        Path inPath = input.toAbsolutePath();
        if (!Files.exists(inPath)) {
            System.err.println("Error! No such input file: " + inPath);
            return 1;
        }
        var builder = org.phenopackets.schema.v1.Phenopacket.newBuilder();
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(Files.newBufferedReader(inPath), builder);
        } catch (IOException e) {
            System.err.println("Error! Unable to read input file, " + e.getMessage() + "\nPlease check the format of file " + inPath);
            return 1;
        }
        var v1Phenopacket = builder.build();
        var inputFileVersion = v1Phenopacket.getMetaData().getPhenopacketSchemaVersion();
        if (!(inputFileVersion.equals("1.0") || inputFileVersion.equals("1.0.0"))) {
            System.err.println("Error! This script converts version 1.0 to version 2.0 but the input file has version \"" + inputFileVersion + "\".");
            return 1;
        }
        var v2Phenopacket = PhenopacketConverter.toV2Phenopacket(v1Phenopacket);

        String json;
        try {
            json = toJson(v2Phenopacket);
        } catch (InvalidProtocolBufferException ex) {
            System.err.println("Unable to convert v" + version + " phenopacket to json. " + ex.getMessage());
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
                System.err.println("Could not write v" + version + " phenopacket to file " + output + " : " + e.getMessage());
                return 1;
            }
        }
        return 0;
    }

    private String toJson(Phenopacket v2Phenopacket) throws InvalidProtocolBufferException {
        return JsonFormat.printer().print(v2Phenopacket);
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
