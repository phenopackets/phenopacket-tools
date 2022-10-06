package org.phenopackets.phenopackettools.util.format;

/**
 * An exception thrown when sniffing of the top-level element of Phenopacket schema cannot be performed.
 */
public class FormatSniffException extends Exception {

    public FormatSniffException() {
        super();
    }

    public FormatSniffException(String message) {
        super(message);
    }

    public FormatSniffException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormatSniffException(Throwable cause) {
        super(cause);
    }

}
