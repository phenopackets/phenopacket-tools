package org.phenopackets.phenopackettools.io;

import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;

/**
 * The implementors provide {@link PhenopacketPrinter}s for serializing top-level phenopacket elements
 * into {@link PhenopacketFormat} using {@link PhenopacketSchemaVersion}.
 */
public interface PhenopacketPrinterFactory {

    static PhenopacketPrinterFactory getInstance() {
        return PhenopacketPrinterFactoryImpl.INSTANCE;
    }

    PhenopacketPrinter forFormat(PhenopacketSchemaVersion schemaVersion,
                                 PhenopacketFormat format) throws PhenopacketPrinterFactoryException;

}
