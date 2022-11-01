package org.phenopackets.phenopackettools.io.base;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.io.PhenopacketParser;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class BasePhenopacketParser implements PhenopacketParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePhenopacketParser.class);

    @Override
    public Message parse(PhenopacketFormat format, PhenopacketElement element, InputStream is) throws IOException {
        return switch (format) {
            case PROTOBUF -> {
                LOGGER.debug("Reading protobuf message");
                yield readProtobufMessage(element, is);
            }
            case JSON -> {
                LOGGER.debug("Reading JSON message");
                yield readJsonMessage(element, is);
            }
            case YAML -> {
                LOGGER.debug("Reading YAML message");
                yield readYamlMessage(element, is);
            }
        };
    }

    protected abstract Message readProtobufMessage(PhenopacketElement element, InputStream is) throws IOException;

    private Message readJsonMessage(PhenopacketElement element, InputStream is) throws IOException {
        // Not closing the BufferedReader as the InputStream should be closed.
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Message.Builder builder = prepareBuilder(element);
        JsonFormat.parser().merge(reader, builder);
        return builder.build();
    }

    protected abstract Message.Builder prepareBuilder(PhenopacketElement element);

    protected abstract Message readYamlMessage(PhenopacketElement element, InputStream is) throws IOException;
}
