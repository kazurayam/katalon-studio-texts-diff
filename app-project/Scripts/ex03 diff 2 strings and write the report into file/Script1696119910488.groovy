import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * ex03 diff 2 strings and write the report into file 
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

// take diff of 2 String, write the report into file
String outpath = "build/tmp/testoutput/ex03-output.md"
CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2, outpath)
Path out = Paths.get(outpath)
assert Files.exists(out)

