Utilizing java-diff-utils in Katalon Studio
===========================================

1st release date: April, 2021
2nd revision: Sept, 2023

author kazurayam

This is a small [Katalon Studio](https://www.katalon.com/) project for demonstration purpose.

This project was originally developed with Katalon Studio v7.9.1 but is not KS-version-dependent at all.

This project was created in order to propose a solution to the following post 
in the Katalon Studio User Forum:

- [How can I compare 2 XML files](https://forum.katalon.com/t/how-can-i-compare-2-xml-files/44854)

# Problem to solve

Web testers often want to compare 2 text files. The files could be in various format: CSV, JSON, XML, etc.

As minimum requirement, they want to know if 2 texts are completely identical or not.

Additionally they want to see the differences, if any, in human-readable format, such as 
[Markdown](https://guides.github.com/features/mastering-markdown/).

Testers usually do not require Patching features which many "diff utilities" provide.

I want to perform texts-diff and reporting in Katalon Studio.

# Solution

Utilize [java-diff-utils](https://github.com/java-diff-utils/java-diff-utils/wiki) in Katalon Studio.

# How to run the demo

- Download the zip of this project from the [Releases](https://github.com/kazurayam/katalon-studio-texts-diff/releases) page, 
unzip it, open it with your Katalon Studio.
- In the `<projectDir>/Drivers` directory, you will find `java-diff-utils-4.9.jar` installed.
- open [`Test Cases/TC1`](./Scripts/TC1/Script1619137698459.groovy) and run it.
- the script will compare 2 xml files
  - [`doc1.xml`](./Include/fixtures/doc1.xml)
  - [`doc2.xml`](./Include/fixtures/doc2.xml)
- it will create a diff report in Markdown format, save it into a file `<projectDir>/out.md`

# How the report looks like

The `<projectDir>/out.md` will be formated in Markdown like this:

```
- original: /Users/username/projectdir/Include/fixtures/doc1.xml
- revised : /Users/username/projectdir//Include/fixtures/doc2.xml

|line#|original|revised|
|-----|--------|-------|
|1|&lt;doc&gt;|&lt;doc&gt;|
|2|&lt;body&gt;|&lt;body&gt;|
|3|*&lt;section&gt;*|**&lt;section id="main"&gt;**|
|4|&lt;p&gt;Hello, *John!&lt;*/p&gt;|&lt;p&gt;Hello, **Paul!&lt;**/p&gt;|
|5||**&lt;p&gt;Have a break!&lt;/p&gt;**|
|6|&lt;/section&gt;|&lt;/section&gt;|
|7|&lt;/body&gt;|&lt;/body&gt;|
|8|&lt;/doc&gt;|&lt;/doc&gt;|
```

I copy & past it here:

----
- original: `/Users/username/projectdir/Include/fixtures/doc1.xml`
- revised : `/Users/username/projectdir//Include/fixtures/doc2.xml`

|line#|original|revised|
|-----|--------|-------|
|1|&lt;doc&gt;|&lt;doc&gt;|
|2|&lt;body&gt;|&lt;body&gt;|
|3|*&lt;section&gt;*|**&lt;section id="main"&gt;**|
|4|&lt;p&gt;Hello, *John!&lt;*/p&gt;|&lt;p&gt;Hello, **Paul!&lt;**/p&gt;|
|5||**&lt;p&gt;Have a break!&lt;/p&gt;**|
|6|&lt;/section&gt;|&lt;/section&gt;|
|7|&lt;/body&gt;|&lt;/body&gt;|
|8|&lt;/doc&gt;|&lt;/doc&gt;|
----

I think it is readable enough. Of course you can change the Test Case code so that it outputs in any format you like.



## Where to get external dependencies

- https://mvnrepository.com/artifact/io.github.java-diff-utils/java-diff-utils/4.9
