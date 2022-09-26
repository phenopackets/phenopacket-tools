package org.phenopackets.phenopackettools.validator.core;

import org.phenopackets.phenopackettools.validator.core.convert.CohortConverter;
import org.phenopackets.phenopackettools.validator.core.convert.FamilyConverter;
import org.phenopackets.phenopackettools.validator.core.convert.PhenopacketConverter;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

/**
 * Static class to get {@link PhenopacketFormatConverter}s for top-level elements of the Phenopacket schema.
 */
public class PhenopacketFormatConverters {

    private PhenopacketFormatConverters() {
        // static utility class
    }

    public static PhenopacketFormatConverter<PhenopacketOrBuilder> phenopacketConverter() {
        return PhenopacketConverter.getInstance();
    }

    public static PhenopacketFormatConverter<FamilyOrBuilder> familyConverter() {
        return FamilyConverter.getInstance();
    }

    public static PhenopacketFormatConverter<CohortOrBuilder> cohortConverter() {
        return CohortConverter.getInstance();
    }
}
