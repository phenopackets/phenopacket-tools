package org.phenopackets.phenopackettools.validator.core.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorInputException;
import org.phenopackets.schema.v2.Phenopacket;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Parse a JsonNode object and a Message object from various input formats. This results in
 * two standard objects for the actual validation code.
 * @author peter.robinson@jax.org
 */
public class DefaultPhenopacketIngestor implements Ingestor {

    private final JsonNode jsonNode;

    private final Message message;


    public DefaultPhenopacketIngestor(InputStream stream) throws PhenopacketValidatorInputException {
        byte[] content;
        String jsonString;
        try {
            jsonString = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
           // content = stream.readAllBytes();
            ObjectMapper mapper = new ObjectMapper();
            this.jsonNode = mapper.readTree(jsonString);
        } catch (IOException e) {
            throw new PhenopacketValidatorInputException("Could not read input stream: " + e.getMessage());
        }
        // read the protobuf message if possible
        // if we get here, content is not null;
        try {
            Phenopacket.Builder builder = Phenopacket.newBuilder();
            JsonFormat.parser().merge(jsonString, builder);
            message = builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new PhenopacketValidatorInputException("Invalid Phenopacket Message: " + e.getMessage());
        }
    }

    public DefaultPhenopacketIngestor(byte[] content) throws PhenopacketValidatorInputException {
        if (content == null) {
            throw new PhenopacketValidatorInputException("input (\"byte[] content\" was null");
        }
        String jsonText = new String(content);
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.jsonNode = mapper.readTree(jsonText);
        } catch (IOException e) {
            throw new PhenopacketValidatorInputException("Could not read input stream: " + e.getMessage());
        }
        try {
            Phenopacket.Builder builder = Phenopacket.newBuilder();
            JsonFormat.parser().merge(jsonText, builder);
            message = builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new PhenopacketValidatorInputException("Invalid Phenopacket Message: " + e.getMessage());
        }
    }

    public DefaultPhenopacketIngestor(byte[] content, Charset charset) throws PhenopacketValidatorInputException {
        if (content == null) {
            throw new PhenopacketValidatorInputException("input (\"byte[] content\" was null");
        }
        // decode the bytes according to the provided charset
        String jsonString = new String(content, charset);
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.jsonNode = mapper.readTree(jsonString);
        } catch (IOException e) {
            throw new PhenopacketValidatorInputException("Could not read input stream: " + e.getMessage());
        }
        try {
            Phenopacket.Builder builder = Phenopacket.newBuilder();
            JsonFormat.parser().merge(Arrays.toString(content), builder);
            message = builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new PhenopacketValidatorInputException("Invalid Phenopacket Message: " + e.getMessage());
        }
    }

    public DefaultPhenopacketIngestor(String jsonString) throws PhenopacketValidatorInputException {
        if (jsonString == null) {
            throw new PhenopacketValidatorInputException("input (\"String jsonString\" was null");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.jsonNode = mapper.readTree(jsonString);
        } catch (IOException e) {
            throw new PhenopacketValidatorInputException("Could not read input stream: " + e.getMessage());
        }
        try {
            Phenopacket.Builder builder = Phenopacket.newBuilder();
            JsonFormat.parser().merge(jsonString, builder);
            message = builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new PhenopacketValidatorInputException("Invalid Phenopacket Message: " + e.getMessage());
        }
    }

    public DefaultPhenopacketIngestor(File file) throws PhenopacketValidatorInputException {
        if (file == null) {
            throw new PhenopacketValidatorInputException("Null file handle passed.");
        } else if (! file.isFile()) {
            throw new PhenopacketValidatorInputException(String.format("Invalid file path (%s) passed.",
                    file.getAbsolutePath()));
        }
        String jsonString;
        try {
            jsonString = Files.readString(file.toPath());

        } catch (IOException e) {
            throw new PhenopacketValidatorInputException("I/O error while reading " +
                    file.getName() +": " + e.getMessage());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.jsonNode = mapper.readTree(jsonString);
        } catch (IOException e) {
            throw new PhenopacketValidatorInputException("Could not read input stream: " + e.getMessage());
        }
        try {
            Phenopacket.Builder builder = Phenopacket.newBuilder();
            JsonFormat.parser().merge(jsonString, builder);
            message = builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new PhenopacketValidatorInputException("Invalid Phenopacket Message: " + e.getMessage());
        }
    }


    @Override
    public JsonNode jsonNode() {
        return this.jsonNode;
    }

    @Override
    public Message message() {
        return this.message;
    }

}
