.. _rsttutorial:

========
Tutorial
========

This tutorial walks through the installation of *phenopacket-tools* command-line interface application
and provides an overview of the *conversion* of phenopackets from `v1` to the current `v2` format and
the *validation* functionality, including custom validation rules.

We show *phenopacket-tools* functionality within a Unix shell due to the fact that Unix is the predominant computational
environment in bioinformatics. We use several Unix-specific concepts such as I/O redirection to demonstrate
use of *phenopacket-tools* in a process pipeline, and we declare environment variables and command aliases
to reduce the amount of boilerplate code. Note that *phenopacket-tools* is a cross-platform tool and
will work on Windows shells with the appropriate adjustments.


Setup
=====

*Phenopacket-tools* is written in Java 17 and requires Java 17 or better to run.
We distribute the CLI application as a ZIP archive with an executable Java Archive (JAR) file
and several examples for running this tutorial.


Prerequisites
^^^^^^^^^^^^^

Java 17 or newer must be present on your ``$PATH``. Run the following to check the availability
and version of Java on your machine::

  java -version

The command should print a similar output::

  openjdk version "17" 2021-09-14
  OpenJDK Runtime Environment (build 17+35-2724)
  OpenJDK 64-Bit Server VM (build 17+35-2724, mixed mode, sharing)


Download *phenopacket-tools*
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

A prebuilt distribution ZIP file is available for download from the
`release section <https://github.com/phenopackets/phenopacket-tools/releases>`_
of our GitHub repository.
Use your favorite web browser to download the ZIP archive with the latest release |release| and unpack the archive
into a folder of your choice.

.. _rstsetupaliastutorial:


Set up alias
^^^^^^^^^^^^

In general, Java command line applications are invoked as ``java -jar executable.jar``. However, such incantation is
a bit too verbose and we can shorten it a bit by defining an alias.

Let's define a command alias for *phenopacket-tools*. Assuming the distribution ZIP was unpacked into
phenopacket-tools-cli-|release| directory, run the following to set up and check the command alias:

.. parsed-literal::
  alias pxf="java -jar $(pwd)/phenopacket-tools-cli-\ |release|\ /phenopacket-tools-cli-|release|.jar"
  pxf --help

.. note::
  From now on, we will use the ``pxf`` alias instead of the longer form.


Set up examples directory
^^^^^^^^^^^^^^^^^^^^^^^^^

We will demonstrate *phenopacket-tools* functionality using a collection of example phenopackets that are bundled
in the distribution ZIP file. The folder with the phenopacket collection resides next to the *phenopacket-tools*
JAR file and has the following structure::

  examples
    |- convert
      \ - Schreckenbach-2014-TPM3-II.2.json
    |- phenopackets
      \ - retinoblastoma.json
    \- validate
      | - ...
      \ - ...

To reduce the amount of boilerplate code in the following sections, let's define an environment variable to point
to the example phenopacket directory::

  examples=path/to/examples

Make sure you set the variable to the actual path in your environment.

.. note::
  See :ref:`rsttutorialexamples` for detailed info of the example phenopackets.


Set up autocompletion
^^^^^^^^^^^^^^^^^^^^^

As a quick way to increase the user convenience, *phenopacket-tools* offers autocompletion for completing the command
or options after pressing the `TAB` key on Bash or ZSH Unix shells.

Run the following to enable the autocompletion for the tutorial session:

.. parsed-literal::
  source <(pxf generate-completion)

.. note::
  See the :ref:`rstcli` for setting up the autocompletion to last beyond the current shell session.


Convert
=======

Version 1 of the GA4GH Phenopacket schema was released in 2019 to elicit community feedback.
In response to this feedback, the schema was extended and refined and version 2 was released in 2021
and published in 2022 by the International Standards Organization (ISO).
The `convert` command of *phenopacket-tools* converts version 1 phenopackets into version 2.

For the purpose of this tutorial, we will first convert a single v1 phenopacket
and then 384 v1 phenopackets published by Robinson et al., 2020\ [1]_.


Convert single phenopacket
^^^^^^^^^^^^^^^^^^^^^^^^^^

Due to differences between phenopacket versions 1 and 2, there are two ways how to convert v1 phenopackets into
the v2 format.
Briefly, the conversion either assumes that the `Variant`\ s are *causal* with respect to a `Disease` of the
v1 phenopacket, or skips conversion of `Variant`\ s altogether. The logic is controlled with ``--convert-variants``
CLI option and the conversion can be done iff the v1 phenopacket has one `Disease`.

.. note::
  See the :ref:`rstconverting` section for more information.

