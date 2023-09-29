package com.kazurayam.ks

import java.nio.charset.StandardCharsets
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
public class TextsDiffer {

	public TextsDiffer() {}

	@Keyword
	public void diffFiles(String text1, String text2, String output) {
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		this.diffFiles(Paths.get("."), Paths.get(text1), Paths.get(text2), Paths.get(output));
	}

	@Keyword
	public void diffFiles(String baseDir, String text1, String text2, String output) {
		Objects.requireNonNull(baseDir)
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		Path dir = Paths.get(baseDir)
		this.diffFiles(dir, dir.resolve(text1), dir.resolve(text2), dir.resolve(output))
	}

	@Keyword
	public void diffURLs(String url1, String url2, String output) {
		this.diffURLs(new URL(url1), new URL(url2), Paths.get(output))
	}

	public void diffFiles(Path baseDir, Path text1, Path text2, Path output) {
		validateInputs(baseDir, text1, text2)
		baseDir = baseDir.toAbsolutePath()

		//read all lines of the two text files
		List<String> original = Files.readAllLines(text1)
		List<String> revised = Files.readAllLines(text2)

		StringBuilder sb = new StringBuilder()

		// compile the diff report with the file path information
		sb.append(mdFilePath(baseDir, text1, text2))
		sb.append("\n")
		sb.append(compileReport(original, revised))

		//println the diff report into the output file
		ensureParentDir(output)
		output.toFile().text = sb.toString()
	}

	@Keyword
	public String diffStrings(String text1, String text2) {
		return diffReaders(new StringReader(text1), new StringReader(text2))
	}

	public String diffReaders(Reader reader1, Reader reader2) {
		List<String> original = readAllLines(reader1)
		List<String> revised = readAllLines(reader2)

		StringBuilder sb = new StringBuilder()
		sb.append(compileReport(original, revised))
		return sb.toString()
	}

	public String diffInputStreams(InputStream is1, InputStream is2) {
		Objects.requireNonNull(is1)
		Objects.requireNonNull(is2)
		Reader reader1 = new InputStreamReader(is1, StandardCharsets.UTF_8)
		Reader reader2 = new InputStreamReader(is2, StandardCharsets.UTF_8)
		return diffReaders(reader1, reader2)
	}

	public void diffURLs(URL url1, URL url2, Path output) {
		InputStream is1 = url1.openStream()
		InputStream is2 = url2.openStream()
		String report = this.diffInputStreams(is1, is2)
		ensureParentDir(output)
		output.toFile().text = report
	}

	public String compileReport(List<String> original, List<String> revised) {
		// compute the difference between the two
		DiffRowGenerator generator =
				DiffRowGenerator.create()
				.showInlineDiffs(true)
				.inlineDiffByWord(true)
				.oldTag({ f -> f ? "<span style=\"color:red; font-weight:bold; background-color:#ffeef0\">" : "</span>" } as Function)
				.newTag({ f -> f ? "<span style=\"color:green; font-weight:bold; background-color:#e6ffec\">" : "</span>" } as Function)
				.build();

		List<DiffRow> rows = generator.generateDiffRows(original, revised);
		List<DiffRow> insertedRows = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.INSERT}).collect(Collectors.toList());
		List<DiffRow> deletedRows  = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.DELETE}).collect(Collectors.toList());
		List<DiffRow> changedRows  = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.CHANGE}).collect(Collectors.toList());
		List<DiffRow> equalRows    = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.EQUAL}).collect(Collectors.toList());

		// generate a diff report in Markdown format
		StringBuilder sb = new StringBuilder()
		sb.append(mdDifferentOrNot(rows, equalRows))
		sb.append("\n")
		sb.append(mdStats(insertedRows, deletedRows, changedRows, equalRows))
		sb.append("\n")
		sb.append(mdDetail(rows))
		return sb.toString()
	}

	//-----------------------------------------------------------------

	private List<String> readAllLines(Reader reader) {
		return new BufferedReader(reader).lines().collect(Collectors.toList());
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
		sb.append("- revised : `${ relativize(baseDir, text2) }`\n")
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
		sb.append("|line#|S|original|revised|\n")
		sb.append("|-----|-|--------|-------|\n")
		rows.eachWithIndex { DiffRow row, index ->
			sb.append("|" + (index+1) + "|" + getStatus(row) + "|" +
					row.getOldLine() + "|" + row.getNewLine() + "|\n")
		}
		return sb.toString()
	}

	private String getStatus(DiffRow dr) {
		if (dr.getTag() == DiffRow.Tag.INSERT) {
			return "I"
		} else if (dr.getTag() == DiffRow.Tag.DELETE) {
			return "D"
		} else if (dr.getTag() == DiffRow.Tag.CHANGE) {
			return "C"
		} else {
			return " "
		}
	}

	private static String TAG_INSERTED_COLOR = "#e6ffec";
	private static String TAG_DELETED_COLOR  = "#ffeef0";
	private static String TAG_CHANGED_COLOR  = "#dbedff";

}
