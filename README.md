[![Java CI](https://github.com/phenopackets/phenopacket-tools/workflows/Java%20CI/badge.svg)](https://github.com/phenopackets/phenopacket-tools/actions/workflows/main.yml)
[![GitHub release](https://img.shields.io/github/release/phenopackets/phenopacket-tools.svg)](https://github.com/phenopackets/phenopacket-tools/releases)

# Phenopacket-tools

*Phenopacket-tools* is an open-source Java library and command-line interface (CLI) application for construction, conversion, 
and validation of phenopackets. 

The library simplifies construction of phenopackets by providing concise builders, 
programmatic shortcuts, and predefined building blocks (ontology classes) for commonly used elements 
such as anatomical organs, age of onset, biospecimen type, and clinical modifiers. 

The library validates the basic syntax and semantic requirements of the Phenopacket Schema as well 
as the adherence to additional user-defined requirements. 

## Availability

### CLI application

A ZIP file with prebuilt CLI application is available for download from the [Releases page](https://github.com/phenopackets/phenopacket-tools/releases).
Alternatively, the application can be built from source, as described in the library user guide (see the links below).

### Library

We provide *phenopacket-tools* library for use in JVM-based applications. 
We publish the JAR artifacts to public [Maven central](https://mvnrepository.com/artifact/org.phenopackets.phenopackettools) 
repository, and we make the Javadoc API documentation available for the latest release as well as the bleeding edge code. 

## Documentation

We provide documentation with a *Tutorial* showing how to use the CLI, a comprehensive *CLI user guide*,
a *Library user guide*, and the *Javadoc API documentation*.

The documentation is published in two documentation branches:
- **stable**: corresponds to the latest published release, and generally also to the last commit of the `main` Git branch
  - [Tutorial](http://phenopackets.org/phenopacket-tools/stable/tutorial.html)
  - [CLI user guide](http://phenopackets.org/phenopacket-tools/stable/cli.html)
  - [Library user guide](http://phenopackets.org/phenopacket-tools/stable)
  - [Javadoc API documentation](http://phenopackets.org/phenopacket-tools/stable/apidocs)
- **latest**: corresponds to the bleeding edge code that is on the `develop` Git branch
  - [Tutorial](http://phenopackets.org/phenopacket-tools/latest/tutorial.html)
  - [CLI user guide](http://phenopackets.org/phenopacket-tools/latest/cli.html)
  - [Library user guide](http://phenopackets.org/phenopacket-tools/latest)
  - [Javadoc API documentation](http://phenopackets.org/phenopacket-tools/latest/apidocs)  

## Issues

Comments, questions or issues? Feel free to submit a ticket to our [GitHub tracker](https://github.com/phenopackets/phenopacket-tools/issues).
