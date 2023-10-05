import com.kazurayam.ks.TextsDiffer
import com.kazurayam.ks.JsonPrettyPrinter

// ex43 pretty print JSON then diff

String textA = """{
	"key1": "value1",
	"key2": "value2"
}
"""
File fileA = new File("build/tmp/testOutput/ex43-fileA.json")
fileA.text = textA

String textB = """{
	"key2": "value2",
	"key1": "value1"
}
"""
File fileB = new File("build/tmp/testOutput/ex43-fileB.json")
fileB.text = textB

// compare 2 JSON files as is
TextsDiffer differ = new TextsDiffer()
differ.diffFiles(fileA, fileB, new File("build/tmp/testOutput/ex43-output1.md"))

// convert 2 JSON texts (order Map entries by keys)
String ppA = JsonPrettyPrinter.orderMapEntriesByKeys(textA)
String ppB = JsonPrettyPrinter.orderMapEntriesByKeys(textB)

// compare 2 converted JSON texts
differ.diffStrings(ppA, ppB, "build/tmp/testOutput/ex43-output2.md")
