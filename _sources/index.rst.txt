.. phenopacket-tools documentation master file, created by
   sphinx-quickstart on Fri Aug 12 11:03:28 2022.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to phenopacket-tools's documentation!
=============================================

The `Phenopacket Schema <https://phenopacket-schema.readthedocs.io/en/latest/>`_ of the
`Global Alliance for Genomics and Health (GA4GH) <https://www.ga4gh.org/>`_ was
approved by the GA4GH in 2022 and additional certified by the International Standards
    Organization (ISO) as `ISO 4454:2022 <https://www.iso.org/standard/79991.html>`_.
A description of the schema was published in
`Jacobsen JOB, et al. 2022 <https://pubmed.ncbi.nlm.nih.gov/35705716/>`_ and a detailed tutorial
appeared in `Ladewig et al., 2022 <  https://onlinelibrary.wiley.com/doi/full/10.1002/ggn2.202200016>`_.

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
   :maxdepth: 2
   :caption: Contents:

   _static/creating<Creating GA4GH phenopackets>
   modules


.. image:: https://onlinelibrary.wiley.com/cms/asset/1cc0a141-da65-45a3-b7b0-6316b7b02069/ggn2202200016-fig-0002-m.jpg
    :alt: GA4GH Phenopacket
