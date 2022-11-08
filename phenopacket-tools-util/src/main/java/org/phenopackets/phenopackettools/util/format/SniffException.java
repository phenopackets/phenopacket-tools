package org.phenopackets.phenopackettools.util.format;

import org.phenopackets.phenopackettools.core.PhenopacketToolsException;

/**
 * A checked exception thrown in case of encountering some content sniffing issues.
 */
public class SniffException extends PhenopacketToolsException {

    public SniffException() {
        super();
    }

    public SniffException(String message) {
        super(message);
    }

    public SniffException(String message, Throwable cause) {
        super(message, cause);
    }

    public SniffException(Throwable cause) {
        super(cause);
    }

    protected SniffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
