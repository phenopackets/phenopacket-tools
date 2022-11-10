.. _rsttutorial:

========
Tutorial
========

This tutorial walks through the installation of *phenopacket-tools* and provides an overview
of the command-line interface functionality.

Setup
=====

*Phenopacket-tools* is distributed as a ZIP archive that contains an executable JAR file
and several resource files for running this tutorial. Let's check that Java is installed on the machine,
download the distribution ZIP and set up an alias as a shortcut for running the *phenopacket-tools*.

Prerequisites
^^^^^^^^^^^^^

*Phenopacket-tools* is written in Java 17 and requires Java 17 or better to run. An appropriate Java executable
must be present on your ``$PATH``. Run the following to determine the availability and version of Java on your machine::

  java -version

which prints a similar output for Java 17::

  openjdk version "17" 2021-09-14
  OpenJDK Runtime Environment (build 17+35-2724)
  OpenJDK 64-Bit Server VM (build 17+35-2724, mixed mode, sharing)

Download *phenopacket-tools*
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

A prebuilt distribution ZIP file is available for download from
`phenopacket-tools release section <https://github.com/phenopackets/phenopacket-tools/releases>`_
of the GitHub repository.

Download and unpack the ZIP file from the releases section:

.. parsed-literal::

  URL=https://github.com/phenopackets/phenopacket-tools/releases/download/v\ |release|\ /phenopacket-tools-cli-|release|-distribution.zip
  curl -o phenopacket-tools-cli-|release|-distribution.zip ${URL}
  unzip phenopacket-tools-cli-|release|-distribution.zip

Set up alias
^^^^^^^^^^^^

In general, Java command line applications are invoked as ``java -jar executable.jar``. However, this is just
too verbose and we can shorten the command by defining an alias.

Let's define an alias for *phenopacket-tools*. Assuming the distribution ZIP was unpacked into
phenopacket-tools-cli-|release| directory, run the following to set up the alias:

.. parsed-literal::
  alias pxf="java -jar $(pwd)/phenopacket-tools-cli-\ |release|\ /phenopacket-tools-cli-|release|.jar"

Now, let's check that the alias works by printing the help message:

.. parsed-literal::
  pxf --help

Convert
=======

Version 1 of the GA4GH Phenopacket schema was released in 2019 to elicit community feedback.
In response to this feedback, the schema was extended and refined and version 2 was released in 2021
and published in 2022 by the International Standards Organization (ISO).

The `convert` command of *phenopacket-tools* converts version 1 phenopackets into version 2. In this tutorial,
we will convert 384 v1 phenopackets published by Robinson et al., 2020\ [1]_ into version 2. The phenopackets
represent 384 individuals described in published case reports with Human Phenotype Ontology terms,
causal genetic variants, and OMIM disease identifiers.

Let's start by downloading and unpacking the phenopacket dataset.
The phenopacket dataset is available for download from Zenodo\ [2]_. Then, we extract the archive content into
a folder named as ``v1``::

  curl -o phenopackets.v1.zip https://zenodo.org/record/3905420/files/phenopackets.zip
  unzip -d v1 phenopackets.v1.zip

Due to differences between version 1 and 2, there are two ways how to convert *v1* phenopackets into *v2*.
Briefly, the conversion either assumes that the `Variant`s are *causal* with respect to a `Disease` of the
v1 phenopacket, or skips conversion of `Variant`s altogether. The logic is controlled with ``--convert-variants``
CLI option and the conversion can be done iff the *v1* phenopacket has one `Disease`.
See the :ref:`rstconverting` section for more information.

Let's convert all *v1* phenopackets and store the results in JSON format in a new folder ``v2``::

  # Make the folder for converted phenopackets.
  mkdir -p v2

  # Convert the phenopackets.
  for pp in $(find v1 -name "*.json"); do
    pp_name=$(basename ${pp})
    pxf convert --convert-variants -i ${pp} > v2/${pp_name}
  done

  printf "Converted %s phenopackets\n" $(ls v2/ | wc -l)

We converted 384 phenopackets into *v2* format and stored the JSON files in the ``v2`` folder.

Validate
========

The `validate` command of *phenopacket-tools* validates correctness of phenopackets, families and cohorts.
This section focuses on the *off-the-shelf* phenopacket validators.
See the :ref:`rstvalidation` and the `Java Documentation`_ to learn how to implement a custom validator.

We will work with a suite of phenopackets that are bundled in the *phenopacket-tools* distribution ZIP file.
The phenopackets are located in `examples` folder next to the executable JAR file:

.. parsed-literal::
  examples=$(pwd)/phenopacket-tools-cli-\ |release|\ /examples

We will describe each validation and show an example validation errors and a proposed solution in a table.

.. note::
  The validation examples use `Phenopacket`\ s, but the validation functionality is available for all top-level Phenopacket Schema
  elements, including `Cohort` and `Family`.
.. note::
  The validation is implemented for *v2* phenopackets only. The *v1* phenopackets must be converted to *v2* prior
  running validation.


Base validation
^^^^^^^^^^^^^^^

First, let's check if the phenopackets meet the base requirements, as described by the Phenopacket Schema.
All phenopackets, regardless of their aim or scope must pass this requirement to be valid.

All required fields must be present
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The `BaseValidator` checks that all required fields are not empty::

  pxf validate -i ${examples}/base/missing-fields.json

