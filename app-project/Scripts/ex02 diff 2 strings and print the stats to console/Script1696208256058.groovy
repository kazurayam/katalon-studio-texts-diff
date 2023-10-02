import groovy.json.JsonSlurper

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

// pass 2 arguments of String to receive a String as report
String json = CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2)
println json

JsonSlurper slurper = new JsonSlurper()
def object = slurper.parseText(json)

assert object instanceof Map
assert object.isDifferent == true
assert object.changedRows == 2
