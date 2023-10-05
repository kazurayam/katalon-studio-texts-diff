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
import groovy.json.JsonOutput

/**
 * Compare 2 texts, create a report which shows the diff of the 2 inputs.
 * The input text could be an instance of various classes: java.nio.file.Path,
 * java.io.File, java.lang.String, java.net.URL, java.io.Reader and java.io.InputStream.
 *
 * This class can be used as a custom keyword in Katalon Studio.
 *
 * The output report is compiled in Markdown format.
 * So you would require a Markdown viewer.
 * For example, Visual Studio Code with Markdown:
 *
 * https://code.visualstudio.com/Docs/languages/markdown
 *
 * @author kazurayam
 * @date Sept, 2023
 */
public final class TextsDiffer {

	public TextsDiffer() {}

	@Keyword
	public final String diffFiles(String text1, String text2, String output) {
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		return this.diffFiles(Paths.get("."), Paths.get(text1), Paths.get(text2), Paths.get(output));
	}

	@Keyword
	public final String diffFiles(String baseDir, String text1, String text2, String output) {
		Objects.requireNonNull(baseDir)
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		Path dir = Paths.get(baseDir)
		return this.diffFiles(dir, Paths.get(text1), Paths.get(text2), Paths.get(output))
	}

	public final String diffFiles(File file1, File file2, File output) {
		return diffFiles(file1.toPath(), file2.toPath(), output.toPath())	
	}
	
	public final String diffFiles(Path file1, Path file2, Path output) {
		return this.diffFiles(Paths.get("."), file1, file2, output)
	}

	public final String diffFiles(Path baseDir, Path text1, Path text2, Path output) {
		baseDir = baseDir.toAbsolutePath()
		Path t1 = baseDir.resolve(text1)
		// here is a small trick which you may not be aware of
		// if the text1 is relative, then t1 will be an absolute path based on the baseDir + text1
		// if the text1 is absolute, then t1 will just the same as text1
		Path t2 = baseDir.resolve(text2)
		validateInputs(baseDir, t1, t2)

		//read all lines of the two text files
		List<String> original = Files.readAllLines(t1)
		List<String> revised = Files.readAllLines(t2)
		DiffInfo diffInfo = new DiffInfo(original, revised)

		StringBuilder sb = new StringBuilder()
		// compile the diff report with the file path information
		sb.append(mdFilePath(baseDir, text1, text2))
		sb.append("\n")
		sb.append(compileMarkdownReport(diffInfo))
		//println the diff report into the output file
		ensureParentDir(output)
		output.toFile().text = sb.toString()

		// return the concise statistics in JSON
		return compileJsonReport(diffInfo)
	}

	@Keyword
	public final String diffStrings(String text1, String text2) {
		List<String> original = readAllLines(new StringReader(text1))
		List<String> revised = readAllLines(new StringReader(text2))
		DiffInfo diffInfo = new DiffInfo(original, revised)
		return compileJsonReport(diffInfo)
	}

	@Keyword
	public final String diffStrings(String text1, String text2, String output) {
		List<String> original = readAllLines(new StringReader(text1))
		List<String> revised = readAllLines(new StringReader(text2))
		DiffInfo diffInfo = new DiffInfo(original, revised)
		Path out = Paths.get(output)
		ensureParentDir(out)
		out.toFile().text = compileMarkdownReport(diffInfo)
		return compileJsonReport(diffInfo)
	}

	@Keyword
	public final String diffURLs(String url1, String url2, String output) {
		return this.diffURLs(new URL(url1), new URL(url2), Paths.get(output))
	}

	public final String diffURLs(URL url1, URL url2, Path output) {
		StringBuilder sb = new StringBuilder()
		sb.append("- original: `${ url1.toString() }`\n")
		sb.append("- revised : `${ url2.toString() }`\n")
		sb.append("\n")
		InputStream is1 = url1.openStream()
		InputStream is2 = url2.openStream()
		DiffInfo diffInfo = this.diffByteStreams(is1, is2)
		sb.append(compileMarkdownReport(diffInfo))
		ensureParentDir(output)
		output.toFile().text = sb.toString()
		return compileJsonReport(diffInfo)
	}

	public final String compileMarkdownReport(DiffInfo diffInfo) {
		// generate a diff report in Markdown format
		StringBuilder sb = new StringBuilder()
		sb.append(diffInfo.mdDifferentOrNot())
		sb.append("\n")
		sb.append(diffInfo.mdStats())
		sb.append("\n")
		sb.append(diffInfo.mdDetail())
		return sb.toString()
	}

