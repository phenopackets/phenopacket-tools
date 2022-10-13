package org.phenopackets.phenopackettools.writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.writer.ValidationResultsAndPath;
import org.phenopackets.phenopackettools.validator.core.writer.ValidationResultsWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Write {@link org.phenopackets.phenopackettools.validator.core.ValidationResults} into provided {@link OutputStream}
 * in CSV format, including comments with validation metadata.
 */
public class CSVValidationResultsWriter implements ValidationResultsWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVValidationResultsWriter.class);

    private final OutputStream os;
    private final String phenopacketToolsVersion;
    private final LocalDateTime dateTime;

    /**
     * Create the writer using a given {@link OutputStream}. Note that the {@link OutputStream} is <em>not</em> closed.
     *
     * @param os                      where to write to
     * @param phenopacketToolsVersion phenopacket tools version
     * @param dateTime
     */
    public CSVValidationResultsWriter(OutputStream os, String phenopacketToolsVersion, LocalDateTime dateTime) {
        this.os = os;
        this.phenopacketToolsVersion = phenopacketToolsVersion;
        this.dateTime = dateTime;
    }

    @Override
    public void writeValidationResults(List<ValidatorInfo> validators, List<ValidationResultsAndPath> results) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));

        try {
            CSVPrinter printer = CSVFormat.DEFAULT.builder()
                    .setCommentMarker('#')
                    .build()
                    .print(writer);
            printHeader(validators, printer);
            printValidationResults(results, printer);
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                LOGGER.warn("Error during flushing the writer: {}", e.getMessage(), e);
            }
        }
    }

    private void printHeader(List<ValidatorInfo> results, CSVPrinter printer) throws IOException {
        // Print header
        printer.printComment("phenopacket-tools validate %s".formatted(phenopacketToolsVersion));
        printer.printComment("date=%s".formatted(dateTime));

        // Print validators
        for (ValidatorInfo validator : results) {
            printer.printComment("validator_id=%s;validator_name=%s;description=%s".formatted(validator.validatorId(), validator.validatorName(), validator.description()));
        }
    }

    private static void printValidationResults(List<ValidationResultsAndPath> results, CSVPrinter printer) throws IOException {
        // Header
        printer.printRecord("PATH", "LEVEL", "VALIDATOR_ID", "CATEGORY", "MESSAGE");
        // Validation results
        for (ValidationResultsAndPath rp : results) {
            String path = rp.path() == null ? "-" : rp.path().toAbsolutePath().toString();
            for (ValidationResult result : rp.results().validationResults()) {
                printer.print(path);
                printer.print(result.level());
                printer.print(result.validatorInfo().validatorId());
                printer.print(result.category());
                printer.print(result.message());
                printer.println();
            }
        }
    }
}
