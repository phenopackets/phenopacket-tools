package org.phenopackets.phenopackettools.core;

/**
 * Base unchecked exception thrown by phenopacket-tools.
 */
public class PhenopacketToolsRuntimeException extends RuntimeException {

    public PhenopacketToolsRuntimeException() {
        super();
    }

    public PhenopacketToolsRuntimeException(String message) {
        super(message);
    }

    public PhenopacketToolsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhenopacketToolsRuntimeException(Throwable cause) {
        super(cause);
    }

    protected PhenopacketToolsRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
