package org.phenopackets.phenopackettools.builder.builders;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import org.ga4gh.vrsatile.v1.Extension;


public class Extensions {


  public static Extension mosaicism(double percentage) {
      String percentageString = String.format("%.1f%%", percentage);
      ByteString bstring =  ByteString.copyFromUtf8(percentageString);
      Any perc = Any.newBuilder().setValue(bstring).build();
      return Extension.newBuilder().setName("mosaicism").addValue(perc).build();
  }
}
