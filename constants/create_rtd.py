from os import listdir
from os.path import isfile, join
from create_classes import Entry, ConstantItem, parse_csv



# https://docutils.sourceforge.io/docs/ref/rst/directives.html#csv-table-1
"""
    .. csv-table:: Frozen Delights!
   :header: "Treat", "Quantity", "Description"
   :widths: 15, 10, 30

   "Albatross", 2.99, "On a stick!"
   "Crunchy Frog", 1.49, "If we took the bones out, it wouldn't be
   crunchy, now would it?"
   "Gannet Ripple", 1.99, "On a stick!"

"""

entries = []

csv_files = [f for f in listdir(".") if isfile( f) and f.endswith("csv")]
for f in csv_files:
    constant_entry = parse_csv(f)
    entries.append(constant_entry)
    


def create_csv_table(entry, fh):
    title = entry.name
    n = len(title)
    
    fh.write(f"{title}\n")
    fh.write("^"*n + "\n\n")
    fh.write(f".. csv-table:: \n")
    fh.write("   :header: \"id\", \"label\", \"function name\"\n")
    fh.write("   :widths: 30, 200, 200\n")
    fh.write("\n")
    items = entry.items
    for item in items:
        fh.write(f"   \"{item.ontology_id}\", \"{item.ontology_label}\", \"{item.function_name}()\"\n")
    fh.write("\n\n")


fh = open("constants.rst", "wt")
fh.write(".. _rstconstants:\n\n")
fh.write("=========\n")
fh.write("Constants\n")
fh.write("=========\n\n")
fh.write("The phenopacket-tools library offers a selection of recommended and predefined OntologyClass objects for commonly used concepts.\n")
fh.write("For instance, this is the code one would need to write using the native Protobuf framework")
fh.write("to get an OntologyClass instance that represents the modifier ``Left``.\n\n")
fh.write(".. code-block:: java\n\n")
fh.write("   OntologyClass left = OntologyClass.newBuilder()\n")
fh.write("     .setId(\"HP:0012835\")\n")
fh.write("     .setLabel(\"Left\")\n")
fh.write("     .build();\n")
fh.write("\n\n")
fh.write("In contrast, this is the code required with phenopacket-tools (omitting import statements in both cases)\n\n")  
fh.write(".. code-block:: java\n\n")
fh.write("   OntologyClass left = left();\n")
fh.write("\n\n")
fh.write("The following tables present the available static functions with predefined concepts.\n\n")
for e in entries:
    create_csv_table(e, fh=fh)
fh.close()