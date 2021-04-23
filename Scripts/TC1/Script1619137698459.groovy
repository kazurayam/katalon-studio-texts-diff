import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Function

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
DiffRowGenerator generator = DiffRowGenerator.create()
								.showInlineDiffs(true)
								.inlineDiffByWord(true)
								.oldTag({ f -> "~" } as Function)
								.newTag({ f -> "**" } as Function)
								.build()

List<DiffRow> rows = generator.generateDiffRows(original, revised)

// print the diff info in Markdown format into a file out.md
StringBuilder sb = new StringBuilder() 
sb.append("|#|original|revised|\n");
sb.append("|---|--------|-------|\n");
rows.eachWithIndex { DiffRow row, int index ->
	sb.append("|" + index+1 + "|" + row.getOldLine() + "|" + row.getNewLine() + "|\n");
}

//println sb.toString()
Path out = projectDir.resolve("out.md")
out.toFile().text = sb.toString()