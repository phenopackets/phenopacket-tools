package org.phenopackets.phenotools.builder.examples;

import org.phenopackets.schema.v2.Phenopacket;

public interface PhenopacketExample {


    String getJson();
    Phenopacket getPhenopacket();
}
