package org.phenopackets.phenopackettools.io;

import org.phenopackets.phenopackettools.core.PhenopacketToolsRuntimeException;

public class PhenopacketParserFactoryException extends PhenopacketToolsRuntimeException {

    public PhenopacketParserFactoryException() {
        super();
    }

    public PhenopacketParserFactoryException(String message) {
        super(message);
    }

    public PhenopacketParserFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhenopacketParserFactoryException(Throwable cause) {
        super(cause);
    }

    protected PhenopacketParserFactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
