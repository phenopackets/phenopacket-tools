.. _rstvalidation:


=======================
Validating Phenopackets
=======================

Protobuf
^^^^^^^^
Phenopackets schema uses protobuf, an exchange format developed
in 2008 by Google. We refer readers to the excellent
`Wikipedia page <https://en.wikipedia.org/wiki/Protocol_Buffers>`_
on Protobuf and to `Google’s documentation <https://developers.google.com/protocol-buffers/>`_
for details.
In Protobuf (version 3, which is what the Phenopacket Schema uses),
all fields are optional. However, the Phenopacket Schema defines
certain fields to be optional
(See `documentation <https://phenopacket-schema.readthedocs.io/en/latest/index.html>`_ for details).
Also, a phenopacket message can be represented in native protobuf (binary) format, JSON, YAML, and
other formats.

Validation
^^^^^^^^^^

The *phenopacket-tools* library offers JSON-Schema-based and semantic validations. The syntactic validation
is done using JSON schema. Additionally, an interface is provided to perform arbitrary kinds of validation.
This validation should be performed for all phenopackets.

Additional constraints and requirements may be made for phenopackets that are used in a specific
project or for a specific collaboration or consortium. For instance, a rare-disease consortium
may require that all phenotypic features be recorded using valid HPO terms. An example class is
provided that checks all ``PhenotypicFeature`` elements, ensures that they use HPO terms with valid
(i.e., primary) id's, and checks whether both a term and an ancestor of the term are used - if so
a warning is emitted, because an annotation with a specific HPO term
(e.g., `Perimembranous ventricular septal defect <https://hpo.jax.org/app/browse/term/HP:0011682>`_)
implies all of the ancestors of the term (e.g., a patient with perimembranous VSD by necessity also has
`Ventricular septal defect <https://hpo.jax.org/app/browse/term/HP:0001629>`_).


Base validation
^^^^^^^^^^^^^^^
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

.. TODO - continue


