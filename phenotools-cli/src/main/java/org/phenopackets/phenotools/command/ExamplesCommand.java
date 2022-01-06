package org.phenopackets.phenotools.command;

import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.phenotools.examples.PhenopacketExamples;
import org.phenopackets.schema.v2.Phenopacket;
import picocli.CommandLine.Command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Parameters;

@Command(name = "examples",
        mixinStandardHelpOptions = true,
        description = "Write example phenopackets to directory.")
public class ExamplesCommand implements Callable<Integer> {

    @Parameters(index = "0", arity = "0..1", description = "Output directory (default: current directory)")
    private Path outDir = Path.of(".");

    private void outputPhenopacket(String fileName, Phenopacket phenopacket) throws Exception {
        Path outDirectory = createOutdirectoryIfNeeded(outDir);
        Path path = outDirectory.resolve(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            String json = JsonFormat.printer().print(phenopacket);
            writer.write(json);
        } catch (IOException e) {
            throw new PhenotoolsRuntimeException(e.getMessage());
        }
    }

    private Path createOutdirectoryIfNeeded(Path outDirectory) {
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
    public Integer call() {
        try {
            outputPhenopacket("bethleham-myopathy.json", PhenopacketExamples.bethlemMyopathy());
            outputPhenopacket("thrombocytopenia2.json", PhenopacketExamples.thrombocytopenia2());
            outputPhenopacket("marfan.json", PhenopacketExamples.marfanSyndrome());
            outputPhenopacket("acute-myeloid-leukemia.json", PhenopacketExamples.acuteMyeloidLeukemia());
            outputPhenopacket("squamous-cell-esophageal-carcinoma.json", PhenopacketExamples.squamousCellEsophagealCarcinoma());
            outputPhenopacket("urothelial-cancer.json", PhenopacketExamples.urothelialCarcinoma());
            outputPhenopacket("covid.json", PhenopacketExamples.covid19());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
        return 0;
    }
}
