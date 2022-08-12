package org.phenopackets.phenopackettools.builder.builders;

import org.ga4gh.vrs.v1.Variation;
import org.ga4gh.vrsatile.v1.*;
import org.phenopackets.phenopackettools.builder.constants.AllelicState;
import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.List;

public class VariationDescriptorBuilder {

    final VariationDescriptor.Builder builder;

    /**
     * Constructor if no identifier is to be used
     */
    private VariationDescriptorBuilder() {
        builder = VariationDescriptor.newBuilder();
    }

    /**
     * @param id an arbitrary identifier
     */
    private VariationDescriptorBuilder(String id) {
        builder = VariationDescriptor.newBuilder().setId(id);
    }

    public static VariationDescriptorBuilder builder() {
        return new VariationDescriptorBuilder();
    }

    public static VariationDescriptorBuilder builder(String variantId) {
        return new VariationDescriptorBuilder(variantId);
    }

    public VariationDescriptorBuilder label(String lbl) {
        builder.setLabel(lbl);
        return this;
    }

    public VariationDescriptorBuilder variation(Variation variation) {
        builder.setVariation(variation);
        return this;
    }

    public VariationDescriptorBuilder description(String desc) {
        builder.setDescription(desc);
        return this;
    }

    public VariationDescriptorBuilder geneContext(GeneDescriptor gene) {
        builder.setGeneContext(gene);
        return this;
    }

    public VariationDescriptorBuilder vcfRecord(VcfRecord vcf) {
        builder.setVcfRecord(vcf);
        return this;
    }

    public VariationDescriptorBuilder addXref(String xref) {
        builder.addXrefs(xref);
        return this;
    }

    public VariationDescriptorBuilder addAllXrefs(List<String> xrefs) {
        builder.addAllXrefs(xrefs);
        return this;
    }

    public VariationDescriptorBuilder addAlternateLabel(String altLabel) {
        builder.addAlternateLabels(altLabel);
        return this;
    }

    public VariationDescriptorBuilder addAllAlternateLabels(List<String> altLabels) {
        builder.addAllAlternateLabels(altLabels);
        return this;
    }

    public VariationDescriptorBuilder genomic() {
        builder.setMoleculeContext(MoleculeContext.genomic);
        return this;
    }

    public VariationDescriptorBuilder protein() {
        builder.setMoleculeContext(MoleculeContext.protein);
        return this;
    }

    public VariationDescriptorBuilder transcript() {
        builder.setMoleculeContext(MoleculeContext.transcript);
        return this;
    }


    public VariationDescriptorBuilder structuralType(OntologyClass structuralType) {
        builder.setStructuralType(structuralType);
        return this;
    }

    public VariationDescriptorBuilder heterozygous() {
        builder.setAllelicState(AllelicState.heterozygous());
        return this;
    }

    public VariationDescriptorBuilder homozygous() {
        builder.setAllelicState(AllelicState.homozygous());
        return this;
    }

    public VariationDescriptorBuilder hemizygous() {
        builder.setAllelicState(AllelicState.hemizygous());
        return this;
    }

    public VariationDescriptorBuilder hgvs(String value) {
        Expression expression = Expression.newBuilder()
                .setSyntax("hgvs")
                .setValue(value)
                .build();
        builder.addExpressions(expression);
        return this;
    }

    public VariationDescriptorBuilder spdi(String value) {
        Expression expression = Expression.newBuilder()
                .setSyntax("spdi")
                .setValue(value)
                .build();
        builder.addExpressions(expression);
        return this;
    }

    public VariationDescriptorBuilder iscn(String value) {
        Expression expression = Expression.newBuilder()
                .setSyntax("iscn")
                .setValue(value)
                .build();
        builder.addExpressions(expression);
        return this;
    }

    public VariationDescriptorBuilder addExpression(Expression expression) {
        builder.addExpressions(expression);
        return this;
    }

    public VariationDescriptorBuilder vcfHg38(String chromosome, int position, String ref, String alt) {
        VcfRecord vcf = VcfRecordBuilder.of("GRCh38", chromosome, position, ref, alt);
        builder.setVcfRecord(vcf);
        return this;
    }

    public VariationDescriptorBuilder vcfHg37(String chromosome, int position, String ref, String alt) {
        VcfRecord vcf = VcfRecordBuilder.of("GRCh37", chromosome, position, ref, alt);
        builder.setVcfRecord(vcf);
        return this;
    }



    /**
     * @param percentage estimated percentage of cells affected by mosaic variant, e.g., 40%
     */
    public VariationDescriptorBuilder mosaicism(double percentage) {
       Extension percentageExtension = Extensions.mosaicism(percentage);
       builder.addExtensions(percentageExtension);
       return this;
    }


    /**
     * @param frequency estimated frequency (IN PERCENT) of an allele (generally a somatic mutation)
     */
    public VariationDescriptorBuilder alleleFrequency(double frequency) {
        Extension alleleFrequencyExtension = Extensions.alleleFrequency(frequency);
        builder.addExtensions(alleleFrequencyExtension);
        return this;
    }


    public VariationDescriptor build() {
        return builder.build();
    }
}
