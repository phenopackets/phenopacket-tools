package org.phenopackets.phenopackettools.io;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.test.TestData;
import org.phenopackets.schema.v1.Cohort;
import org.phenopackets.schema.v1.Family;
import org.phenopackets.schema.v1.Phenopacket;

import java.nio.file.Path;

@Disabled
public class NaiveYamlPrinterTest {

    @Test
    public void printPhenopacket() throws Exception {
        NaiveYamlPrinter<Phenopacket> printer = NaiveYamlPrinter.getInstance();
        Phenopacket pp = TestData.V1.comprehensivePhenopacket();
        printer.print(pp, Path.of("phenopacket.v1.yaml"));
    }

    @Test
    public void printFamily() throws Exception {
        NaiveYamlPrinter<Family> printer = NaiveYamlPrinter.getInstance();
        Family pp = TestData.V1.comprehensiveFamily();
        printer.print(pp, Path.of("family.v1.yaml"));
    }

    @Test
    public void printCohort() throws Exception {
        NaiveYamlPrinter<Cohort> printer = NaiveYamlPrinter.getInstance();
        Cohort pp = TestData.V1.comprehensiveCohort();
        printer.print(pp, Path.of("cohort.v1.yaml"));
    }

}