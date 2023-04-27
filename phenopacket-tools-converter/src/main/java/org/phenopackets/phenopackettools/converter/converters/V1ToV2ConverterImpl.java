package org.phenopackets.phenopackettools.converter.converters;

import org.ga4gh.vrsatile.v1.MoleculeContext;
import org.ga4gh.vrsatile.v1.VariationDescriptor;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.converter.converters.v2.*;
import org.phenopackets.phenopackettools.core.PhenopacketToolsRuntimeException;
import org.phenopackets.schema.v1.core.Variant;
import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;


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
        boolean isDefault = true;
        Phenopacket.Builder builder = Phenopacket.newBuilder();

        if (!phenopacket.getId().isEmpty()) {
            isDefault = false;
            builder.setId(phenopacket.getId());
        }

        Optional<Individual> individual = IndividualConverter.toIndividual(phenopacket.getSubject());
        if (individual.isPresent()) {
            isDefault = false;
            builder.setSubject(individual.get());
        }

        List<PhenotypicFeature> phenotypicFeatures = PhenotypicFeatureConverter.toPhenotypicFeatures(phenopacket.getPhenotypicFeaturesList());
        if (!phenotypicFeatures.isEmpty()) {
            isDefault = false;
            builder.addAllPhenotypicFeatures(phenotypicFeatures);
        }

        List<Biosample> biosamples = BiosampleConverter.toBiosamples(phenopacket.getBiosamplesList());
        if (!biosamples.isEmpty()) {
            isDefault = false;
            builder.addAllBiosamples(biosamples);
        }

        if (convertVariants) {
            Optional<Interpretation> interpretation = toV2Interpretation(phenopacket);
            if (interpretation.isPresent()) {
                isDefault = false;
                builder.addInterpretations(interpretation.get());
            }
        }

        List<Disease> diseases = DiseaseConverter.toDiseases(phenopacket.getDiseasesList());
        if (!diseases.isEmpty()) {
            isDefault = false;
            builder.addAllDiseases(diseases);
        }

        List<File> files = FileConverter.toFiles(phenopacket.getHtsFilesList());
        if (!files.isEmpty()) {
            isDefault = false;
            builder.addAllFiles(files);
        }

        Optional<MetaData> metaData = MetaDataConverter.toMetaData(phenopacket.getMetaData());
        if (metaData.isPresent()) {
            isDefault = false;
            builder.setMetaData(metaData.get());
        }

        return isDefault
                ? Phenopacket.getDefaultInstance()
                : builder.build();
    }

    public Family convertFamily(org.phenopackets.schema.v1.Family family) {
        boolean isDefault = true;
        Family.Builder builder = Family.newBuilder();

        if (!family.getId().isEmpty()) {
            isDefault = false;
            builder.setId(family.getId());
        }

        Optional<Pedigree> pedigree = PedigreeConverter.toPedigree(family.getPedigree());
        if (pedigree.isPresent()) {
            isDefault = false;
            builder.setPedigree(pedigree.get());
        }

        Phenopacket proband = convertPhenopacket(family.getProband());
        if (!proband.equals(Phenopacket.getDefaultInstance())) {
            isDefault = false;
            builder.setProband(proband);
        }

        List<Phenopacket> relatives = family.getRelativesList().stream()
                .map(this::convertPhenopacket)
                .filter(relative -> !relative.equals(Phenopacket.getDefaultInstance()))
                .toList();
        if (!relatives.isEmpty()) {
            isDefault = false;
            builder.addAllRelatives(relatives);
        }

        List<File> files = FileConverter.toFiles(family.getHtsFilesList());
        if (!files.isEmpty()) {
            isDefault = false;
            builder.addAllFiles(files);
        }

        Optional<MetaData> metaData = MetaDataConverter.toMetaData(family.getMetaData());
        if (metaData.isPresent()) {
            isDefault = false;
            builder.setMetaData(metaData.get());
        }

        return isDefault
                ? Family.getDefaultInstance()
                : builder.build();
    }

    public Cohort convertCohort(org.phenopackets.schema.v1.Cohort cohort) {
        boolean isDefault = true;
        Cohort.Builder builder = Cohort.newBuilder();

        if (!cohort.getId().isEmpty()) {
            isDefault = false;
            builder.setId(cohort.getId());
        }

        if (!cohort.getDescription().isEmpty()) {
            isDefault = false;
            builder.setDescription(cohort.getDescription());
        }

        List<Phenopacket> members = cohort.getMembersList().stream()
                .map(this::convertPhenopacket)
                .filter(member -> !member.equals(Phenopacket.getDefaultInstance()))
                .toList();
        if (!members.isEmpty()) {
            isDefault = false;
            builder.addAllMembers(members);
        }

        List<File> files = FileConverter.toFiles(cohort.getHtsFilesList());
        if (!files.isEmpty()) {
            isDefault = false;
            builder.addAllFiles(files);
        }

        Optional<MetaData> metaData = MetaDataConverter.toMetaData(cohort.getMetaData());
        if (metaData.isPresent()) {
            isDefault = false;
            builder.setMetaData(metaData.get());
        }

        return isDefault
                ? Cohort.getDefaultInstance()
                : builder.build();
    }

    private static Optional<Interpretation> toV2Interpretation(org.phenopackets.schema.v1.Phenopacket v1) {
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
                return Optional.empty();
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

        return Optional.of(InterpretationBuilder.builder(v1.getId())
                .solved(diagnosis.build()));
    }

    private static Function<Variant, VariationDescriptor> toVariationDescriptor() {
        return variant -> {
            var zygosity = variant.getZygosity();
            OntologyClass v2zygosity = OntologyClassBuilder.ontologyClass(zygosity.getId(), zygosity.getLabel());
            return switch (variant.getAlleleCase()) {
                case HGVS_ALLELE -> {
                    var hgvsAllele = variant.getHgvsAllele();
                    Optional<MoleculeContext> moleculeContext = guessMoleculeContext(hgvsAllele.getHgvs());
                    yield VariationDescriptorBuilder.builder(hgvsAllele.getId())
                            .zygosity(v2zygosity)
                            .moleculeContext(moleculeContext.orElse(MoleculeContext.unspecified_molecule_context))
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
                            .genomic()
                            .zygosity(v2zygosity)
                            .build();
                }
                case ISCN_ALLELE -> {
                    var iscnAllele = variant.getIscnAllele();
                    yield VariationDescriptorBuilder.builder(iscnAllele.getId())
                            .iscn(iscnAllele.getIscn())
                            .genomic()
                            .zygosity(v2zygosity)
                            .build();
                }
                // cannot ever happen, but if it does...
                case ALLELE_NOT_SET -> throw new PhenopacketToolsRuntimeException("Did not recognize variant type");
            };
        };
    }

    private static Optional<MoleculeContext> guessMoleculeContext(String hgvs) {
        if (hgvs.startsWith("NM_") || hgvs.startsWith("XM_") || hgvs.startsWith("ENST")) {
            return Optional.of(MoleculeContext.transcript);
        } else if (hgvs.startsWith("NP_") || hgvs.startsWith("XP_") || hgvs.startsWith("ENSP")) {
            return Optional.of(MoleculeContext.protein);
        } else if (hgvs.startsWith("NC_")) {
            return Optional.of(MoleculeContext.genomic);
        } else {
            LOGGER.debug("Could not determine molecule context {transcript, protein, genomic} from HGVS string {}", hgvs);
            return Optional.empty();
        }
    }

}
