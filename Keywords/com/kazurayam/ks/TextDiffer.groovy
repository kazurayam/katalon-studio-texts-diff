package com.kazurayam.ks

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Function
import java.util.stream.Collectors

import com.github.difflib.text.DiffRow
import com.github.difflib.text.DiffRowGenerator
import com.kms.katalon.core.annotation.Keyword

/**
 * Compare 2 text files, create a report which shows the diff of the 2 inputs.
 * The report is in Markdown format. 
 * 
 * @author kazurayam
 */
public class TextDiffer {

	public TextDiffer() {}

	@Keyword
	public void processFiles(String text1, String text2, String output) {
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		this.processFiles(Paths.get("."), Paths.get(text1), Paths.get(text2), Paths.get(output));
	}

	@Keyword
	public void processFiles(String baseDir, String text1, String text2, String output) {
		Objects.requireNonNull(baseDir)
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		Path dir = Paths.get(baseDir)
		this.processFiles(dir, dir.resolve(text1), dir.resolve(text2), dir.resolve(output))
	}

	public void processFiles(Path baseDir, Path text1, Path text2, Path output) {
		validateInputs(baseDir, text1, text2)
		baseDir = baseDir.toAbsolutePath()
		ensureParentDir(output)

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
		sb.append(mdFilePath(baseDir, text1, text2))
		sb.append(mdDifferentOrNot(rows, equalRows))
		sb.append("\n")
		sb.append(mdStats(insertedRows, deletedRows, changedRows, equalRows))
		sb.append("\n")
		sb.append(mdDetail(rows))
		
		//println the diff report into the output file
		output.toFile().text = sb.toString()
	}

	private void validateInputs(Path baseDir, Path text1, Path text2) throws Exception {
		// baseDir can be null
		if (baseDir != null) {
			if (!Files.exists(baseDir)) {
				throw new FileNotFoundException(baseDir.toString())
			}
		}
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		if (!Files.exists(text1)) {
			throw new FileNotFoundException(text1.toString())
		}
		if (!Files.exists(text2)) {
			throw new FileNotFoundException(text2.toString())
		}
	}

	private void ensureParentDir(Path output) {
		Path p = output.getParent();
		if (!Files.exists(p)) {
			Files.createDirectories(p)
		}
	}

	private String relativize(Path baseDir, Path file) {
		assert baseDir.isAbsolute()
		if (file.isAbsolute()) {
			return file.normalize().toString()
		} else {
			try {
				return baseDir.relativize(file).normalize().toString()
			} catch (Exception e) {
				return file.normalize().toString()
			}
		}
	}
	
	private String mdFilePath(Path baseDir, Path text1, Path text2) {
		StringBuilder sb = new StringBuilder()
		sb.append("- original: `${ relativize(baseDir, text1) }`\n")
		sb.append("- revised : `${ relativize(baseDir, text2) }`\n\n")
		return sb.toString()
	}
	
	private String mdDifferentOrNot(List<DiffRow> rows, List<DiffRow> equalRows) {
		StringBuilder sb = new StringBuilder()
		sb.append((equalRows.size() < rows.size()) ? '**DIFFERENT**' : '**NO DIFF**')
		sb.append("\n")
		return sb.toString()
	}
	
	private String mdStats(List<DiffRow> insertedRows, List<DiffRow> deletedRows, 
		List<DiffRow> changedRows, List<DiffRow> equalRows) {
		StringBuilder sb = new StringBuilder();
		sb.append("- inserted rows: ${insertedRows.size()}\n")
		sb.append("- deleted rows : ${deletedRows.size()}\n")
		sb.append("- changed rows : ${changedRows.size()}\n")
		sb.append("- equal rows:  : ${equalRows.size()}\n")
		return sb.toString()
	}
	
	private String mdDetail(List<DiffRow> rows) {
		StringBuilder sb = new StringBuilder()
		sb.append("|line#|original|revised|\n")
		sb.append("|-----|--------|-------|\n")
		rows.eachWithIndex { DiffRow row, index ->
			sb.append("|" + (index+1) + "|" + row.getOldLine() + "|" + row.getNewLine() + "|\n")
		}
		return sb.toString()
	}
}
