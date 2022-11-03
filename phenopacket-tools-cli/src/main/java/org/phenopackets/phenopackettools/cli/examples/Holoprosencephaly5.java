package org.phenopackets.phenopackettools.cli.examples;

import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Status;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.TimeElement;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

/**
 * Case report based on Xiong J, et al.
 * Case report: a novel mutation in ZIC2 in an infant with microcephaly, holoprosencephaly, and arachnoid cyst.
 * Medicine (Baltimore). 2019 Mar;98(10):e14780.
 * doi: 10.1097/MD.0000000000014780. PMID: 30855487; PMCID: PMC6417543.
 * Note that we add single ventricle and semilobar HPE as congenital because these are only congenital defects
 * even if the observation age was the MRI at 9 months
 * @author peter.robinson@jax.org
 */
public class Holoprosencephaly5 implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "1";
    private static final String INTERPRETATION_ID = "arbitrary interpretation id";
    private static final String PROBAND_ID = "nine-month old infant";

    private static final TimeElement nineMonths = TimeElements.age("P9M");

    private static final TimeElement gestationalAge40w = TimeElements.gestationalAge(40);

    private static final OntologyClass holoprosencephaly5 = ontologyClass("OMIM:609637", "Holoprosencephaly 5");

    private final Phenopacket phenopacket;

    public Holoprosencephaly5() {
        var authorAssertion = EvidenceBuilder.authorStatementEvidence("PMID:30855487", "Xiong J, et al., 2019");
        var individual = IndividualBuilder.builder(PROBAND_ID).female().ageAtLastEncounter("P9M").build();
        var metaData = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.hpoVersion("2021-08-02"))
                .addResource(Resources.genoVersion("2020-03-08"))
                .addExternalReference(authorAssertion.getReference())
                .build();
        var vcfRecord = VcfRecordBuilder.of("GRCh38",
        "NC_000013.11",
       99983133,
        "C",
         "G");
        var variationDescriptor =
                VariationDescriptorBuilder.builder("variant id")
                        .heterozygous()
                        .hgvs("NM_007129.3:c.1069C>G")
                        .geneContext(GeneDescriptor.newBuilder().setSymbol("ZIC2").setValueId("HGNC:12873 ").build())
                        .vcfRecord(vcfRecord)
                        .build();
        var zic2VariantInterpretation = VariantInterpretationBuilder.of(variationDescriptor, Status.pathogenic());
        var genomicInterpretation =
                GenomicInterpretationBuilder.builder("genomic interpretation id")
                    .causative()
                        .variantInterpretation(zic2VariantInterpretation)
                        .build();
        var diagnosis = DiagnosisBuilder.builder(holoprosencephaly5).addGenomicInterpretation(genomicInterpretation).build();
        var interpretation = InterpretationBuilder.builder(INTERPRETATION_ID).completed(diagnosis);
        var arachnoidCyst =
                PhenotypicFeatureBuilder.builder("HP:0100702", "Arachnoid cyst")
                        .onset(nineMonths)
                        .addEvidence(authorAssertion)
                        .build();
        var cerebellarAtrophy =
                PhenotypicFeatureBuilder.builder("HP:0001272", "Cerebellar atrophy")
                        .onset(nineMonths)
                        .addEvidence(authorAssertion)
                        .build();
        var gdd =  PhenotypicFeatureBuilder.builder("HP:0001263", "Global developmental delay")
                .infantileOnset()
                .addEvidence(authorAssertion)
                .build();
        var hydrocephalus = PhenotypicFeatureBuilder.builder("HP:0000238", "Hydrocephalus")
                .onset(gestationalAge40w)
                .addEvidence(authorAssertion)
                .build();
        var hyperreflexia = PhenotypicFeatureBuilder.builder("HP:0001347", "Hyperreflexia")
                .onset(gestationalAge40w)
                .addEvidence(authorAssertion)
                .build();
        var semilobarHPE = PhenotypicFeatureBuilder.builder("HP:0002507", "Semilobar holoprosencephaly")
                .congenitalOnset()
                .addEvidence(authorAssertion)
                .build();
        var singleVentricle = PhenotypicFeatureBuilder.builder("HP:0001750", "Single ventricle")
                .onset(gestationalAge40w)
                .addEvidence(authorAssertion)
                .build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metaData)
                .individual(individual)
                .addPhenotypicFeature(hydrocephalus)
                .addPhenotypicFeature(semilobarHPE)
                .addPhenotypicFeature(singleVentricle)
                .addPhenotypicFeature(cerebellarAtrophy)
                .addPhenotypicFeature(arachnoidCyst)
                .addPhenotypicFeature(cerebellarAtrophy)
                .addPhenotypicFeature(gdd)
                .addPhenotypicFeature(hyperreflexia)
                .addInterpretation(interpretation)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}
