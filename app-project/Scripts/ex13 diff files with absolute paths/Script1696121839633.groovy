import com.kms.katalon.core.configuration.RunConfiguration

/**
 *  ex13 diff 2 files with absolute paths
 */

CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffFiles'(
	"${System.getProperty('user.home')}/tmp/doc1.xml",
	"${System.getProperty('user.home')}/tmp/doc2.xml",
	"${RunConfiguration.getProjectDir()}/build/tmp/testOutput/ex13-output.md")