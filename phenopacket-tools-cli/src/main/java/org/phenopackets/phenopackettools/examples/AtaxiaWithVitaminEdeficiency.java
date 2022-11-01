package org.phenopackets.phenopackettools.examples;

import org.ga4gh.vrsatile.v1.GeneDescriptor;
import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;
import static org.phenopackets.phenopackettools.builder.constants.Assays.creatineKinaseActivity;
import static org.phenopackets.phenopackettools.builder.constants.Response.favorableResponse;
import static org.phenopackets.phenopackettools.builder.constants.SpatialPattern.generalized;
import static org.phenopackets.phenopackettools.builder.constants.Unit.*;

/**
 * Based on  Thapa, S, Shah, S, Chand, S, et al.
 * Ataxia due to vitamin E deficiency: A case report and updated review.
 * Clin Case Rep. 2022; 10:e06303. doi: 10.1002/ccr3.6303
 */
public class AtaxiaWithVitaminEdeficiency implements PhenopacketExample {


    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String INDIVIDUAL_ID = "16-year-old boy";

    private final Phenopacket phenopacket;

    public AtaxiaWithVitaminEdeficiency() {

       var externalRef = ExternalReferenceBuilder.of("doi:10.1002/ccr3.6303",
               "Ataxia due to vitamin E deficiency: A case report and updated review");

        var metadata = MetaDataBuilder.builder("2022-04-21T10:35:00Z", "anonymous biocurator")
               .addResource(Resources.ncitVersion("21.05d"))
                .addResource(Resources.hpoVersion("2022-06-11"))
                .addResource(Resources.mondoVersion("v2022-09-06"))
                .addResource(Resources.uberonVersion("2021-07-27"))
                .addExternalReference(externalRef)
               .build();

       Individual proband = IndividualBuilder.builder(INDIVIDUAL_ID).
               ageAtLastEncounter("P10Y").
               female().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addPhenotypicFeatures(getMedicalHistory())
                .addPhenotypicFeatures(getPhenotypicFeatures())
                .addDisease(getGeneralDiseaseDiagnosis())
                .addMeasurements(getMeasurements())
                .addInterpretation(fxnExcluded())
                .addInterpretation(aved())
                .addMedicalAction(vitaminEtreatment())
                .addMeasurement(getPostTreatmentMeasurement())
                .build();
    }

    /**
     * Upon presentation the patient was diagnosed with an atactic disorder
     * (We are not coding the mis-diagnosis Friedreich ataxia). This will be
     * made more precise by the genomic diagnosis in the Interpretation element.
     */
    private Disease getGeneralDiseaseDiagnosis() {
        return DiseaseBuilder
                .builder("MONDO:0100308","atactic disorder")
                .onset(TimeElements.childhoodOnset())
                .build();
    }

    /**
     * Vitamin A 	45 	20–60 mcg/dl
     * Vitamin E 	3.9 	6–10 mg/dl   //0.8−3.8
     * Creatinine kinase 	80 	10–120 mcg/l (Note, we are coding using the more common U/L)
     */
    private List<Measurement> getMeasurements() {
        TimeElement age16y = TimeElements.age("P16Y");
        var retinol = ontologyClass("LOINC:2923-1", "Retinol [Mass/volume] in Serum or Plasma");
        var retinolValue = ValueBuilder.of(microgramPerDeciliter(), 45, 20, 60);
        var m1 = MeasurementBuilder.builder(retinol, retinolValue).timeObserved(age16y).build();
        var tocopherol = ontologyClass("LOINC:1823-4","Alpha tocopherol [Mass/volume] in Serum or Plasma");
        var tocopherolValue = ValueBuilder.of(milligramPerDeciliter(), 3.9, 6, 10);
        var m2 = MeasurementBuilder.builder(tocopherol, tocopherolValue).timeObserved(age16y).build();
        var ckValue = ValueBuilder.of(enzymeUnitPerLiter(), 80, 22, 198);
        var m3 = MeasurementBuilder.builder(creatineKinaseActivity(), ckValue).timeObserved(age16y).build();
        return List.of(m1, m2, m3);
    }

