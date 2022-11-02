package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class BiospecimenType {

  private static final OntologyClass BONE_MARROW_ASPIRATE = OntologyClassBuilder.ontologyClass("NCIT:C133261", "Bone Marrow Aspirate");
  private static final OntologyClass BLOOD_DNA = OntologyClassBuilder.ontologyClass("NCIT:C158416", "Blood DNA");
  private static final OntologyClass CSF_SAMPLE = OntologyClassBuilder.ontologyClass("NCIT:C185194", "Cerebrospinal Fluid Sample");
  private static final OntologyClass FORMALIN_FIXED_PARAFIN_DNA = OntologyClassBuilder.ontologyClass("NCIT:C156435", "Formalin-Fixed Paraffin-Embedded DNA");
  private static final OntologyClass BAL_FLUID = OntologyClassBuilder.ontologyClass("NCIT:C13195", "Bronchoalveolar Lavage Fluid");
  private static final OntologyClass PERICARDIAL_FLUID_SAMPLE = OntologyClassBuilder.ontologyClass("NCIT:C187062", "Pericardial Fluid Specimen");
  private static final OntologyClass PERTONIAL_FLUID_SAMPLE = OntologyClassBuilder.ontologyClass("NCIT:C185197", "Peritoneal Fluid Sample");
  private static final OntologyClass TOTAL_RNA = OntologyClassBuilder.ontologyClass("NCIT:C163995", "Total RNA");
  private static final OntologyClass TUMOR_TISSUE = OntologyClassBuilder.ontologyClass("NCIT:C18009", "Tumor Tissue");


  public static OntologyClass boneMarrowAspirate() { return BONE_MARROW_ASPIRATE; }
  public static OntologyClass bloodDNA() { return BLOOD_DNA; }
  public static OntologyClass cerebrospinalFluidSample() { return CSF_SAMPLE; }
  public static OntologyClass formalinFixedParaffinEmbeddedDNA() { return FORMALIN_FIXED_PARAFIN_DNA; }
  public static OntologyClass bronchoalveolarLavageFluid() { return BAL_FLUID; }
  public static OntologyClass pericardialFluidSpecimen() { return PERICARDIAL_FLUID_SAMPLE; }
  public static OntologyClass peritonealFluidSample() { return PERTONIAL_FLUID_SAMPLE; }
  public static OntologyClass totalRNA() { return TOTAL_RNA; }
  public static OntologyClass tumorTissue() { return TUMOR_TISSUE; }

}
