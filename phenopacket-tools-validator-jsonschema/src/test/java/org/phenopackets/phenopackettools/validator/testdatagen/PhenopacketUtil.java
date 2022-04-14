package org.phenopackets.phenopackettools.validator.testdatagen;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;


import org.phenopackets.schema.v2.core.Resource;
import org.phenopackets.schema.v2.core.Sex;
import org.phenopackets.schema.v2.core.TimeElement;
import org.phenopackets.schema.v2.core.Update;
import org.phenopackets.schema.v2.core.VitalStatus;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.Age;
import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.File;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;


public class PhenopacketUtil {
    public static final String SCHEMA_VERSION = "2.0";
    public static final OntologyClass CONGENITAL_ONSET = ontologyClass("HP:0003577", "Congenital onset");
    public static final OntologyClass CHILDHOOD_ONSET = ontologyClass("HP:0011463", "Childhood onset");
    public static final OntologyClass ADULT_ONSET = ontologyClass("HP:0003581", "Adult onset");
    public static final OntologyClass SEVERE = ontologyClass("HP:0012828", "Severe");

    public static ExternalReference externalReference(String id, String description) {
        return ExternalReference.newBuilder().setId(id).setDescription(description).build();
    }

    public static OntologyClass ontologyClass(String termid, String label) {
        return OntologyClass.newBuilder()
            .setId(termid)
            .setLabel(label)
            .build();
    }


    public static Resource resource(String id, String name, String nsPrefix, String iriPrefix, String url, String version) {
        return Resource.newBuilder()
        .setId(id)
        .setName(name)
        .setNamespacePrefix(nsPrefix)
        .setIriPrefix(iriPrefix)
        .setUrl(url)
        .setVersion(version)
        .build();
    }

    public static PhenotypicFeature phenotypicFeatureCongenital(String termid, String label) {
        return PhenotypicFeature.newBuilder()
            .setType(ontologyClass(termid, label))
            .setType(CONGENITAL_ONSET)
            .build();
    }

    public static PhenotypicFeature phenotypicFeatureChildhood(String termid, String label) {
        return PhenotypicFeature.newBuilder()
            .setType(ontologyClass(termid, label))
            .setType(CHILDHOOD_ONSET)
            .build();
    }



    /**
     * This has convenience methods for building PhenotypicFeature messages with some
     * commonly used options.
     * @author Peter N Robinson
     */
    public static class PhenotypicFeatureBuilder {
        private final String termid;
        private final String label;
        private OntologyClass onset = null;
        private OntologyClass severity = null;
        private final List<OntologyClass> modifiers = new ArrayList<>();

        public PhenotypicFeatureBuilder(String id, String label) {
            this.termid = id;
            this.label = label;
        }

        public PhenotypicFeatureBuilder onset(String id, String label) {
            onset = ontologyClass(id, label);
            return this;
        }

        public PhenotypicFeatureBuilder congenitalOnset() {
            onset = CONGENITAL_ONSET;
            return this;
        }

        public PhenotypicFeatureBuilder childhoodOnset() {
            onset = CHILDHOOD_ONSET;
            return this;
        }

        public PhenotypicFeatureBuilder adultOnset() {
            onset = ADULT_ONSET;
            return this;
        }

        public PhenotypicFeatureBuilder severity(String id, String label) {
            severity = ontologyClass(id, label);
            return this;
        }

        public PhenotypicFeatureBuilder severe() {
            severity = SEVERE;
            return this;
        }

        public PhenotypicFeature build() {
            OntologyClass hpoTerm = ontologyClass(termid, label);
            PhenotypicFeature.Builder builder = PhenotypicFeature.newBuilder().setType(hpoTerm);
            if (this.onset != null) {
                builder.mergeFrom(builder.build()).setOnset(TimeElement.newBuilder().setOntologyClass(onset));
            }
            if (this.severity != null) {
                builder.mergeFrom(builder.build()).setSeverity(severity);
            }
            if (! this.modifiers.isEmpty()) {
                for (OntologyClass clz : this.modifiers) {
                    builder.mergeFrom(builder.build()).addModifiers(clz);
                }
            }

            return builder.build();
        }

        public static PhenotypicFeatureBuilder create(String id, String label) {
            return new PhenotypicFeatureBuilder(id, label);
        }
    }

   

    public static Resource hpoResource(String version) {
        return resource("hp", "Human Phenotype Ontology", "HP", "http://purl.obolibrary.org/obo/HP_","http://purl.obolibrary.org/obo/hp.owl",version);
    }

    public static Resource mondoResource(String version) {
        return resource("mondo", "Mondo Disease Ontology", "Mondo", "http://purl.obolibrary.org/obo/mondo_","http://purl.obolibrary.org/obo/mondo.owl",version);
    }

