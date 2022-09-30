/**
 * The package provides {@link org.phenopackets.phenopackettools.validator.core.PhenopacketValidator}s
 * for <em>metadata validation</em> - checking that {@link org.phenopackets.schema.v2.core.MetaData} of
 * the top-level elements of the Phenopacket schema contains a {@link org.phenopackets.schema.v2.core.Resource}
 * for all {@link org.phenopackets.schema.v2.core.OntologyClass}es of the element.
 * <p>
 * Use {@link org.phenopackets.phenopackettools.validator.core.metadata.MetaDataValidators} to get
 * a {@link org.phenopackets.phenopackettools.validator.core.PhenopacketValidator}
 * for validating {@link org.phenopackets.schema.v2.Phenopacket},
 * {@link org.phenopackets.schema.v2.Family}, or {@link org.phenopackets.schema.v2.Cohort}.
 */
package org.phenopackets.phenopackettools.validator.core.metadata;