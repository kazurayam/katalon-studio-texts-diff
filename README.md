# Utilizing java-diff-utils in Katalon Studio - Reborn

- 1st release date: April, 2021
- 2nd revision: Oct, 2023
- 3rd revision: Feb, 2024
- author kazurayam

This project presents a custom Keyword (a helper class in Groovy language) named `com.kazurayam.ks.TextsDiffer` to be used in [Katalon Studio](https://www.katalon.com/) projects.

The class was orignially developed using Katalon Studio v8.6.6 but is not dependent on the Katalon Studio's version. It should work on any newer versions of Katalon Studio as well.

## How is this Git repository structured

This project reqpository 'katalon-studio-texts-diff' contains 3 sub directories: `lib-project`, `lib-project` and `docs`.

The `lib-project` contains a Katalon Studio project where I developed the source code of the Custom keyword for Katalon Studio. The `lib-project` has a `build.gradle` which build a jar file `katalon-studio-texts-diff-xxxxx.jar` that contains the custom keyword's binary. The jar file can be exported into any other Katalon Studio projects.

The `app-project` contains a Katalon Studio project where I imported the jar built in the `lib-project`. The Test Cases in the `app-project` demonstrates how to use the `katalon-studio-texts-diff` feature. To get started with the `katalon-studio-texts-diff`, you should look at the following code:

- [app-project/Test Cases/ex01 diff 2 strings and write the report into file](https://github.com/kazurayam/katalon-studio-texts-diff/blob/develop/app-project/Scripts/ex01%20diff%202%20strings%20and%20write%20the%20report%20into%20file/Script1696166007171.groovy)

## Problem to solve

Web testers often want to compare 2 text files. The files could be in various format: CSV, JSON, XML, etc. They sometimes just want to check if 2 texts are identical or not, and if not, they would want to see the differences. Katalon Studio does not offer any such feature. So I have created it.

## Solution

Utilize the [java-diff-utils](https://github.com/java-diff-utils/java-diff-utils/wiki) in Katalon Studio.


## How to apply this to your own Katalon project

In the `Drivers` directory of your Katalon project, you NEED to install 3 external jar files.

![Library Management](https://kazurayam.github.io/katalon-studio-texts-diff/images/LibraryManagement.png)

1. [`java-diff-utils-4.12.jar`](https://mvnrepository.com/artifact/io.github.java-diff-utils/java-diff-utils/4.12)
2. [`katalon-studio-texts-diff-x.x.x.jar`](https://github.com/kazurayam/katalon-studio-texts-diff/releases), the latest version
3. [`java-diff-report-x.x.x.jar`](https://github.com/kazurayam/java-diff-report/releases), the latest version

The [`java-diff-utils-x.x.x`](https://github.com/java-diff-utils/java-diff-utils) is an open source library in Java for performing the comparison operations between texts. This enables us to generate diff information.

Unfortunately the `java-diff-utils` library does not provide a code that generates human-readable reports out of box. To supplement this, kazurayam has developed a java library `java-diff-report`. This library provides a set of utility classes that generates human-readable reports easily.

I developed the `katalon-studio-texts-diff-x.x.x` library which wraps the `java-diff-report` and the `java-diff-utils` library to utilize the texts diff feature in arbitrary Katalon Studio projects. With it, you will get a diff report in Markdown format with side-by-side view by just calling a custom keyword.

## Creating and running your first test

You want to start Katalon Studio GUI and open your project.

You want to create a new Test Case `ex02` (... any name you can choose in fact), of which code looks as follows. You can just copy and paste this:

```
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

```

Now you can run this Test Case as usual in Katalon Studio by clicking the green button ![run](./docs/images/run_katalon_test.png).

Once done, the script will write a file `build/tmp/testOutput/ex02-output.md`. The text will be formatted in Markdown. The file will like this:

```
### Stats

**DIFFERENT** at 2024-02-07 09:01:07

- inserted rows : 1
- deleted rows  : 1
- changed rows  : 2
- equal rows:   : 5

### Detail

|row#|S|original|revised|
|----|-|--------|-------|
| 1 |   | &lt;doc&gt; | &lt;doc&gt; |
| 2 |   | &lt;body&gt; | &lt;body&gt; |
| 3 | C | <span style="color:red; font-weight:bold; background-color:#ffeef0">&lt;section&gt;</span> | <span style="color:green; font-weight:bold; background-color:#e6ffec">&lt;section id="main"&gt;</span> |
| 4 | C | &lt;p&gt;Hello, <span style="color:red; font-weight:bold; background-color:#ffeef0">John!&lt;</span>/p&gt; | &lt;p&gt;Hello, <span style="color:green; font-weight:bold; background-color:#e6ffec">Paul!&lt;</span>/p&gt; |
| 5 | I |  | <span style="color:green; font-weight:bold; background-color:#e6ffec">&lt;p&gt;Have a break!&lt;/p&gt;</span> |
| 6 |   | &lt;/section&gt; | &lt;/section&gt; |
| 7 | D | <span style="color:red; font-weight:bold; background-color:#ffeef0">&lt;p&gt;&lt;/p&gt;</span> |  |
| 8 |   | &lt;/body&gt; | &lt;/body&gt; |
| 9 |   | &lt;/doc&gt; | &lt;/doc&gt; |

```

A Markdown text in raw format is hard to read in a plain text editor. You would definitely require some viewer application tailored for Markdown. For example, I personally use [Visual Studio Code, Markdown preview](https://code.visualstudio.com/Docs/languages/markdown#_markdown-preview). You can preview the report in VSCode as follows.

![VSCode with Markdown Viewer](https://kazurayam.github.io/katalon-studio-texts-diff/images/ex02.png)

This looks nice, doesn't it?

## Compact report

Your input texts sometimes could be very long. Let me assume, for example, that I want to compare the following 2 texts.

1. [bootstrap-icons.css 1.5.0](https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css)
2. [bootstrap-icons.css 1.7.2](https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css)

Both of them are very long: over 1500 lines. And these texts are slightly different, as the following stats shows:

- inserted rows : 166
- deleted rows  : 0
- changed rows  : 2
- equal rows:   : 1388

Obviously I am only interested in the difference (inserted, deleted, changed); I am not interested in the equal parts. In this case, approximately 90% of lines are not interesting for me. I do not like the diff report to contain the boring equal lines.

So, the `java-diff-report` library supports 2 format of diff report: "compact" and "full". You can chose either by specifying a parameter. "compact" is the default. The compact report will trim most of the equal lines. The diff report of the above inputs will contain have only 200 lines, not 1500 lines.The report will look something like this:

![large inputs](https://kazurayam.github.io/katalon-studio-texts-diff/images/diff_large_inputs.png)

Please note that there is a gap between the line#24 and line#1391 where hundreds of "equal" lines are trimmed off. Therefore the report file is small and easy to look through quickly.


## More types of input text

The sample Test Case `ex02` uses `com.kazurayam.ks.TextsDiffer.diffStrings()` method that takes 2 Strings as input. The `com.kazurayam.ks.TextsDiffer` class implements more variations of method signature that can take other types: `java.io.File`, `java.nio.Path` and `java.net.URL`.

See the [`Examples`](https://kazurayam.github.io/katalon-studio-texts-diff/) document for more use cases.

## API

You can have a look at the API documentation of `TextsDiffer` class at

- [API doc](https://kazurayam.github.io/katalon-studio-texts-diff/api/index.html)
