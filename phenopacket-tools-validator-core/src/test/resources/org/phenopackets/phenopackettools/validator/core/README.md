# README

The folder contains test resources.

## HPO
The `hp.module.json` contains Human Phenotype Ontology module in Obographs format. For the *Phenotypic abnormality* 
subhierarchy, the module contains *Arachnodactyly* (`HP:0001166`), *Focal clonic seizure* (`HP:0002266`),
and their ancestors. The module contains all other HPO subhierarchies.

Run the following to prepare the module:

```shell
module load robot/1.9.8

HPO=https://github.com/obophenotype/human-phenotype-ontology/releases/download/v2026-06-06/hp.obo

wget $HPO

# Arachnodactyly HP:0001166
robot extract --input hp.obo --method BOT --term HP:0001166 \
  convert --output arachnodactyly.hp.obo

# Focal clonic seizure HP:0002266  
robot extract --input hp.obo --method BOT --term HP:0002266 \
  convert --output fcs.hp.obo
  
# We use a kind of a hack to include both
# Clinical modifier HP:0012823
robot extract --input hp.obo --method BOT --term HP:0012823 \
  convert --output cm.bot.hp.obo
robot extract --input hp.obo --method TOP --term HP:0012823 \
  convert --output cm.top.hp.obo
  
# Frequency HP:0040279
robot extract --input hp.obo --method BOT --term HP:0040279 \
  convert --output freq.bot.hp.obo
robot extract --input hp.obo --method TOP --term HP:0040279 \
  convert --output freq.top.hp.obo
  
# Mode of inheritance HP:0000005
robot extract --input hp.obo --method BOT --term HP:0000005 \
  convert --output moi.bot.hp.obo
robot extract --input hp.obo --method TOP --term HP:0000005 \
  convert --output moi.top.hp.obo

# Past medical history HP:0032443
robot extract --input hp.obo --method BOT --term HP:0032443 \
  convert --output pmh.bot.hp.obo
robot extract --input hp.obo --method TOP --term HP:0032443 \
  convert --output pmh.top.hp.obo
  
# Blood group HP:0032223
robot extract --input hp.obo --method BOT --term HP:0032223 \
  convert --output bg.bot.hp.obo
robot extract --input hp.obo --method TOP --term HP:0032223 \
  convert --output bg.top.hp.obo
  
# Merge into one file
robot merge --input arachnodactyly.hp.obo \
  --input fcs.hp.obo \
  --input cm.bot.hp.obo \
  --input cm.top.hp.obo \
  --input freq.bot.hp.obo \
  --input freq.top.hp.obo \
  --input moi.bot.hp.obo \
  --input moi.top.hp.obo \
  --input pmh.bot.hp.obo \
  --input pmh.top.hp.obo \
  --input bg.bot.hp.obo \
  --input bg.top.hp.obo \
  --output hp.module.json

rm *.obo
```