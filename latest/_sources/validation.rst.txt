.. _rstvalidation:


=======================
Validating Phenopackets
=======================

Phenopackets schema uses protobuf, an exchange format developed in 2008 by Google. We refer readers to the excellent
`Wikipedia page <https://en.wikipedia.org/wiki/Protocol_Buffers>`_
on Protobuf and to `Google’s documentation <https://developers.google.com/protocol-buffers/>`_ for details.
In Protobuf (version 3, which is what the Phenopacket Schema uses), all fields are *optional*.
However, the Phenopacket Schema defines certain fields as *required*.
(See `documentation <https://phenopacket-schema.readthedocs.io/en/latest/index.html>`_ for details).
Moreover, projects and consortia can require application of specific constraints and requirements for the phenopackets.

*Phenopacket-tools* provides an extensible API for validation of all schema components,
including a model of validation workflow and validation results.

This document outlines the validation workflow API and demonstrates how to use the off-the-shelf validators present
in the *phenopacket-tools* library.

Validation workflow
^^^^^^^^^^^^^^^^^^^

The validation workflow consists of a list of steps. The workflow includes a mandatory *base* validation step that
validates syntax and cardinality of each component, to verify the basic requirements of Phenopacket Schema,
such as presence of identifier fields and metadata. The base validation is implemented using JSON schema
and Java code (``MetaDataValidator``).

The workflow can be extended by any number of validation steps for checking specific logical or semantic requirements.
*Phenopacket-tools* offers an API for the validation steps to allow encoding custom validation logic as well
as several off-the-shelf validators.

The central element of the validation API is ``PhenopacketValidator<T extends MessageOrBuilder>`` that represents
a single validation step. The validator is generic over ``T`` where ``T`` must be a top-level element
of the Phenopacket Schema: ``Phenopacket``, ``Family``, or ``Cohort``.
The validator is identified by ``ValidationInfo`` with the name, type and description of the validation functionality.
The validation reports any errors as ``ValidationResult`` objects, one result per error.
The execution of the workflow is orchestrated by the ``ValidationWorkflowRunner``. The runner applies the validators
in the correct order, ensuring that the base validation is done first, and gathers the results into
a ``ValidationResults`` container. The container represents the results of the validation as immutable value objects,
``ValidatorInfo``, ``ValidationResult``, suitable for reporting back to the user.

.. _rstbasevalidation:

Base validation workflow
~~~~~~~~~~~~~~~~~~~~~~~~

Let's demonstrate setting up of the base validation workflow for phenopacket validation.

..
  The code below is at
  org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunnerTest.DocumentationTest.simpleValidationWorkflowRunner

.. code-block:: java

  ValidationWorkflowRunner<PhenopacketOrBuilder> runner = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                                                             .build();

The `JsonSchemaValidationWorkflowRunner` provides factory methods for getting a workflow runner builder
for phenopacket, family, and cohort. The validation workflow can be extended by calling builder methods.
However, in this case we are only interested in the base validation, hence we conclude the building
by calling the `build()` method.

As a convenience, the `runner` can validate phenopacket in several different input types:

.. code-block:: java

  // A path
  Path path = Path.of("bethlem-myopathy.json");
  ValidationResults results = runner.validate(path);

  // An input stream
  try (InputStream is = new FileInputStream(path.toFile())) {
      results = runner.validate(is);
  }

  // A byte array
  try (InputStream is = new FileInputStream(path.toFile())) {
      results = runner.validate(is.readAllBytes());
  }

  // A JSON/YAML string
  try (BufferedReader reader = Files.newBufferedReader(path)) {
      String jsonString = reader.lines().collect(Collectors.joining(System.lineSeparator()));
      results = runner.validate(jsonString);
  }

  // Or a phenopacket.
  PhenopacketParser parser = PhenopacketParserFactory.getInstance()
                               .forFormat(PhenopacketSchemaVersion.V2);
  Phenopacket phenopacket = (Phenopacket) parser.parse(path);
  results = runner.validate(phenopacket);

``JsonSchemaValidationWorkflowRunner`` provides static factory method for getting builders for all top-level elements
of Phenopacket Schema:

..
  The code below is at
  org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunnerTest.DocumentationTest.availableBuilders

