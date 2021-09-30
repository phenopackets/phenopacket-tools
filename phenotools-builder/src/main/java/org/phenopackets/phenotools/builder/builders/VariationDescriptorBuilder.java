package org.phenopackets.phenotools.builder.builders;

import org.ga4gh.vrs.v1.Variation;
import org.ga4gh.vrsatile.v1.*;
import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.List;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.ontologyClass;

public class VariationDescriptorBuilder {

    VariationDescriptor.Builder builder;

    public VariationDescriptorBuilder(String id) {
        builder = VariationDescriptor.newBuilder().setId(id);
    }

    public VariationDescriptorBuilder label(String lbl) {
        builder = builder.mergeFrom(builder.build()).setLabel(lbl);
        return this;
    }

    public VariationDescriptorBuilder variation(Variation variation) {
        builder = builder.mergeFrom(builder.build()).setVariation(variation);
        return this;
    }

    public VariationDescriptorBuilder description(String desc) {
        builder = builder.mergeFrom(builder.build()).setDescription(desc);
        return this;
    }

    public VariationDescriptorBuilder geneContext(GeneDescriptor gene) {
        builder = builder.mergeFrom(builder.build()).setGeneContext(gene);
        return this;
    }

    public VariationDescriptorBuilder vcfVecord(VcfRecord vcf) {
        builder = builder.mergeFrom(builder.build()).setVcfRecord(vcf);
        return this;
    }

    public VariationDescriptorBuilder xref(String xref) {
        builder = builder.mergeFrom(builder.build()).addXrefs(xref);
        return this;
    }

    public VariationDescriptorBuilder addAllXrefs(List<String> xrefs) {
        builder = builder.mergeFrom(builder.build()).addAllXrefs(xrefs);
        return this;
    }

    public VariationDescriptorBuilder alternateLabels(String altLabel) {
        builder = builder.mergeFrom(builder.build()).addAlternateLabels(altLabel);
        return this;
    }

    public VariationDescriptorBuilder addAllAlternateLabels(List<String> altLabels) {
        builder = builder.mergeFrom(builder.build()).addAllAlternateLabels(altLabels);
        return this;
    }

    public VariationDescriptorBuilder genomic() {
        builder = builder.mergeFrom(builder.build()).setMoleculeContext(MoleculeContext.genomic);
        return this;
    }

    public VariationDescriptorBuilder protein() {
        builder = builder.mergeFrom(builder.build()).setMoleculeContext(MoleculeContext.protein);
        return this;
    }

    public VariationDescriptorBuilder structuralType(OntologyClass clz) {
        builder = builder.mergeFrom(builder.build()).setStructuralType(clz);
        return this;
    }

    public VariationDescriptorBuilder heterozygous() {
        OntologyClass heterozygous = ontologyClass("GENO:0000135", "heterozygous");
        builder = builder.mergeFrom(builder.build()).setAllelicState(heterozygous);
        return this;
    }

    public VariationDescriptorBuilder homozygous() {
        OntologyClass heterozygous = ontologyClass("GENO:0000136", "homozygous");
        builder = builder.mergeFrom(builder.build()).setAllelicState(heterozygous);
        return this;
    }
    public VariationDescriptorBuilder hemizygous() {
        OntologyClass heterozygous = ontologyClass("GENO:0000134", "hemizygous");
        builder = builder.mergeFrom(builder.build()).setAllelicState(heterozygous);
        return this;
    }

    public VariationDescriptorBuilder hgvs(String value) {
        Expression expression = Expression.newBuilder()
                .setSyntax("hgvs")
                .setValue(value)
                .build();
        builder = builder.mergeFrom(builder.build()).addExpressions(expression);
        return this;
    }

    public VariationDescriptorBuilder spdi(String value) {
        Expression expression = Expression.newBuilder()
                .setSyntax("spdi")
                .setValue(value)
                .build();
        builder = builder.mergeFrom(builder.build()).addExpressions(expression);
        return this;
    }

    public VariationDescriptorBuilder iscn(String value) {
        Expression expression = Expression.newBuilder()
                .setSyntax("iscn")
                .setValue(value)
                .build();
        builder = builder.mergeFrom(builder.build()).addExpressions(expression);
        return this;
    }





    public VariationDescriptor build() {
        return builder.build();
    }

    public static VariationDescriptorBuilder create(String id) {
        return new VariationDescriptorBuilder(id);
    }
}
