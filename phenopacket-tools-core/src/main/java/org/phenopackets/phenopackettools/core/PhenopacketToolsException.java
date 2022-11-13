package org.phenopackets.phenopackettools.core;

/**
 * Base checked exception thrown by phenopacket-tools.
 */
public class PhenopacketToolsException extends Exception {

    public PhenopacketToolsException() {
        super();
    }

    public PhenopacketToolsException(String message) {
        super(message);
    }

    public PhenopacketToolsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhenopacketToolsException(Throwable cause) {
        super(cause);
    }

    protected PhenopacketToolsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