.. code-block:: java

  ValidationWorkflowRunner<PhenopacketOrBuilder> phenopacket = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
          .build();

  ValidationWorkflowRunner<FamilyOrBuilder> family = JsonSchemaValidationWorkflowRunner.familyBuilder()
          .build();

  ValidationWorkflowRunner<CohortOrBuilder> cohort = JsonSchemaValidationWorkflowRunner.cohortBuilder()
          .build();


To validation workflow can be introspected by calling `validators()` method:

..
  The code below is at
  org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunnerTest.DocumentationTest.workflowIntrospection

.. code-block:: java

  ValidationWorkflowRunner<PhenopacketOrBuilder> runner = JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
          .build();

  List<ValidatorInfo> validators = runner.validators();

`ValidationResults`
~~~~~~~~~~~~~~~~~~~

The validation returns `ValidationResults`, a container object that aggregates issues discovered by all validators
of the workflow.

..
  The code below is at
  org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunnerTest.DocumentationTest.validationResults

.. code-block:: java

  ValidationResults results = runner.validate(path);

The `results.isValid()` returns `true` if no validation errors were discovered:

.. code-block:: java

  assert results.isValid();

The `results` has information regarding the applied validation checks:

.. code-block:: java

  List<ValidatorInfo> validators = results.validators();

and the discovered issues (if any):

.. code-block:: java

  List<ValidationResult> issues = results.validationResults();


`ValidationResult`
~~~~~~~~~~~~~~~~~~

`ValidationResult` represents a single validation issue. The object is a simple POJO/value object with several attributes:

..
  The code below is at
  org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunnerTest.DocumentationTest.validationResults

.. code-block:: java

  ValidationResult issue = issues.get(0);

  // The validator that pointed out the issue.
  ValidatorInfo validatorInfo = issue.validatorInfo();

  // The issue severity (warning or error).
  ValidationLevel level = issue.level();

  // Category of the issue, useful for grouping the issues.
  // One validator can produce issues with different categories.
  String category = issue.category();

  // A message targeted for the user.
  String message = issue.message();


The API documentation of the core validation API can be found in the
`org.phenopackets.phenopackettools.validator.core <http://phenopackets.org/phenopacket-tools/apidocs/org.phenopackets.phenopackettools.validator.core/module-summary.html>`_
module.


.. _rstofftheshelfvalidation:

*Off-the-shelf* validators
^^^^^^^^^^^^^^^^^^^^^^^^^^

Additional constraints and requirements may be made for phenopackets that are used in a specific
project or for a specific collaboration or consortium.

For instance, one may want to require that phenopackets made for rare-disease diagnostics include age of the proband
and use Human Phenotype Ontology (HPO) terms to represent phenotypic features. Additionally, one may want
to enforce requirements that are difficult to encode using JSON Schema, such as that only a valid term id is used
(currently, the HPO has over 16,000 terms), or that the phenopacket does not encode both a term
and a parent or ancestor of the term, or that the phenopacket annotates or excludes abnormality
in selected organ systems.


Here we use a bunch of *off-the-shelf* validators from *phenopacket-tools* to show a validation workflow for checking
the above requirements. Let's start by creating a validation workflow runner builder:

.. code-block:: java

  JsonSchemaValidationWorkflowRunnerBuilder<PhenopacketOrBuilder> builder = JsonSchemaValidationWorkflowRunner.phenopacketBuilder();


.. _rstcustomvalidation:

Custom validation
~~~~~~~~~~~~~~~~~

*Phenopacket-tools* offers a validator for enforcing custom requirements encoded as JSON schema.
In the context of the example above, a project may require including age of the proband
and use HPO terms to represent phenotypic features, as well as presence of a certain number
of phenotypic features.

Let's write a JSON schema document ``hpo-rare-disease-schema.json`` to enforce the requirements.

Custom JSON schema header
#########################

To use a JSON schema with *phenopacket-tools*, the schema header must include the following elements::

  {
    "$schema": "https://json-schema.org/draft/2019-09/schema",
    "$id": "https://example.com/hpo-rare-disease-validator",
    "title": "HPO Rare Disease Phenopacket Schema",
    "description": "An example JSON schema for validating a phenopacket in context of the rare-disease research",
    "type": "object"
  }

The elements are used for the following purpose:

* `$schema` - *phenopacket-tools*  uses draft `2019-09` JSON schema specification. The `$schema` element MUST be present
  in the document and it MUST use the `2019-09` specification.
