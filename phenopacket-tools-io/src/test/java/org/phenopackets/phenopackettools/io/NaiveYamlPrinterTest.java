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

    private final NaiveYamlPrinter printer = NaiveYamlPrinter.getInstance();

    @Test
    public void printPhenopacket() throws Exception {
        Phenopacket pp = TestData.V1.comprehensivePhenopacket();
        printer.print(pp, Path.of("phenopacket.v1.yaml"));
    }

    @Test
    public void printFamily() throws Exception {
        Family pp = TestData.V1.comprehensiveFamily();
        printer.print(pp, Path.of("family.v1.yaml"));
    }

    @Test
    public void printCohort() throws Exception {
        Cohort pp = TestData.V1.comprehensiveCohort();
        printer.print(pp, Path.of("cohort.v1.yaml"));
    }

}