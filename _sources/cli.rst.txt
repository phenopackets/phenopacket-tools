.. _rstcli:

============================
Command line interface (CLI)
============================

*phenopacket-tools* CLI provides functionality for viewing, conversion and validation
of the top-level elements of Phenopacket schema. This document describes how to set up the CLI application
on Linux, Mac and Windows environments.

.. note::
  *phenopacket-tools* is written in Java 17 and requires Java 17 or newer to run.

*phenopacket-tools* is distributed as a standalone executable Java Archive (JAR) file. The application requires
no special installation procedure if Java 17 or better is available in your environment.

Setup
~~~~~

Most users should *download* the distribution ZIP file with precompiled JAR file from *phenopacket-tools* release page.
However, it is also possible to *build* the JAR from sources.

Download
^^^^^^^^

*phenopacket-tools* JAR is provided in the distribution ZIP file as part of *phenopacket-tools*' release schedule
from `Releases <https://github.com/phenopackets/phenopacket-tools/releases>`_.

The ZIP archive contains the executable JAR file along with README and example phenopackets required to run the setup
and the tutorial.

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

After a successful build, a distribution ZIP file ``phenopacket-tools-cli-${project.version}-distribution.zip``
will be created in the ``phenopacket-tools-cli/target`` directory. Use the ZIP archive in the same way as the archive
downloaded from *phenopacket-tools* releases.

.. note::
  Replace ``${project.version}`` with a given version (e.g. ``0.4.6``).


Commands
~~~~~~~~

*phenopacket-tools* CLI provides the following commands:

* ``examples`` - generate examples of the top-level elements
* ``convert`` - convert top-level elements from *v1* to *v2* format
* ``validate`` - validate semantic and syntactic correctness of top-level Phenopacket schema elements

The ``examples`` command is fairly simple; it writes a bunch of example phenopackets, cohorts and families
into the provided directory. The ``convert`` and ``validate`` commands, despite being a bit more elaborate, work in
a similar manner. The parts shared by the both command are be described in greater detail
in the ``convert`` command section.

In the next sections, we will run *phenopacket-tools* by using the following alias::

  $ alias pxf="java -jar phenopacket-tools-cli-${project.version}.jar"

.. note::
  The commands report warnings and errors by default. Use `-v` to increase the verbosity and see what's
  going on under the hood. The `-v` can be specified multiple times (e.g. `-vvv`).

*examples* - generate examples of the top-level elements
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``examples`` command writes example phenopackets (including family and cohort examples) into
a provided base directory. Starting from a `base` directory, the examples are written into three sub-folders::

  base
  |- phenopackets
  |- families
  \- cohorts

The ``examples`` command requires an optional ``-o | --output`` argument. By default, the examples will be placed
into the current directory.

The following command writes the examples into the ``path/to/examples`` directory::

  $ pxf examples -o path/to/examples


*convert* - convert top-level elements from *v1* to *v2* format
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``convert`` command converts a phenopacket, family, or a cohort from *v1* to *v2* format of Phenopacket schema.

Usage
#####

Let's assume we have an example phenopacket ``phenopacket.v1.json``, family ``family.v1.json``,
and cohort ``cohort.v1.json``.

We can convert a *v1* phenopacket into *v2* by running::

  $ cat phenopacket.v1.json | pxf convert > phenopacket.v2.json



*phenopacket-tools* makes an educated guess to determine if the input is in *JSON*, *Protobuf*, or *YAML* format.
The guessing is, however, naive and can fail in parsing e.g. gzipped *JSON* file. Turn of the format guessing
by providing the ``-f | --format`` option::

  $ # Explicit JSON input
  $ cat phenopacket.v1.json | pxf convert -f json > phenopacket.v2.json
  $
  $ # Explicit protobuf input
  $ cat phenopacket.v1.pb | pxf convert -f protobuf > phenopacket.v2.pb

The ``-f | --format`` option accepts one of the following 3 values: ``{json, pb, yaml}``.



By default, the output is written in the format of the input data.
However, we can override this by using ``--output-format`` option::

  $ cat phenopacket.v1.json | pxf convert --output-format pb > phenopacket.v2.pb

The ``--output-format`` option takes the same values as ``--format``: ``{json, pb, yaml}``.


The ``convert`` command expects to receive a phenopacket by default. However, it can also convert the other
top-level elements of the Phenopacket schema: family and cohort. Use the ``-e | --element`` option to indicate if
the input is a ``family`` or a ``cohort``::

  $ cat family.v1.json | pxf convert -e family > family.v2.json
  $ cat cohort.v1.json | pxf convert -e cohort > cohort.v2.json

We can convert one or more item at the time by using the ``-i | --input`` option. If the ``-i`` option is used only once,
the STDIN is ignored and the conversion proceeds in the same way as in the examples above. However, ``-i`` option can
be provided more than once, to convert a collection of items in a single run. The results of the bulk processing
are written into a directory supplied via the ``-O | --output-directory`` option (the option is mandatory if using
>1 ``-i``).

For instance::

  $ pxf convert -i phenopacket.a.v1.json -i phenopacket.b.v1.json -O converted

converts the input phenopackets and stores the results in the ``converted`` folder. The converted files will be stored
under the same names.


*validate* - validate semantic and syntactic correctness
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``validate`` command checks *syntactic* and *semantic* correctness of a *phenopacket*, *family*, or *cohort*.

Briefly, to be syntactically correct, a phenopacket must be well formatted (valid Protobuf message, JSON document, etc.)
and meet the requirements of the Phenopacket schema; all REQUIRED attributes are set  (e.g. ``phenopacket.id`` and
``phenopacket.meta_data``), and ``MetaData`` includes a ``Resource`` for all ontology concepts.

The *semantic* correctness ensures that the element, when taken as a whole, is ... TODO - finish

Usage
#####

The ``validate`` command shares many CLI options with ``convert``.

The same options are used to indicate the input formats and element types. The input can be provided through STDIN
as well as in bulk. The bulk processing makes sense especially if we e.g. load the HPO graph for each validation.

Results are written into STDOUT in CSV/TSV format. The CSV output has a header, each header line starts with ``#`` character.
The header contains phenopacket-tools version, date time of validation, and list of validators that were run.
A row with column names follows the header, and then the individual validation results.

..
  TODO - check the validation description.

Set up autocompletion
~~~~~~~~~~~~~~~~~~~~~

TODO - write the section

