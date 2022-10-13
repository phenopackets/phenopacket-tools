package org.phenopackets.phenopackettools.validator.core.writer;

import org.phenopackets.phenopackettools.validator.core.ValidationResults;

import java.nio.file.Path;

/**
 * A record to use for writing {@link ValidationResults} by {@link ValidationResultsWriter}.
 *
 * @param results validation results to be written.
 * @param path source of the input data or {@code null} if the input was received from standard input.
 */
public record ValidationResultsAndPath(ValidationResults results, Path path) {
}
