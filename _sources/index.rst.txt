Welcome to phenopacket-tools's documentation!
=============================================

The `Phenopacket Schema <https://phenopacket-schema.readthedocs.io/en/latest/>`_ of the
`Global Alliance for Genomics and Health (GA4GH) <https://www.ga4gh.org/>`_ was
approved by the GA4GH in 2022 and additional certified by the International Standards
Organization (ISO) as `ISO 4454:2022 <https://www.iso.org/standard/79991.html>`_.
A description of the schema was published in
`Jacobsen JOB, et al. 2022 <https://pubmed.ncbi.nlm.nih.gov/35705716/>`_ and a detailed tutorial
appeared in `Ladewig et al., 2022 <https://onlinelibrary.wiley.com/doi/full/10.1002/ggn2.202200016>`_.

A Phenopacket characterizes an individual person or biosample, linking that individual to detailed phenotypic descriptions,
genetic information, diagnoses, and treatments. The Phenopacket schema supports the FAIR principles
(findable, accessible, interoperable, and reusable), and computability. Specifically,
Phenopackets are designed to be both human and machine-interpretable, enabling computing operations and validation on
the basis of defined relationships between diagnoses, lab measurements, and genotypic information.

The phenopacket-tools library was written as a modular Java 17 library and has three main goals.

- To provide a simplified interface for creating GA4GH phenopackets with Java code
- To provide an extensible validation framework that can be used to check phenopackets for syntactical and semantic correctness.
- To enable developers to extend the validation framework to encode the specific requirements of consortia or projects using either JSON schema or programmatic tools.

Additionally, phenopacket-tools provides code to convert version 1 to the version 2 (the current version) of the Schema.



.. toctree::
   :maxdepth: 1
   :caption: Contents:

   creating
   converting
   validation
   constants


GA4GH Phenopacket Schema
^^^^^^^^^^^^^^^^^^^^^^^^
The Global Alliance for Genomics and Health (GA4GH) is developing a suite of coordinated standards for genomics for
healthcare. The Phenopacket is a new GA4GH standard for sharing disease and phenotype information that characterizes
an individual person, linking that individual to detailed phenotypic descriptions, genetic information, diagnoses, and
treatments (`Jacobsen et al, 2022 <https://pubmed.ncbi.nlm.nih.gov/35705716/>`_).
A detailed tutorial is available `here <https://onlinelibrary.wiley.com/doi/full/10.1002/ggn2.202200016>`_.



.. figure:: https://onlinelibrary.wiley.com/cms/asset/1cc0a141-da65-45a3-b7b0-6316b7b02069/ggn2202200016-fig-0002-m.jpg
    :width: 800
    :align: center
    :alt: GA4GH Phenopacket

    Phenopacket Schema overview. The GA4GH Phenopacket Schema is a hierarchical structure that represents clinical data about an individual.
