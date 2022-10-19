package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.Resource;

public class Resources {

    private Resources() {
    }

    private static final Resource.Builder HPO_BUILDER = Resource.newBuilder()
            .setId("hp")
            .setName("human phenotype ontology")
            .setNamespacePrefix("HP")
            .setIriPrefix("http://purl.obolibrary.org/obo/HP_")
            .setUrl("http://purl.obolibrary.org/obo/hp.owl");

    private static final Resource.Builder GENO_BUILDER = Resource.newBuilder()
            .setId("geno")
            .setName("Genotype Ontology")
            .setNamespacePrefix("GENO")
            .setIriPrefix("http://purl.obolibrary.org/obo/GENO_")
            .setUrl("http://purl.obolibrary.org/obo/geno.owl");

    private static final Resource.Builder PATO_BUILDER = Resource.newBuilder()
            .setId("pato")
            .setName("PhenotypicFeature And Trait Ontology")
            .setNamespacePrefix("PATO")
            .setUrl("http://purl.obolibrary.org/obo/pato.owl")
            .setIriPrefix("http://purl.obolibrary.org/obo/PATO_");

    private static final Resource.Builder EFO_BUILDER = Resource.newBuilder()
            .setId("efo")
            .setName("Experimental Factor Ontology")
            .setNamespacePrefix("EFO")
            .setUrl("http://www.ebi.ac.uk/efo/efo.owl")
            .setIriPrefix("http://purl.obolibrary.org/obo/EFO_");

    private static final Resource.Builder CL_BUILDER = Resource.newBuilder()
            .setId("cl")
            .setName("Cell Ontology")
            .setNamespacePrefix("CL")
            .setUrl("http://purl.obolibrary.org/obo/cl.owl")
            .setIriPrefix("http://purl.obolibrary.org/obo/CL_");

    private static final Resource.Builder NCIT_BUILDER = Resource.newBuilder()
            .setId("ncit")
            .setName("NCI Thesaurus")
            .setNamespacePrefix("NCIT")
            .setUrl("http://purl.obolibrary.org/obo/ncit.owl")
            .setIriPrefix("http://purl.obolibrary.org/obo/NCIT_");

    private static final Resource.Builder MONDO_BUILDER = Resource.newBuilder()
                    .setId("mondo")
                    .setName("Mondo Disease Ontology")
                    .setUrl("http://purl.obolibrary.org/obo/mondo.obo")
                    .setIriPrefix("http://purl.obolibrary.org/obo/MONDO_")
                    .setNamespacePrefix("MONDO");

    private static final Resource.Builder UBERON_BUILDER = Resource.newBuilder()
            .setId("uberon")
            .setName("Uber-anatomy ontology")
            .setNamespacePrefix("UBERON")
            .setUrl("http://purl.obolibrary.org/obo/uberon.owl")
            .setIriPrefix("http://purl.obolibrary.org/obo/UBERON_");

    private static final Resource.Builder NCBI_TAXON_BUILDER = Resource.newBuilder()
            .setId("ncbitaxon")
            .setName("NCBI organismal classification")
            .setNamespacePrefix("NCBITaxon")
            .setUrl("http://purl.obolibrary.org/obo/ncbitaxon.owl")
            .setIriPrefix("http://purl.obolibrary.org/obo/NCBITaxon_");

    private static final Resource.Builder SO_BUILDER = Resource.newBuilder()
            .setId("so")
            .setName("Sequence types and features ontology")
            .setNamespacePrefix("SO")
            .setUrl("http://purl.obolibrary.org/obo/so.owl")
            .setIriPrefix("http://purl.obolibrary.org/obo/SO_");

    private static final Resource.Builder UO_BUILDER = Resource.newBuilder()
            .setId("uo")
            .setName("Units of measurement ontology")
            .setNamespacePrefix("UO")
            .setUrl("http://purl.obolibrary.org/obo/uo.owl")
            .setIriPrefix("http://purl.obolibrary.org/obo/UO_");

    private static final Resource.Builder HGNC_BUILDER = Resource.newBuilder()
            .setId("hgnc")
            .setName("HUGO Gene Nomenclature Committee")
            .setNamespacePrefix("HGNC")
            .setUrl("https://www.genenames.org")
            .setIriPrefix("https://www.genenames.org/data/gene-symbol-report/#!/hgnc_id/");

    public static Resource hgncVersion(String version) { return HGNC_BUILDER.setVersion(version).build(); }

    public static Resource hpoVersion(String version) {
        return HPO_BUILDER.setVersion(version).build();
    }

    public static Resource genoVersion(String version) {
        return GENO_BUILDER.setVersion(version).build();
    }

    public static Resource patoVersion(String version) {
        return PATO_BUILDER.setVersion(version).build();
    }

    public static Resource efoVersion(String version) {
        return EFO_BUILDER.setVersion(version).build();
    }

    public static Resource clVersion(String version) {
        return CL_BUILDER.setVersion(version).build();
    }

    public static Resource ncitVersion(String version) {
        return NCIT_BUILDER.setVersion(version).build();
    }

    public static Resource mondoVersion(String version) {
        return MONDO_BUILDER.setVersion(version).build();
    }

    public static Resource uberonVersion(String version) {
        return UBERON_BUILDER.setVersion(version).build();
    }

    public static Resource ncbiTaxonVersion(String version) {
        return NCBI_TAXON_BUILDER.setVersion(version).build();
    }

    public static Resource soVersion(String version) {
        return SO_BUILDER.setVersion(version).build();
    }

    public static Resource uoVersion(String version) {
        return UO_BUILDER.setVersion(version).build();
    }
}
