import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Function
import java.util.stream.Collectors

import com.github.difflib.text.DiffRow
import com.github.difflib.text.DiffRowGenerator
import com.kms.katalon.core.configuration.RunConfiguration


Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path doc1 = projectDir.resolve("Include/fixtures/doc1.xml")
Path doc2 = projectDir.resolve("Include/fixtures/doc2.xml")

//build simple lists of the lines of the two testfiles
List<String> original = Files.readAllLines(doc1);
List<String> revised = Files.readAllLines(doc2);

// Compute the difference between two texts and print it in humann-readable markup style
DiffRowGenerator generator = 
	DiffRowGenerator.create()
					.showInlineDiffs(true)
					.inlineDiffByWord(true)
					.oldTag({ f -> "*" } as Function)
					.newTag({ f -> "**" } as Function)
					.build()

List<DiffRow> rows = generator.generateDiffRows(original, revised)

List<DiffRow> insertedRows = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.INSERT}).collect(Collectors.toList())
List<DiffRow> deletedRows  = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.DELETE}).collect(Collectors.toList())
List<DiffRow> changedRows  = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.CHANGE}).collect(Collectors.toList())
List<DiffRow> equalRows    = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.EQUAL}).collect(Collectors.toList())

// print the diff info in Markdown format into a file out.md
StringBuilder sb = new StringBuilder()
sb.append("- original: `${doc1}`\n")
sb.append("- revised : `${doc2}`\n\n")

sb.append((equalRows.size() < rows.size()) ? '**DIFFERENT**' : '**NO DIFF**')
sb.append("\n\n")

sb.append("- inserted rows: ${insertedRows.size()}\n")
sb.append("- deleted rows : ${deletedRows.size()}\n")
sb.append("- changed rows : ${changedRows.size()}\n")
sb.append("- equal rows:  : ${equalRows.size()}\n\n")

sb.append("|line#|original|revised|\n");
sb.append("|-----|--------|-------|\n");
rows.eachWithIndex { DiffRow row, index ->
	sb.append("|" + (index+1) + "|" + row.getOldLine() + "|" + row.getNewLine() + "|\n");
}


//println sb.toString()
Path out = projectDir.resolve("out.md")
out.toFile().text = sb.toString()