import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/TC2
 */

// you can specify relative paths to the current workind directory
CustomKeywords.'com.kazurayam.ks.TextsDiffer.processFiles'(
	"src/test/fixtures/doc1.xml",                /* input as original */
	"src/test/fixtures/doc2.xml",                /* input as revised  */
	"build/tmp/TC2-output-A.md"                  /* output */
	)

// you can specify absolute paths as well	
CustomKeywords.'com.kazurayam.ks.TextsDiffer.processFiles'(
	"${System.getProperty('user.home')}/tmp/doc1.xml", /* input as original */
	"${System.getProperty('user.home')}/tmp/doc2.xml", /* input as revised  */
	"${System.getProperty('user.home')}/tmp/TC2-output-B.md"  /* output */
	)

// you can explicity specify the base directory to resolve the relative paths	
CustomKeywords.'com.kazurayam.ks.TextsDiffer.processFiles'(
	".",                                         /* base dir          */
	"src/test/fixtures/doc1.xml",                /* input as original */
	"src/test/fixtures/doc2.xml",                /* input as revised  */
	"build/tmp/TC2-output-C.md"                  /* output */
	)

// how to get the Katalon project's base directory? --- this is the way	
CustomKeywords.'com.kazurayam.ks.TextsDiffer.processFiles'(
	RunConfiguration.getProjectDir(),            /* base dir          */ 
	"src/test/fixtures/doc1.xml",                /* input as original */ 
	"src/test/fixtures/doc2.xml",                /* input as revised  */ 
	"${System.getProperty('user.home')}/tmp/TC2-output-D.md" /* output */ 
	)