* `$id` - identifier of the schema, used at `validator.validatorInfo().validatorId()`.
* `title` - name of the schema for human consumption, used in `validator.validatorInfo().validatorName()`.
* `description` - a short description of the validation check, used in `validator.validatorInfo().description()`.
* `type` - JSON schema and JSON allows several data types, e.g. `number`, `string`, `object`, `boolean`, etc.
  Here we are writing a JSON schema for validating a phenopacket, family, or cohort. Therefore, the `type`
  must be an `object`.

Custom JSON schema body
#######################

Now, let's constrain the schema to first check that the time at last encounter is specified in the phenopacket subject.
To encode this in JSON schema, we require presence of a `subject` property in phenopacket.
The snippet follows the JSON schema header and instructs the JSON schema validator to check presence of
the `subject` field. The `subject` is an `object` and we can add a `description` to self-document the schema.
Phenopacket Schema does not require presence of the `subject`. However, here we make `subject` a required field,
hence presence of the `required` clause::

  {
    // The header ...
    "properties": {
      "subject": {
        "type": "object",
        "description": "The subject element is required for a rare-disease Phenopacket"
      }
    },
    "required": [ "subject" ]
  }


Next, we add a constraint to the `subject` field to require presence of the time at last encounter.
We do this by requiring `timeAtLastEncounter` in the `subject`::

  {
    // The header ...
    "properties": {
      "subject": {
        "type": "object",
        "description": "The subject element is required for a rare-disease Phenopacket",
        "properties": {
          "timeAtLastEncounter": {
            "type": "object",
            "description": "The time at last encounter is required for a rare-disease phenopacket"
          }
        },
        "required": [ "timeAtLastEncounter" ]
      }
    },
    "required": [ "subject" ]
  }

We can encode additional checks using `JSON schema <https://json-schema.org/learn>`_ syntax.
See the JSON schema document described in :ref:`rstcustomjsonschematutorialexample` section for additional examples.


Use custom JSON schema
######################

*Phenopacket-tools* validation API supports including custom JSON schema in the validation workflow.
The custom schema can be added into the `builder` created in the previous section (:ref:`rstofftheshelfvalidation`)
by:

.. code-block::

  Path customSchema = Path.of("hpo-rare-disease-schema.json");
  builder.addJsonSchema(customSchema);

The `addJsonSchema()` adds a step for using the JSON schema in the validation workflow.


.. _rstphenotypevalidation:

Phenotype validators
~~~~~~~~~~~~~~~~~~~~

The validation we discussed until now was fairly simple; it included checking presence, absence, or formatting
of Phenopacket Schema components. However, sometimes we may need to check relationships between individual components.

For instance, in the context of rare-disease research and diagnostics, we may want to check if all phenotypic features
are encoded using valid HPO terms and if the phenotypic annotations are logically consistent. For instance, using both a term
(e.g. `Perimembranous ventricular septal defect <https://hpo.jax.org/app/browse/term/HP:0011682>`_) and
its ancestor (e.g. `Ventricular septal defect <https://hpo.jax.org/app/browse/term/HP:0001629>`_ ) is a logical error,
because an annotation with a specific HPO term (e.g., a patient with perimembranous VSD by necessity also has VSD).

In this section, we describe validators that use HPO file to perform several checks that can be useful in many contexts.
The API documentation and the corresponding Java classes can be found in
`org.phenopackets.phenopackettools.validator.core.phenotype <http://phenopackets.org/phenopacket-tools/apidocs/org.phenopackets.phenopackettools.validator.core/org/phenopackets/phenopackettools/validator/core/phenotype/package-summary.html>`_
package.


.. _rstprimaryphenotypevalidation:

Primary validation
##################

The `HpoPhenotypeValidator` checks if the HPO terms used by a phenopacket are *valid* - well-formatted and present
in the given HPO file, and *current* - not obsolete. If an obsolete term is found, the validator suggests a replacement
with the current term.

In code, we add the primary validation into the validation workflow by running:

.. code-block:: java

  Ontology hpo = OntologyLoader.loadOntology(new File("hp.json"));
  PhenopacketValidator<PhenopacketOrBuilder> primary = HpoPhenotypeValidators.Primary.phenopacketHpoPhenotypeValidator(hpo);
  builder.addValidator(primary);

