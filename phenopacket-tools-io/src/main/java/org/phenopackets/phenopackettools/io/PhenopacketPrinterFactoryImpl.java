package org.phenopackets.phenopackettools.io;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;

class PhenopacketPrinterFactoryImpl implements PhenopacketPrinterFactory {

    static final PhenopacketPrinterFactoryImpl INSTANCE = new PhenopacketPrinterFactoryImpl();

    @Override
    public PhenopacketPrinter forFormat(PhenopacketSchemaVersion schemaVersion, PhenopacketFormat format) throws PhenopacketPrinterFactoryException {
        return switch (format) {
            case PROTOBUF -> Message::writeTo;
            case JSON -> JsonPrinter.getInstance();
            case YAML -> NaiveYamlPrinter.getInstance();
        };
    }

}
