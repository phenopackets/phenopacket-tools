package org.phenopackets.phenotools.command;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenotools.converter.converters.PhenopacketConverter;
import picocli.CommandLine.Command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name = "convert", aliases = {"c"},
        mixinStandardHelpOptions = true,
        description = "convert phenopacket version")
public class ConvertCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "input phenopacket file")
    private File input;

    @Option(names = {"-o", "--output"}, description = "output file")
    private String output = null;

    @Option(names = {"-pv","--phenopackets_version"}, description = "version to convert to (defaults to 2.0)")
    private String version = "2.0";

    @Override
    public Integer call() throws Exception {
        if (input == null) {
            System.err.println("Error! No input file provided");
            return 1;
        }
        Path inPath = input.toPath().toAbsolutePath();
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

        try {
            String json = JsonFormat.printer().print(v2Phenopacket);
            if (output == null) {
                 System.out.println(json);
            } else {
                String v2name = Objects.requireNonNullElseGet(output, () -> getV2FileName(input.getName()));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(v2name))) {
                    writer.write(json);
                    writer.newLine();
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not write V2 phenopacket: " + e.getMessage());
            return 1;
        }
        return 0;
    }

    /**
     * Add "-v2" to an appropriate place in the file name
     * @param input filename (possibly path) of v1 phenopacket
     * @return corresponding v2 filename (possibly path)
     */
    private String getV2FileName(String input) {
        String sep = File.separator;
        String [] components = input.split(sep);
        String base = components[components.length - 1];
        base = base.contains(".") ? base.replace(".", "-v2.") : base + "-v2";
        components[components.length - 1] = base;
        return String.join(sep, components);
    }
}
