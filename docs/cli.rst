.. _rstcli:

======================
Command-line interface
======================

*Phenopacket-tools* command-line interface (CLI) provides functionality for conversion and validation
of the top-level elements of Phenopacket Schema. Here we describe how to set up the CLI application
on Linux, Mac and Windows environments.

.. note::
  *phenopacket-tools* is written in Java 17 and requires Java 17 or newer to run.

We distribute *phenopacket-tools* in a ZIP archive. The application requires no special installation procedure
if Java 17 or better is available in your environment.


Setup
=====

Most users should *download* the distribution ZIP file with precompiled JAR file from *phenopacket-tools* release page.
However, it is also possible to *build* the JAR from sources.


Download
^^^^^^^^

*phenopacket-tools* JAR is provided in the distribution ZIP file as part of *phenopacket-tools*' release schedule
from `Releases <https://github.com/phenopackets/phenopacket-tools/releases>`_.

The ZIP archive contains the executable JAR file along with a `README` file and example phenopackets required
to run the setup and the tutorial.


Build from source code
^^^^^^^^^^^^^^^^^^^^^^

The source code is available in our `GitHub repository <https://github.com/phenopackets/phenopacket-tools>`_.
There are 2 requirements for building the app from sources:

* **Java Development Kit** (JDK) 17 or newer must be present in the environment and ``$JAVA_HOME`` variable must point
  to JDK's location. See `Setting JAVA_HOME <https://docs.oracle.com/en/cloud/saas/enterprise-performance-management-common/diepm/epm_set_java_home_104x6dd63633_106x6dd6441c.html>`_
  for more details regarding setting up ``$JAVA_HOME`` on Windows, Mac, and Linux.
* *phenopacket-tools* uses several open-source Java libraries and a **working internet connection**
  is required to download the libraries.

Run the following commands to check out the source code and to build the application:

.. parsed-literal::
  git clone https://github.com/phenopackets/phenopacket-tools
  cd phenopacket-tools
  git checkout tags/|release|
  ./mvnw -Prelease package

If the build completes, a ZIP archive "phenopacket-tools-cli-|release|-distribution.zip"
is created in the ``phenopacket-tools-cli/target`` directory. Use the archive in the same way as the archive
downloaded from *phenopacket-tools* releases.

Set up alias and autocompletion
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In this *optional* step, we set up an alias and autocompletion for *phenopacket-tools* command-line application.
The autocompletion works thanks to the awesome `Picocli <https://picocli.info>`_ library and it works
on Bash or ZSH Unix shells.

Let's set up the alias first. To reiterate the tutorial :ref:`rstsetupaliastutorial` section,
Java command line applications are invoked as ``java -jar executable.jar``. However, such incantation is
a bit too verbose and we can shorten it a bit by defining an alias.

Assuming the distribution ZIP was unpacked into phenopacket-tools-cli-|release| directory, let's run the following
to set up the alias:

.. parsed-literal::
  alias pxf="java -jar $(pwd)/phenopacket-tools-cli-\ |release|\ /phenopacket-tools-cli-|release|.jar"
  pxf --help

Now the autocompletion. The autocompletion can simplify using the CLI options by completing the command
or option after pressing the `TAB` key.
To enable the autocompletion, make sure the alias for `pxf` is set up correctly and run:

.. parsed-literal::
  source <(pxf generate-completion)

The ``pxf generate-completion`` command generates the autocompletion script and ``source`` uses it to set up
the completion. However, the autocompletion will last only for the duration of the current shell session.

To make the autocompletion permanent, store the script file and add the alias and and sourcing into your `.bashrc`
or `.bash_profile` file:

.. parsed-literal::
  echo "### Install phenopacket-tools" >> .bashrc
  echo alias pxf="java -jar $(pwd)/phenopacket-tools-cli-\ |release|\ /phenopacket-tools-cli-|release|.jar" >> .bashrc
  pxf generate-completion > pxf-completion.sh
  echo source $(pwd)/pxf-completion.sh >> .bashrc

