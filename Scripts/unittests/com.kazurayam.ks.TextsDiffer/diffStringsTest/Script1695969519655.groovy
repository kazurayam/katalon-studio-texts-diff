import com.kazurayam.ks.TextsDiffer

/**
 * Test Cases/unittests/com.kazurayam.ks.TextsDiffer/processStringTest
 */
String text1 = """<doc>
<body>
<section>
<p>Hello, John!</p>
</section>
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

TextsDiffer differ = new TextsDiffer()
String md = differ.diffStrings(text1, text2)

assert md != null
println md