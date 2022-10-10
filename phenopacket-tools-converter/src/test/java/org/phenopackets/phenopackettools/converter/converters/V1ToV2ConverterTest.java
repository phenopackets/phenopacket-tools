package org.phenopackets.phenopackettools.converter.converters;

import org.ga4gh.vrsatile.v1.Expression;
import org.ga4gh.vrsatile.v1.VariationDescriptor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.phenopackets.phenopackettools.test.TestData;
import org.phenopackets.schema.v1.core.HtsFile;
import org.phenopackets.schema.v2.core.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class V1ToV2ConverterTest {

    @Test
    public void convertPhenopacketWithoutConvertingVariants() {
        boolean convertVariants = false;
        V1ToV2Converter converter = V1ToV2Converter.of(convertVariants);

        org.phenopackets.schema.v1.Phenopacket v1 = TestData.V1.comprehensivePhenopacket();

        org.phenopackets.schema.v2.Phenopacket v2 = converter.convertPhenopacket(v1);

        // ID
        assertThat(v2.getId(), equalTo(v1.getId()));

        // Individual
        assertIndividualsAreEqual(v1.getSubject(), v2.getSubject());

        // Phenotypic features
        assertThat(v2.getPhenotypicFeaturesList(), hasSize(v1.getPhenotypicFeaturesCount()));
        for (int i = 0; i < v2.getPhenotypicFeaturesList().size(); i++)
            assertPhenotypicFeatureIsEqual(v1.getPhenotypicFeatures(i), v2.getPhenotypicFeatures(i));

        // Biosample
        assertThat(v2.getBiosamplesList(), hasSize(v1.getBiosamplesCount()));
        for (int i = 0; i < v2.getBiosamplesCount(); i++)
            assertBiosampleIsEqual(v1.getBiosamples(i), v2.getBiosamples(i));

        // No genes and variants are converted as `convertVariants == false`.
        assertThat(v2.getInterpretationsCount(), equalTo(0));

        // Disease
        assertThat(v2.getDiseasesList(), hasSize(v1.getDiseasesCount()));
        for (int i = 0; i < v2.getDiseasesList().size(); i++)
            assertDiseaseIsEqual(v1.getDiseases(i), v2.getDiseases(i));

        // HtsFile -> File
        assertThat(v2.getFilesCount(), equalTo(v1.getHtsFilesCount()));
        for (int i = 0; i < v2.getFilesCount(); i++)
            assertHtsFileIsEqual(v1.getHtsFiles(i), v2.getFiles(i));

        // MetaData
        assertMetaDataAraEqual(v1.getMetaData(), v2.getMetaData());
    }

    @Test
    public void convertPhenopacketWithConvertingVariants() {
        boolean convertVariants = true;
        V1ToV2Converter converter = V1ToV2Converter.of(convertVariants);

        org.phenopackets.schema.v1.Phenopacket v1 = TestData.V1.comprehensivePhenopacket();

        org.phenopackets.schema.v2.Phenopacket v2 = converter.convertPhenopacket(v1);

        // ID
        assertThat(v2.getId(), equalTo(v1.getId()));

        // Individual
        assertIndividualsAreEqual(v1.getSubject(), v2.getSubject());

        // Phenotypic features
        assertThat(v2.getPhenotypicFeaturesList(), hasSize(v1.getPhenotypicFeaturesCount()));
        for (int i = 0; i < v2.getPhenotypicFeaturesList().size(); i++)
            assertPhenotypicFeatureIsEqual(v1.getPhenotypicFeatures(i), v2.getPhenotypicFeatures(i));

        // Biosample
        assertThat(v2.getBiosamplesList(), hasSize(v1.getBiosamplesCount()));
        for (int i = 0; i < v2.getBiosamplesCount(); i++)
            assertBiosampleIsEqual(v1.getBiosamples(i), v2.getBiosamples(i));

        // There is one `Interpretation` since `convertVariants == true`.
        // The following is a deep test of the `Interpretation`.
        assertThat(v2.getInterpretationsCount(), equalTo(1));
        Interpretation interpretation = v2.getInterpretations(0);
        assertThat(interpretation.getId(), equalTo(v1.getId()));
        assertThat(interpretation.getProgressStatus(), equalTo(Interpretation.ProgressStatus.SOLVED));
        Diagnosis diagnosis = interpretation.getDiagnosis();
        assertOntologyClassesAreEqual(v1.getDiseases(0).getTerm(), diagnosis.getDisease());
        assertThat(diagnosis.getGenomicInterpretationsCount(), equalTo(1));
        GenomicInterpretation genomicInterpretation = diagnosis.getGenomicInterpretations(0);
        assertThat(genomicInterpretation.getSubjectOrBiosampleId(), equalTo(v1.getSubject().getId()));
        assertThat(genomicInterpretation.getInterpretationStatus(), equalTo(GenomicInterpretation.InterpretationStatus.CAUSATIVE));
        assertThat(genomicInterpretation.getCallCase(), equalTo(GenomicInterpretation.CallCase.VARIANT_INTERPRETATION));
        assertThat(genomicInterpretation.getVariantInterpretation(), equalTo(VariantInterpretation.newBuilder()
                .setVariationDescriptor(VariationDescriptor.newBuilder()
                        .addExpressions(Expression.newBuilder()
                                .setSyntax("hgvs")
                                .setValue("NM_001848.2:c.877G>A")
                                .build())
                        .setAllelicState(OntologyClass.newBuilder()
                                .setId("GENO:0000135")
                                .setLabel("heterozygous")
                                .build())
                        .build())
                .build()));

        // Disease
        assertThat(v2.getDiseasesList(), hasSize(v1.getDiseasesCount()));
        for (int i = 0; i < v2.getDiseasesList().size(); i++)
            assertDiseaseIsEqual(v1.getDiseases(i), v2.getDiseases(i));

        // HtsFile -> File
        assertThat(v2.getFilesCount(), equalTo(v1.getHtsFilesCount()));
        for (int i = 0; i < v2.getFilesCount(); i++)
            assertHtsFileIsEqual(v1.getHtsFiles(i), v2.getFiles(i));

        // MetaData
        assertMetaDataAraEqual(v1.getMetaData(), v2.getMetaData());
    }

    private static void assertIndividualsAreEqual(org.phenopackets.schema.v1.core.Individual v1, Individual v2) {
        assertThat(v2.getId(), equalTo(v1.getId()));
        assertThat(v2.getAlternateIdsList(), hasSize(v1.getAlternateIdsCount()));
        assertThat(v2.getAlternateIdsList(), hasItems(v1.getAlternateIdsList().toArray(String[]::new)));
        assertThat(v2.getDateOfBirth(), equalTo(v1.getDateOfBirth()));
        assertThat(v2.getSex().name(), equalTo(v1.getSex().name()));
        assertThat(v2.getKaryotypicSex().name(), equalTo(v1.getKaryotypicSex().name()));
        assertOntologyClassesAreEqual(v1.getTaxonomy(), v2.getTaxonomy());
    }

    private static void assertOntologyClassesAreEqual(org.phenopackets.schema.v1.core.OntologyClass v1, OntologyClass v2) {
        assertThat(v2.getId(), equalTo(v1.getId()));
        assertThat(v2.getLabel(), equalTo(v1.getLabel()));
    }

    private static void assertPhenotypicFeatureIsEqual(org.phenopackets.schema.v1.core.PhenotypicFeature v1, PhenotypicFeature v2) {
        assertThat(v1.getDescription(), equalTo(v2.getDescription()));
        assertOntologyClassesAreEqual(v1.getType(), v2.getType());
        assertThat(v1.getNegated(), equalTo(v2.getExcluded()));
        assertOntologyClassesAreEqual(v1.getSeverity(), v2.getSeverity());
        assertThat(v1.getModifiersCount(), equalTo(v2.getModifiersCount()));
        for (int i = 0; i < v1.getModifiersList().size(); i++)
            assertOntologyClassesAreEqual(v1.getModifiers(i), v2.getModifiers(i));

        if (v1.hasAgeOfOnset()) {
            assertThat(v2.getOnset().hasAge(), is(true));
            assertAgeIsEqual(v1.getAgeOfOnset(), v2.getOnset().getAge());
        } else if (v1.hasAgeRangeOfOnset()) {
            assertThat(v2.getOnset().hasAgeRange(), is(true));
            assertAgeRangeIsEqual(v1.getAgeRangeOfOnset(), v2.getOnset().getAgeRange());
        } else if (v1.hasClassOfOnset()) {
            assertThat(v2.getOnset().hasOntologyClass(), is(true));
            assertOntologyClassesAreEqual(v1.getClassOfOnset(), v2.getOnset().getOntologyClass());
        }

        assertThat(v1.getEvidenceCount(), equalTo(v2.getEvidenceCount()));
        for (int i = 0; i < v1.getEvidenceList().size(); i++)
            assertEvidenceIsEqual(v1.getEvidence(i), v2.getEvidence(i));
    }

    private static void assertBiosampleIsEqual(org.phenopackets.schema.v1.core.Biosample v1, Biosample v2) {
        assertThat(v1.getId(), equalTo(v2.getId()));
        assertThat(v1.getIndividualId(), equalTo(v2.getIndividualId()));
        assertThat(v1.getDescription(), equalTo(v2.getDescription()));
        assertOntologyClassesAreEqual(v1.getSampledTissue(), v2.getSampledTissue());

        assertThat(v1.getPhenotypicFeaturesCount(), equalTo(v2.getPhenotypicFeaturesCount()));
        for (int i = 0; i < v2.getPhenotypicFeaturesList().size(); i++)
            assertPhenotypicFeatureIsEqual(v1.getPhenotypicFeatures(i), v2.getPhenotypicFeatures(i));

        assertOntologyClassesAreEqual(v1.getTaxonomy(), v2.getTaxonomy());
        if (v1.hasAgeOfIndividualAtCollection()) {
            assertThat(v2.getTimeOfCollection().hasAge(), is(true));
            assertAgeIsEqual(v1.getAgeOfIndividualAtCollection(), v2.getTimeOfCollection().getAge());
        } else if (v1.hasAgeRangeOfIndividualAtCollection()) {
            assertThat(v2.getTimeOfCollection().hasAgeRange(), is(true));
            assertAgeRangeIsEqual(v1.getAgeRangeOfIndividualAtCollection(), v2.getTimeOfCollection().getAgeRange());
        }

        assertOntologyClassesAreEqual(v1.getHistologicalDiagnosis(), v2.getHistologicalDiagnosis());
        assertOntologyClassesAreEqual(v1.getTumorProgression(), v2.getTumorProgression());
        assertOntologyClassesAreEqual(v1.getTumorGrade(), v2.getTumorGrade());

        assertThat(v1.getDiagnosticMarkersCount(), equalTo(v2.getDiagnosticMarkersCount()));
        for (int i = 0; i < v2.getDiagnosticMarkersCount(); i++)
            assertOntologyClassesAreEqual(v1.getDiagnosticMarkers(i), v2.getDiagnosticMarkers(i));

        assertProcedureIsEqual(v1.getProcedure(), v2.getProcedure());

        assertThat(v2.getFilesCount(), equalTo(v1.getHtsFilesCount()));
        for (int i = 0; i < v2.getFilesCount(); i++)
            assertHtsFileIsEqual(v1.getHtsFiles(i), v2.getFiles(i));

        // TODO - test variants after the conversion is implemented

        if (v1.getIsControlSample()) {
            assertThat(v2.getMaterialSample().getId(), equalTo("EFO:0009654"));
            assertThat(v2.getMaterialSample().getLabel(), equalTo("reference sample"));
        }
        if (v1.hasTumorProgression() || v2.hasTumorGrade()){
            assertThat(v2.getMaterialSample().getId(), equalTo("EFO:0009655"));
            assertThat(v2.getMaterialSample().getLabel(), equalTo("abnormal sample"));
        }
    }

    private static void assertEvidenceIsEqual(org.phenopackets.schema.v1.core.Evidence v1, Evidence v2) {
        assertOntologyClassesAreEqual(v1.getEvidenceCode(), v2.getEvidenceCode());
        assertExternalReferenceIsEqual(v1.getReference(), v2.getReference());
    }

    private static void assertExternalReferenceIsEqual(org.phenopackets.schema.v1.core.ExternalReference v1, ExternalReference v2) {
        assertThat(v1.getId(), equalTo(v2.getId()));
        assertThat(v1.getDescription(), equalTo(v2.getDescription()));
    }

    private static void assertAgeIsEqual(org.phenopackets.schema.v1.core.Age v1, Age v2) {
        assertThat(v1.getAge(), equalTo(v2.getIso8601Duration()));
    }

    private static void assertAgeRangeIsEqual(org.phenopackets.schema.v1.core.AgeRange v1, AgeRange v2) {
        assertAgeIsEqual(v1.getStart(), v2.getStart());
        assertAgeIsEqual(v1.getEnd(), v2.getEnd());
    }

    private static void assertDiseaseIsEqual(org.phenopackets.schema.v1.core.Disease v1, Disease v2) {
        assertOntologyClassesAreEqual(v1.getTerm(), v2.getTerm());

        assertThat(v1.getDiseaseStageCount(), equalTo(v2.getDiseaseStageCount()));
        for (int i = 0; i < v2.getDiseaseStageCount(); i++)
            assertOntologyClassesAreEqual(v1.getDiseaseStage(i), v2.getDiseaseStage(i));

        assertThat(v1.getTnmFindingCount(), equalTo(v2.getClinicalTnmFindingCount()));
        for (int i = 0; i < v2.getClinicalTnmFindingCount(); i++)
            assertOntologyClassesAreEqual(v1.getTnmFinding(i), v2.getClinicalTnmFinding(i));
    }

    private static void assertHtsFileIsEqual(HtsFile v1, File v2) {
        assertThat(v1.getUri(), equalTo(v2.getUri()));
        assertThat(v2.getFileAttributesOrThrow("genomeAssembly"), equalTo(v1.getGenomeAssembly()));
        assertThat(v2.getFileAttributesOrThrow("fileFormat"), equalTo(v1.getHtsFormat().name().toLowerCase()));
        assertThat(v2.getFileAttributesOrThrow("description"), equalTo(v1.getDescription()));
        assertThat(v1.getIndividualToSampleIdentifiersMap(), equalTo(v2.getIndividualToFileIdentifiersMap()));
    }

    private static void assertMetaDataAraEqual(org.phenopackets.schema.v1.core.MetaData v1, MetaData v2) {
        assertThat(v1.getCreated(), equalTo(v2.getCreated()));
        assertThat(v1.getCreatedBy(), equalTo(v2.getCreatedBy()));
        assertThat(v1.getSubmittedBy(), equalTo(v2.getSubmittedBy()));

        assertThat(v1.getResourcesCount(), equalTo(v2.getResourcesCount()));
        for (int i = 0; i < v2.getResourcesCount(); i++)
            assertResourceIsEqual(v1.getResources(i), v2.getResources(i));

        assertThat(v1.getUpdatesCount(), equalTo(v2.getUpdatesCount()));
        for (int i = 0; i < v2.getUpdatesCount(); i++)
            assertUpdateIsEqual(v1.getUpdates(i), v2.getUpdates(i));
    }

    private static void assertResourceIsEqual(org.phenopackets.schema.v1.core.Resource v1, Resource v2) {
        assertThat(v1.getId(), equalTo(v2.getId()));
        assertThat(v1.getName(), equalTo(v2.getName()));
        assertThat(v1.getUrl(), equalTo(v2.getUrl()));
        assertThat(v1.getVersion(), equalTo(v2.getVersion()));
        assertThat(v1.getNamespacePrefix(), equalTo(v2.getNamespacePrefix()));
        assertThat(v1.getIriPrefix(), equalTo(v2.getIriPrefix()));
    }

    private static void assertUpdateIsEqual(org.phenopackets.schema.v1.core.Update v1, Update v2) {
        assertThat(v1.getTimestamp(), equalTo(v2.getTimestamp()));
        assertThat(v1.getUpdatedBy(), equalTo(v2.getUpdatedBy()));
        assertThat(v1.getComment(), equalTo(v2.getComment()));
    }

    private static void assertProcedureIsEqual(org.phenopackets.schema.v1.core.Procedure v1, Procedure v2) {
        assertOntologyClassesAreEqual(v1.getCode(), v2.getCode());
        assertOntologyClassesAreEqual(v1.getBodySite(), v2.getBodySite());
    }


    @Nested
    public class PhenopacketConverterTest {
    }

}