package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.MessageOrBuilder;

/**
 * The implementors allow to convert between formats of the top-level elements of Phenopacket schema.
 * @param <T>
 */
public interface PhenopacketFormatConverter<T extends MessageOrBuilder> {

    /**
     * Convert the {@code payload} into Protobuf representation of a top-level element.
     *
     * @throws ConversionException in case the conversion fails.
     */
    T toItem(byte[] payload) throws ConversionException;

    /**
     * Convert JSON {@code payload} into Protobuf representation of a top-level element.
     *
     * @throws ConversionException in case the conversion fails.
     */
    T toItem(String payload) throws ConversionException;

    /**
     * Convert Protobuf representation of a top-level element into a JSON string.
     */
    String toJson(T item);

    /**
     * Convert the {@code payload} into a JSON string.
     *
     * @throws ConversionException if the conversion fails.
     */
    default String toJson(byte[] payload) throws ConversionException {
        return toJson(toItem(payload));
    }

}
