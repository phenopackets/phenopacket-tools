package org.phenopackets.phenopackettools.html;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Class to display information contained in a Phenopacket in HTML
 * @author Peter Robinson
 */
public class HtmlOutputter {

    private final Phenopacket phenopacket;
    private final String outname;

    enum Section {PROBAND, PHENOTYPICFEATURE, MEASUREMENT}

    public HtmlOutputter(Phenopacket phenopacket, String outname) {
        this.phenopacket = phenopacket;
        this.outname = outname;
    }

    public void write() {
        String phenopacketId = phenopacket.getId();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outname))) {
            writer.write(HtmlUtil.header(phenopacketId));
            writer.write(HtmlUtil.htmlTop());
            wrap(Section.PROBAND, writer);
            wrap(Section.PHENOTYPICFEATURE, writer);
            writer.write(HtmlUtil.htmlBottom());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startSection(Writer writer) throws IOException {
        writer.write("<section><article>\n");
    }

    private void endSection(Writer writer) throws IOException {
        writer.write("</section></article>\n");
    }

    private void wrap(Section section,Writer writer) throws IOException {
        startSection(writer);
        switch (section) {
            case PROBAND: outputProband(writer); break;
            case PHENOTYPICFEATURE: outputPhenotypicFeatures(writer);
        }
        endSection(writer);
    }

    private void outputProband(Writer writer) throws IOException {
        Individual proband = phenopacket.getSubject();
        String probandId = proband.getId(); // required

        List<String> thisRow = new ArrayList<>();
        thisRow.add(probandId);
        thisRow.add(proband.getSex().toString());
        if (proband.hasDateOfBirth()) {
            Timestamp dob = proband.getDateOfBirth();
            thisRow.add(Timestamps.toString(dob));
        } else {
            thisRow.add("-");
        }
        if (proband.hasTimeAtLastEncounter()) {
            String age = getTimeAge(proband.getTimeAtLastEncounter());
            thisRow.add(age);
        } else {
            thisRow.add("-");
        }
        if (proband.hasVitalStatus()) {
            thisRow.add( proband.getVitalStatus().getStatus().name());
        } else {
            thisRow.add("-");
        }
        if (proband.getKaryotypicSex().getNumber() >0) {
            thisRow.add(proband.getKaryotypicSex().toString());
        } else {
            thisRow.add("-");
        }
        if (proband.hasGender()) {
            OntologyClass clz = proband.getGender();
            thisRow.add(String.format("%s (%s)", clz.getLabel(), clz.getId()));
        } else {
            thisRow.add("-");
        }
        writer.write("<h2>Proband</h2>\n");
        writer.write("<table class=\"minimalistBlack\">\n");
        writer.write("<thead>");
        List<String> fields = List.of("id", "sex", "birthdate", "age", "vital status", "karyotypic_sex", "gender");
        for (var f: fields) {
            writer.write("<th>" + f + "</th>");
        }
        writer.write("</thead>\n");
        writer.write(row(thisRow));
        writer.write("</table>\n");
    }

    private void outputPhenotypicFeatures(Writer writer) throws IOException {
        List<PhenotypicFeature> phenotypicFeatureList = phenopacket.getPhenotypicFeaturesList();
        if (phenotypicFeatureList.isEmpty()) {
            writer.write("<p>No phenotypic features.</p>");
            return;
        }
        writer.write("<h2>Phenotypic Features</h2>\n");
        writer.write("<table class=\"minimalistBlack\">\n");
        writer.write("<thead>");
        List<String> fields = List.of("Term", "Id", "Status", "Modifiers", "onset", "resolution", "evidence");
        for (var f: fields) {
            writer.write("<th>" + f + "</th>");
        }
        writer.write("</thead>\n");
        for (var feature : phenotypicFeatureList) {
            List<String> pffields = getPhenotypicFeatureFields(feature);
            writer.write(row(pffields));
        }
        writer.write("</table>\n");
    }

    private String getOntologyTermList(List<OntologyClass> terms) {
        List<String> oclsList = new ArrayList<>();
        for (var ocls : terms) {
            oclsList.add(String.format("%s(%s)", ocls.getLabel(), ocls.getId()));
        }
        return String.join(";", oclsList);
    }

    private List<String> getPhenotypicFeatureFields(PhenotypicFeature feature) {
        List<String> fields = new ArrayList<>();
        OntologyClass ocls = feature.getType();
        fields.add(ocls.getLabel());
        fields.add(ocls.getId());
        if (feature.getExcluded()) {
            fields.add("Excluded");
        } else {
            fields.add("Observed");
        }
        if (feature.getModifiersCount() > 0) {
            fields.add(getOntologyTermList(feature.getModifiersList()));
        } else {
            fields.add("-");
        }
        if (feature.hasOnset()) {
            String timeAge = getTimeAge(feature.getOnset());
            fields.add(timeAge);
        } else {
            fields.add("-");
        }
        if (feature.hasResolution()) {
            String timeAge = getTimeAge(feature.getResolution());
            fields.add(timeAge);
        } else {
            fields.add("-");
        }
        if (feature.getEvidenceCount() > 0) {
            List<Evidence> evilist = feature.getEvidenceList();
            String evi = getEvidenceString(evilist);
            fields.add(evi);
        } else {
            fields.add("-");
        }
        return fields;
    }

    private String getEvidenceString(List<Evidence> evilist) {
        List<String> eviStrList = new ArrayList<>();
        for (var evi : evilist) {
            OntologyClass clz = evi.getEvidenceCode();
            eviStrList.add(String.format("%s", clz.getId()));

        }
        return String.join(";", eviStrList);
    }

    private String getTimeAge(TimeElement onset) {
        if (onset.hasAge()) {
            return onset.getAge().getIso8601Duration();
        } else if (onset.hasGestationalAge()) {
            GestationalAge ga = onset.getGestationalAge();
            return String.format("GA: %dW+%d D", ga.getWeeks(), ga.getDays());
        } else if (onset.hasAgeRange()) {
            AgeRange ar = onset.getAgeRange();
            String start = ar.getStart().getIso8601Duration();
            String end = ar.getEnd().getIso8601Duration();
            return String.format("%s - %s", start, end);
        } else if (onset.hasTimestamp()) {
            Timestamp ts = onset.getTimestamp();
            return Timestamps.toString(ts);
        } else if (onset.hasOntologyClass()) {
            OntologyClass clz = onset.getOntologyClass();
            return String.format("%s (%s)", clz.getLabel(), clz.getId());
        } else if (onset.hasInterval()) {
            TimeInterval ti = onset.getInterval();
            String start = Timestamps.toString(ti.getStart());
            String end = Timestamps.toString(ti.getEnd());
            return String.format("%s - %s", start, end);
        } else {
            // we have exhausted all possibilities and should never get here, but...
            return "N/A";
        }
    }

    private String row(List<String> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        for (var s : fields) {
            sb.append("<td>").append(s).append("</td>");
        }
        sb.append("</tr>\n");
        return sb.toString();
    }


}
