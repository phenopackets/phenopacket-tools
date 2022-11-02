package org.phenopackets.phenopackettools.command;

import com.google.protobuf.Message;

import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;
import org.phenopackets.phenopackettools.core.PhenopacketToolsRuntimeException;
import org.phenopackets.phenopackettools.examples.*;
import org.phenopackets.phenopackettools.io.PhenopacketPrinter;
import org.phenopackets.phenopackettools.io.PhenopacketPrinterFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Command(name = "examples",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        description = "Write example phenopackets to a directory.")
public class ExamplesCommand extends BaseCommand {

    @CommandLine.Option(names = {"-o", "--output"},
            description = "Output directory (default: ${DEFAULT-VALUE})")
    public Path output = Path.of(".");

    private final PhenopacketPrinter jsonPrinter;
    private final PhenopacketPrinter yamlPrinter;

    public ExamplesCommand() {
        PhenopacketPrinterFactory factory = PhenopacketPrinterFactory.getInstance();
        jsonPrinter = factory.forFormat(PhenopacketSchemaVersion.V2, PhenopacketFormat.JSON);
        yamlPrinter = factory.forFormat(PhenopacketSchemaVersion.V2, PhenopacketFormat.YAML);
     }

    @Override
    protected Integer execute() {
        try {
            Path phenopacketDir = createADirectoryIfDoesNotExist(output.resolve("phenopackets"));
            Path familyDir = createADirectoryIfDoesNotExist(output.resolve("families"));
            Path cohortDir = createADirectoryIfDoesNotExist(output.resolve("cohorts"));

            // Phenopackets
            printJsonAndYaml(new AtaxiaWithVitaminEdeficiency().getPhenopacket(), phenopacketDir, "AVED");
            printJsonAndYaml(new BethlehamMyopathy().getPhenopacket(), phenopacketDir, "bethleham-myopathy");
            printJsonAndYaml(new Holoprosencephaly5().getPhenopacket(), phenopacketDir, "holoprosencephaly5");
            printJsonAndYaml(new Marfan().getPhenopacket(), phenopacketDir, "marfan");
            printJsonAndYaml(new NemalineMyopathyPrenatal().getPhenopacket(), phenopacketDir, "nemalineMyopathy");
            printJsonAndYaml(new Pseudoexfoliation().getPhenopacket(), phenopacketDir, "pseudoexfoliation");
            printJsonAndYaml(new DuchenneExon51Deletion().getPhenopacket(), phenopacketDir, "duchenne");
            printJsonAndYaml(new SquamousCellCancer().getPhenopacket(), phenopacketDir, "squamous-cell-esophageal-carcinoma");
            printJsonAndYaml(new UrothelialCancer().getPhenopacket(), phenopacketDir, "urothelial-cancer");
            printJsonAndYaml(new Covid().getPhenopacket(), phenopacketDir, "covid");
            printJsonAndYaml(new Retinoblastoma().getPhenopacket(), phenopacketDir, "retinoblastoma");
            printJsonAndYaml(new WarburgMicroSyndrome().getPhenopacket(), phenopacketDir, "warburg-micro-syndrome");
            printJsonAndYaml(new SevereStatinInducedAutoimmuneMyopathy().getPhenopacket(), phenopacketDir, "statin-myopathy");

            // Families
            printJsonAndYaml(new FamilyWithPedigree().getFamily(), familyDir, "family");

            // Cohorts
            // TODO - write a cohort

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    private static Path createADirectoryIfDoesNotExist(Path path) throws IOException {
        return Files.exists(path)
                ? path
                : Files.createDirectories(path);
    }

    private void printJsonAndYaml(Message message, Path outDir, String basename) {
        Path jsonPath = outDir.resolve(basename + ".json");
        printJsonMessage(message, jsonPath);

        Path yamlPath = outDir.resolve(basename + ".yml");
        printYamlMessage(message, yamlPath);
    }

    private void printJsonMessage(Message message, Path path) {
        try {
            jsonPrinter.print(message, path);
        } catch (IOException e) {
            throw new PhenopacketToolsRuntimeException(e);
        }
    }

    private void printYamlMessage(Message message, Path path) {
        try {
            yamlPrinter.print(message, path);
        } catch (IOException e) {
            throw new PhenopacketToolsRuntimeException(e);
        }
    }

}
