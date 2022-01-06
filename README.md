# phenopacket-tools

Multimodule Java library/app that contains a "streamlined builder" module, a v1 to v2 converter app, a validator API and a JSON validator. 


## Phenopacket tools CLI

The cli application works in a standard UNIX-like manner. 

```shell
mvn package
alias pfx-tools='java -jar phenotools-cli/target/phenotools-cli-0.0.1-SNAPSHOT.jar'
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