.. warning::
  The autocompletion only works if the alias is set to `pxf`. Other alias values will *not* work.


Commands
========

The command-line interface provides the following commands:

* ``examples`` - generate examples of the top-level elements
* ``convert`` - convert top-level elements from *v1* to *v2* format
* ``validate`` - validate semantic and syntactic correctness of top-level Phenopacket schema elements

Before we dive into the commands, let's discuss some common concepts shared by all CLI commands.

Common concepts
^^^^^^^^^^^^^^^

We designed the CLI with aim to make it as easy to use as possible. As a result, the *phenopacket-tools* commands
use several common design principles:

* The input data can be provided either via the standard input *OR* as a list of positional parameters.
* The input *data format* is provided using ``-f | --format`` option.
  *phenopacket-tools* supports phenopackets in `JSON`, `YAML`, or `protobuf` formats.
  In absence of the explicit data format, *phenopacket-tools* makes an educated guess.
* The output is written in the input data format.
* The top-level *element type* of the data input is indicated by the ``-e | --element`` option.
  According to the Phenopacket Schema, the commands supports `phenopacket`, `family`, or `cohort` elements.
* The output is written into the standard output stream. Progress, warnings, and errors are reported
  into standard error.
* The CLI operates in a silent mode by default; only warnings and errors are reported.
  Use ``-v`` to increase the verbosity; the ``-v`` option can be specified multiple times (e.g. ``-vvv``).


We discuss the common concepts further at the relevant places of the next sections.


``examples`` - generate phenopacket examples
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``examples`` command writes example phenopackets (including family and cohort examples) into
a provided base directory. Starting from a `base` directory, the examples are written into three sub-folders::

  base
  |- phenopackets
  |- families
  \- cohorts

The ``examples`` command requires an optional ``-o | --output`` argument. By default, the examples will be placed
into the current directory.

The following command writes the examples into the ``path/to/examples`` directory::

  pxf examples -o path/to/examples


``convert`` - convert top-level elements from *v1* to *v2* format
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``convert`` command converts a phenopacket, family, or a cohort from *v1* to *v2* format of Phenopacket Schema.

Usage
#####

Let's assume we have an example phenopacket ``phenopacket.v1.json``, family ``family.v1.json``,
and cohort ``cohort.v1.json``.

We can convert a *v1* phenopacket into *v2* by running::

  cat phenopacket.v1.json | pxf convert > phenopacket.v2.json



*Phenopacket-tools* makes an educated guess to determine if the input is in `JSON`, `protobuf`, or `YAML` format.
The current format guessing implementation is, however, naïve and can fail in parsing e.g. gzipped `JSON` file.
Turn the format guessing off by providing the ``-f | --format`` option::

  # Explicit JSON input
  cat phenopacket.v1.json | pxf convert -f json > phenopacket.v2.json

  # Explicit protobuf input
  cat phenopacket.v1.pb | pxf convert -f protobuf > phenopacket.v2.pb

The ``-f | --format`` option accepts one of the following 3 values: ``{json, pb, yaml}``.



By default, the output is written in the format of the input data.
However, we can override this by using ``--output-format`` option::

  cat phenopacket.v1.json | pxf convert --output-format pb > phenopacket.v2.pb

The ``--output-format`` option takes the same values as ``--format``: ``{json, pb, yaml}``.


The ``convert`` command expects to receive a phenopacket by default. However, it can also convert the other
top-level elements of the Phenopacket schema: family and cohort. Use the ``-e | --element`` option to indicate if
the input is a ``family`` or a ``cohort``::

  cat family.v1.json | pxf convert -e family > family.v2.json
  cat cohort.v1.json | pxf convert -e cohort > cohort.v2.json

