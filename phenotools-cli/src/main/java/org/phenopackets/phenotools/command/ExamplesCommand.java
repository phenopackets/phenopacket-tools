package org.phenopackets.phenotools.command;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.protobuf.util.JsonFormat;

import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.phenotools.examples.PhenopacketExamples;
import org.phenopackets.phenotools.examples.WarburgMicroSyndrome;
import org.phenopackets.schema.v2.Phenopacket;
import picocli.CommandLine;
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

    @CommandLine.Option(names = {"-o", "--output"}, description = "Output directory")
    private String output = "examples";

    private Path outDir = null;



    private void outputPhenopacket(Phenopacket phenopacket, Path outdir,String fileName) {
        Path path = outdir.resolve(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            String json = JsonFormat.printer().print(phenopacket);
            writer.write(json);
        } catch (IOException e) {
            throw new PhenotoolsRuntimeException(e.getMessage());
        }
    }

    private void outputYamlPhenopacket(Phenopacket phenopacket, Path outdir, String fileName) {
        Path path = outdir.resolve(fileName);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            String jsonString = JsonFormat.printer().print(phenopacket);
            JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
            JsonNode node = JsonNodeFactory.instance.objectNode().set("phenopacket", jsonNodeTree);
            mapper.writeValue(writer, node);
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
        return outputAllPhenopackets();
    }



    private int output(Phenopacket phenopacket, Path outDir, String basename) {
        String yamlName = basename + ".yml";
        outputYamlPhenopacket(phenopacket, outDir, yamlName);
        String jsonName = basename + ".json";
        outputPhenopacket(phenopacket, outDir,jsonName);
        return 0;
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
        return 0;
    }



}
