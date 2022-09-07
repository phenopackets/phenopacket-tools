.. _rstconstants:

=========
Constants
=========

The phenopacket-tools library offers a selection of recommended and predefined OntologyClass objects for commonly used concepts.
For instance, this is the code one would need to write using the native Protobuf frameworkto get an OntologyClass instance that represents the modifier ``Left``.

.. code-block:: java

   OntologyClass left = OntologyClass.newBuilder()
     .setId("HP:0012835")
     .setLabel("Left")
     .build();


In contrast, this is the code required with phenopacket-tools (omitting import statements in both cases)

.. code-block:: java

   OntologyClass left = left();


The following tables present the available static functions with predefined concepts.

Laterality
^^^^^^^^^^

.. csv-table::
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "HP:0012834", "Right", "right()"
   "HP:0012835", "Left", "left()"
   "HP:0012833", "Unilateral", "unilateral()"
   "HP:0012832", "Bilateral", "bilateral()"


AllelicState
^^^^^^^^^^^^

.. csv-table::
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "GENO:0000135", "heterozygous", "heterozygous()"
   "GENO:0000136", "homozygous", "homozygous()"
   "GENO:0000134", "hemizygous", "hemizygous()"


