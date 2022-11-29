package org.phenopackets.phenopackettools.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.util.print.PhenopacketPrintUtil;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A naive implementation of YAML printer that first prints the {@link MessageOrBuilder} into a JSON string,
 * then decodes the string into {@link JsonNode} and prints as YAML document.
 * <p>
 * This is, of course, not efficient. However, it works OK as a prototype printer.
 */
class NaiveYamlPrinter implements PhenopacketPrinter {

    private static final NaiveYamlPrinter INSTANCE = new NaiveYamlPrinter();

    static NaiveYamlPrinter getInstance() {
        return INSTANCE;
    }

    private final ObjectMapper jsonMapper;
    private final ObjectMapper yamlMapper;

    private NaiveYamlPrinter() {
        jsonMapper = new ObjectMapper();
        yamlMapper = YAMLMapper.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();
    }

    @Override
    public void print(Message message, OutputStream os) throws IOException {
        String jsonString = PhenopacketPrintUtil.getPrinter().print(message);
        JsonNode jsonNode = jsonMapper.readTree(jsonString);
        yamlMapper.writeValue(os, jsonNode);
    }

}
