package org.phenopackets.phenopackettools.converter.converters;

import org.ga4gh.vrsatile.v1.VariationDescriptor;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.core.PhenopacketToolsRuntimeException;
import org.phenopackets.schema.v1.core.Variant;
import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Interpretation;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

import static org.phenopackets.phenopackettools.converter.converters.v2.BiosampleConverter.toBiosamples;
import static org.phenopackets.phenopackettools.converter.converters.v2.DiseaseConverter.toDiseases;
import static org.phenopackets.phenopackettools.converter.converters.v2.FileConverter.toFiles;
import static org.phenopackets.phenopackettools.converter.converters.v2.IndividualConverter.toIndividual;
import static org.phenopackets.phenopackettools.converter.converters.v2.MetaDataConverter.toMetaData;
import static org.phenopackets.phenopackettools.converter.converters.v2.PedigreeConverter.toPedigree;
import static org.phenopackets.phenopackettools.converter.converters.v2.PhenotypicFeatureConverter.toPhenotypicFeatures;

/**
 * The default implementation of {@link V1ToV2Converter} that delegates the conversion to the static methods of
 * the {@link org.phenopackets.phenopackettools.converter.converters.v2} package.
 */
class V1ToV2ConverterImpl implements V1ToV2Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(V1ToV2ConverterImpl.class);

    private final boolean convertVariants;

    V1ToV2ConverterImpl(boolean convertVariants) {
        this.convertVariants = convertVariants;
    }

    public Phenopacket convertPhenopacket(org.phenopackets.schema.v1.Phenopacket phenopacket) {
        Phenopacket.Builder builder = Phenopacket.newBuilder();

        if (!phenopacket.getId().isEmpty()) {
            builder.setId(phenopacket.getId());
        }
        if (phenopacket.hasSubject()) {
            builder.setSubject(toIndividual(phenopacket.getSubject()));
        }
        if (phenopacket.getPhenotypicFeaturesCount() > 0) {
            builder.addAllPhenotypicFeatures(toPhenotypicFeatures(phenopacket.getPhenotypicFeaturesList()));
        }
        if (phenopacket.getBiosamplesCount() > 0) {
            builder.addAllBiosamples(toBiosamples(phenopacket.getBiosamplesList()));
        }

        if (convertVariants) {
            Interpretation interpretation = toV2Interpretation(phenopacket);
            if (!Interpretation.getDefaultInstance().equals(interpretation))
                builder.addInterpretations(interpretation);
        }

        if (phenopacket.getDiseasesCount() > 0) {
            builder.addAllDiseases(toDiseases(phenopacket.getDiseasesList()));
        }
        if (phenopacket.getHtsFilesCount() > 0) {
            builder.addAllFiles(toFiles(phenopacket.getHtsFilesList()));
        }
        if (phenopacket.hasMetaData()) {
            builder.setMetaData(toMetaData(phenopacket.getMetaData()));
        }
        return builder.build();
    }

    public Family convertFamily(org.phenopackets.schema.v1.Family family) {
        Family.Builder builder = Family.newBuilder();

        if (family.hasMetaData()) {
            builder.setMetaData(toMetaData(family.getMetaData()));
        }
        if (!family.getId().isEmpty()) {
            builder.setId(family.getId());
        }
        if (family.hasPedigree()) {
            builder.setPedigree(toPedigree(family.getPedigree()));
        }
        if (family.hasProband()) {
            builder.setProband(convertPhenopacket(family.getProband()));
        }

        if (family.getRelativesCount() > 0) {
            family.getRelativesList().stream()
                    .map(this::convertPhenopacket)
                    .forEachOrdered(builder::addRelatives);
        }

        if (family.getHtsFilesCount() > 0) {
            builder.addAllFiles(toFiles(family.getHtsFilesList()));
        }

        return builder.build();
    }

    public Cohort convertCohort(org.phenopackets.schema.v1.Cohort cohort) {
        Cohort.Builder builder = Cohort.newBuilder();

        if (cohort.hasMetaData()) {
            builder.setMetaData(toMetaData(cohort.getMetaData()));
        }
        if (!cohort.getId().isEmpty()) {
            builder.setId(cohort.getId());
        }
        if (!cohort.getDescription().isEmpty()) {
            builder.setDescription(cohort.getDescription());
        }
        if (cohort.getMembersCount() > 0) {
            builder.addAllMembers(cohort.getMembersList().stream()
                    .map(this::convertPhenopacket)
                    .toList());
        }
        if (cohort.getHtsFilesCount() > 0) {
            builder.addAllFiles(toFiles(cohort.getHtsFilesList()));
        }

        return builder.build();
    }

    private static Interpretation toV2Interpretation(org.phenopackets.schema.v1.Phenopacket v1) {
        /*
        Assumption is that the variants are causative for the disease and there is no other disease,
        so we will use the v1 phenopacket id for the interpretation id.
        */
        if (v1.getDiseasesCount() != 1) {
            if (v1.getVariantsCount() == 0) {
                // If there are no variants then we do not care about having exactly one disease.
                // We can still create a meaningful phenopacket, however, this may be not what the user intended,
                // and we'll warn.
                LOGGER.warn("Unable to convert disease and variant data since there are no variants in phenopacket '{}'", v1.getId());
                return Interpretation.getDefaultInstance();
            } else {
                // Non-empty variant list but not a single disease, we throw.
                throw new PhenopacketToolsRuntimeException("Can only convert variants if there is exactly one disease in v1 phenopacket!");
            }
        }

        var v1disease = v1.getDiseases(0);
        var v1diseaseOntologyClz = v1disease.getTerm();
        DiagnosisBuilder diagnosis = DiagnosisBuilder.builder(v1diseaseOntologyClz.getId(), v1diseaseOntologyClz.getLabel());

        List<VariationDescriptor> descriptors = v1.getVariantsList().stream()
                .map(toVariationDescriptor())
                .toList();

        for (var descriptor : descriptors) {
            GenomicInterpretationBuilder genomicInterpretation = GenomicInterpretationBuilder.builder(v1.getSubject().getId())
                    .causative()
                    .variantInterpretation(VariantInterpretationBuilder.builder(descriptor).acmgNotProvided().actionabilityUnknown());
            diagnosis.addGenomicInterpretation(genomicInterpretation.build());
        }

        return InterpretationBuilder.builder(v1.getId())
                .solved(diagnosis.build());
    }

    private static Function<Variant, VariationDescriptor> toVariationDescriptor() {
        return variant -> {
            var zygosity = variant.getZygosity();
            OntologyClass v2zygosity = OntologyClassBuilder.ontologyClass(zygosity.getId(), zygosity.getLabel());
            return switch (variant.getAlleleCase()) {
                case HGVS_ALLELE -> {
                    var hgvsAllele = variant.getHgvsAllele();
                    yield VariationDescriptorBuilder.builder(hgvsAllele.getId())
                            .zygosity(v2zygosity)
                            .hgvs(hgvsAllele.getHgvs())
                            .build();
                }
                case VCF_ALLELE -> {
                    var vcfAllele = variant.getVcfAllele();
                    var vcfRecord = VcfRecordBuilder.builder(vcfAllele.getGenomeAssembly(),
                                    vcfAllele.getChr(), vcfAllele.getPos(),
                                    vcfAllele.getRef(),
                                    vcfAllele.getAlt())
                            .build();
                    yield VariationDescriptorBuilder.builder(vcfAllele.getId())
                            .vcfRecord(vcfRecord)
                            .genomic()
                            .zygosity(v2zygosity)
                            .build();
                }
                case SPDI_ALLELE -> {
                    var spdiAllele = variant.getSpdiAllele();
                    String spdi = String.join(":",
                            spdiAllele.getSeqId(),
                            String.valueOf(spdiAllele.getPosition()),
                            spdiAllele.getDeletedSequence(),
                            spdiAllele.getInsertedSequence());
                    yield VariationDescriptorBuilder.builder(spdiAllele.getId())
                            .spdi(spdi)
                            .zygosity(v2zygosity)
                            .build();
                }
                case ISCN_ALLELE -> {
                    var iscnAllele = variant.getIscnAllele();
                    yield VariationDescriptorBuilder.builder(iscnAllele.getId())
                            .iscn(iscnAllele.getIscn())
                            .zygosity(v2zygosity)
                            .build();
                }
                // cannot ever happen, but if it does...
                case ALLELE_NOT_SET -> throw new PhenopacketToolsRuntimeException("Did not recognize variant type");
            };
        };
    }

}
