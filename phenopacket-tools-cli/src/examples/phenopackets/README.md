# README

The folder contains a few phenopackets for demonstrating the validation functionality of *phenopacket-tools*. 
The validator will report the validation issues, one issue per line. The next sections show different types 
of validation errors that can be found using *phenopacket-tools*. 

## `syntax-errors.json`

The `syntax-errors.json` is a phenopacket where several required attributes are missing. 
The *phenopacket-tools* validator will point out the following issues:

| Path                                 | Message                        | Solution                                                             |
|:-------------------------------------|:-------------------------------|:---------------------------------------------------------------------|
| `$.id`                               | Is missing but it is required. | Add phenopacket ID.                                                  |
| `$.subject.id`                       | Is missing but it is required. | Add subject ID.                                                      |
| `$.phenotypicFeatures[0].type.label` | Is missing but it is required. | Add the `label` attribute into `$.phenotypicFeatures[0].type.label`. |

## `semantic-errors.json`

The `semantic-errors.json` is a phenopacket with no syntax errors. However, there are several semantic inconsistencies:

| Path  | Message | Solution |
|:------|:--------|:---------|
|       |         |          |
|       |         |          |

 
- `retinoblastoma.json`