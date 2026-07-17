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
     * Captures a screenshot of the current browser window.
     *
     * @param driver    WebDriver instance
     * @param testName Name of the test case
     * @return Absolute path of the saved screenshot
     */

    public static String takeScreenshot(WebDriver driver, String testName) {

        // Generate a unique timestamp for the screenshot file
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS")
                .format(new Date());

        // Create the screenshot file path
        String screenshotPath =
                System.getProperty("user.dir")
                        + "/target/screenshots/"
                        + testName + "_" + timeStamp + ".png";

        try {

            // Capture screenshot from the current browser window
            File source =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            // Create destination file
            File destination = new File(screenshotPath);

            // Copy screenshot to the destination folder
            FileUtils.copyFile(source, destination);

            // Print success message
            System.out.println("Screenshot saved successfully: "
                    + screenshotPath);

            return screenshotPath;

        } catch (IOException e) {

            // Handle file saving errors
            System.err.println("Failed to save screenshot: "
                    + e.getMessage());

            return null;

        } catch (Exception e) {

            // Handle unexpected errors during screenshot capture
            System.err.println("Error while capturing screenshot: "
                    + e.getMessage());

            return null;
        }
    }
}
