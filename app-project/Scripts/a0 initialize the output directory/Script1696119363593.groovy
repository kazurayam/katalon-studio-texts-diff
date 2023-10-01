import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.kms.katalon.core.configuration.RunConfiguration
import org.apache.commons.io.FileUtils

/**
 * a0 initialize the output directory
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path outputDir = projectDir.resolve("build/tmp/testOutput")

if (Files.exists(outputDir)) {
	FileUtils.deleteDirectory(outputDir.toFile())	
}
Files.createDirectories(outputDir)