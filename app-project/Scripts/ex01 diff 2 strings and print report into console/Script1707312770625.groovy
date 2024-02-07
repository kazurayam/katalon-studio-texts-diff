import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * ex01 diff 2 strings and print report into console 
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

// take diff of 2 Strings, write the diff report into a file
String report = CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2)
println report