We can convert one or more item at the time by passing the paths to the input files as a positional parameters.
In case one parameter is provided, the STDIN is ignored and the conversion proceeds in the same way as in the examples
above. The command can accept two or more files as positional parameters for bulk conversion. To perform
the bulk conversion, the ``-O | --output-directory`` option must be provided to set the location of the directory
for writing the converted phenopackets.

For instance::

  pxf convert -O converted phenopacket.a.v1.json phenopacket.b.v1.json

converts the input phenopackets and stores the results in the ``converted`` folder. The converted files will be stored
under the same names.


``validate`` - validate Phenopacket Schema elements
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``validate`` command checks *phenopacket*, *family*, or *cohort* for the *base* requirements imposed by
the Phenopacket Schema as well as additional user-defined constraints.

Briefly, to meet the base requirements, the phenopacket must be well formatted (valid Protobuf message, JSON document, etc.)
and meet the requirements of the Phenopacket schema; all REQUIRED attributes are set (e.g. ``phenopacket.id`` and
``phenopacket.meta_data``), and ``MetaData`` includes a ``Resource`` for all ontology concepts.

The validation can include a number of additional steps, as required by a project or a consortium.
*Phenopacket-tools* offers several off-the-shelf validators and the CLI uses the validators in the validation workflow
if the required resources are present.

Usage
#####

The ``validate`` command can validate one or more phenopacket files provided either via standard input or
as positional parameters. Results are written into the standard output in CSV format including an optional header
containing the validation metadata. The header lines start with ``#`` and contain *phenopacket-tools* version,
date and time of validation, and the list of validators that were run.
The header is followed by a row with column names, and the individual validation results.

Base validation example
~~~~~~~~~~~~~~~~~~~~~~~

Let's demonstrate the base validation usage using a few examples. Phenopacket can be validated on a stream::

  cat phenopacket.json | pxf validate

or as a positional parameter::

  pxf validate phenopacket.json

Use ``-H | --include-header`` to include the validation metadata in the output and store the results in a file::

  pxf validate -H phenopacket.json > phenopacket.validation.csv


Custom validation example
~~~~~~~~~~~~~~~~~~~~~~~~~

On top of the base validation, *phenopacket-tools* supports validation using a custom requirements.
See the :ref:`rstcustomvalidation` section to learn how to define a custom JSON schema.

The CLI can be provided with one or more JSON schema documents using the ``--require`` option::

  pxf validate --require custom-schema.json phenopacket.json

Phenotype validation
~~~~~~~~~~~~~~~~~~~~

*Phenopacket-tools* includes off-the-shelf validators for pointing out annotation errors in phenopackets that use
Human Phenotype Ontology (HPO) to represent clinical findings of the subjects.
The validators check presence of obsolete or unknown ontology concepts and violations
of the annotation propagation rule based on a HPO file.

The CLI will automatically add the phenotype validation steps into the validation workflow if path to a HPO JSON file
is provided via the ``--hpo`` option::

  pxf validate --hpo hp.json phenopacket.json

.. note::
  The bulk validation where phenopackets are provided as positional parameters is much faster
  since the HPO graph parsing, a computationally expensive operation, is done only once.

Organ system validation
~~~~~~~~~~~~~~~~~~~~~~~

It can be desirable to check annotation of specific organ systems in the phenopacket. *Phenopacket-tools* can validate
annotation of specific organ systems by using the corresponding top-level HPO concepts, such as
`Eye <https://hpo.jax.org/app/browse/term/HP:0000478>`_,
`Cardiovascular <https://hpo.jax.org/app/browse/term/HP:0001626>`_, or
`Respiratory <https://hpo.jax.org/app/browse/term/HP:0002086>`_ organ systems.

The organ systems are provided using ``-s | --organ-system`` option::

  pxf validate --hpo hp.json \
    -s HP:0000478 \
    -s HP:0001626 \
    -s HP:0002086 \
    phenopacket.json

.. note::
  The organ system validation requires HPO file to run.

