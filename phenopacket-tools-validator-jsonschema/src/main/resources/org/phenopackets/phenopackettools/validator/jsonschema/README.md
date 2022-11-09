# README

This folder contains JSON schemas for validating top-level Phenopacket Schema elements and the `Variation` element 
embedded in the Phenopacket Schema.

## VRSATILE notes

The datatype of the `VcfRecord.pos` field in `vrsatile.proto` is:
```
uint64 pos = 3;
```

Since Protobuf's `JSONFormat` serializes `uint64` fields into a JSON `string` instead of a JSON `number`,
the JSON schema element for validation of the `VcfRecord.pos` field is: 

```
"type": "string",
"pattern": "^[1-9][0-9]*$"
```

instead of a more straightforward:

```
"type": "integer"
```
