package org.phenopackets.phenotools.examples;

import org.phenopackets.schema.v2.Phenopacket;

public class PhenopacketExamples {

    private static final Phenopacket BETHLEM_MYOPATHY = new BethlehamMyopathy().getPhenopacket();
    private static final Phenopacket THROMBOCYTOPENIA = new Thrombocytopenia2().getPhenopacket();
    private static final Phenopacket MARFAN_SYNDROME = new Marfan().getPhenopacket();
    private static final Phenopacket AML = new Aml().getPhenopacket();
    private static final Phenopacket SCC = new SquamousCellCancer().getPhenopacket();
    private static final Phenopacket UROTHELIAL_CARCINOMA = new UrothelialCancer().getPhenopacket();
    private static final Phenopacket COVID_19 = new Covid().getPhenopacket();
    private static final Phenopacket RETINOBLASTOMA = new Retinoblastoma().getPhenopacket();

    private PhenopacketExamples() {
    }

    public static Phenopacket bethlemMyopathy() {
        return BETHLEM_MYOPATHY;
    }

    public static Phenopacket thrombocytopenia2() {
        return THROMBOCYTOPENIA;
    }

    public static Phenopacket marfanSyndrome() {
        return MARFAN_SYNDROME;

    }

    public static Phenopacket acuteMyeloidLeukemia() {
        return AML;
    }

    public static Phenopacket squamousCellEsophagealCarcinoma() {
        return SCC;
    }

    public static Phenopacket urothelialCarcinoma() {
        return UROTHELIAL_CARCINOMA;
    }

    public static Phenopacket covid19() {
        return COVID_19;
    }

    public static Phenopacket retinoblastoma() { return RETINOBLASTOMA; }
}
