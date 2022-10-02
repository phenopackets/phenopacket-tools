package org.phenopackets.phenopackettools.converter.converters;

import org.ga4gh.vrsatile.v1.VariationDescriptor;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.Diagnosis;
import org.phenopackets.schema.v2.core.Interpretation;
import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.phenopackets.phenopackettools.converter.converters.v2.BiosampleConverter.toBiosamples;
import static org.phenopackets.phenopackettools.converter.converters.v2.DiseaseConverter.toDiseases;
import static org.phenopackets.phenopackettools.converter.converters.v2.FileConverter.toFiles;
import static org.phenopackets.phenopackettools.converter.converters.v2.IndividualConverter.toIndividual;
import static org.phenopackets.phenopackettools.converter.converters.v2.MetaDataConverter.toMetaData;
import static org.phenopackets.phenopackettools.converter.converters.v2.PhenotypicFeatureConverter.toPhenotypicFeatures;

public class PhenopacketConverter {

    private PhenopacketConverter() {
    }

    public static Phenopacket toV2Phenopacket(org.phenopackets.schema.v1.Phenopacket v1PhenoPacket) {
        Phenopacket.Builder builder = Phenopacket.newBuilder();

        if (v1PhenoPacket.hasMetaData()) {
            builder.setMetaData(toMetaData(v1PhenoPacket.getMetaData()));
        }
        if (!v1PhenoPacket.getId().isEmpty()) {
            builder.setId(v1PhenoPacket.getId());
        }
        if (v1PhenoPacket.hasSubject()) {
            builder.setSubject(toIndividual(v1PhenoPacket.getSubject()));
        }
        if (v1PhenoPacket.getPhenotypicFeaturesCount() > 0) {
            builder.addAllPhenotypicFeatures(toPhenotypicFeatures(v1PhenoPacket.getPhenotypicFeaturesList()));
        }
        if (v1PhenoPacket.getBiosamplesCount() > 0) {
            builder.addAllBiosamples(toBiosamples(v1PhenoPacket.getBiosamplesList()));
        }
        if (v1PhenoPacket.getDiseasesCount() > 0) {
            builder.addAllDiseases(toDiseases(v1PhenoPacket.getDiseasesList()));
        }
        if (v1PhenoPacket.getHtsFilesCount() > 0) {
            builder.addAllFiles(toFiles(v1PhenoPacket.getHtsFilesList()));
        }
        return builder.build();
    }

    public static Interpretation toV2Interpretation(org.phenopackets.schema.v1.Phenopacket v1PhenoPacket) {
        InterpretationBuilder ibuilder =  InterpretationBuilder.builder(v1PhenoPacket.getId());
        var v1disease = v1PhenoPacket.getDiseases(0);
        var v1diseaseOntologyClz = v1disease.getTerm();
        // assumption is that the variants are causative for the disease
        // there is no other disease, so we will use the v1 phenopacket id for the interpretation id
        DiagnosisBuilder dbuilder = DiagnosisBuilder.builder(v1diseaseOntologyClz.getId(),
                v1diseaseOntologyClz.getLabel());
        List<VariationDescriptor> descriptorList = new ArrayList<>();
        if (v1PhenoPacket.getVariantsCount() > 0) {
            for (var variant : v1PhenoPacket.getVariantsList()) {
                var zygosity = variant.getZygosity();
                OntologyClass v2zygosity = OntologyClassBuilder.ontologyClass(zygosity.getId(), zygosity.getLabel());
                if (variant.hasVcfAllele()) {

                    var vcfAllele = variant.getVcfAllele();
                    var vcfRecord =
                            VcfRecordBuilder.builder(vcfAllele.getGenomeAssembly(),
                                    vcfAllele.getChr(), vcfAllele.getPos(),
                                    vcfAllele.getRef(),
                                    vcfAllele.getAlt()).
                                    build();
                    var variationDesc = VariationDescriptorBuilder
                            .builder()
                            .vcfRecord(vcfRecord)
                            .genomic()
                            .zygosity(v2zygosity)
                            .build();
                    descriptorList.add(variationDesc);
                } else if (variant.hasHgvsAllele()) {
                    var hgvsAllele = variant.getHgvsAllele();
                    var variationDescriptor =
                            VariationDescriptorBuilder.builder(hgvsAllele.getId())
                                    .zygosity(v2zygosity)
                                    .hgvs(hgvsAllele.getHgvs())
                                    .build();
                    descriptorList.add(variationDescriptor);
                } else if (variant.hasIscnAllele()) {
                    var iscnAllele = variant.getIscnAllele();
                    var variationDescription =
                            VariationDescriptorBuilder.builder(iscnAllele.getId())
                                    .iscn(iscnAllele.getIscn())
                                    .zygosity(v2zygosity)
                                    .build();
                    descriptorList.add(variationDescription);
                } else if (variant.hasSpdiAllele()) {
                    var spdiAllele = variant.getSpdiAllele();
                    var variationDescription =
                            VariationDescriptorBuilder.builder(spdiAllele.getId())
                                    .build(); // TODO -- HOW TO REPRESENT SPDI in VRS

                    descriptorList.add(variationDescription);
                } else {
                    // cannot ever happen, but if it does...
                    throw new PhenotoolsRuntimeException("Did not recognize variant type");
                }
            }
            if (v1PhenoPacket.getDiseasesCount() != 1) {
                throw new PhenotoolsRuntimeException("Can only convert variants if there is exactly one disease in v1 phenopacket!");
            }

            for (var vardesc : descriptorList) {
                GenomicInterpretationBuilder gibuilder = GenomicInterpretationBuilder.builder(v1PhenoPacket.getSubject().getId());
                gibuilder.causative();
                gibuilder.variantInterpretation(VariantInterpretationBuilder.builder(vardesc));
                dbuilder.addGenomicInterpretation(gibuilder.build());
            }

        }
        dbuilder.build();
       return ibuilder.solved(dbuilder.build());
    }

    public static List<Phenopacket> toV2Phenopackets(List<org.phenopackets.schema.v1.Phenopacket> v1Phenopackets) {
        return v1Phenopackets.stream().map(PhenopacketConverter::toV2Phenopacket).collect(Collectors.toUnmodifiableList());
    }
}
