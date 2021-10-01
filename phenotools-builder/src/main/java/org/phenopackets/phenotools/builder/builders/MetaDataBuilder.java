package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.Update;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.SCHEMA_VERSION;
import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.fromRFC3339;

public class MetaDataBuilder {



    private final MetaData.Builder builder;


    public MetaDataBuilder(String created, String createdBy) {
        builder = MetaData.newBuilder()
                .setCreated(fromRFC3339(created))
                .setCreatedBy(createdBy)
                .setPhenopacketSchemaVersion(SCHEMA_VERSION); // only one option for schema version!
    }


    public MetaDataBuilder submittedBy(String submitter) {
        builder.setSubmittedBy(submitter);
        return this;
    }

    public MetaDataBuilder addResource(Resource r) {
        builder.addResources(r);
        return this;
    }

    public MetaDataBuilder addUpdate(Update u) {
        builder.addUpdates(u);
        return this;
    }

    public MetaDataBuilder addExternalReference(ExternalReference er) {
        builder.addExternalReferences(er);
        return this;
    }

    public MetaDataBuilder addExternalReference(String id, String description) {
        ExternalReference er = ExternalReference.newBuilder().setId(id).setDescription(description).build();
        builder.addExternalReferences(er);
        return this;
    }

    public MetaDataBuilder hpWithVersion(String version) {
        Resource hp = Resource.newBuilder()
                 .setId("hp")
                .setName("human phenotype ontology")
                .setNamespacePrefix("HP")
                .setIriPrefix("http://purl.obolibrary.org/obo/HP_")
                .setUrl("http://purl.obolibrary.org/obo/hp.owl")
                .setVersion(version)
                .build();
        builder.addResources(hp);
        return this;
    }

    public MetaDataBuilder genoWithVersion(String version) {
        Resource geno = Resource.newBuilder()
                .setId("geno")
                .setName("Genotype Ontology")
                .setNamespacePrefix("GENO")
                .setIriPrefix("http://purl.obolibrary.org/obo/GENO_")
                .setUrl("http://purl.obolibrary.org/obo/geno.owl")
                .setVersion(version)
                .build();
        builder.addResources(geno);
        return this;
    }

    public MetaDataBuilder patoWithVersion(String version) {
        Resource pato = Resource.newBuilder()
                .setId("pato")
                .setName("PhenotypicFeature And Trait Ontology")
                .setNamespacePrefix("PATO")
                .setUrl("http://purl.obolibrary.org/obo/pato.owl")
                .setIriPrefix("http://purl.obolibrary.org/obo/PATO_")
                .setVersion(version)
                .build();
        builder.addResources(pato);
        return this;
    }

    public MetaDataBuilder efoWithVersion(String version) {
        Resource efo = Resource.newBuilder()
                .setId("efo")
                .setName("Experimental Factor Ontology")
                .setNamespacePrefix("EFO")
                .setUrl("http://www.ebi.ac.uk/efo/efo.owl")
                .setIriPrefix("http://purl.obolibrary.org/obo/EFO_")
                .setVersion(version)
                .build();
        builder.addResources(efo);
        return this;
    }

    public MetaDataBuilder clWithVersion(String version) {
        Resource cl = Resource.newBuilder()
                .setId("cl")
                .setName("Cell Ontology")
                .setNamespacePrefix("CL")
                .setUrl("http://purl.obolibrary.org/obo/cl.owl")
                .setIriPrefix("http://purl.obolibrary.org/obo/CL_")
                .setVersion(version)
                .build();
        builder.addResources(cl);
        return this;
    }


    public MetaDataBuilder ncitWithVersion(String version) {
        /*
         Ontology IRI: http://purl.obolibrary.org/obo/ncit.owl
Version IRI: http://purl.obolibrary.org/obo/ncit/releases/2021-05-25/ncit.owl
Ontology ID: ncit
Version:
Number of terms: 165673
Last loaded: Thu Jul 15 23:06:07 BST 2021
         */
        Resource cl = Resource.newBuilder()
                .setId("ncit")
                .setName("NCI Thesaurus")
                .setNamespacePrefix("NCIT")
                .setUrl("http://purl.obolibrary.org/obo/ncit.owl")
                .setIriPrefix("http://purl.obolibrary.org/obo/NCIT_")
                .setVersion(version)
                .build();
        builder.addResources(cl);
        return this;
    }


    public MetaDataBuilder uberonWithVersion(String version) {
        Resource uberon = Resource.newBuilder()
                 .setId("uberon")
                .setName("Uber-anatomy ontology")
                .setNamespacePrefix("UBERON")
                .setUrl("http://purl.obolibrary.org/obo/uberon.owl")
                .setIriPrefix("http://purl.obolibrary.org/obo/UBERON_")
                .setVersion(version)
                .build();
        builder.addResources(uberon);
        return this;
    }
    public MetaDataBuilder ncbiTaxonWithVersion(String version) {
        Resource ncbiTaxon = Resource.newBuilder()
                .setId("ncbitaxon")
                .setName("NCBI organismal classification")
                .setNamespacePrefix("NCBITaxon")
                .setUrl("http://purl.obolibrary.org/obo/ncbitaxon.owl")
                .setIriPrefix("http://purl.obolibrary.org/obo/NCBITaxon_")
                .setVersion(version)
                .build();
        builder.addResources(ncbiTaxon);
        return this;
    }

    public MetaData build() {
        return builder.build();
    }

    public static MetaDataBuilder create(String created, String createdBy) {
        return new MetaDataBuilder(created, createdBy);
    }
}
