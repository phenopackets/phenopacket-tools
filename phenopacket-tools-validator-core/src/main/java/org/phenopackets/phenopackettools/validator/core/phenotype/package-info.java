/**
 * Package with <em>off-the-shelf</em> validators that work with Human Phenotype Ontology (HPO).
 * <p>
 * The validators are exposed via a static factory class, there is a method for getting a validator for each top-level
 * Phenopacket Schema component.
 * <p>
 * The package includes a utility class with HPO {@link org.monarchinitiative.phenol.ontology.data.TermId}s
 * that correspond to organ systems
 * (e.g. {@link org.phenopackets.phenopackettools.validator.core.phenotype.HpoOrganSystems#EYE} for
 * <a href="https://hpo.jax.org/app/browse/term/HP:0000478">Abnormality of the eye</a>) that can be used
 * in combination with
 * {@link org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators.OrganSystem} validators.
 */
package org.phenopackets.phenopackettools.validator.core.phenotype;