Let's convert an example v1 phenopacket ``Schreckenbach-2014-TPM3-II.2.json`` to v2 format::

  pxf convert ${examples}/convert/Schreckenbach-2014-TPM3-II.2.json > Schreckenbach-2014-TPM3-II.2.v2.json

The example phenopacket represents a case report with several variants that are causal with respect to the disease.
Therefore, we can use ``--convert-variants`` to convert `Variant`\ s into v2 `Interpretation` element::

  pxf convert --convert-variants ${examples}/convert/Schreckenbach-2014-TPM3-II.2.json \
    > Schreckenbach-2014-TPM3-II.2.v2-with-variants.json


A real-life example
^^^^^^^^^^^^^^^^^^^

Let's convert 384 individuals described in published case reports with Human Phenotype Ontology terms,
causal genetic variants, and OMIM disease identifiers.

Let's start by downloading and unpacking the phenopacket dataset.
The phenopacket dataset is available for download from Zenodo\ [2]_. Then, we extract the archive content into
a folder named as ``v1``::

  curl -o phenopackets.v1.zip https://zenodo.org/record/3905420/files/phenopackets.zip
  unzip -d v1 phenopackets.v1.zip

Now, we convert all v1 phenopackets and store the results in JSON format in a new folder ``v2``::

  # Create a folder for the v2 phenopackets.
  mkdir -p v2

  # Convert the phenopackets.
  for pp in $(find v1 -name "*.json"); do
    pp_name=$(basename ${pp})
    pxf convert --convert-variants ${pp} > v2/${pp_name}
  done

  printf "Converted %s phenopackets\n" $(ls v2/ | wc -l)


Validate
========

The `validate` command of *phenopacket-tools* validates correctness of phenopackets, families and cohorts.
This section outlines usage of the off-the-shelf validators available in the CLI application.

We will describe each validation and show an example validation errors and a proposed solution in a table.
The validation examples use `Phenopacket`\ s, but the validation functionality is available for all top-level Phenopacket Schema
elements, including `Cohort` and `Family`.

The validation is implemented for *v2* phenopackets only. The *v1* phenopackets must be converted to *v2* prior
running validation.


Base validation
^^^^^^^^^^^^^^^

First, let's check if the phenopackets meet the base requirements, as described by the Phenopacket Schema.
All phenopackets, regardless of their aim or scope must pass this requirement to be valid.

.. note::
  See :ref:`rstbasevalidation` for more details.

All required fields must be present
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The `BaseValidator` checks that all required fields are present::

  pxf validate ${examples}/validate/base/missing-fields.json

The validator will find 3 errors and emit 3 CSV lines with the following issues:

.. csv-table::
   :header: "Validation error", "Solution"
   :widths: 350, 550

   'id' is missing but it is required,                                Add the phenopacket ID
   'subject.id' is missing but it is required,                        Add the subject ID
   'phenotypicFeatures[0].type.label' is missing but it is required,  Add the `label` attribute into the `type` of the first phenotypic feature

.. note::
  The ``validate`` command reports errors in CSV format the validation results can be easily stored in a CSV file by
  using output stream redirection. Use the ``-H | --include-header`` option to include a header
  with validation metadata.

All ontologies are well-defined
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Phenopacket Schema relies heavily on use of ontologies and ontology concepts. `MetaData` element lists
the ontologies used in the particular phenopacket. To ensure data traceability, Phenopacket Schema requires
phenopacket to contain a `Resource` with ontology metadata such as version and IRI for each used ontology concept.

The `MetaDataValidator` checks if the `MetaData` has an ontology `Resource` for all used ontology concepts::

  pxf validate ${examples}/validate/base/missing-resources.json

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
The schema is located next to phenopacket examples for this section
at ``examples/custom-json-schema/hpo-rare-disease-schema.json``.

Using the custom JSON schema via ``--require`` option will point out issues in the 4 example phenopackets::

  pxf validate --require ${examples}/validate/custom-json-schema/hpo-rare-disease-schema.json \
    ${examples}/validate/custom-json-schema/marfan.no-subject.json \
    ${examples}/validate/custom-json-schema/marfan.no-phenotype.json \
    ${examples}/validate/custom-json-schema/marfan.not-hpo.json \
    ${examples}/validate/custom-json-schema/marfan.no-time-at-last-encounter.json

.. csv-table::
  :header: "Validation error", "Solution"
  :widths: 350, 550

  'subject' is missing but it is required, Add the `Subject` element
  'phenotypicFeatures' is missing but it is required, Add at least one `PhenotypicFeature`
  'phenotypicFeatures[0].type.id' does not match the regex pattern ``^HP:\d{7}$``, Use Human Phenotype Ontology in `PhenotypicFeature`\ s
  'subject.timeAtLastEncounter' is missing but it is required, Add the time at last encounter field

