import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration

/**
 * ex02 diff 2 strings and print the stats to console
 */

String text1 = """<doc>
<body>
<section>
<p>Hello, John!</p>
</section>
<p></p>
</body>
</doc>
"""

String text2 = """<doc>
<body>
<section id="main">
<p>Hello, Paul!</p>
<p>Have a break!</p>
</section>
</body>
</doc>
"""

// output
Path outDir = Paths.get(RunConfiguration.getProjectDir()).resolve("build/tmp/testOutput")
Files.createDirectories(outDir)
Path reportFile = outDir.resolve("ex02-output.md")

// pass 2 arguments of String to receive a report in Markdown format
String stats = CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2, reportFile.toString())
println stats

String report = reportFile.text;
assert report.contains("**DIFFERENT**")
assert report.contains("inserted rows")
assert report.contains("deleted rows")
assert report.contains("changed rows")
assert report.contains("equal rows")

assert report.contains("| 1 |")
assert report.contains("&lt;doc&gt;")
