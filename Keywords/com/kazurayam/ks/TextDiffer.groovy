package com.kazurayam.ks

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Function
import java.util.stream.Collectors

import com.github.difflib.text.DiffRow
import com.github.difflib.text.DiffRowGenerator
import com.kms.katalon.core.annotation.Keyword

public class TextDiffer {

	public TextDiffer() {}
	
	@Keyword
	public void execute(String text1, String text2, String output) {
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		this.execute(null, Paths.get(text1), Paths.get(text2), Paths.get(output));
	}
	
	@Keyword
	public void execute(String baseDir, String text1, String text2, String output) {
		Objects.requireNonNull(baseDir)
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		Path dir = Paths.get(baseDir)
		this.execute(dir, dir.resolve(text1), dir.resolve(text2), dir.resolve(output))
	}
	
	public void execute(Path baseDir, Path text1, Path text2, Path output) {
		validateParams(baseDir, text1, text2, output)
		
		//read all lines of the two text files
		List<String> original = Files.readAllLines(text1)
		List<String> revised = Files.readAllLines(text2)

		// compute the difference between the two
		DiffRowGenerator generator =
				DiffRowGenerator.create()
				.showInlineDiffs(true)
				.inlineDiffByWord(true)
				.oldTag({ f -> "*" } as Function)
				.newTag({ f -> "**" } as Function)
				.build();

		List<DiffRow> rows = generator.generateDiffRows(original, revised);
		List<DiffRow> insertedRows = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.INSERT}).collect(Collectors.toList());
		List<DiffRow> deletedRows  = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.DELETE}).collect(Collectors.toList());
		List<DiffRow> changedRows  = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.CHANGE}).collect(Collectors.toList());
		List<DiffRow> equalRows    = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.EQUAL}).collect(Collectors.toList());

		// generate a diff report in Markdown format
		StringBuilder sb = new StringBuilder()
		sb.append("- original: `${(baseDir != null) ? baseDir.relativize(text1) : text1}`\n")
		sb.append("- revised : `${(baseDir != null) ? baseDir.relativize(text2) : text2}`\n\n")

		sb.append((equalRows.size() < rows.size()) ? '**DIFFERENT**' : '**NO DIFF**')
		sb.append("\n\n")

		sb.append("- inserted rows: ${insertedRows.size()}\n")
		sb.append("- deleted rows : ${deletedRows.size()}\n")
		sb.append("- changed rows : ${changedRows.size()}\n")
		sb.append("- equal rows:  : ${equalRows.size()}\n\n")

		sb.append("|line#|original|revised|\n")
		sb.append("|-----|--------|-------|\n")
		rows.eachWithIndex { DiffRow row, index ->
			sb.append("|" + (index+1) + "|" + row.getOldLine() + "|" + row.getNewLine() + "|\n")
		}

		//println the diff report into the output file
		output.toFile().text = sb.toString()
	}

	private void validateParams(Path baseDir, Path text1, Path text2, Path output) throws Exception {
		// baseDir can be null
		if (baseDir != null) {
			if (!Files.exists(baseDir)) {
				throw new FileNotFoundException(baseDir)
			}
		}
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		if (!Files.exists(text1)) {
			throw new FileNotFoundException(text1)
		}
		if (!Files.exists(text2)) {
			throw new FileNotFoundException(text2)
		}
		Path outDir = output.getParent()
		if (!Files.exists(outDir)) {
			throw new FileNotFoundException(outDir)
		}
	}

}