    private Measurement getPostTreatmentMeasurement() {
        TimeElement age16y6m = TimeElements.age("P16Y6M");
        var tocopherol = ontologyClass("LOINC:1823-4","Alpha tocopherol [Mass/volume] in Serum or Plasma");
        var tocopherolValue = ValueBuilder.of(milligramPerDeciliter(), 17.34, 6, 10);
        return MeasurementBuilder.builder(tocopherol, tocopherolValue).timeObserved(age16y6m).build();


    }



    /**
     * Genetic analysis of the frataxin gene confirmed two alleles in the normal size
     * range and no evidence of an expansion. (frataxin is FXN, HGNC:3951). This
     * message encodes the fact that the FXN was excluded as the cause of disease.
     */
    private Interpretation fxnExcluded() {
        GeneDescriptor fxn = GeneDescriptorBuilder.of("HGNC:3951", "FXN");
        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder(INDIVIDUAL_ID);
        gbuilder.rejected();
        gbuilder.geneDescriptor(fxn);
        Diagnosis fa = DiagnosisBuilder.builder("MONDO:0100340", "Friedreich ataxia 1")
                .addGenomicInterpretation(gbuilder.build())
                .build();
        return InterpretationBuilder.of("interpretation.1",
                Interpretation.ProgressStatus.COMPLETED,
                fa,
                "Genetic analysis of the frataxin gene confirmed two alleles in the normal size range and no evidence of an expansion.");
    }

    /*
    There was a homozygous pathogenic frame shift mutation in the TTPA gene c.706del (p.[His236fs]),
    which results in loss of activity of the α-TTP.
    // not in ClinVar
    //NM_000370.3(TTPA):c.706del
    //NP_000361.1:p.(His236IlefsTer28)
    //NC_000008.11:g.63061383del
    // GRCh38:8:63061382:TG:T
     */
    private Interpretation aved() {
        AlleleBuilder abuilder = AlleleBuilder.builder();
        abuilder.sequenceId("refseq:NC_000008.11");
        abuilder.interbaseStartEnd( 63061382, 63061383);
        abuilder.altAllele("");
        VariationDescriptorBuilder vbuilder = VariationDescriptorBuilder.builder("variant.1")
                .variation(abuilder.buildVariation())
                .genomic()
                .homozygous()
                .label("NP_000361.1:p.(His236IlefsTer28)")
                .genomic()
                .vcfHg38("NC_000008.11", 63061382, "TG", "T")
                .geneContext(GeneDescriptorBuilder.of("HGNC:12404", "TTPA"))
                .addExpression(Expressions.hgvsCdna("NM_000370.3(TTPA):c.706del"))
                .addExpression(Expressions.transcriptReference("NM_000370.3"));
        // wrap in VariantInterpretation
        VariantInterpretationBuilder vibuilder = VariantInterpretationBuilder.builder(vbuilder);
        vibuilder.pathogenic();
        vibuilder.actionable();

        GenomicInterpretationBuilder gbuilder = GenomicInterpretationBuilder.builder(INDIVIDUAL_ID);
        gbuilder.causative();
        gbuilder.variantInterpretation(vibuilder);


        Diagnosis diagnosis = DiagnosisBuilder.builder("MONDO:0010188", "familial isolated deficiency of vitamin E")
                .addGenomicInterpretation(gbuilder.build())
                .build();
        return  InterpretationBuilder.builder("interpretation.2")
                .solved(diagnosis);
    }



