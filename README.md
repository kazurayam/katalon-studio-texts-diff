# Utilizing java-diff-utils in Katalon Studio - Reborn

- 1st release date: April, 2021
- 2nd revision: Oct, 2023
- author kazurayam

This project presents a custom Keyword (a helper class in Groovy language) named `com.kazurayam.ks.TextsDiffer` to be used in [Katalon Studio](https://www.katalon.com/) projects.

The class is developed using Katalon Studio v8.6.6 but is not dependent on the Katalon Studio's version. It should work on KS v7 as well.

## How is this Git repository structured

This project reqpository 'katalon-studio-texts-diff' contains 3 sub directories: `app-project`, `docs` and `lib-project`. The `lib-project` contains a Katalon Studio project where I developed the source code of the custom keyword. The `app-project` contains a Katalon Studio project where I applied the custom Keyword to demonstrate how to use it.

If you are going to use the custom Keyword in your own Katalon project, you want to look at the `app-project`.

## Problem to solve

Web testers often want to compare 2 text files. The files could be in various format: CSV, JSON, XML, etc. They sometimes just want to check if 2 texts are identical or not. And they may want to see the detail differences. Katalon Studio does not offer any texts-diff feature; I want it.

## Solution

Utilize [java-diff-utils](https://github.com/java-diff-utils/java-diff-utils/wiki) in Katalon Studio.


## How to apply this to your own Katalon project

In the `Drivers` directory of your Katalon project, you NEED to install 2 external jar files.

![Library Management](https://kazurayam.github.io/katalon-studio-texts-diff/images/LibraryManagement.png)

1. [`java-diff-utils-4.12.jar`](https://mvnrepository.com/artifact/io.github.java-diff-utils/java-diff-utils/4.12)
2. [`katalon-studio-texts-diff-x.x.x.jar`](https://github.com/kazurayam/katalon-studio-texts-diff/releases)

The [`java-diff-utils-x.x.x`](https://github.com/java-diff-utils/java-diff-utils) is an OpenSource library in Java for performing the comparison operations between texts. This enables us to generate diff information.

I developed the `katalon-studio-texts-diff-x.x.x` library which wraps the `java-diff-utils-x.x.x` to utilize it in your Katalon Studio project. With it, you get a diff report in Markdown format with side-by-side view.

### Creating and running your first test

You want to start Katalon Studio GUI and open your project.

You want to create a new Test Case `ex01` (... any name you can choose in fact), of which code looks as follows. You can just copy and paste this:

```
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

// take diff of 2 Strings, write the diff
CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffStrings'(text1, text2, outpath)

Path out = Paths.get(outpath)
assert Files.exists(out)
assert out.toFile().length() > 0
```

Now you can run it as a usual Katalon Test Case by clicking the green button ![run](./docs/images/run_katalon_test.png).

Once done, the script will create a diff report and save it into a file `build/tmp/testOutput/ex01-output.md`. The output will be formatted in Markdown. The file will like this:

```
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
```

The raw Markdown text is hard to read in a plain text editor. So you want to view it using some Markdown viewer. For example, I personally use [Visual Studio Code, Markdown preview](https://code.visualstudio.com/Docs/languages/markdown#_markdown-preview). You can preview the report in VSCode as follows.

![ex01](https://kazurayam.github.io/katalon-studio-texts-diff/images/ex01.png)

This looks nice, doesn't it?

## More types of input text

The sample Test Case `ex01` uses `com.kazurayam.ks.TextsDiffer.diffStrings()` method that takes 2 Strings as input. The `TextsDiffer` class implements more methods that can take various types: Files, URLs, etc.

See the [`Examples`](https://kazurayam.github.io/katalon-studio-texts-diff/) document for more use cases.

## API

You can have a look at the API documentation of `TextsDiffer` class at

- [API doc](https://kazurayam.github.io/katalon-studio-texts-diff/api/index.html)
