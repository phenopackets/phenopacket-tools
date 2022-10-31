package org.phenopackets.phenopackettools.core;

/**
 * An enum with currently supported Phenopacket schema versions.
 */
public enum PhenopacketSchemaVersion {

    /**
     * The version 1 of the GA4GH Phenopacket schema released in 2019 to elicit community response.
     * The {@code V1} has been deprecated in favor of {@link #V2}.
     */
    V1,
    /**
     * The version 2 of the GA4GH Phenopacket schema. This is the current version.
     */
    V2

}
