package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrs.v1.Variation;
import org.ga4gh.vrsatile.v1.*;
import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.List;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;
public class VariationDescriptorBuilder {

    final VariationDescriptor.Builder builder;

    public VariationDescriptorBuilder(String id) {
        builder = VariationDescriptor.newBuilder().setId(id);
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

    public VariationDescriptorBuilder vcfVecord(VcfRecord vcf) {
        builder.setVcfRecord(vcf);
        return this;
    }

    public VariationDescriptorBuilder xref(String xref) {
        builder.addXrefs(xref);
        return this;
    }

    public VariationDescriptorBuilder addAllXrefs(List<String> xrefs) {
        builder.addAllXrefs(xrefs);
        return this;
    }

    public VariationDescriptorBuilder alternateLabels(String altLabel) {
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

    public VariationDescriptorBuilder structuralType(OntologyClass clz) {
        builder.setStructuralType(clz);
        return this;
    }

    public VariationDescriptorBuilder heterozygous() {
        OntologyClass heterozygous = ontologyClass("GENO:0000135", "heterozygous");
        builder.setAllelicState(heterozygous);
        return this;
    }

    public VariationDescriptorBuilder homozygous() {
        OntologyClass heterozygous = ontologyClass("GENO:0000136", "homozygous");
        builder.setAllelicState(heterozygous);
        return this;
    }
    public VariationDescriptorBuilder hemizygous() {
        OntologyClass heterozygous = ontologyClass("GENO:0000134", "hemizygous");
        builder.setAllelicState(heterozygous);
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


    public VariationDescriptor build() {
        return builder.build();
    }

    public static VariationDescriptorBuilder create(String id) {
        return new VariationDescriptorBuilder(id);
    }
}
