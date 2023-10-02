import com.kms.katalon.core.configuration.RunConfiguration

/**
 *  ex12 diff 2 files with relative paths to the specified base directory
 */

CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffFiles'(
	RunConfiguration.getProjectDir(),     /* base directory */
	"src/test/fixtures/doc1.xml",
	"src/test/fixtures/doc2.xml",
	"build/tmp/testOutput/ex12-output.md")