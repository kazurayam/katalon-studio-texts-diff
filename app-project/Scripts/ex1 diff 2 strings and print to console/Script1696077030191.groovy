import com.kazurayam.ks.TextsDiffer

/**
 * ex1 diff 2 strings and print to console
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
String md = CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2)
println md

