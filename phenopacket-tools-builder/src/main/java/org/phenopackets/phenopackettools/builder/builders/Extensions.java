package org.phenopackets.phenopackettools.builder.builders;

import org.ga4gh.vrsatile.v1.Extension;


public class Extensions {

    public static final String MOSAICISM = "mosaicism";
    public static final String ALLELE_FREQUENCY = "allele-frequency";

  public static Extension mosaicism(double percentage) {
      String percentageString = String.format("%.1f%%", percentage);
      return Extension.newBuilder().setName(MOSAICISM).setValue(percentageString).build();
  }

    /**
     * @param frequency estimated frequency (IN PERCENT) of an allele (generally a somatic mutation)
     */
  public static Extension alleleFrequency(double frequency) {
      String percentageString = String.format("%.1f%%", frequency);
      return Extension.newBuilder().setName(ALLELE_FREQUENCY).setValue(percentageString).build();
  }


}
