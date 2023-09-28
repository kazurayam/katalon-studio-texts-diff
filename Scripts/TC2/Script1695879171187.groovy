import com.kazurayam.ks.TextDiffer
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/TC2
 */

CustomKeywords.'com.kazurayam.ks.TextDiffer.execute'(
	/* base dir */ RunConfiguration.getProjectDir(),  
	/* original */ "Include/fixtures/doc1.xml",
	/* revised  */ "Include/fixtures/doc2.xml", 
	/* output   */ "build/tmp/TC2-output.md")
