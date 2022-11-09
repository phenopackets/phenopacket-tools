# README

This folder contains JSON schemas for validating top-level Phenopacket schema elements and VRS elements.

## VRSATILE notes

The datatype of the `VcfRecord.pos` field in `vrsatile.proto` is:
```
uint64 pos = 3;
```

Since Protobuf's `JSONFormat` serializes `uint64` fields into a JSON `string` instead of JSON `number`,
the JSON type of the `VcfRecord.pos` field is a: 

```
  "type": "string",
  "pattern": "^[1-9][0-9]*$"
```

instead of a more straightforward:

```
"type": "integer"
```
