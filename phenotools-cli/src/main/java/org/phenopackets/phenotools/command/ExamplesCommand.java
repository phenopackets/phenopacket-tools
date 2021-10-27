package org.phenopackets.phenotools.command;

import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.phenotools.examples.*;
import org.phenopackets.schema.v2.Phenopacket;
import picocli.CommandLine;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(name = "examples", aliases = {"e"},
        mixinStandardHelpOptions = true,
        description = "output example phenopackets to file")
public class ExamplesCommand implements Runnable {

    @CommandLine.Option(names = {"-o", "--outdir"}, description = "path to out directory (default: current directory)")
    public String outdir = ".";

    private void outputPhenopacket(String fileName, Phenopacket phenopacket) {
        Path outDirectory = createOutdirectoryIfNeeded(outdir);
        Path path = outDirectory.resolve(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            String json = JsonFormat.printer().print(phenopacket);
            writer.write(json);
        } catch (IOException e) {
            throw new PhenotoolsRuntimeException(e.getMessage());
        }
    }

    private Path createOutdirectoryIfNeeded(String outDir) {
        Path outDirectory = Path.of(outDir);
        if (Files.exists(outDirectory)) {
            return outDirectory;
        }
        try {
            return Files.createDirectory(outDirectory);
        } catch (IOException e) {
            // swallow
        }
        throw new PhenotoolsRuntimeException("Could not create directory " + outDir);
    }

    @Override
    public void run() {
        outputPhenopacket("bethleham-myopathy.json", PhenopacketExamples.bethlemMyopathy());
        outputPhenopacket("thrombocytopenia2.json", PhenopacketExamples.thrombocytopenia2());
        outputPhenopacket("marfan.json", PhenopacketExamples.marfanSyndrome());
        outputPhenopacket("acute-myeloid-leukemia.json", PhenopacketExamples.acuteMyeloidLeukemia());
        outputPhenopacket("squamous-cell-esophageal-carcinoma.json", PhenopacketExamples.squamousCellEsophagealCarcinoma());
        outputPhenopacket("urothelial-cancer.json", PhenopacketExamples.urothelialCarcinoma());
        outputPhenopacket("covid.json", PhenopacketExamples.covid19());
    }
}