	public final String compileJsonReport(DiffInfo diffInfo) {
		// generate a diff report in JSON format
		StringBuilder sb = new StringBuilder()
		sb.append(diffInfo.jsReport())
		return JsonOutput.prettyPrint(sb.toString())
	}

	//-------------------------------------------------------------------------

	private final DiffInfo diffByteStreams(InputStream is1, InputStream is2) {
		Objects.requireNonNull(is1)
		Objects.requireNonNull(is2)
		Reader reader1 = new InputStreamReader(is1, StandardCharsets.UTF_8)
		Reader reader2 = new InputStreamReader(is2, StandardCharsets.UTF_8)
		return diffCharacterStreams(reader1, reader2)
	}

	private final DiffInfo diffCharacterStreams(Reader reader1, Reader reader2) {
		List<String> original = readAllLines(reader1)
		List<String> revised = readAllLines(reader2)
		// compute the difference between the two
		return new DiffInfo(original, revised)
	}

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

	/**
	 * 
	 */
	static class DiffInfo {
		private List<DiffRow> rows
		private List<DiffRow> insertedRows
		private List<DiffRow> deletedRows
		private List<DiffRow> changedRows
		private List<DiffRow> equalRows

		private static String TAG_INSERTED_COLOR = "#e6ffec";
		private static String TAG_DELETED_COLOR  = "#ffeef0";
		private static String TAG_CHANGED_COLOR  = "#dbedff";

		DiffInfo(List<String> original, List<String> revised) {
			// compute the difference between the two
			DiffRowGenerator generator =
					DiffRowGenerator.create()
					.showInlineDiffs(true)
					.inlineDiffByWord(true)
					.oldTag({ f -> f ? "<span style=\"color:red; font-weight:bold; background-color:#ffeef0\">" : "</span>" } as Function)
					.newTag({ f -> f ? "<span style=\"color:green; font-weight:bold; background-color:#e6ffec\">" : "</span>" } as Function)
					.build();
			rows         = generator.generateDiffRows(original, revised);
			insertedRows = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.INSERT}).collect(Collectors.toList());
			deletedRows  = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.DELETE}).collect(Collectors.toList());
			changedRows  = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.CHANGE}).collect(Collectors.toList());
			equalRows    = rows.stream().filter({ DiffRow dr -> dr.getTag() == DiffRow.Tag.EQUAL}).collect(Collectors.toList());
		}

		List<DiffRow> getRows() {
			return rows
		}

		List<DiffRow> getInsertedRows() {
			return insertedRows
		}

		List<DiffRow> getDeletedRows() {
			return deletedRows
		}

		List<DiffRow> getChangedRows() {
			return changedRows
		}

		List<DiffRow> getEqualRows() {
			return equalRows
		}

		String mdDifferentOrNot() {
			StringBuilder sb = new StringBuilder()
			sb.append((this.getEqualRows().size() < this.getRows().size()) ?
					'**DIFFERENT**' : '**NO DIFF**')
			sb.append("\n")
			return sb.toString()
		}

		String mdStats() {
			StringBuilder sb = new StringBuilder();
			sb.append("- inserted rows: ${this.getInsertedRows().size()}\n")
			sb.append("- deleted rows : ${this.getDeletedRows().size()}\n")
			sb.append("- changed rows : ${this.getChangedRows().size()}\n")
			sb.append("- equal rows:  : ${this.getEqualRows().size()}\n")
			return sb.toString()
		}

		String mdDetail() {
			StringBuilder sb = new StringBuilder()
			sb.append("|line#|S|original|revised|\n")
			sb.append("|-----|-|--------|-------|\n")
			this.getRows().eachWithIndex { DiffRow row, index ->
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

		String jsReport() {
			StringBuilder sb = new StringBuilder()
			sb.append("{")
			sb.append("\"rows\":${this.getRows().size()},")
			sb.append("\"isDifferent\":${this.getEqualRows().size() < this.getRows().size()},")
			sb.append("\"insertedRows\":${this.getInsertedRows().size()},")
			sb.append("\"deletedRows\":${this.getDeletedRows().size()},")
			sb.append("\"changedRows\":${this.getChangedRows().size()},")
			sb.append("\"equalRows\":${this.getEqualRows().size()}")
			sb.append("}")
			return JsonOutput.prettyPrint(sb.toString())
		}
	}
}
