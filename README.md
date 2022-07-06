# phenopacket-tools

[![GitHub release](https://img.shields.io/github/release/phenopackets/phenopacket-tools.svg)](https://github.com/phenopackets/phenopacket-tools/releases)
[![Java CI](https://github.com/phenopackets/phenopacket-tools/workflows/Java%20CI/badge.svg)](https://github.com/phenopackets/phenopacket-tools/actions/workflows/main.yml)

Multimodule Java library/app that contains a "streamlined builder" module, a v1 to v2 converter app, a validator API and a JSON validator. 


## Phenopacket tools CLI

The cli application works in a standard UNIX-like manner. 

```shell
cd phenopacket-tools
./mvnw package
alias pfx-tools='java -jar $(pwd)/phenopacket-tools-cli/target/phenopacket-tools-cli-*.jar'
```

### Example Phenopackets
The ``examples`` command in the CLI module writes a series of example Phenopackets to file.

```shell
# writes to the current directory by default
pfx-tools examples
# or write explicitly to the current directory
pfx-tools examples .
# or write to another directory
pfx-tools examples ~/phenopacket-examples
```

### Convert Phenopacket versions

```shell
pfx-tools convert phenopacket.json
```

### Validate Phenopacket JSON files

```shell
pfx-tools validate ~/phenopacket-examples/covid.json
# or all the json files in a directory
pfx-tools validate ~/phenopacket-examples/*.json
```
