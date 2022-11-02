package org.phenopackets.phenopackettools.io;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;

public interface PhenopacketPrinterFactory {

    static PhenopacketPrinterFactory getInstance() {
        return PhenopacketPrinterFactoryImpl.INSTANCE;
    }

    <T extends Message> PhenopacketPrinter<T> forFormat(PhenopacketSchemaVersion schemaVersion,
                                                        PhenopacketElement element,
                                                        PhenopacketFormat format) throws PhenopacketPrinterFactoryException;

}
