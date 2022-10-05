.. _rstcli:

============================
Command line interface (CLI)
============================

*phenopacket-tools* CLI provides functionality for viewing, conversion and validation
of the top-level elements of Phenopacket schema . This document describes how to set up the CLI application
on Linux, Mac and Windows environments.

.. note::
  *phenopacket-tools* is written in Java 17 and requires Java 17 or newer to run.

*phenopacket-tools* is distributed as a standalone executable Java Archive (JAR) file. Provided that Java 17 or better
is available in the environment, the application requires no special installation procedure.

Setup
~~~~~

Most users should download the precompiled JAR file from *phenopacket-tools* release page.
However, it is also possible to build the JAR from sources.

Download
^^^^^^^^

*phenopacket-tools* JAR is provided as part of *phenopacket-tools*' release schedule
from `Releases <https://github.com/phenopackets/phenopacket-tools/releases>`_.

Build from source code
^^^^^^^^^^^^^^^^^^^^^^

The source code is available in our `GitHub repository <https://github.com/phenopackets/phenopacket-tools>`_.
There are 2 requirements for building the app from sources:

* **Java Development Kit** (JDK) 17 or newer must be present in the environment and ``$JAVA_HOME`` variable must point
  to JDK's location. See `Installing Apache Maven <https://maven.apache.org/install.html>`_ for more details regarding
  setting up JDK and ``$JAVA_HOME`` on your system.
* *phenopacket-tools* leverages several open-source Java libraries and a **working internet connection**
  is required to download the libraries.

Run the following commands to check out the stable source code and to build the application::

  $ git clone https://github.com/phenopackets/phenopacket-tools
  $ cd phenopacket-tools
  $ ./mvnw -Prelease package

After a successful build, a file ``phenopacket-tools-cli-${project.version}.jar`` will be created in
the ``phenopacket-tools-cli/target`` directory. Use the JAR file in the same way as the JAR downloaded
from *phenopacket-tools* releases.

.. note::
  Replace ``${project.version}`` with a given version (e.g. ``0.4.6``).


Commands
~~~~~~~~

This section describes the commands of *phenopacket-tools* CLI.

``validate`` - validate semantic and syntactic correctness
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``validate`` command checks *syntactic* and *semantic* correctness of a *phenopacket*, *family*, or *cohort*.
Briefly, phenopacket is syntactically correct if it is well formatted (valid Protobuf message, JSON document, etc.),
meets the requirements of the *Phenopacket schema* (e.g. the REQUIRED attributes such as ``phenopacket.id`` and
``phenopacket.meta_data``, are set), and ``MetaData`` includes a ``Resource`` for all ontology concepts.



..
  TODO - check the validation description.

``convert`` - convert from v1 to v2 format
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

TODO - write the section

Set up autocompletion
~~~~~~~~~~~~~~~~~~~~~

TODO - write the section

