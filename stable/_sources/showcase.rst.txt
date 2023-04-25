.. _rstshowcase:

===================================================
Showcase of applications that use phenopacket-tools
===================================================

We present a list of tools and applications that use phenopacket-tools to read, build, or Q/C phenopackets.


We present a list of tools and applications that use phenopacket-tools to read, build, or Q/C phenopackets and other components of the Phenopacket Schema.

* `LIRICAL <https://www.cell.com/ajhg/fulltext/S0002-9297(20)30230-5>`_ runs phenotype-driven prioritization of Mendelian diseases on a Phenopacket.

   LIRICAL uses `phenopacket-tools-io` to `read phenopackets <https://github.com/TheJacksonLaboratory/LIRICAL/blob/f516e463986880285436fd93540da3c6b7510fb7/lirical-io/src/main/java/org/monarchinitiative/lirical/io/analysis/PhenopacketImportUtil.java#L18>`_.

..
   TODO
   and `phenopacket-tools-validator-core` for input `quality control <>`_.

* `SvAnna <https://genomemedicine.biomedcentral.com/articles/10.1186/s13073-022-01046-6>`_ applies phenotype-driven prioritization on coding and regulatory structural variants in long-read genome sequencing.

   SvAnna uses `phenopacket-tools-io` for `reading input <https://github.com/TheJacksonLaboratory/SvAnna/blob/master/svanna-cli/src/main/java/org/monarchinitiative/svanna/cli/cmd/PhenopacketAnalysisDataUtil.java>`_
   formatted as a phenopacket.

* `HPO Case Annotator <https://github.com/monarch-initiative/HpoCaseAnnotator>`_ is a GUI application for biocuration of case reports, cohorts and families.

   Hpo Case Annotator uses `phenopacket-tools-builder` for `mapping the entered data into Phenopacket Schema <https://github.com/monarch-initiative/HpoCaseAnnotator/tree/development/hpo-case-annotator-export/src/main/java/org/monarchinitiative/hpo_case_annotator/export/ppv2>`_
   format and `phenopacket-tools-io` for `storing phenopackets <https://github.com/monarch-initiative/HpoCaseAnnotator/blob/509480ff78af3996bdacb6513feedb1f18e39c17/hpo-case-annotator-app/src/main/java/org/monarchinitiative/hpo_case_annotator/app/controller/MainController.java#L727>`_ into JSON files.


Please let us know about your application by submitting a request on our `issue tracker <https://github.com/phenopackets/phenopacket-tools/issues>`_.
