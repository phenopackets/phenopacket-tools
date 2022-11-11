.. _rstvalidation:


=======================
Validating Phenopackets
=======================

Phenopackets schema uses protobuf, an exchange format developed in 2008 by Google. We refer readers to the excellent
`Wikipedia page <https://en.wikipedia.org/wiki/Protocol_Buffers>`_
on Protobuf and to `Google’s documentation <https://developers.google.com/protocol-buffers/>`_ for details.
In Protobuf (version 3, which is what the Phenopacket Schema uses), all fields are optional.
However, the Phenopacket Schema defines certain fields to be optional
(See `documentation <https://phenopacket-schema.readthedocs.io/en/latest/index.html>`_ for details).
Moreover, projects and consortia can require application of specific constraints and requirements for the phenopackets.

*Phenopacket-tools* provides a functionality for validating phenopackets.

This document provides a comprehensive description of the functionality of the *off-the-shelf* validators
as well as the validation workflow API.

Validation workflow
^^^^^^^^^^^^^^^^^^^

*Phenopacket-tools* defines an API for phenopacket validation workflow. The workflow is consists of
a list of validation steps. There are two types of steps: *syntax* and *semantic*. The syntax steps check syntax
and cardinality of each component separately. The semantic validators are run after syntax checks and validate
the components in the context of the entire phenopacket.

There is one mandatory syntax validation step that is always run first: the *base* validation. The base validation
ensures the phenopacket message meets the requirements of the Phenopacket Schema.

The results of the validation are aggregated into a container object that consists
of immutable value objects that describe the performed validations and the validation results suitable
for reporting back to the user.

.. Additional constraints and requirements may be made for phenopackets that are used in a specific
   project or for a specific collaboration or consortium. For instance, a rare-disease consortium
   may require that all phenotypic features be recorded using valid HPO terms. An example class is
   provided that checks all ``PhenotypicFeature`` elements, ensures that they use HPO terms with valid
   (i.e., primary) id's, and checks whether both a term and an ancestor of the term are used - if so
   a warning is emitted, because an annotation with a specific HPO term
   (e.g., `Perimembranous ventricular septal defect <https://hpo.jax.org/app/browse/term/HP:0011682>`_)
   implies all of the ancestors of the term (e.g., a patient with perimembranous VSD by necessity also has
   `Ventricular septal defect <https://hpo.jax.org/app/browse/term/HP:0001629>`_).

**API**

See the ``TODO - add JavaDoc link`` for the API documentation.

.. TODO - refer to org.phenopackets.phenopackettools.validator.jsonschema module
.. Describe validation workflow in general

*Off-the-shelf* validators
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. TODO - continue

TODO - describe *off-the-shelf* validators in great detail.

.. _rstbasevalidation:

Base validation
~~~~~~~~~~~~~~~

All phenopackets should be tested against the base JSON Schema (analogously for all ``Family`` and ``Cohort`` messages).
In code, this can be implemented as follows.

.. code-block:: java

    JsonSchemaValidator validator = PhenopacketWorkflowRunnerBuilder.getBaseRequirementsValidator();
    Path phenopacketPath = ...; // get Path to a JSON file representing a GA4GH phenopacket

    try (InputStream is = Files.newInputStream(phenopacketPath)) {
        ValidationResults results = validator.validate(is);
        List<ValidationResult> validationResults = results.validationResults();
        if (validationResults.isEmpty()) {
            System.out.printf("%s - OK%n", fileName);
        } else {
            for (ValidationResult result : validationResults) {
                System.out.printf("%s [%s] - %s: %s%n", fileName, result.level(), result.category(), result.message());
            }
        }
    } catch (IOException e)
         System.out.println("Error opening the phenopacket: " + e);
    }

**API**

See the ``TODO - add JavaDoc link`` for the API documentation.

.. TODO - refer to ... and to org/phenopackets/phenopackettools/validator/core/metadata

.. _rstphenotypevalidation:

Phenotype validation
~~~~~~~~~~~~~~~~~~~~

TODO - write
.. TODO - continue

**API**

See the ``TODO - add JavaDoc link`` for the API documentation.

.. TODO - refer to org/phenopackets/phenopackettools/validator/core/phenotype

.. _rstcustomvalidation:

Custom validation
~~~~~~~~~~~~~~~~~

TODO - write
.. TODO - continue


**API**

See the ``TODO - add JavaDoc link`` for the API documentation.

.. TODO - refer to TODO - somewhere in JSON-schema validation


.. _rstorgsysvalidation:

Organ system validation
~~~~~~~~~~~~~~~~~~~~~~~

TODO - write
.. TODO - continue

We can validate presence of annotation for specific organ systems in a phenopacket.

As an example, we work with toy phenopackets that represent patients with
`Marfan syndrome <https://hpo.jax.org/app/browse/disease/OMIM:154700>`_. Due to the nature of the Marfan syndrome,
we may require annotation of three organ systems:

* Eye
* Cardiovascular system
* Respiratory system

The annotation is done either by *excluding* the corresponding top-level HPO term or by adding a descendent term:

.. list-table::
   :header-rows: 1

   * - Organ system
     - Top-level HPO term
     - Example descendent
   * - Eye
     - `Abnormality of the eye <https://hpo.jax.org/app/browse/term/HP:0000478>`_
     - `Ectopia lentis <https://hpo.jax.org/app/browse/term/HP:0001083>`_
   * - Cardiovascular system
     - `Abnormality of the cardiovascular system <https://hpo.jax.org/app/browse/term/HP:0001626>`_
     - `Mitral regurgitation <https://hpo.jax.org/app/browse/term/HP:0001653>`_
   * - Respiratory system
     - `Abnormality of the respiratory system <https://hpo.jax.org/app/browse/term/HP:0002086>`_
     - `Pneumothorax <https://hpo.jax.org/app/browse/term/HP:0002107>`_

**API**

See the ``TODO - add JavaDoc link`` for the API documentation.

.. TODO - refer to org/phenopackets/phenopackettools/validator/core/phenotype/orgsys


