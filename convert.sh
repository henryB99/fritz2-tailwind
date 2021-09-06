#! /bin/bash

# script to convert heroicons

for f in `ls *.svg`; do 
    content=`sed '1d;$d' $f`
    name=`echo $f | sed "s/-/_/g" | sed "s/.svg$//"`
    echo "val $name: IconDefinition = { content(\"\"\"$content\"\"\".trimIndent())}"
done