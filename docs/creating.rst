.. _rstcreating:


=====================
Creating Phenopackets
=====================

Google's `Protocol Buffer (protobuf)`_ framework automatically generates
Java bindings for building and working with Phenopackets. However, the code can be unwieldy. Additionally, many users
of the phenopacket framework will want to use a recommended set of ontology terms for specific kinds of data.
*Phenopacket-tools* library provides terms and constants that are more convenient to use than manually creating
the equivalent message.

This section exemplifies usage of `PhenotypicFeatureBuilder`, one of many builders provided by the
`org.phenopackets.phenopackettools.builder <http://phenopackets.org/phenopacket-tools/apidocs/org.phenopackets.phenopackettools.builder/module-summary.html>`_
module.


`phenopacket-tools` builder pattern
===================================

In this example, let's imagine we want to create a ``PhenotypicFeature`` element to denote that severe weakness of the
left triceps muscle was observed in a patient at the age of 11 years and 4 months. First, let us code this using
the Phenopacket code that is automatically generated by the protobuf framework.


.. code-block:: java

    OntologyClass tricepsWeakness = OntologyClass.newBuilder()
                .setId("HP:0031108")
                .setLabel("Triceps weakness")
                .build();
    OntologyClass left = OntologyClass.newBuilder()
                .setId("HP:0012835")
                .setLabel("Left")
                .build();
    OntologyClass severe = OntologyClass.newBuilder()
                .setId("HP:0012828")
                .setLabel("Severe")
                .build();
    Age iso8601duration = Age.newBuilder().setIso8601Duration("P11Y4M").build();
    TimeElement ageElement = TimeElement.newBuilder().setAge(iso8601duration)
                .setAge(iso8601duration)
                .build();
    PhenotypicFeature phenotypicFeature = PhenotypicFeature.newBuilder()
                .setType(tricepsWeakness)
                .setOnset(ageElement)
                .setSeverity(severe)
                .addModifiers(left)
                .build();

The following code block uses functions from the *phenopacket-tools* library to simplify the creation
of this ``PhenotypicFeature`` element.

.. code-block:: java

    PhenotypicFeature phenotypicFeature2 = PhenotypicFeatureBuilder.builder("HP:0031108", "Triceps weakness")
                .addModifier(severe())
                .addModifier(left())
                .onset(TimeElements.age("P11Y4M"))
                .build();

Both code snippets generate identical phenopacket code.

.. code-block:: json

    {
        "type": {
            "id": "HP:0031108",
            "label": "Triceps weakness"
        },
        "severity": {
            "id": "HP:0012828",
            "label": "Severe"
        },
        "modifiers": [{
            "id": "HP:0012835",
            "label": "Left"
        }],
        "onset": {
            "age": {
                "iso8601duration": "P11Y4M"
            }
        }
    }


See also
========

See the API documentation of the
`org.phenopackets.phenopackettools.builder <http://phenopackets.org/phenopacket-tools/apidocs/org.phenopackets.phenopackettools.builder/module-summary.html>`_
module for a comprehensive list of ontology constants, convenience methods, and builders provided
by the *phenopacket-tools* library.

Several detailed examples are available in the ``phenopackets-tools-cli`` module in the
`org.phenopackets.phenopackettools.cli.examples <https://github.com/phenopackets/phenopacket-tools/tree/main/phenopacket-tools-cli/src/main/java/org/phenopackets/phenopackettools/cli/examples>`_
package.

.. _Protocol Buffer (protobuf): https://developers.google.com/protocol-buffers
