import com.kazurayam.ks.TextDiffer
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/TC2
 */

CustomKeywords.'com.kazurayam.ks.TextDiffer.execute'(
	"src/test/fixtures/doc1.xml",                /* input as original */
	"src/test/fixtures/doc2.xml",                /* input as revised  */
	"build/tmp/TC2-output-A.md"                  /* output */
	// relative paths to the current workind directory
	)

CustomKeywords.'com.kazurayam.ks.TextDiffer.execute'(
	"${System.getProperty('user.home')}/tmp/doc1.xml",        /* input as original */
	"${System.getProperty('user.home')}/tmp/doc2.xml",        /* input as revised  */
	"${System.getProperty('user.home')}/tmp/TC2-output-B.md"  /* output */
	// you can specify absolute paths as well
	)
	
CustomKeywords.'com.kazurayam.ks.TextDiffer.execute'(
	// you can explicity specify the base directory to resolve the relative paths
	".",                                         /* base dir          */
	"src/test/fixtures/doc1.xml",                /* input as original */
	"src/test/fixtures/doc2.xml",                /* input as revised  */
	"build/tmp/TC2-output-C.md"                  /* output */
	)
	
CustomKeywords.'com.kazurayam.ks.TextDiffer.execute'(
	// how to get the project directory? --- this is the way
	RunConfiguration.getProjectDir(),            /* base dir          */ 
	"src/test/fixtures/doc1.xml",                /* input as original */ 
	"src/test/fixtures/doc2.xml",                /* input as revised  */ 
	"${System.getProperty('user.home')}/tmp/TC2-output-D.md" /* output */ 
	)
