package org.phenopackets.phenopackettools.builder.builders;


import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Quantity;
import org.phenopackets.schema.v2.core.ReferenceRange;
import org.phenopackets.schema.v2.core.Value;


public class ValueBuilder {

    private ValueBuilder() {
    }

    public static Value of(String id, String label) {
        OntologyClass ontologyClass = OntologyClassBuilder.ontologyClass(id, label);
        return of(ontologyClass);
    }

    public static Value of(OntologyClass ontologyClass) {
        return Value.newBuilder().setOntologyClass(ontologyClass).build();
    }

    public static Value of(Quantity quantity) {
        return Value.newBuilder().setQuantity(quantity).build();
    }


    /**
     * @param id The ontology term id of The assay used for the measurement
     * @param label The ontology term label of The assay used for the measurement
     * @param value The test result
     * @return  a Value without reference range
     */
    public static Value of(String id, String label, double value) {
        Quantity quantity = QuantityBuilder.of(id, label, value);
        return of(quantity);
    }

    /**
     * @param assay The assay used for the measurement (and for which the reference range applies)
     * @param value The test result
     * @return  a Value without reference range
     */
    public static Value of(OntologyClass assay, double value) {
        Quantity quantity = QuantityBuilder.of(assay, value);
        return of(quantity);
    }

    /**
     * @param assay The assay used for the measurement (and for which the reference range applies)
     * @param value The test result
     * @param ref The reference range for the test (with lower and upper limit of normal)
     * @return  a Value with reference range
     */
    public static Value of(OntologyClass assay, double value, ReferenceRange ref) {
        Quantity quantity = QuantityBuilder.of(assay, value, ref);
        return of(quantity);
    }

    /**
     * @param assay The assay used for the measurement (and for which the reference range applies)
     * @param value The test result
     * @param low The lower limit of normal for the test
     * @param high The upper limit of normal for the test
     * @return  a Value with reference range
     */
    public static Value of(OntologyClass assay, double value, double low, double high) {
        ReferenceRange ref = ReferenceRangeBuilder.of(assay, low, high);
        Quantity quantity = QuantityBuilder.of(assay, value, ref);
        return of(quantity);
    }

}
