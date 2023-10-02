# Examples

-   date October, 2023

-   author kazurayam

This page shows examples of using com.kazurayam.ks.TextsDiffer class in Katalon Studio.

## ex01 diff 2 strings and write the report into file

The following script does

1.  The following script calls `TextsDiffer.diffString(String,String,String)`.

2.  The 1st and 2nd arguments are regarded as input text. The inputs contain XML texts which are similar but different in detail.

3.  The 3rd argument is regarded as a path of output file.

4.  The output will be in Markdown text format.

<!-- -->

    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.Paths

    /**
     * ex01 diff 2 strings and write the report into file 
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

    String outpath = "build/tmp/testoutput/ex01-output.md"

    // take diff of 2 Strings, write the diff report into a file
    CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2, outpath)

    Path out = Paths.get(outpath)
    assert Files.exists(out)
    assert out.toFile().length() > 0

The diff report generated looks like this:

## ex02 diff 2 strings and print the stats to console

The following script does

1.  The following script calls `TextsDiffer.diffString(String,String)`.

2.  The 1st and 2nd arguments are regarded as input text. The inputs contain XML texts which are similar but different in detail.

3.  `TextsDiffer.diffString(String,String)` with 2 arguments will return a String value.

4.  The returned String will be a short JSON text, which contains the statistical information of the diff report.

<!-- -->

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

When I run it, this script emits the following JSON in the console. You would need no explanation about this.

    {
        "rows": 9,
        "isDifferent": true,
        "insertedRows": 1,
        "deletedRows": 1,
        "changedRows": 2,
        "equalRows": 5
    }

You can quickly parse the returned JSON string into an instance of `java.util.Map` using `groovy.json.JsonSlurper` and get access to the content. The `ex02` contains sample script how to do it.
== ex03 diff 2 strings and print the stats to console - as Plain Old Class
== ex11 diff 2 files with relative paths to the current working directory
== ex12 diff 2 files with relative paths to the specified base directory
== ex13 diff files with absolute paths
== ex21 diff 2 URLs
== ex31 chronos diff
== ex32 twins diff
