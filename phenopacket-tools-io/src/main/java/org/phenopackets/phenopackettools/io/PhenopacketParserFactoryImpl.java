package org.phenopackets.phenopackettools.io;

import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;
import org.phenopackets.phenopackettools.io.v1.V1PhenopacketParser;
import org.phenopackets.phenopackettools.io.v2.V2PhenopacketParser;

class PhenopacketParserFactoryImpl implements PhenopacketParserFactory {

    static final PhenopacketParserFactoryImpl INSTANCE = new PhenopacketParserFactoryImpl();

    @Override
    public PhenopacketParser forFormat(PhenopacketSchemaVersion version) throws PhenopacketParserFactoryException {
        return switch (version) {
            case V1 -> V1PhenopacketParser.INSTANCE;
            case V2 -> V2PhenopacketParser.INSTANCE;
        };
    }

}
