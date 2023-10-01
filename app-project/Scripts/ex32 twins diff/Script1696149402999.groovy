import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.TextsDiffer
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * ex32 twins diff 
 * 
 * download HTML from a pair of URLs of similar looking, then diff
 */

def downloadURL(URL url, File output) {
	FileOutputStream fos = new FileOutputStream(output)
	FileChannel fch = fos.getChannel()
	ReadableByteChannel rbch = Channels.newChannel(url.openStream())
	fch.transferFrom(rbch, 0, Long.MAX_VALUE);
}

URL url1 = new URL("http://myadmin.kazurayam.com/")
URL url2 = new URL("http://devadmin.kazurayam.com/")


Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Files.createDirectories(projectDir.resolve("build/tmp/testOutput/ex32"))
Path text1 = Paths.get("build/tmp/testOutput/ex32/text1.html")
Path text2 = Paths.get("build/tmp/testOutput/ex32/text2.html")

// 1st download a JSON from the URL
downloadURL(url1, text1.toFile())

// 2nd download a JSON from the same URL
downloadURL(url2, text2.toFile())

// then diff the 2 texts
TextsDiffer differ = new TextsDiffer()
Path out = projectDir.resolve("build/tmp/testOutput/ex32/diff.md")

differ.diffFiles(text1, text2, out)

