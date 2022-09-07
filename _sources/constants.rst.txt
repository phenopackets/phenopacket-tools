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

Onset
^^^^^

Terms from the `HPO <https://hpo.jax.org/app/>`_ are used to describe age of onset or diseases or specific phenotypic features.

.. csv-table:: 
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "HP:0030674", "Antenatal onset", "antenatalOnset()"
   "HP:0011460", "Embryonal onset", "embryonalOnset()"
   "HP:0011461", "Fetal onset", "fetalOnset()"
   "HP:0034199", "Late first trimester onset", "lateFirstTrimesterOnset()"
   "HP:0034198", "Second trimester onset", "secondTrimesterOnset()"
   "HP:0034197", "Third trimester onset", "thirdTrimesterOnset()"
   "HP:0003577", "Congenital onset", "congenitalOnset()"
   "HP:0003623", "Neonatal onset", "neonatalOnset()"
   "HP:0003593", "Infantile onset", "infantileOnset()"
   "HP:0011463", "Childhood onset", "childhoodOnset()"
   "HP:0003621", "Juvenile onset", "juvenileOnset()"
   "HP:0003581", "Adult onset", "adultOnset()"
   "HP:0011462", "Young adult onset", "youngAdultOnset()"
   "HP:0025708", "Early young adult onset", "earlyYoungAdultOnset()"
   "HP:0025709", "Intermediate young adult onset", "intermediateYoungAdultOnset()"
   "HP:0025710", "Late young adult onset", "lateYoungAdultOnset()"
   "HP:0003596", "Middle age onset", "middleAgeOnset()"
   "HP:0003584", "Late onset", "lateOnset()"


Unit
^^^^

With some exceptions, terms from the `The Unified Code for Units of Measure <https://units-of-measurement.org/>`_ are used to denote units.

.. csv-table:: 
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "UCUM:mg", "milligram", "milligram()"
   "UCUM:m", "gram", "gram()"
   "UCUM:ug", "microgram", "microgram()"
   "UCUM:kg", "kiligram", "kilogram()"
   "UCUM:uL", "microliter", "microliter()"
   "UCUM:mL", "milliliter", "milliliter()"
   "UCUM:L", "liter", "liter()"
   "UCUM:um", "micrometer", "micrometer()"
   "UCUM:mm", "millimeter", "millimeter()"
   "UCUM:m", "meter", "meter()"
   "UCUM:mg.kg-1", "milligram per kilogram", "mgPerKg()"
   "UO:0000272", "millimetres of mercury", "mmHg()"
   "NCIT:C100899", "Diopter", "diopter()"


Organ
^^^^^

Terms from the `UBERON ontology <https://www.ebi.ac.uk/ols/ontologies/uberon>`_ are used to describe organs.

.. csv-table:: 
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "UBERON:0000955", "brain", "brain()"
   "UBERON:0002037", "cerebellum", "cerebellum()"
   "UBERON:0001690", "ear", "ear()"
   "UBERON:0000970", "eye", "eye()"
   "UBERON:0002107", "heart", "heart()"
   "UBERON:0002113", "kidney", "kidney()"
   "UBERON:0000059", "large intestine", "largeIntestine()"
   "UBERON:0002107", "liver", "liver()"
   "UBERON:0002048", "lung", "lung()"
   "UBERON:0000004", "nose", "nose()"
   "UBERON:0002108", "small intestine", "smallIntestine()"
   "UBERON:0002240", "spinal cord", "spinalCord()"
   "UBERON:0002106", "spleen", "spleen()"
   "UBERON:0001723", "tongue", "tongue()"
   "UBERON:0002370", "thymus", "thymus()"


MedicalActions
^^^^^^^^^^^^^^

Terms from the `NCI Thesaurus <https://www.ebi.ac.uk/ols/ontologies/ncit>`_ are used for components of medical action messages.

.. csv-table:: 
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "NCIT:C38222", "Intraarterial Route of Administration", "intraArterialAdministration()"
   "NCIT:C38288", "Oral Route of Administration", "oralAdministration()"
   "NCIT:C64576", "Once", "once()"
   "NCIT:C125004", "Once Daily", "onceDaily()"
   "NCIT:C64496", "Twice Daily", "twiceDaily()"
   "NCIT:C64527", "Three Times Daily", "threetimesDaily()"
   "NCIT:C64530", "Four Times Daily", "fourtimesDaily()"
   "NCIT:C41331", "Adverse Event", "adverseEvent()"


Laterality
^^^^^^^^^^

Modifier terms from the `HPO <https://hpo.jax.org/app/>`_ are used to describe laterality.

.. csv-table:: 
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "HP:0012834", "Right", "right()"
   "HP:0012835", "Left", "left()"
   "HP:0012833", "Unilateral", "unilateral()"
   "HP:0012832", "Bilateral", "bilateral()"


AllelicState
^^^^^^^^^^^^

Terms from the `GENE ontology <https://www.ebi.ac.uk/ols/ontologies/geno>`_ are used to describe the allelic state of variants.

.. csv-table:: 
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "GENO:0000135", "heterozygous", "heterozygous()"
   "GENO:0000136", "homozygous", "homozygous()"
   "GENO:0000134", "hemizygous", "hemizygous()"


SpatialPattern
^^^^^^^^^^^^^^

Modifier terms from the `HPO <https://hpo.jax.org/app/>`_ are used to describe spatial patterns of phenotypic abnormalities.

.. csv-table:: 
   :header: "id", "label", "function name"
   :widths: 30, 200, 200

   "HP:0032544", "Predominant small joint localization", "predominantSmallJointLocalization()"
   "HP:0031450", "Polycyclic", "polycyclic()"
   "HP:0025287", "Axial", "axial()"
   "HP:0033813", "Perilobular", "perilobular()"
   "HP:0033814", "Paraseptal", "paraseptal()"
   "HP:0033815", "Bronchocentric", "bronchocentric()"
   "HP:0033816", "Centrilobular", "centrilobular()"
   "HP:0033817", "Miliary", "miliary()"
   "HP:0012837", "Generalized", "generalized()"
   "HP:0033819", "Perilymphatic", "perilymphatic()"
   "HP:0012838", "Localized", "localized()"
   "HP:0033818", "Reticular", "reticular()"
   "HP:0012839", "Distal", "distal()"
   "HP:0030645", "Central", "central()"
   "HP:0025290", "Upper-body predominance", "upperBodyPredominance()"
   "HP:0032539", "Joint extensor surface localization", "jointExtensorSurfaceLocalization()"
   "HP:0025295", "Herpetiform", "herpetiform()"
   "HP:0025296", "Morbilliform", "morbilliform()"
   "HP:0030649", "Pericentral", "pericentral()"
   "HP:0025294", "Dermatomal", "dermatomal()"
   "HP:0030648", "Midperipheral", "midperipheral()"
   "HP:0025293", "Distributed along Blaschko lines", "distributedAlongBlaschkoLines()"
   "HP:0025292", "Acral", "acral()"
   "HP:0030647", "Paracentral", "paracentral()"
   "HP:0025275", "Lateral", "lateral()"
   "HP:0030646", "Peripheral", "peripheral()"
   "HP:0025291", "Lower-body predominance", "lowerBodyPredominance()"
   "HP:0020034", "Diffuse", "diffuse()"
   "HP:0012840", "Proximal", "proximal()"
   "HP:0033820", "Apical", "apical()"
   "HP:0030650", "Focal", "focal()"
   "HP:0030651", "Multifocal", "multifocal()"
   "HP:0032540", "Jointflexorsurfacelocalization", "jointFlexorSurfaceLocalization()"


