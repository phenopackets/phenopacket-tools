package org.phenopackets.phenopackettools.io;

import com.google.protobuf.Message;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;

class PhenopacketPrinterFactoryImpl implements PhenopacketPrinterFactory {

    static final PhenopacketPrinterFactoryImpl INSTANCE = new PhenopacketPrinterFactoryImpl();

    @Override
    public <T extends Message> PhenopacketPrinter<T> forFormat(PhenopacketSchemaVersion schemaVersion,
                                                               PhenopacketElement element,
                                                               PhenopacketFormat format) throws PhenopacketPrinterFactoryException {
        return switch (format) {
            case PROTOBUF -> Message::writeTo;
            case JSON -> JsonPrinter.getInstance();
            case YAML -> NaiveYamlPrinter.getInstance();
        };
    }

}
