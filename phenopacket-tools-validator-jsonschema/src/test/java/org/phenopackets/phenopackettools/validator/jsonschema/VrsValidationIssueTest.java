package org.phenopackets.phenopackettools.validator.jsonschema;

import com.google.protobuf.Timestamp;
import org.ga4gh.vrs.v1.*;
import org.ga4gh.vrs.v1.Number;
import org.ga4gh.vrsatile.v1.MoleculeContext;
import org.ga4gh.vrsatile.v1.VariationDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.ValidationResults;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.*;

@Disabled("To be run manually.")
public class VrsValidationIssueTest {

    private JsonSchemaValidationWorkflowRunner<PhenopacketOrBuilder> runner;

    @BeforeEach
    public void setUp() {
        runner = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                .build();
    }

    @Test
    public void minimalPhenopacketYieldsNoErrors() {
        Phenopacket pp = minimalPhenopacket();

        ValidationResults results = runner.validate(pp);

        System.err.printf("Found %d errors%n", results.validationResults().size());
        results.validationResults()
                .forEach(System.err::println);
    }

    @Test
    public void variationDescriptorWithoutVariantYieldsNoErrors() {
        VariationDescriptor variationDescriptor = VariationDescriptor.newBuilder()
                .setId("variation-descriptor-id")
                .setMoleculeContext(MoleculeContext.genomic)
                .build();

        Phenopacket pp = minimalPhenopacket().toBuilder()
                .addInterpretations(exampleInterpretation(variationDescriptor))
                .build();

        ValidationResults results = runner.validate(pp);

        System.err.printf("Found %d errors%n", results.validationResults().size());
        results.validationResults()
                .forEach(System.err::println);
    }

    @Test
    public void variationWithAlleleDoesNotValidate() {
        VariationDescriptor variationDescriptor = VariationDescriptor.newBuilder()
                .setId("variation-descriptor-id")
                .setMoleculeContext(MoleculeContext.genomic)
                .setVariation(exampleVariationWithAllele())
                .build();

        Phenopacket pp = minimalPhenopacket().toBuilder()
                .addInterpretations(exampleInterpretation(variationDescriptor))
                .build();

        ValidationResults results = runner.validate(pp);
        // TODO - why does this fail?

        System.err.printf("Found %d errors%n", results.validationResults().size());
        results.validationResults()
                .forEach(System.err::println);
    }

    private static Variation exampleVariationWithAllele() {
        return Variation.newBuilder()
                .setAllele(Allele.newBuilder()
                        .setSequenceLocation(SequenceLocation.newBuilder()
                                .setSequenceId("refseq:NC_000013.11")
                                .setSequenceInterval(SequenceInterval.newBuilder()
                                        .setStartNumber(Number.newBuilder().setValue(48367511))
                                        .setEndNumber(Number.newBuilder().setValue(48367512))
                                        .build())
                                .build())
                        .setLiteralSequenceExpression(LiteralSequenceExpression.newBuilder()
                                .setSequence("T")
                                .build())
                        .build())
                .build();
    }

    private static Interpretation exampleInterpretation(VariationDescriptor variationDescriptor) {
        return Interpretation.newBuilder()
                .setId("example-interpretation")
                .setProgressStatus(Interpretation.ProgressStatus.SOLVED)
                .setDiagnosis(Diagnosis.newBuilder()
                        .setDisease(OntologyClass.newBuilder().setId("OMIM:123456").setLabel("DISEASE EXAMPLE").build())
                        .addGenomicInterpretations(GenomicInterpretation.newBuilder()
                                .setSubjectOrBiosampleId("example-id")
                                .setInterpretationStatus(GenomicInterpretation.InterpretationStatus.CAUSATIVE)
                                .setVariantInterpretation(VariantInterpretation.newBuilder()
                                        .setAcmgPathogenicityClassification(AcmgPathogenicityClassification.PATHOGENIC)
                                        .setTherapeuticActionability(TherapeuticActionability.ACTIONABLE)
                                        .setVariationDescriptor(variationDescriptor)
                                        .build())
                                .build())
                        .build())
                .build();
    }

    private static Phenopacket minimalPhenopacket() {
        return Phenopacket.newBuilder()
                .setId("example-id")
                .setMetaData(MetaData.newBuilder()
                        .setCreated(Timestamp.newBuilder()
                                .setSeconds(123_456_789)
                                .build())
                        .setCreatedBy("example-curator")
                        .setPhenopacketSchemaVersion("2.0.0")
                        .build())
                .build();
    }
}
