package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.HOMO_SAPIENS;
import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.fromRFC3339;

/**
 * An Individual in the version 2 phenopacket can have these attributes. Only the id is required.
 * <ol>
 *      <li>id (string)</li>
 *      <li>alternate_ids (a list)</li>
 *      <li>date_of_birth (timestamp)</li>
 *      <li>time_at_last_encounter (TimeElement) </li>
 *      <li>vital_status (VitalStatus)</li>
 *      <li>sex (Sex)</li>
 *      <li>karyotypic_sex (KaryotypicSex)</li>
 *      <li>taxonomy (Ontology term)</li>
 * </ol>
 * @author Peter N Robinson
 */
public class IndividualBuilder {

    private Individual.Builder builder;

    public IndividualBuilder(String id) {
        builder = Individual.newBuilder().setId(id);
    }

    public IndividualBuilder alternateId(String altId) {
        builder = builder.mergeFrom(builder.build()).addAlternateIds(altId);
        return this;
    }

    public IndividualBuilder alternateId(List<String> altIdList) {
        builder = builder.mergeFrom(builder.build()).addAllAlternateIds(altIdList);
        return this;
    }

    public IndividualBuilder dateOfBirth(String dobirth) {
        Timestamp dob = fromRFC3339(dobirth);
        builder.setDateOfBirth(dob);
        return this;
    }

    public IndividualBuilder timestampAtLastEncounter(String timestamp) {
        Timestamp stamp = fromRFC3339(timestamp);
        TimeElement t = TimeElement.newBuilder().setTimestamp(stamp).build();
        builder = builder.mergeFrom(builder.build()).setTimeAtLastEncounter(t);
        return this;
    }
    public IndividualBuilder ageAtLastEncounter(String iso8601) {
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

    public IndividualBuilder XX() {
        builder = builder.mergeFrom(builder.build()).setKaryotypicSex(KaryotypicSex.XX);
        return this;
    }

    public IndividualBuilder XY() {
        builder = builder.mergeFrom(builder.build()).setKaryotypicSex(KaryotypicSex.XY);
        return this;
    }

    public IndividualBuilder XO() {
        builder = builder.mergeFrom(builder.build()).setKaryotypicSex(KaryotypicSex.XO);
        return this;
    }

    public IndividualBuilder XXY() {
        builder = builder.mergeFrom(builder.build()).setKaryotypicSex(KaryotypicSex.XXY);
        return this;
    }

    public IndividualBuilder XXX() {
        builder = builder.mergeFrom(builder.build()).setKaryotypicSex(KaryotypicSex.XXX);
        return this;
    }

    public IndividualBuilder XXYY() {
        builder = builder.mergeFrom(builder.build()).setKaryotypicSex(KaryotypicSex.XXYY);
        return this;
    }

    public IndividualBuilder XXXY() {
        builder = builder.mergeFrom(builder.build()).setKaryotypicSex(KaryotypicSex.XXXY);
        return this;
    }

    public IndividualBuilder XXXX() {
        builder = builder.mergeFrom(builder.build()).setKaryotypicSex(KaryotypicSex.XXXX);
        return this;
    }

    public IndividualBuilder taxonomy(OntologyClass taxon) {
        builder = builder.mergeFrom(builder.build()).setTaxonomy(taxon);
        return this;
    }

    public IndividualBuilder homoSapiens() {
        builder = builder.mergeFrom(builder.build()).setTaxonomy(HOMO_SAPIENS);
        return this;
    }

    public Individual build() {
        return builder.build();
    }

    public static IndividualBuilder create(String id) {
        return new IndividualBuilder(id);
    }

}