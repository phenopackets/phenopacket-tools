package org.phenopackets.phenopackettools.converter.converters;

import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;

/**
 * Convert top-level elements of the phenopacket schema from <em>v1</em> to <em>v2</em> format.
 * <p>
 * <em>WARNING</em>: the conversion can be lossy. The default {@link V1ToV2Converter} implementation attempts to convert
 * {@link org.phenopackets.schema.v1.Phenopacket} variants into {@link org.phenopackets.schema.v2.core.Interpretation},
 * assuming all {@link org.phenopackets.schema.v1.core.Variant}s are
 * {@link org.phenopackets.schema.v2.core.GenomicInterpretation.InterpretationStatus#CAUSATIVE}. For this to work,
 * there must be exactly one {@link org.phenopackets.schema.v1.core.Disease} in the phenopacket, otherwise
 * a {@link org.phenopackets.phenopackettools.builder.exceptions.PhenotoolsRuntimeException} is thrown.
 */
public interface V1ToV2Converter {

    static V1ToV2Converter of(boolean convertVariants) {
        return new V1ToV2ConverterImpl(convertVariants);
    }

    /**
     * Convert <em>v1</em> {@link org.phenopackets.schema.v1.Phenopacket} to <em>v2</em> {@link Phenopacket}.
     */
    Phenopacket convertPhenopacket(org.phenopackets.schema.v1.Phenopacket phenopacket);

    /**
     * Convert <em>v1</em> {@link org.phenopackets.schema.v1.Family} to <em>v2</em> {@link Family}.
     */
    Family convertFamily(org.phenopackets.schema.v1.Family family);

    /**
     * Convert <em>v1</em> {@link org.phenopackets.schema.v1.Cohort} to <em>v2</em> {@link Cohort}.
     */
    Cohort convertCohort(org.phenopackets.schema.v1.Cohort cohort);

}
