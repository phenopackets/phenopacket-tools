from os import listdir
from os.path import isfile, join
from csv import DictReader

# This is the path we write the class files to
JAVA_DIR_PATH='../phenopacket-tools-builder/src/main/java/org/phenopackets/phenopackettools/builder/constants/'

class ConstantItem:
    def __init__(self, id, label, varname, funname) -> None:
        # Check that none of the arguments has a trailing or leading whitespace
        # which could lead to oddities in the generated Java code
        for x in [id, label, varname, funname]:
            if x.startswith(" ") or x.endswith(" "):
                raise ValueError(f"CSV item \"{x}\" has stray whitespace. Correct this and try again")
        self._ontology_id    = id
        self._ontology_label = label
        self._variable_name	 = varname
        self._function_name  = funname
        
    @property
    def ontology_id(self):
        return self._ontology_id
    
    @property
    def ontology_label(self):
        return self._ontology_label
    
    @property
    def variable_name(self):
        return self._variable_name
    
    @property
    def function_name(self):
        return self._function_name



class Entry:
    def __init__(self, filename, items):
        suffix = ".csv"
        if  filename.endswith(suffix):
            self._name = filename[:-len(suffix)]
        else:
            raise ValueError(f"Malformed csv filename: {filename}")
        if not isinstance(items, list):
            raise ValueError(f"items argument must be a list but we got {type(items)}")
        self._constant_items = items
        
    @property
    def name(self):
        return self._name
    
    @property
    def items(self):
        return self._constant_items


def parse_csv(fname):
    if not isfile(fname):
        raise ValueError(f"fname argument {fname} was not a valid file")
    items = []
    with open(fname) as f:
        reader = DictReader(f, delimiter='\t')
        for row in reader:
            item = ConstantItem(id=row['ontology.id'], label=row['ontology.label'], varname=row['variable.name'], funname=row['function.name'])
            items.append(item)
    return Entry(filename=fname, items=items)




def create_java_class(entry):
    java_file_name = entry.name + ".java" # LATER adjust path
    java_file_path = join(JAVA_DIR_PATH, java_file_name)
    fh = open(java_file_path, 'wt')
    fh.write("package org.phenopackets.phenopackettools.builder.constants;\n\n")
    fh.write("import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;\n")
    fh.write("import org.phenopackets.schema.v2.core.OntologyClass;\n\n")
    fh.write(f"public class {entry.name} {{\n\n")
    items = entry.items
    for item in items:
        # e.g.,   private static final OntologyClass HETEROZYGOUS = OntologyClassBuilder.ontologyClass("GENO:0000135", "heterozygous");
        fh.write(f"  private static final OntologyClass {item.variable_name} = OntologyClassBuilder.ontologyClass(")
        fh.write(f"\"{item.ontology_id}\", \"{item.ontology_label}\");\n")
    fh.write("\n\n")
    for item in items:
        # e.g.,  public static OntologyClass heterozygous() {return HETEROZYGOUS; }
        fh.write(f"  public static OntologyClass {item.function_name}() {{ return {item.variable_name}; }}\n")
    fh.write("\n}\n")
    fh.close()











entries = []

csv_files = [f for f in listdir(".") if isfile( f) and f.endswith("tsv")]
for f in csv_files:
    constant_entry = parse_csv(f)
    entries.append(constant_entry)
    
    
for e in entries:
    create_java_class(e)
    



