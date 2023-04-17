.. _rsttutorialexamples:

====================
Example phenopackets
====================

A set of example phenopackets is distributed with the *phenopacket-tools* binary. The example files should be used
to demonstrate the tools' functionality.

The files are grouped in sub-folders::

  examples
  ├── convert
  ├── phenopackets
  └── validate
    ├── base
    ├── custom-json-schema
    ├── organ-systems
    └── phenotype-validation


Convert
^^^^^^^

The ``convert`` folder contains one v1 phenopacket to demonstrate the conversion functionality::

  Schreckenbach-2014-TPM3-II.2.json

The phenopacket describes a case report of a 45 years-old female diagnosed with
`NEMALINE MYOPATHY 1; NEM1 <https://www.omim.org/entry/609284>`_
caused by heterozygous mutation in `TPM3 <https://www.genenames.org/data/gene-symbol-report/#!/hgnc_id/HGNC:12012>`_.


Validate
^^^^^^^^

The ``validate`` directory contains files for demonstrating *off-the-shelf* phenopacket validation functionalities.


``base`` - base validation functionality
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The folder contains a few phenopackets for demonstrating the base validation functionality of *phenopacket-tools*;
the validation that **any phenopacket must pass**.

.. csv-table::
   :header: "File name", "Description"

   missing-fields.json,                "An invalid phenopacket with missing `id`, `subject.id` and `phenotypicFeatures[0].type.label` attributes."
   missing-fields-valid.json,          A valid version of the above phenopacket with IDs and the label.
   missing-resources.json,             An invalid phenopacket with missing `Resource` for the `NCBITaxon:9606` ontology concept used to represent organism of the subject.
   missing-resources-valid.json,       A valid version of the above phenopacket with the `Resource` for describing `NCBITaxon`.


.. _rstcustomjsonschematutorialexample:

``custom-json-schema`` - validate custom requirements
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

A bunch of phenopackets for showing how a custom JSON schema can be used to validate user-specific requirements.

.. csv-table::
   :header: "File name", "Description"

   hpo-rare-disease-schema.json,                      A custom JSON schema for enforcing user-specific requirements.
   marfan.no-phenotype.json,                          The phenopacket is *invalid* since it contains no phenotypic features.
   marfan.no-subject.json,                            The phenopacket is *invalid* since the `subject` is missing.
   marfan.no-time-at-last-encounter.json,             The phenopacket is *invalid* due to missing time at last encounter.
   marfan.not-hpo.json,                               The phenopacket is *invalid* because HPO terms are not used to represent phenotypic features.
   marfan.json,                                       A phenopacket that meets the custom requirements.


``organ-systems`` - validate annotation of organ systems
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Phenopackets for showing organ system validation. As an example, we work with phenopackets of patients with
`Marfan syndrome <https://hpo.jax.org/app/browse/disease/OMIM:154700>`_ and we require annotation
of
`Eye <https://hpo.jax.org/app/browse/term/HP:0000478>`_,
`Cardiovascular <https://hpo.jax.org/app/browse/term/HP:0001626>`_, and
`Respiratory <https://hpo.jax.org/app/browse/term/HP:0002086>`_ organ systems \
either by *excluding* the corresponding top-level HPO term or by adding a descendent term.

The phenopackets include

.. list-table::
   :header-rows: 1

   * - File name
     - Description
   * - marfan.no-abnormalities.json
     - A valid phenopacket of a proband with no abnormalities of the target organ systems.
   * - marfan.all-organ-system-annotated.json
     - A valid phenopacket of a proband who had an abnormality of eye and cardiovascular systems but
       no abnormality of respiratory system. Note that it is OK to have phenotypic feature of other organ system,
       such as Arachnodactyly in this case.
   * - marfan.missing-eye-annotation.json
     - An invalid phenopacket of a proband without any annotation of the eye.

``phenotype-validation`` - validate custom requirements
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Phenopackets for demonstrating ontology-based validation.

.. list-table::
   :header-rows: 1

   * - File name
     - Description
   * - marfan.annotation-propagation-rule.json
     - | Invalid phenopacket due to logical inconsistency in phenotypic features. The phenopacket contains
       | both *Aortic root aneurysm* and its ancestor *Aortic aneurysm*. Only the more specific term should be used.
   * - marfan.obsolete-term.json
     - The phenopacket is *invalid* because it contains an obsolete HPO term.
   * - marfan.json
     - A phenopacket that meets the phenotype validation requirements.