    /**
     * Parse a google protobuf timestamp object from a string in RFC 3339 format (e.g., 2019-10-12T07:20:50.52Z)
     * @param tstamp RFC 3339 string
     * @return corresponding protobuf timestamp object
     */
    public static Timestamp fromRFC3339(String tstamp) {
        try {
            return Timestamps.parse(tstamp);
        } catch (Exception e) {
            LocalDateTime timeNow = LocalDateTime.now();
            return Timestamp.newBuilder()
            .setSeconds(timeNow.toEpochSecond(ZoneOffset.UTC))
            .build();
        }
    }
 

    public static class MetaDataBuilder {
       

        private MetaData.Builder builder;


        public MetaDataBuilder(String created, String createdBy) {
            builder = MetaData.newBuilder()
                .setCreated(fromRFC3339(created))
                .setCreatedBy(createdBy)
                .setPhenopacketSchemaVersion(SCHEMA_VERSION); // only one option for schema version!
        }


        public static MetaDataBuilder create(String created, String createdBy) {
            return new MetaDataBuilder(created, createdBy);
        }

        public MetaDataBuilder submittedBy(String submitter) {
            builder = builder.mergeFrom(builder.build()).setSubmittedBy(submitter);
            return this;
        }

        public MetaDataBuilder addResource(Resource r) {
            builder = builder.mergeFrom(builder.build()).addResources(r);
            return this;
        }

        public MetaDataBuilder addUpdate(Update u) {
            builder = builder.mergeFrom(builder.build()).addUpdates(u);
            return this;
        }

        public MetaDataBuilder addExternalReference(ExternalReference er) {
            builder = builder.mergeFrom(builder.build()).addExternalReferences(er);
            return this;
        }

        public MetaDataBuilder addExternalReference(String id, String description) {
            ExternalReference er = ExternalReference.newBuilder().setId(id).setDescription(description).build();
            builder = builder.mergeFrom(builder.build()).addExternalReferences(er);
            return this;
        }

        public MetaData build() {
            return builder.build();
        }

    }


    public static class FileBuilder {

        private final File.Builder builder;

        public FileBuilder(String uri) {
            builder = File.newBuilder().setUri(uri);
        }

        public FileBuilder addFileAttribute(String k, String v) {
            builder.mergeFrom(builder.build()).putFileAttributes(k, v);
            return this;
        }

        public FileBuilder addIndividualToFileIdentifiers(String individual, String fileIdentifier) {
            builder.mergeFrom(builder.build()).putIndividualToFileIdentifiers(individual, fileIdentifier);
            return this;
        }

        public File build() {
            return builder.build();
        }

        public static FileBuilder create(String uri) {
            return new FileBuilder(uri);
        }

        public static FileBuilder hg38vcf(String uri) {
            FileBuilder fb = new FileBuilder(uri);
            fb.addFileAttribute("genomeAssembly", "GRCh38");
            fb.addFileAttribute("fileFormat", "vcf");
            return fb;
        }
    }

    public static class IndividualBuilder {

        private Individual.Builder builder;

        public IndividualBuilder(String id) {
            builder = Individual.newBuilder().setId(id);
        }

        public IndividualBuilder dateOfBirth(String dobirth) {
            Timestamp dob = fromRFC3339(dobirth);
            builder = builder.mergeFrom(builder.build()).setDateOfBirth(dob);
            return this;
        }


        public IndividualBuilder timeAtLastEncounter(String iso8601) {
            TimeElement t = TimeElement.newBuilder().setAge(Age.newBuilder().setIso8601Duration(iso8601)).build();
            builder = builder.mergeFrom(builder.build()).setTimeAtLastEncounter(t);
            return this;
        }

        public IndividualBuilder alive() {
            VitalStatus status = VitalStatus.newBuilder().setStatus(VitalStatus.Status.ALIVE).build();
            builder = builder.mergeFrom(builder.build()).setVitalStatus(status);
            return this;
        }

        public IndividualBuilder deceased() {
            VitalStatus status = VitalStatus.newBuilder().setStatus(VitalStatus.Status.DECEASED).build();
            builder = builder.mergeFrom(builder.build()).setVitalStatus(status);
            return this;
        }

        public IndividualBuilder male() {
            builder = builder.mergeFrom(builder.build()).setSex(Sex.MALE);
            return this;
        }

        public IndividualBuilder female() {
            builder = builder.mergeFrom(builder.build()).setSex(Sex.FEMALE);
            return this;
        }


        public Individual build() {
            return builder.build();
        }

        public static IndividualBuilder create(String id) {
            return new IndividualBuilder(id);
        }

    }


}
