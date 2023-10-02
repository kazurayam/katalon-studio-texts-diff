-   [Examples](#examples)
    -   [ex01 diff 2 strings and write the report into file](#ex01-diff-2-strings-and-write-the-report-into-file)
    -   [ex02 diff 2 strings and print the stats to console](#ex02-diff-2-strings-and-print-the-stats-to-console)
    -   [ex03 do the same as ex02 by calling TextsDiff as Plain Old class (not as Keyword)](#ex03-do-the-same-as-ex02-by-calling-textsdiff-as-plain-old-class-not-as-keyword)
    -   [ex11 diff 2 files with relative paths to the current working directory](#ex11-diff-2-files-with-relative-paths-to-the-current-working-directory)
    -   [ex12 diff 2 files with relative paths to the specified base directory](#ex12-diff-2-files-with-relative-paths-to-the-specified-base-directory)
    -   [ex13 diff files with absolute paths](#ex13-diff-files-with-absolute-paths)
    -   [ex21 diff 2 URLs](#ex21-diff-2-urls)
    -   [ex31 chronos diff](#ex31-chronos-diff)
    -   [ex32 twins diff](#ex32-twins-diff)

# Examples

-   Date: October, 2023

-   Author: kazurayam

This page shows the examples how to use `com.kazurayam.ks.TextsDiffer` class in Katalon Studio. You can download the jar at the \[`Releases`\](<https://github.com/kazurayam/katalon-studio-texts-diff/releases>) page.

## ex01 diff 2 strings and write the report into file

1.  The following script calls the `diffString(String,String,String)` method of the `com.kazurayam.ks.TestsDiffer` class.

2.  If you would like, you can use it as an **custom Keyword** in the Manual mode of the Test Case editor.

3.  The 1st and 2nd arguments are regarded as input text. The inputs in the sample contain XML texts which are similar but different in detail.

4.  The 3rd argument is regarded as a path of file. The `TextDiffer` writes a diff report into the file.

5.  The output will be in Markdown text format.

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

    **DIFFERENT**

    - inserted rows: 1
    - deleted rows : 1
    - changed rows : 2
    - equal rows:  : 5

    |line#|S|original|revised|
    |-----|-|--------|-------|
    |1| |&lt;doc&gt;|&lt;doc&gt;|
    |2| |&lt;body&gt;|&lt;body&gt;|
    |3|C|<span style="color:red; font-weight:bold; background-color:#ffeef0">&lt;section&gt;</span>|<span style="color:green; font-weight:bold; background-color:#e6ffec">&lt;section id="main"&gt;</span>|
    |4|C|&lt;p&gt;Hello, <span style="color:red; font-weight:bold; background-color:#ffeef0">John!&lt;</span>/p&gt;|&lt;p&gt;Hello, <span style="color:green; font-weight:bold; background-color:#e6ffec">Paul!&lt;</span>/p&gt;|
    |5|I||<span style="color:green; font-weight:bold; background-color:#e6ffec">&lt;p&gt;Have a break!&lt;/p&gt;</span>|
    |6| |&lt;/section&gt;|&lt;/section&gt;|
    |7|D|<span style="color:red; font-weight:bold; background-color:#ffeef0">&lt;p&gt;&lt;/p&gt;</span>||
    |8| |&lt;/body&gt;|&lt;/body&gt;|
    |9| |&lt;/doc&gt;|&lt;/doc&gt;|

It is difficult to read in a plain text editor. You need a tool to view this nicely. If you use [Visual Studio Code, Markdown Preview](https://code.visualstudio.com/Docs/languages/markdown#_markdown-preview), you can see a nice preview, like this:

<figure>
<img src="https://kazurayam.github.io/katalon-studio-texts-diff/images/ex01.png" alt="ex01" />
</figure>

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

    // parse the returned JSON, read the content to make assertions
    JsonSlurper slurper = new JsonSlurper()
    def object = slurper.parseText(json)
    assert object instanceof Map
    assert object.isDifferent == true
    assert object.changedRows == 2

When I run it, this script emits the following JSON in the console.

    {
        "rows": 9,
        "isDifferent": true,
        "insertedRows": 1,
        "deletedRows": 1,
        "changedRows": 2,
        "equalRows": 5
    }

With this JSON, you can quickly see if the 2 input texts are different or not. You can parse the returned JSON string into an instance of `java.util.Map` using `groovy.json.JsonSlurper` and get access to the content. The `ex02` contains sample script how to do it.

## ex03 do the same as ex02 by calling TextsDiff as Plain Old class (not as Keyword)

The `com.kazurayam.ks.TextsDiff` is a plain old Groovy class. You can call it directly.

    import com.kazurayam.ks.TextsDiffer

    /**
     * ex03 call the TextsDiffer class directly, not as a custom Keyword
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

This script does not use `CustomKeywords.'fully qualified class name.methodName'(args)` syntax. This script does the same processing as the `ex02`

## ex11 diff 2 files with relative paths to the current working directory

## ex12 diff 2 files with relative paths to the specified base directory

## ex13 diff files with absolute paths

## ex21 diff 2 URLs

## ex31 chronos diff

## ex32 twins diff
