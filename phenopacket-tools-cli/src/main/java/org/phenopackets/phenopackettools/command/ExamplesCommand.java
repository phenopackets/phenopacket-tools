package org.phenopackets.phenopackettools.command;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import org.phenopackets.phenopackettools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.phenopackettools.examples.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "examples",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        description = "Write example phenopackets to a directory.")
public class ExamplesCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-o", "--output"},
            description = "Output directory (default: ${DEFAULT-VALUE})")
    public Path output = Path.of(".");


    @Override
    public Integer call() throws Exception {
        Path phenopacketDir = createADirectoryIfDoesNotExist(output.resolve("phenopackets"));
        Path familyDir = createADirectoryIfDoesNotExist(output.resolve("families"));
        Path cohortDir = createADirectoryIfDoesNotExist(output.resolve("cohorts"));

        try {
            // Phenopackets
            output(new AtaxiaWithVitaminEdeficiency().getPhenopacket(), phenopacketDir, "AVED");
            output(new BethlehamMyopathy().getPhenopacket(), phenopacketDir, "bethleham-myopathy");
            output(new Holoprosencephaly5().getPhenopacket(), phenopacketDir, "holoprosencephaly5");
            output(new Marfan().getPhenopacket(), phenopacketDir, "marfan");
            output(new NemalineMyopathyPrenatal().getPhenopacket(), phenopacketDir, "nemalineMyopathy");
            output(new Pseudoexfoliation().getPhenopacket(),  phenopacketDir,"pseudoexfoliation");
            output(new DuchenneExon51Deletion().getPhenopacket(), phenopacketDir, "duchenne");
            output(new SquamousCellCancer().getPhenopacket(), phenopacketDir, "squamous-cell-esophageal-carcinoma");
            output(new UrothelialCancer().getPhenopacket(), phenopacketDir, "urothelial-cancer");
            output(new Covid().getPhenopacket(), phenopacketDir, "covid");
            output(new Retinoblastoma().getPhenopacket(), phenopacketDir, "retinoblastoma");
            output(new WarburgMicroSyndrome().getPhenopacket(), phenopacketDir, "warburg-micro-syndrome");
            output(new SevereStatinInducedAutoimmuneMyopathy().getPhenopacket(), phenopacketDir, "statin-myopathy");

            // Families
            outputFamily(new FamilyWithPedigree().getFamily(), familyDir, "family");

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

    private static void output(Message phenopacket, Path outDir, String basename) {
        String yamlName = basename + ".yml";
        outputYamlPhenopacket(phenopacket, outDir, yamlName);
        String jsonName = basename + ".json";
        outputPhenopacket(phenopacket, outDir,jsonName);
    }

    private static void outputPhenopacket(Message phenopacket, Path outdir,String fileName) {
        outputJsonMessage(phenopacket, outdir, fileName);
    }

    private static void outputYamlPhenopacket(Message phenopacket, Path outdir, String fileName) {
        outputYamlMessage(phenopacket, outdir, fileName, "phenopacket");

    }

    private static void outputFamily(Message family, Path outDir, String basename) {
        String yamlName = basename + ".yml";
        outputYamlFamily(family, outDir, yamlName);
        String jsonName = basename + ".json";
        outputJsonFamily(family, outDir,jsonName);
    }

    private static void outputJsonFamily(Message family, Path outDir, String jsonName) {
        outputJsonMessage(family, outDir, jsonName);
    }

    private static void outputYamlFamily(Message family, Path outDir, String yamlName) {
        outputYamlMessage(family, outDir, yamlName, "family");
    }

    private static void outputJsonMessage(Message message, Path outDir, String fileName) {
        Path path = outDir.resolve(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            String json = JsonFormat.printer().print(message);
            writer.write(json);
        } catch (IOException e) {
            throw new PhenotoolsRuntimeException(e.getMessage());
        }
    }

    private static void outputYamlMessage(Message family, Path outDir, String yamlName, String messageName) {
        Path path = outDir.resolve(yamlName);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            String jsonString = JsonFormat.printer().print(family);
            JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
            JsonNode node = JsonNodeFactory.instance.objectNode().set(messageName, jsonNodeTree);
            mapper.writeValue(writer, node);
        } catch (IOException e) {
            throw new PhenotoolsRuntimeException(e.getMessage());
        }
    }




}
