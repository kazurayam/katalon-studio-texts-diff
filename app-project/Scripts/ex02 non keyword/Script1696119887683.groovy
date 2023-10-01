import com.kazurayam.ks.TextsDiffer

/**
 * ex02 call the TextsDiffer class directly, not as a custom Keyword
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
TextsDiffer differ = new TextsDiffer()
String md = differ.diffStrings(text1, text2)
println md

