package org.phenopackets.phenopackettools.io;

import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;

public interface PhenopacketParserFactory {

    static PhenopacketParserFactory getInstance() {
        return PhenopacketParserFactoryImpl.INSTANCE;
    }

    /**
     * Get a {@link PhenopacketParser} to parse phenopacket with given {@link PhenopacketSchemaVersion}.
     *
     * @throws PhenopacketParserFactoryException if a {@link PhenopacketParser} for the given {@code version}
     * is not available
     */
    PhenopacketParser forFormat(PhenopacketSchemaVersion version) throws PhenopacketParserFactoryException;

}
