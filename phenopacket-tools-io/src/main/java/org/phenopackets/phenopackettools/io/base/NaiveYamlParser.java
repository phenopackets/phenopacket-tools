package org.phenopackets.phenopackettools.io.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.io.InputStream;

/**
 * A naive and inefficient implementation of YAML -> {@link Message} parsing that first maps YAML into JSON String
 * and then decodes the JSON into {@link Message}.
 */
class NaiveYamlParser {

    private static final JsonFormat.Parser JSON_PARSER = JsonFormat.parser();

    static final NaiveYamlParser INSTANCE = new NaiveYamlParser();
    private final ObjectMapper yamlMapper;
    private final ObjectMapper jsonMapper;
    private NaiveYamlParser() {
        yamlMapper = new YAMLMapper();
        jsonMapper = new ObjectMapper();
    }

    void deserializeYamlMessage(InputStream is, Message.Builder builder) throws IOException {
        JsonNode node = yamlMapper.readTree(is);
        String jsonString = jsonMapper.writeValueAsString(node);
        JSON_PARSER.merge(jsonString, builder);
    }
}
