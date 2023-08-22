/**
 * The {@code uniq} package checks uniqueness of the phenotypic features - each feature must be specified exactly once.
 * <p>
 * Currently, the validators only perform naive check, largely ignoring the phenotypic feature
 * modifiers, except of the {@linkplain org.phenopackets.schema.v2.core.PhenotypicFeature#getExcluded()} field.
 * A phenotypic feature can only be present once within the present or excluded features.
 * Note, however, that other validators must check the extreme case of a feature being present and excluded
 * at the same time.
 */
package org.phenopackets.phenopackettools.validator.core.phenotype.uniq;