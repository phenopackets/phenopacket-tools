# README

The folder contains a few phenopackets for demonstrating the base validation functionality of *phenopacket-tools*; 
the validation that any phenopacket must pass.

The validator will report the validation issues, one issue per line. The next sections show different types 
of validation errors that can be found using *phenopacket-tools*. 

## `missing-fields.json`

The `missing-fields.json` is a phenopacket where several required attributes are missing. Presence of all required
attributes is checked at the beginning of the validation, before any other checks. The *phenopacket-tools* validator 
will point out the following issues:

| Message                                                           | Solution                                                             |
|:------------------------------------------------------------------|:---------------------------------------------------------------------|
| `id` is missing but it is required.                               | Add phenopacket ID.                                                  |
| `subject.id` is missing but it is required.                       | Add subject ID.                                                      |
| `phenotypicFeatures[0].type.label` is missing but it is required. | Add the `label` attribute into `phenotypicFeatures[0].type.label`.   |

See `missing-fields-valid.json` for a valid version of the phenopacket:

```shell
# Use UNIX diff to highlight differences between two files
diff missing-fields.json missing-fields-valid.json
```

## `missing-resources.json`

The `missing-resources.json` is a phenopacket with no missing fields, so it passes the syntax validation. 
However, the phenopacket is invalid because it uses ontologies that are not defined in the `metaData.resource` section.
The validator will point out the following issues:

| Message                                           | Solution                                                   |
|:--------------------------------------------------|:-----------------------------------------------------------|
| No ontology corresponding to ID 'NCBITaxon:9606'  | Add a `Resource` for `NCBITaxon` into `metadata.resources` |

See `missing-resources-valid.json` for a valid version of the phenopacket:

```shell
diff missing-resources.json missing-resources-valid.json
```
