import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Test Cases/TC2
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

// pass 2 arguments of String to receive a String as report
String report = CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2)
assert report != null
println report

// pass 3 arguments of String type: 2 String as text to compare, and the output file path
String outpath = "build/tmp/testoutput/TC2-output.md"
CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2, outpath)
Path out = Paths.get(outpath)
assert Files.exists(out)

// pass 5 arguments of String type: 2 String as text to compare, and the output file path, 2 string that describes the inputs
String outpath2 = "build/tmp/testoutput/TC2-output_with_description.md"
CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2, outpath2, "John comes in", "They are hungry")
Path out2 = Paths.get(outpath2)
assert Files.exists(out2)