The validator requires an HPO `Ontology` object. We use `Phenol <https://github.com/monarch-initiative/phenol>`_
library to parse the HPO JSON file. The `OntologyLoader` is part of
`phenol-io <https://mvnrepository.com/artifact/org.monarchinitiative.phenol/phenol-io>`_ module, you may need to add
an appropriate dependency into your build file.

.. _rstuniquephenotypevalidation:

Unique phenotypic features
##########################

The `HpoUniqueValidator` checks if the HPO terms used by a phenopacket are *unique* - present at most once.
If a term is not unique, the validator points this out along with the term's occurrence count.

In code, we add the corresponding validator into the validation workflow by running:

.. code-block:: java

  builder.addValidator(HpoPhenotypeValidators.Unique.phenopacketValidator(hpo));


.. _rstancestryphenotypevalidation:

Ancestry validation
###################

The `HpoAncestryValidator` checks if the HPO terms are logically consistent; the phenotype features do not include both
a term and its ancestor.

Apart from a mere id of the phenotype feature, Phenopacket Schema also models observation status of a feature.
A feature can be either present/observed, or absent/excluded in the subject. The ancestry validator takes
the observation status into the account, which leads to several possible outcomes regarding validity of a term combination:

.. csv-table::
   :header: "`Tonic seizure <https://hpo.jax.org/app/browse/term/HP:0032792>`_ (Term)", "`Seizure <https://hpo.jax.org/app/browse/term/HP:0001250>`_ (Ancestor)", "Is valid", "Explanation"

   observed,     observed,     No,   "*Tonic seizure* is a type of *Seizure*. Use the *most* specific term (Tonic seizure)."
   excluded,     excluded,     No,   "Absence of a *Seizure* implies absence of the *Tonic seizure*. Use the *less* specific term (Seizure)."
   observed,     excluded,     No,   "Absence of a *Seizure* implies absence of the *Tonic seizure*. Keep one of the terms depending on the case context."
   excluded,     observed,    Yes,   "A valid phenotype term combination. A subject can be annotated with a term and having a sub-type excluded at the same time."


Using ancestry validator in the validator workflow is fairly straightforward if we can get ahold of a HPO `Ontology` object:

.. code-block:: java

  PhenopacketValidator<PhenopacketOrBuilder> ancestry = HpoPhenotypeValidators.Ancestry.phenopacketHpoAncestryValidator(hpo);
  builder.addValidator(ancestry);


.. _rstorgsysvalidation:

Organ system validation
#######################

In some cases it may be desirable to ensure presence of annotation for specific organ systems.
For instance, phenopackets that represent patients with
`Marfan syndrome <https://hpo.jax.org/app/browse/disease/OMIM:154700>`_ may require annotation of three organ systems:

* Eye
* Cardiovascular system
* Respiratory system

To annotate organ system, we either *exclude* the corresponding top-level HPO term or by adding a descendent term:

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

The `HpoOrganSystemValidator` requires HPO `Ontology` and a list of top-level HPO terms:

.. code-block:: java

  List<TermId> organSystemIds = List.of(HpoOrganSystems.EYE, HpoOrganSystems.CARDIOVASCULAR, HpoOrganSystems.RESPIRATORY);
  PhenopacketValidator<PhenopacketOrBuilder> organSystem = HpoPhenotypeValidators.OrganSystem.phenopacketHpoOrganSystemValidator(hpo, organSystemIds);
  builder.addValidator(organSystem);

*Phenopacket-tools* includes a convenience class `HpoOrganSystems` with IDs of the commonly-used top-level HPO terms.
However, any valid term ID can be used for the organ system validation.
For instance, ``List.of(TermId.of("HP:0001250"))`` validates annotation
of a `Seizure (HP:0001250) <https://hpo.jax.org/app/browse/term/HP:0001250>`_.


See also
^^^^^^^^

Check out the
`org.phenopackets.phenopackettools.validator.core <http://phenopackets.org/phenopacket-tools/apidocs/org.phenopackets.phenopackettools.validator.core/module-summary.html>`_
and
`org.phenopackets.phenopackettools.validator.jsonschema <http://phenopackets.org/phenopacket-tools/apidocs/org.phenopackets.phenopackettools.validator.jsonschema/module-summary.html>`_
modules for more information regarding the public validation API.
