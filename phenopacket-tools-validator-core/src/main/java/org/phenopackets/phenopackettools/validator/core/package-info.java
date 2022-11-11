/**
 * The package provides APIs and default implementations of phenopacket validation.
 * <p>
 * <h3>Actors</h3>
 * This section describes the actors of the validation workflow (the classes for representing behavior
 * for <em>"doing stuff"</em>), starting from the basic elements.
 * <p>
 * {@link org.phenopackets.phenopackettools.validator.core.PhenopacketFormatConverters} is a static factory class
 * for providing {@link org.phenopackets.phenopackettools.validator.core.PhenopacketFormatConverter}s to convert
 * the top-level elements of Phenopacket Schema between the supported
 * {@link org.phenopackets.phenopackettools.core.PhenopacketFormat}s.
 * <p>
 * {@link org.phenopackets.phenopackettools.validator.core.PhenopacketValidator} represents a single step
 * of the validation workflow.
 * <p>
 * {@link org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner} applies
 * the {@link org.phenopackets.phenopackettools.validator.core.PhenopacketValidator}s of the validation workflow in
 * the correct order, ensuring the <em>base</em> validation is always run as first.
 * The {@link org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner} validates
 * a top-level element.
 * <p>
 * The {@link org.phenopackets.phenopackettools.validator.core.ValidationWorkflowDispatcher} exposes methods
 * for validating all top-level elements of the Phenopacket Schema.
 *
 * <h3>Value objects</h3>
 * The package includes stateful objects with no complex behavior starting from the most complex objects.
 * <p>
 * The {@link org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner}
 * and {@link org.phenopackets.phenopackettools.validator.core.ValidationWorkflowDispatcher} return
 * {@link org.phenopackets.phenopackettools.validator.core.ValidationResults}, a container with results
 * of the validation workflow.
 * <p>
 * {@link org.phenopackets.phenopackettools.validator.core.ValidationResult} contains results of
 * a single validation step.
 * <p>
 * {@link org.phenopackets.phenopackettools.validator.core.ValidatorInfo} describes
 * the {@link org.phenopackets.phenopackettools.validator.core.PhenopacketValidator}.
 * <p>
 * {@link org.phenopackets.phenopackettools.validator.core.ValidationLevel}
 */
package org.phenopackets.phenopackettools.validator.core;