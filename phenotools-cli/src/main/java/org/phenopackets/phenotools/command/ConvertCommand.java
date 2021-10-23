package org.phenopackets.phenotools.command;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.phenotools.converter.converters.PhenopacketConverter;
import picocli.CommandLine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(name = "convert", aliases = {"c"},
        mixinStandardHelpOptions = true,
        description = "convert phenopacket version")
public class ConvertCommand implements Runnable{

    @CommandLine.Option(names = {"-i","--input"}, description = "input file")
    private String input = "";

    @CommandLine.Option(names = {"-o", "--output"}, description = "output file")
    private String output = null;

    @CommandLine.Option(names = {"-pv","--phenopackets_version"}, description = "version to convert to (defaults to 2.0)")
    private String version = "2.0";

    @Override
    public void run() {
        if (input.isEmpty()) {
            System.err.println("[ERROR] No input file provided");
            return;
        }
        Path inPath = Path.of(input).toAbsolutePath();
        var builder = org.phenopackets.schema.v1.Phenopacket.newBuilder();
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(Files.newBufferedReader(inPath), builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var v1Phenopacket = builder.build();
        var version = v1Phenopacket.getMetaData().getPhenopacketSchemaVersion();
        if (! version.equals("1.0.0")) {
            throw new PhenotoolsRuntimeException("This script converts version 1.0.0 to version 2.0.0 but you passed version \"" + version + "\".");
        }
        var v2Phenopacket = PhenopacketConverter.convertToV2(v1Phenopacket);

        try {
            String json = JsonFormat.printer().print(v2Phenopacket);
            System.out.println(json);
            String v2name;
            if (this.output == null) {
                v2name = getV2FileName(this.input);
            } else {
                v2name = output;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(v2name));
            writer.write(json);
            writer.close();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not write V2 phenopacket: " + e.getMessage());
        }
    }

    /**
     * Add "-v2" to an appropriate place in the file name
     * @param input filename (possibly path) of v1 phenopacket
     * @return corresponding v2 filename (possibly path)
     */
    private String getV2FileName(String input) {
        File f = new File(input);
        String sep = File.separator;
        String [] components = input.split(sep);
        String base = components[components.length - 1];
        base = base.contains(".") ? base.replace(".", "-v2.") : base + "-v2";
        components[components.length - 1] = base;
        return String.join(sep, components);
    }
}
