# phenopacket-tools

[![GitHub release](https://img.shields.io/github/release/phenopackets/phenopacket-tools.svg)](https://github.com/phenopackets/phenopacket-tools/releases)
[![Java CI](https://github.com/phenopackets/phenopacket-tools/workflows/Java%20CI/badge.svg)](https://github.com/phenopackets/phenopacket-tools/actions/workflows/main.yml)

Multimodule Java library/app that contains a "streamlined builder" module, a v1 to v2 converter app, a validator API and a JSON validator. 

Both library and app requires Java 17 or better to run.

## Phenopacket tools CLI

The cli application works in a standard UNIX-like manner. 

```shell
cd phenopacket-tools
./mvnw package
PXF_VERSION="0.4.5-SNAPSHOT"
alias pfx-tools="java -jar $(pwd)/phenopacket-tools-cli/target/phenopacket-tools-cli-${PXF_VERSION}.jar"
pfx-tools --help
```

### Example Phenopackets
The ``examples`` command in the CLI module writes a series of example phenopackets to a directory.

The command creates `phenopackets`, `families` and `cohorts` sub-folders in the provided directory to write 
example *phenopackets*, *families*, and *cohorts*.

```shell
# create sub-folders folder and write the messages to the current directory by default
pfx-tools examples
# or write explicitly to the current directory
pfx-tools examples -o .
# or write to another directory
pfx-tools examples --output ~/phenopacket-examples
```

### Validate Phenopacket JSON files

```shell
# validate a single phenopacket 
pfx-tools validate phenopacket ~/phenopacket-examples/phenopackets/covid.json
# or all the phenopacket json files in a directory
pfx-tools validate phenopacket ~/phenopacket-examples/phenopackets/*.json
# or all families
pfx-tools validate family ~/phenopacket-examples/families/*.json
```

### Convert Phenopacket versions

```shell
pfx-tools convert phenopacket.json
```


see this for VRS -- https://github.com/ga4gh/vrs/blob/76542a903b913110e67811885a8958625bbc3aae/schema/vrs.json
import it like vrsatile