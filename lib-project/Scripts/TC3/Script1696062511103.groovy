import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/TC3
 */

// you can specify relative paths to the current workind directory
CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffFiles'(
	"src/test/fixtures/doc1.xml",                /* input as original */
	"src/test/fixtures/doc2.xml",                /* input as revised  */
	"build/tmp/testOutput/TC3-output-A.md"       /* output */
	)

// you can specify absolute paths as well	
CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffFiles'(
	"${System.getProperty('user.home')}/tmp/doc1.xml", /* input as original */
	"${System.getProperty('user.home')}/tmp/doc2.xml", /* input as revised  */
	"${System.getProperty('user.home')}/tmp/testOutput/TC3-output-B.md"  /* output */
	)

// you can explicity specify the base directory to resolve the relative paths	
CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffFiles'(
	".",                                         /* base dir          */
	"src/test/fixtures/doc1.xml",                /* input as original */
	"src/test/fixtures/doc2.xml",                /* input as revised  */
	"build/tmp/testOutput/TC3-output-C.md"                  /* output */
	)

// how to get the Katalon project's base directory? --- this is the way	
CustomKeywords.'com.kazurayam.ks.TextsDiffer.diffFiles'(
	RunConfiguration.getProjectDir(),            /* base dir          */ 
	"src/test/fixtures/doc1.xml",                /* input as original */ 
	"src/test/fixtures/doc2.xml",                /* input as revised  */ 
	"${System.getProperty('user.home')}/tmp/testOutput/TC3-output-D.md" /* output */ 
	)