The validator emits 3 lines with the following issues:

.. csv-table::
   :header: "Validation error", "Solution"
   :widths: 350, 550

   'id' is missing but it is required,                                Add the phenopacket ID
   'subject.id' is missing but it is required,                        Add the subject ID
   'phenotypicFeatures[0].type.label' is missing but it is required,  Add the `label` attribute into the `type` of the first phenotypic feature


All ontologies are defined
~~~~~~~~~~~~~~~~~~~~~~~~~~

Phenopacket Schema relies heavily on use of ontologies and ontology concepts. `MetaData` element lists
the ontologies used in the particular phenopacket.

The `MetaDataValidator` checks if the `MetaData` has an ontology `Resource` for all concepts used in the phenopacket::

  pxf validate -i ${examples}/base/missing-resources.json

The validator points out the absence of `NCBITaxon` definition:

.. csv-table::
  :header: "Validation error", "Solution"
  :widths: 350, 550

  No ontology corresponding to ID 'NCBITaxon:9606' found in MetaData, Add a `Resource` element with `NCBITaxon` definition into `MetaData`


Custom validation rules
^^^^^^^^^^^^^^^^^^^^^^^

Projects or consortia can enforce specific requirements by designing a custom JSON schema.
For instance, a rare disease project may require presence of several elements that are not required by the default schema:

1. Subject (proband being investigated)
2. At least one `PhenotypicFeature` element and using HPO terms for phenotypic features
3. Time at last encounter (sub-element of subject), representing the age of the proband

*Phenopacket-tools* ships with a JSON schema for enforcing the above requirements.
The schema is located at ``examples/custom-json-schema/hpo-rare-disease-schema.json``.

Using the custom JSON schema via ``--require`` option will point out issues in the 4 example phenopackets::

  pxf validate --require ${examples}/custom-json-schema/hpo-rare-disease-schema.json \
    -i ${examples}/custom-json-schema/marfan.no-subject.invalid.json \
    -i ${examples}/custom-json-schema/marfan.no-phenotype.invalid.json \
    -i ${examples}/custom-json-schema/marfan.not-hpo.invalid.json \
    -i ${examples}/custom-json-schema/marfan.no-time-at-last-encounter.invalid.json

.. csv-table::
  :header: "Validation error", "Solution"
  :widths: 350, 550

  'subject' is missing but it is required, Add the `Subject` element
  'phenotypicFeatures' is missing but it is required, Add at least one `PhenotypicFeature`
  'phenotypicFeatures[0].type.id' does not match the regex pattern ``^HP:\d{7}$``, Use Human Phenotype Ontology in `PhenotypicFeature`\ s
  'subject.timeAtLastEncounter' is missing but it is required, Add the time at last encounter field


Phenotype validation
^^^^^^^^^^^^^^^^^^^^

*Phenopacket-tools* offers a validator for checking logical consistency of phenotypic features in the phenopacket.
The phenotype validation requires the Human Phenotype Ontology (HPO) file to work.

.. note::
  The examples below assume that the latest HPO in JSON format has been downloaded to ``hp.json``.
  The HPO file can be downloaded from `HPO releases`_.


Phenopackets use non-obsolete term IDs
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The `HpoPhenotypeValidator` checks if the phenopacket contains obsolete HPO terms::

  pxf validate --hpo hp.json -i ${examples}/phenotype-validation/marfan.obsolete-term.invalid.json

It turns out that ``marfan.obsolete-term.invalid.json`` uses an obsolete ``HP:0002631`` instead of
the primary ``HP:0002616`` for *Aortic root aneurysm*:

.. csv-table::
  :header: "Validation error", "Solution"
  :widths: 350, 550

  Using obsolete id (HP:0002631) instead of current primary id (HP:0002616) in id-C, Replace the obsolete ID with the primary ID


The annotation-propagation rule is not violated
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Due to annotation propagation rule, it is a logical error to use both a term and its ancestor
(e.g. *Arachnodactyly* and *Abnormality of finger*).
When choosing HPO terms for phenotypic features, the *most* specific terms should be used for the *observed* clinical features.
In contrary, the *least* specific terms should be used for the *excluded* clinical features.

The `HpoAncestryValidator` checks that the annotation propagation rule is not violated::

  pxf validate --hpo hp.json -i ${examples}/phenotype-validation/marfan.annotation-propagation-rule.invalid.json

.. csv-table::
  :header: "Validation error", "Solution"
  :widths: 350, 550

  "Phenotypic features of id-C must not contain both an observed term (Aortic root aneurysm, HP:0002616) and an observed ancestor (Aortic aneurysm, HP:0004942)", Remove the less specific term

.. note::
  Presence of excluded descendant and observed ancestor does not violate the annotation propagation rule.
  A phenopacket with excluded *Aortic root aneurysm* and present *Aortic aneurysm* is valid,
  see ``marfan.valid.json``.

.. TODO - Organ system validation

.. [1] https://pubmed.ncbi.nlm.nih.gov/32755546
.. [2] https://zenodo.org/record/3905420
.. _Java Documentation: https://javadoc.io/doc/org.phenopackets.phenopackettools/phenopacket-tools-validator-core/latest/org.phenopackets.phenopackettools.validator.core/module-summary.html
.. _HPO releases: https://hpo.jax.org/app/data/ontology