package org.phenopackets.phenopackettools.test;

/**
 * A class to serve <em>comprehensive</em> but not necessarily medically correct top-level elements of
 * the Phenopacket schema, mainly usable for testing.
 */
public class TestData {

    private TestData() {
    }

    public static class V1 {

        private V1() {
        }

        public static org.phenopackets.schema.v1.Phenopacket comprehensivePhenopacket() {
            return org.phenopackets.phenopackettools.test.V1.comprehensivePhenopacket();
        }

        public static org.phenopackets.schema.v1.Family comprehensiveFamily() {
            return org.phenopackets.phenopackettools.test.V1.comprehensiveFamily();
        }

        public static org.phenopackets.schema.v1.Cohort comprehensiveCohort() {
            return org.phenopackets.phenopackettools.test.V1.comprehensiveCohort();
        }

    }

    public static class V2 {

        private V2() {
        }

        public static org.phenopackets.schema.v2.Phenopacket comprehensivePhenopacket() {
            return org.phenopackets.phenopackettools.test.V2.comprehensivePhenopacket();
        }

        public static org.phenopackets.schema.v2.Family comprehensiveFamily() {
            return org.phenopackets.phenopackettools.test.V2.comprehensiveFamily();
        }

        public static org.phenopackets.schema.v2.Cohort comprehensiveCohort() {
            return org.phenopackets.phenopackettools.test.V2.comprehensiveCohort();
        }

    }
}
