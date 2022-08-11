package org.phenopackets.phenopackettools.validator.core.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorInputException;
import org.phenopackets.schema.v2.Phenopacket;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * To do, add Default Cohort Ingestor etc
 */
public class DefaultPhenopacketIngestor implements Ingestor {

    private final JsonNode jsonNode;

    private final Message message;


    public DefaultPhenopacketIngestor(InputStream stream) throws PhenopacketValidatorInputException {
        byte[] content;
        try {
            content = stream.readAllBytes();
            ObjectMapper mapper = new ObjectMapper();
            this.jsonNode = mapper.readTree(content);
        } catch (IOException e) {
            throw new PhenopacketValidatorInputException("Could not read input stream: " + e.getMessage());
        }
        // read the protobuf message if possible
        // if we get here, content is not null;
        try {
            message = Phenopacket.parseFrom(content);
        } catch (InvalidProtocolBufferException e) {
            throw new PhenopacketValidatorInputException("Invalid Phenopacket Message: " + e.getMessage());
        }
    }

    public DefaultPhenopacketIngestor(byte[] content) throws PhenopacketValidatorInputException {
        if (content == null) {
            throw new PhenopacketValidatorInputException("input (\"byte[] content\" was null");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.jsonNode = mapper.readTree(content);
        } catch (IOException e) {
            throw new PhenopacketValidatorInputException("Could not read input stream: " + e.getMessage());
        }
        try {
            message = Phenopacket.parseFrom(content);
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
            message = Phenopacket.parseFrom(content);
        } catch (InvalidProtocolBufferException e) {
            throw new PhenopacketValidatorInputException("Invalid Phenopacket Message: " + e.getMessage());
        }
    }

    public Message fromString(String msg) throws PhenopacketValidatorInputException {
        try {
            Phenopacket.Builder builder = Phenopacket.newBuilder();
            TextFormat.Parser parser = TextFormat.Parser.newBuilder().build();
            parser.merge(msg, builder);
            return builder.build();
        } catch (TextFormat.ParseException e ) {
            throw new PhenopacketValidatorInputException("Could not decode Message: " + e.getMessage());
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

        message = fromString(jsonString);

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
