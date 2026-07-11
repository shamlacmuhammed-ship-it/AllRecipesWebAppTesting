package utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    /**
     * Captures a screenshot of the active browser viewport and saves it to the target directory.
     * 
     * @param driver     The thread-isolated WebDriver instance.
     * @param testName   The name of the currently executing test method.
     * @return String    The absolute file path of the saved screenshot, or null if an error occurs.
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        // 1. Generates a precise timestamp including milliseconds to secure parallel file naming uniqueness
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        
        // 2. Defines the absolute storage location path for the output PNG capture payload
        String screenshotPath = System.getProperty("user.dir") + "/target/screenshots/" + testName + "_" + timeStamp + ".png";
        
        try {
            // 3. Casts the active driver session to capture visual page pixel paint arrays
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            
            // 4. Commits and streams the captured image artifact file straight to storage blocks
            FileUtils.copyFile(source, destination);
            System.out.println("📸 Failure snapshot saved successfully at: " + screenshotPath);
            return screenshotPath;
            
        } catch (IOException e) {
            System.err.println("❌ Critical Failure: Framework crashed attempting to write screenshot payload to storage: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error tracing active browser interface view canvas: " + e.getMessage());
            return null;
        }
    }
}
