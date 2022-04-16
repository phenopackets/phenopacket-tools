package org.phenopackets.phenopackettools.html;

import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

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
            writer.write(HtmlUtil.bottom());
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
        Map<String, String> kv = new TreeMap<>();
        kv.put("id", probandId);
        kv.put("sex", proband.getSex().name());
        if (proband.hasVitalStatus()) {
            kv.put("vital status", proband.getVitalStatus().getStatus().name());
        }
        writer.write("<h2>Proband</h2>\n");
        writer.write("<table class=\"minimalistBlack\">\n");
        for (var e : kv.entrySet()) {
            writer.write(row2(e.getKey(), e.getValue()));
        }
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
        }

        return "todo";
    }


    private String row2(String s1, String s2) {
        return String.format("<tr><td>%s</td><td>%s</td></tr>\n", s1, s2);
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


    private void outputSection(Writer writer, String Title, Map<String, String> kv) {

    }




}
