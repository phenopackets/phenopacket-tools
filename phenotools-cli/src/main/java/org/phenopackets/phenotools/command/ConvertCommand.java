package org.phenopackets.phenotools.command;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenotools.converter.converters.PhenopacketConverter;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(name = "convert", aliases = {"c"},
        mixinStandardHelpOptions = true,
        description = "convert phenopacket version")
public class ConvertCommand implements Runnable{

    @CommandLine.Option(names = {"-i","--input"}, description = "input file")
    String input = "";

    @CommandLine.Option(names = {"-v","--version"}, description = "version to convert to (defaults to 2.0)")
    String version = "2.0";

    @Override
    public void run() {
        if (input.isEmpty()) {
            System.out.println("Please provide and input file");
        }
        Path inPath = Path.of(input).toAbsolutePath();
        var builder = org.phenopackets.schema.v1.Phenopacket.newBuilder();
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(Files.newBufferedReader(inPath), builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var v2Phenopacket = PhenopacketConverter.convertToV2(builder.build());
        try {
            System.out.println(JsonFormat.printer().print(v2Phenopacket));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
