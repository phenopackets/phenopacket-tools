package org.phenopackets.phenopackettools.io;

import org.phenopackets.phenopackettools.core.PhenopacketToolsRuntimeException;

public class PhenopacketPrinterFactoryException extends PhenopacketToolsRuntimeException {

    public PhenopacketPrinterFactoryException() {
        super();
    }

    public PhenopacketPrinterFactoryException(String message) {
        super(message);
    }

    public PhenopacketPrinterFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhenopacketPrinterFactoryException(Throwable cause) {
        super(cause);
    }

    protected PhenopacketPrinterFactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
