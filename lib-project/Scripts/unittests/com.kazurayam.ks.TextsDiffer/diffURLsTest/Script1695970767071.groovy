import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.TextsDiffer
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/unittests/com.kazurayam.ks.TextsDiffer/diffURLsTest
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path outputDir = projectDir.resolve("build/tmp/testOutput")
Files.createDirectories(outputDir)

Path doc1 = projectDir.resolve("src/test/fixtures/doc1.xml")
Path doc2 = projectDir.resolve("src/test/fixtures/doc2.xml")
Path out = outputDir.resolve("diffURLsTest-output.md")

TextsDiffer differ = new TextsDiffer()
differ.diffURLs(doc1.toFile().toURI().toURL(), doc2.toFile().toURI().toURL(), out)

assert Files.exists(out)
assert out.toFile().length() > 0

