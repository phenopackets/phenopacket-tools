package org.phenopackets.phenopackettools.validator.core.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.protobuf.Message;

public interface Ingestor {

    JsonNode jsonNode();
    Message message();

}
