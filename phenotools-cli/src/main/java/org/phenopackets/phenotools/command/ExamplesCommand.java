package org.phenopackets.phenotools.command;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.phenotools.examples.FamilyWithPedigree;
import org.phenopackets.phenotools.examples.PhenopacketExamples;
import org.phenopackets.phenotools.examples.WarburgMicroSyndrome;
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
        description = "Write example phenopackets to directory.")
public class ExamplesCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-o", "--output"}, description = "Output directory")
    private String output = "examples";

    private Path outDir = null;



    private void outputPhenopacket(Message phenopacket, Path outdir,String fileName) {
        outputJsonMessage(phenopacket, outdir, fileName);
    }

    private void outputYamlPhenopacket(Message phenopacket, Path outdir, String fileName) {
        outputYamlMessage(phenopacket, outdir, fileName, "phenopacket");

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
        throw new PhenotoolsRuntimeException("Could not create directory " + outDirectory);
    }

    @Override
    public Integer call() {
        return outputAllPhenopackets();
    }



    private int output(Message phenopacket, Path outDir, String basename) {
        String yamlName = basename + ".yml";
        outputYamlPhenopacket(phenopacket, outDir, yamlName);
        String jsonName = basename + ".json";
        outputPhenopacket(phenopacket, outDir,jsonName);
        return 0;
    }


    private void outputFamily(Message family, Path outDir, String basename) {
        String yamlName = basename + ".yml";
        outputYamlFamily(family, outDir, yamlName);
        String jsonName = basename + ".json";
        outputJsonFamily(family, outDir,jsonName);
    }

    private void outputJsonFamily(Message family, Path outDir, String jsonName) {
    }

    private void outputYamlFamily(Message family, Path outDir, String yamlName) {
        outputYamlMessage(family, outDir, yamlName, "family");
    }

    private void outputJsonMessage(Message message, Path outDir, String fileName) {
        Path path = outDir.resolve(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            String json = JsonFormat.printer().print(message);
            writer.write(json);
        } catch (IOException e) {
            throw new PhenotoolsRuntimeException(e.getMessage());
        }
    }

    private void outputYamlMessage(Message family, Path outDir, String yamlName, String messageName) {
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


    private int outputAllPhenopackets() {
        Path outDir = Path.of(output);
        Path outDirectory = createOutdirectoryIfNeeded(outDir);
        try {
            output(PhenopacketExamples.bethlemMyopathy(), outDirectory, "bethleham-myopathy");
            output(PhenopacketExamples.thrombocytopenia2(), outDirectory, "thrombocytopenia2");
            output(PhenopacketExamples.marfanSyndrome(), outDirectory, "marfan");
            output(PhenopacketExamples.acuteMyeloidLeukemia(), outDirectory, "nemalineMyopathy");
            output(PhenopacketExamples.squamousCellEsophagealCarcinoma(), outDirectory, "squamous-cell-esophageal-carcinoma");
            output(PhenopacketExamples.urothelialCarcinoma(), outDirectory, "urothelial-cancer");
            output(PhenopacketExamples.covid19(), outDirectory, "covid");
            output(PhenopacketExamples.retinoblastoma(), outDirectory, "retinoblastoma");
            output(new WarburgMicroSyndrome().getPhenopacket(), outDirectory, "warburg-micro-syndrome");
            outputFamily(new FamilyWithPedigree().getFamily(), outDirectory, "family");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
        return 0;
    }



}
