package org.phenopackets.phenopackettools.command;


import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.html.HtmlOutputter;
import org.phenopackets.schema.v2.Phenopacket;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Create an HTML file to represent a Phenopacket for human consumption.
 * This command does not check validity of the Phenopacket, but the code
 * can be combined with the validator if needed.
 * @author Peter N. Robinson
 */
@CommandLine.Command(name = "html",
        mixinStandardHelpOptions = true,
        description = "Format phenopacket as HTML.")
public class HtmlOutputCommand  implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "Input phenopacket file")
    private Path inPath;

    @CommandLine.Option(names = {"-o","--outfile"}, description = "name of output file (default ${DEFAULT})")
    private String outName = "phenopacket.html";

    @Override
    public Integer call() {
        var builder = Phenopacket.newBuilder();
        try (BufferedReader reader = Files.newBufferedReader(inPath)) {
            JsonFormat.parser().ignoringUnknownFields().merge(reader, builder);
        } catch (IOException e) {
            System.err.println("Error! Unable to read input file, " + e.getMessage() + "\nPlease check the format of file " + inPath);
            return 1;
        }
        var v2Phenopacket = builder.build();
        HtmlOutputter outputter = new HtmlOutputter(v2Phenopacket, outName);
        outputter.write();
        return 0;
    }
}
