package com.kazurayam.ks

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// https://github.com/kazurayam/java-diff-report
import com.kazurayam.difflib.text.DiffInfo
import com.kazurayam.difflib.text.MarkdownReporter
import com.kazurayam.difflib.text.Differ

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration

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
 * @date Feb, 2024
 */
public final class TextsDiffer {

	public TextsDiffer() {}

	@Keyword
	public final String diffFiles(String text1, String text2, String output) {
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		return this.diffFiles(Paths.get(text1), Paths.get(text2), Paths.get(output));
	}

	@Keyword
	public final String diffFiles(String baseDir, String relativePath1, String relativePath2, String output) {
		Objects.requireNonNull(baseDir)
		Objects.requireNonNull(relativePath1)
		Objects.requireNonNull(relativePath2)
		Objects.requireNonNull(output)
		Path dir = Paths.get(RunConfiguration.getProjectDir()).resolve(baseDir)  // relative to the project's base directory
		return this.diffFiles(dir.resolve(relativePath1), dir.resolve(relativePath2), dir.resolve(output))
	}

	public final String diffFiles(File file1, File file2, File output) {
		return diffFiles(file1.toPath(), file2.toPath(), output.toPath())
	}

	public final String diffFiles(Path file1, Path file2, Path output) {
		Files.createDirectories(output.getParent())
		DiffInfo diffInfo = Differ.diffFiles(file1, file2)
		MarkdownReporter reporter = new MarkdownReporter.Builder(diffInfo).build();
		output.toFile().text = reporter.compileMarkdownReport()
		return reporter.compileStats()
	}

	//-------------------------------------------------------------------------

	@Keyword
	public final String diffStrings(String text1, String text2) {
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		DiffInfo diffInfo = Differ.diffStrings(text1, text2)
		MarkdownReporter reporter = new MarkdownReporter.Builder(diffInfo).build();
		return reporter.compileMarkdownReport()
	}

	@Keyword
	public final String diffStrings(String text1, String text2, String output) {
		return diffStrings(text1, text2, output, null, null)
	}

	@Keyword
	public final String diffStrings(String text1, String text2, String output, String description1, String description2) {
		Objects.requireNonNull(text1)
		Objects.requireNonNull(text2)
		Objects.requireNonNull(output)
		Path out = Paths.get(output)
		Files.createDirectories(out.getParent())
		DiffInfo diffInfo = Differ.diffStrings(text1, text2)
		if (description1 != null) {
			diffInfo.setPathOriginal(description1)
		}
		if (description2 != null) {
			diffInfo.setPathRevised(description2)
		}
		MarkdownReporter reporter = new MarkdownReporter.Builder(diffInfo).build();
		out.toFile().text = reporter.compileMarkdownReport()
		return reporter.compileStats()
	}

	//-------------------------------------------------------------------------

	@Keyword
	public final String diffURLs(String url1, String url2, String output) {
		Objects.requireNonNull(url1)
		Objects.requireNonNull(url2)
		Objects.requireNonNull(output)
		return this.diffURLs(new URL(url1), new URL(url2), Paths.get(output))
	}

	public final String diffURLs(URL url1, URL url2, Path output) {
		Files.createDirectories(output.getParent())
		DiffInfo diffInfo = Differ.diffURLs(url1, url2);
		MarkdownReporter reporter = new MarkdownReporter.Builder(diffInfo).build();
		output.toFile().text = reporter.compileMarkdownReport();
		return reporter.compileStats()
	}
}
