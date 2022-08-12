package org.phenopackets.phenopackettools.validator.core.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.phenopackets.schema.v2.Phenopacket;

public interface Ingestor {

    JsonNode jsonNode();
    Phenopacket message();

}
