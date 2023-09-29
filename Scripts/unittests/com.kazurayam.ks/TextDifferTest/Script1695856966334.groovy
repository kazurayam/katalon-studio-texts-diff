import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.TextDiffer
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/unittests/com.kazurayam.ks/TextDifferTest
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path outputDir = projectDir.resolve("build/tmp")
Files.createDirectories(outputDir)

Path doc1 = projectDir.resolve("src/test/fixtures/doc1.xml")
Path doc2 = projectDir.resolve("src/test/fixtures/doc2.xml")
Path out = outputDir.resolve("test-output.md")

TextDiffer differ = new TextDiffer()
differ.processFiles(projectDir, doc1, doc2, out)

assert Files.exists(out)
assert out.toFile().length() > 0