.. note::
  See :ref:`rstcustomvalidation` for more details.


.. _rstphenotypevalidationtutorial:

Phenotype validation
^^^^^^^^^^^^^^^^^^^^

*Phenopacket-tools* offers a validator for checking logical consistency of clinical abnormalities in the phenopacket.
The validator assumes Human Phenotype Ontology (HPO) is used to represent the clinical abnormalities and
the phenotype validation requires the HPO file to work.

.. note::
  The examples below assume that the latest HPO in JSON format has been downloaded to ``hp.json``.
  Get the HPO JSON from `HPO releases`_.

.. note::
  See :ref:`rstphenotypevalidation` for more details.


Phenopackets use non-obsolete term IDs
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The `HpoPhenotypeValidator` points out if the phenopacket contains obsolete HPO terms::

  pxf validate --hpo hp.json ${examples}/validate/phenotype-validation/marfan.obsolete-term.json

It turns out that ``marfan.obsolete-term.json`` uses an obsolete ``HP:0002631`` instead of
the primary ``HP:0002616`` for *Aortic root aneurysm*:

.. csv-table::
  :header: "Validation error", "Solution"
  :widths: 350, 550

  Using obsolete id (HP:0002631) instead of current primary id (HP:0002616) in id-C, Replace the obsolete ID with the primary ID


The annotation-propagation rule is not violated
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Due to the annotation propagation rule, it is a logical error to use both a term and its ancestor
(e.g. *Arachnodactyly* and *Abnormality of finger*) for annotation of a single item.
When choosing HPO terms for phenotypic features, the *most* specific terms should be used for the *observed* clinical features.
In contrary, the *least* specific terms should be used for the *excluded* clinical features.
There is one exception to these rules: a term and its ancestor can co-exist in the phenopacket if the parent term
is *observed* and the child term is *excluded* (e.g. phenopacket with present *Aortic aneurysm*
but excluded *Aortic root aneurysm*, see ``marfan.valid.json``).

The `HpoAncestryValidator` checks that the annotation propagation rule is not violated::

  pxf validate --hpo hp.json \
  ${examples}/validate/phenotype-validation/marfan.annotation-propagation-rule.json \
  ${examples}/validate/phenotype-validation/marfan.valid.json


.. csv-table::
  :header: "Validation error", "Solution"
  :widths: 350, 550

  "Phenotypic features of id-C must not contain both an observed term (Aortic root aneurysm, HP:0002616) and an observed ancestor (Aortic aneurysm, HP:0004942)", Remove the ancestor term


Annotation of organ systems
^^^^^^^^^^^^^^^^^^^^^^^^^^^

We can validate presence of annotation for specific organ systems in a phenopacket.

Using the term IDs of the top-level HPO terms, we can validate annotation of
`Eye <https://hpo.jax.org/app/browse/term/HP:0000478>`_,
`Cardiovascular <https://hpo.jax.org/app/browse/term/HP:0001626>`_, and
`Respiratory <https://hpo.jax.org/app/browse/term/HP:0002086>`_ organ systems
in 3 phenopackets of toy `Marfan syndrome <https://hpo.jax.org/app/browse/disease/OMIM:154700>`_ patients::

  pxf validate --hpo hp.json \
     --organ-system HP:0000478 --organ-system HP:0001626 --organ-system HP:0002086 \
    ${examples}/validate/organ-systems/marfan.all-organ-system-annotated.json \
    ${examples}/validate/organ-systems/marfan.missing-eye-annotation.json \
    ${examples}/validate/organ-systems/marfan.no-abnormalities.json

.. note::
  Organ system validation requires HPO ontology. See the :ref:`rstphenotypevalidationtutorial` for more details about getting
  the HPO file.

The `HpoOrganSystemValidator` will point out one error in the `marfan.missing-eye-annotation.json` phenopacket:

.. csv-table::
   :header: "Validation error", "Solution"
   :widths: 350, 550

   Missing annotation for Abnormality of the eye [HP:0000478] in id-C, Annotate the eye or exclude any abnormality.

.. note::
  See :ref:`rstorgsysvalidation` for more details regarding the organ system validation.

That's it, you made it to the end of the *phenopacket-tools* tutorial! We set up the command-line application
and covered the conversion and validation functionality. The next section provides an in-depth explanation
of the CLI functionality.


.. [1] https://pubmed.ncbi.nlm.nih.gov/32755546
.. [2] https://zenodo.org/record/3905420
.. _Java Documentation: https://javadoc.io/doc/org.phenopackets.phenopackettools/phenopacket-tools-validator-core/latest/org.phenopackets.phenopackettools.validator.core/module-summary.html
.. _HPO releases: https://hpo.jax.org/app/data/ontology