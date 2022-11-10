.. _rstconverting:


==========================
Converting V1 Phenopackets
==========================


The Phenopacket is a Global Alliance for Genomics and Health (GA4GH) standard for sharing disease and phenotype information.
To build the standard, requirements and specifications were established through a community effort.
Version 1 of the GA4GH standard was released in 2019 to elicit feedback from the community.
Version 2 was developed on the basis of this feedback and should be used henceforth. Version 2 has many additional fields compared to version 1, but
the fields used for reporting phenotype ontology terms are nearly identical and version 1 can easily be converted to version 2.
Version 1 had fields for reporting variants but did not specify how the variants related to disease diagnoses that were reported.

The conversion methods provided by the *phenopacket-tools* library does not convert the variants by default.
The variants can be converted under the assumption that only one disease was specified in the ``diseases`` field
of the version 1 phenopacket and that the reported variants are interpreted to be causal for the disease.
If this is not the case, then users would need to write new code to perform
the conversion according to the logic of their application.


To use library code for converting a phenopacket, adapt the following.

.. TODO - point to JavaDoc

.. code-block:: java

    boolean convertVariants = true; // or false, as desired
    V1ToV2Converter converter = V1ToV2Converter.of(convertVariants);
    Phenopacket v2 = converter.convertPhenopacket(v1Phenopacket);


Alternatively, use the ``convert`` command of the command-line interface.
Both of the following commands print output to the standard output.

.. code-block:: bash

    pfx convert -i ${examples}/convert/Schreckenbach-2014-TPM3-II.2.json
    pfx convert --convert-variants -i ${examples}/convert/Schreckenbach-2014-TPM3-II.2.json

.. note::
  The commands above assume `pfx` is an alias and ``${examples}`` points ot location of examples folder,
  both set up in :ref:`rsttutorial`.