    /**
     * His gait difficulties were associated with wasting of his muscles and slurring of speech.
     * Pregnancy and birth history were normal. Family history was insignificant for any neurological disease.
     * There was no parent consanguinity or gastrointestinal symptoms. Based on the clinical course of the patient,
     * he was initially diagnosed as Friedreich ataxia. Neurological examination showed a broad-based gait which
     * was clearly ataxic. His broad-based gait was associated with tongue fasciculation, intentional tremors,
     * and dysdiadokokinesia. Upper and lower limb examination confirmed cerebellar ataxia with intention tremor and
     * absent deep tendon reflexes. He was noted to have ankle clonus. Motor examination revealed decreased
     * extremity strength with 3/5 in the lower extremities versus 3/5 in the upper extremities.
     * Joint position sensation, vibration sense, sensitivity, and cranial nerve function were normal.
     * No head titubation or abnormal movement was observed. Intellectual abilities were adequate for the age of the
     * patient. All others to systemic examination were unremarkable. His height and weight were normal for his age group.
     * <p>
     * The results of the patient's routine blood chemistries, electrocardiography, echocardiography, brain magnetic resonance, visual evoked potentials, and troncular evoked auditory potentials had no pathologic alterations. Ophthalmologic examination revealed simple myopia.
     */
    private List<PhenotypicFeature> getPhenotypicFeatures() {
        String iso8601age = "P16Y";
        var pf1 = PhenotypicFeatureBuilder.builder("HP:0002066","Gait ataxia")
                .isoISO8601onset(iso8601age).build();
        var pf2 = PhenotypicFeatureBuilder.builder("HP:0001308","Tongue fasciculations")
                .isoISO8601onset(iso8601age).build();
        var pf3 = PhenotypicFeatureBuilder.builder("HP:0002080","Intention tremor")
                .isoISO8601onset(iso8601age).build();
        var pf4 = PhenotypicFeatureBuilder.builder("HP:0002075","Dysdiadochokinesis")
                .isoISO8601onset(iso8601age).build();
        var pf5 = PhenotypicFeatureBuilder.builder("HP:0001251","Ataxia")
                .isoISO8601onset(iso8601age).build();
        var pf6 = PhenotypicFeatureBuilder.builder("HP:0001284","Areflexia")
                .isoISO8601onset(iso8601age).build();
        var pf7 = PhenotypicFeatureBuilder.builder("HP:0011448","Ankle clonus")
                .isoISO8601onset(iso8601age).build();
        var pf8 = PhenotypicFeatureBuilder.builder("HP:0003690","Limb muscle weakness")
                .isoISO8601onset(iso8601age).build();
        var pf9 = PhenotypicFeatureBuilder.builder("HP:0003474","Somatic sensory dysfunction")
                .excluded()
                .isoISO8601onset(iso8601age).build();
        var pf10 = PhenotypicFeatureBuilder.builder("HP:0002599","Head titubation")
                .excluded()
                .isoISO8601onset(iso8601age).build();
        var pf11 = PhenotypicFeatureBuilder.builder("HP:0031910","Abnormal cranial nerve physiology")
                .excluded()
                .isoISO8601onset(iso8601age).build();
        return List.of(pf1,pf2,pf3, pf4, pf5, pf6, pf7, pf8, pf9, pf10,pf11);
    }

    /**
     * -
     * Treatment was started with 1800 mg/d oral vitamin E.
     * His vitamin E level improved to 17.34 mg/dl.
     * He was discharged from the hospital and on follow-up, after a month
     * he was improving and had no fresh issues.
     */
    private MedicalAction vitaminEtreatment() {
        OntologyClass vitE = ontologyClass("DrugCentral:257", "Vitamin E");
        TreatmentBuilder tbuilder = TreatmentBuilder.oralAdministration(vitE);
        return MedicalActionBuilder.builder(tbuilder.build())
                .responseToTreatment(favorableResponse())
                .build();
    }

    /**
     * he was 10-years old with progressive gait difficulties and generalized weakness of the body.
     */
    private List<PhenotypicFeature> getMedicalHistory() {
        String iso8601age = "P10Y";
        var gaitDisturbance = PhenotypicFeatureBuilder.builder("HP:0001288", "Gait disturbance")
                .isoISO8601onset(iso8601age)
                .build();
        var weakness = PhenotypicFeatureBuilder.builder("HP:0001324", "Muscle weakness")
                .addModifier(generalized())
                .isoISO8601onset(iso8601age)
                .build();
        return List.of(gaitDisturbance, weakness);
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }

}
