import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/TC2
 */

CustomKeywords.'com.kazurayam.ks.TextsDiffer.processFiles'(
	// you can specify relative paths to the current workind directory
	"src/test/fixtures/doc1.xml",                /* input as original */
	"src/test/fixtures/doc2.xml",                /* input as revised  */
	"build/tmp/TC2-output-A.md"                  /* output */
	)

CustomKeywords.'com.kazurayam.ks.TextsDiffer.processFiles'(
	// you can specify absolute paths as well
	"${System.getProperty('user.home')}/tmp/doc1.xml", /* input as original */
	"${System.getProperty('user.home')}/tmp/doc2.xml", /* input as revised  */
	"${System.getProperty('user.home')}/tmp/TC2-output-B.md"  /* output */
	)
	
CustomKeywords.'com.kazurayam.ks.TextsDiffer.processFiles'(
	// you can explicity specify the base directory to resolve the relative paths
	".",                                         /* base dir          */
	"src/test/fixtures/doc1.xml",                /* input as original */
	"src/test/fixtures/doc2.xml",                /* input as revised  */
	"build/tmp/TC2-output-C.md"                  /* output */
	)
	
CustomKeywords.'com.kazurayam.ks.TextsDiffer.processFiles'(
	// how to get the Katalon project's base directory? --- this is the way
	RunConfiguration.getProjectDir(),            /* base dir          */ 
	"src/test/fixtures/doc1.xml",                /* input as original */ 
	"src/test/fixtures/doc2.xml",                /* input as revised  */ 
	"${System.getProperty('user.home')}/tmp/TC2-output-D.md" /* output */ 
	)
