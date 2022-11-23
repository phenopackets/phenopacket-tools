=============================================
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

*Phenopacket-tools* is an open-source Java library and command-line interface (CLI) application for working
with GA4GH phenopackets. The library has three main goals:

- To simplify *creating* phenopackets with Java code using streamlined builders and predefined building blocks such
  as units, anatomical organs, and clinical modifiers.
- To *convert* phenopackets from the obsoleted version 1 to the version 2 (current) of the Schema.
- To provide a *validation* framework for checking phenopackets for syntactical and semantic correctness
  and to enable developers to extend the validation framework to encode the specific requirements of consortia
  or projects using either JSON schema or programmatic tools.

On top of the library, we provide a standalone CLI application for
conversion and validation. The source code is available
from our `GitHub repository <https://github.com/phenopackets/phenopacket-tools>`_.

The following sections describe phenopacket-tools library and CLI application.
We start with :ref:`rsttutorial` to provide a quick overview of the CLI functionality.
We follow with comprehensive description of the :ref:`rstcli`.
The rest of the documentation offers an in-depth user guide for using the library functionality
in JVM-based applications.

.. toctree::
   :maxdepth: 1
   :caption: Contents:

   tutorial
   cli
   constants
   creating
   converting
   validation
   examples


.. figure:: https://onlinelibrary.wiley.com/cms/asset/1cc0a141-da65-45a3-b7b0-6316b7b02069/ggn2202200016-fig-0002-m.jpg
    :alt: GA4GH Phenopacket Schema
    :width: 800px

    Overview of the GA4GH Phenopacket Schema.
    