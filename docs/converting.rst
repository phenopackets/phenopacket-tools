.. _rstconverting:


==========================
Converting v1 Phenopackets
==========================


The requirements and specifications for building the GA4GH Phenopacket Schema were established incrementally
through a community effort.
Version 1 of the GA4GH standard was released in 2019 to elicit feedback from the community.
Version 2 was developed on the basis of this feedback and should be used henceforth. Version 2 has many additional fields compared to version 1, but
the fields used for reporting phenotype ontology terms are nearly identical and version 1 can easily be converted to version 2.
Version 1 had fields for reporting variants but did not specify how the variants related to disease diagnoses that were reported.

The conversion methods provided by the *phenopacket-tools* library do not convert the `Variant`\ s by default.
The variants can be converted under the assumption that only one `Disease` was specified in the ``diseases`` field
of the version 1 phenopacket and that the reported variants are interpreted to be *causal* for the disease.
If this is not the case, then users would need to write new code to perform
the conversion according to the logic of their application.

Example conversion
^^^^^^^^^^^^^^^^^^

To use library code for converting a phenopacket, adapt the following code:

..
  The code is at
  org.phenopackets.phenopackettools.converter.converters.V1ToV2ConverterTest.DocumentationTest.converterWorks

.. code-block:: java

  // Set up the converter
  boolean convertVariants = true; // or false, as desired
  V1ToV2Converter converter = V1ToV2Converter.of(convertVariants);

  // Get v1 phenopacket.
  org.phenopackets.schema.v1.Phenopacket v1 = TestData.V1.comprehensivePhenopacket();

  // Convert to v2 phenopacket.
  org.phenopackets.schema.v2.Phenopacket v2 = converter.convertPhenopacket(v1);

See also
^^^^^^^^

The API documentation of the conversion functionality is located in the
`org.phenopackets.phenopackettools.converter <http://phenopackets.org/phenopacket-tools/apidocs/org.phenopackets.phenopackettools.converter/module-summary.html>`_
module.
