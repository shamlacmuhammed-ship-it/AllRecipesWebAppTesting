package base;

// Import Selenium WebDriver interface
import org.openqa.selenium.WebDriver;

// Import exception thrown when browser/network/WebDriver errors occur
import org.openqa.selenium.WebDriverException;

// Import TestNG annotations
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {

    // WebDriver object used by all test classes
    // 'protected' allows child classes to access it directly
    protected WebDriver driver;

    // This method executes BEFORE every @Test method
    // alwaysRun=true ensures it runs even when tests belong to different groups
    @BeforeMethod(alwaysRun = true)

    // Reads the "browser" parameter from testng.xml
    @Parameters("browser")

    // If browser parameter is not passed,
    // Chrome is used as the default browser
    public void setup(@Optional("chrome") String browser) {

        // browser variable receives values like:
        // chrome
        // edge
        // firefox

        // Initialize browser using DriverManager
        // DriverManager creates a separate browser instance
        // for each thread (ThreadLocal)
        DriverManager.initializeDriver(browser);

        // Get the browser instance created by DriverManager
        driver = DriverManager.getDriver();

        // Maximize browser window
        driver.manage().window().maximize();

        // Website to be opened before each test
        String targetUrl = "https://allrecipes.com";

        // Maximum number of retry attempts
        // if website fails to load
        int maxRetries = 2;

        // Keeps track of current attempt
        int attempt = 0;

        // Flag indicating whether page loaded successfully
        boolean isLoaded = false;

        // Retry loop
        // Continues until either:
        // 1. Website loads successfully
        // OR
        // 2. Maximum retries reached
        while (attempt < maxRetries && !isLoaded) {

            try {

                // Increment attempt count
                attempt++;

                // Open the website
                driver.get(targetUrl);

                // If page opens successfully,
                // exit the loop
                isLoaded = true;

            } catch (WebDriverException e) {

                // Handles browser/network failures
                System.err.println(
                        "Network failure on attempt "
                        + attempt
                        + ": "
                        + e.getMessage());

                // If maximum retries already completed,
                // throw exception and stop execution
                if (attempt >= maxRetries) {
                    throw e;
                }

                // Wait 5 seconds before retrying
                try {

                    Thread.sleep(5000);

                } catch (InterruptedException ie) {

                    // Restore interrupted status
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Runs AFTER every @Test method
    // Closes browser regardless of test result
    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        // Quit browser and remove ThreadLocal instance
        DriverManager.quitDriver();
    }

    // Getter method
    // Returns current WebDriver object
    // Used by Screenshot Listener and other classes
    public WebDriver getDriver() {
        return driver;
    }
}
