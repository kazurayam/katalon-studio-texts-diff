import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.kms.katalon.core.configuration.RunConfiguration
import com.kazurayam.ks.TextsDiffer
import groovy.json.JsonOutput

/**
 * ex31 download URL into file, do it twice, the diff the 2 files
 */

def downloadURL(URL url, File output) {
	FileOutputStream fos = new FileOutputStream(output)
	FileChannel fch = fos.getChannel()
	ReadableByteChannel rbch = Channels.newChannel(url.openStream())
	fch.transferFrom(rbch, 0, Long.MAX_VALUE);
}

def prettyPrintJson(Path json) {
	String t = JsonOutput.prettyPrint(json.toFile().text)
	json.toFile().text = t	
}

// Thanks to https://worldtimeapi.org/pages/examples
URL url = new URL("http://worldtimeapi.org/api/ip")

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Files.createDirectories(projectDir.resolve("build/tmp/ex31"))
Path text1 = Paths.get("build/tmp/ex31/text1.json")
Path text2 = Paths.get("build/tmp/ex31/text2.json")

// 1st download the content of the URL 
downloadURL(url, text1.toFile())
prettyPrintJson(text1)

// wait for a few seconds
Thread.sleep(3000)

// 2nd download the same URL
downloadURL(url, text2.toFile())
prettyPrintJson(text2)

// then diff the 2 texts
TextsDiffer differ = new TextsDiffer()
Path out = projectDir.resolve("build/tmp/ex31/diff.md")

differ.diffFiles(text1, text2, out)
