package base;

// Selenium WebDriver interface
import org.openqa.selenium.WebDriver;

// Chrome browser classes
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

// Microsoft Edge browser classes
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

// Firefox browser classes
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

// Automatically downloads and manages browser driver versions
import io.github.bonigarcia.wdm.WebDriverManager;

// Used for creating immutable collections
import java.util.Collections;

public class DriverManager {

    // ThreadLocal stores a separate WebDriver instance for each thread.
    // This enables safe parallel execution without browsers interfering.
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Initializes the browser based on the value received from testng.xml
    public static void initializeDriver(String browser) {

        // Prevent creating multiple browser instances in the same thread
        if (driver.get() != null) {
            return;
        }

        // If no browser is provided, use Chrome as the default browser
        if (browser == null || browser.trim().isEmpty()) {
            browser = "chrome";
        }

        // Read headless mode from Maven command
        // Example:
        // mvn test -Dheadless=true
        boolean isHeadless =
                Boolean.parseBoolean(System.getProperty("headless", "false"));

        // Select browser based on the browser parameter
        switch (browser.toLowerCase().trim()) {

            // ===========================
            // Chrome Browser
            // ===========================
            case "chrome":

                // Automatically download and configure the correct ChromeDriver
                WebDriverManager.chromedriver().setup();

                // Create Chrome browser options
                ChromeOptions chromeOptions = new ChromeOptions();

                // Open browser in maximized mode
                chromeOptions.addArguments("--start-maximized");

                // Hide Selenium automation indicators
                chromeOptions.setExperimentalOption(
                        "excludeSwitches",
                        Collections.singletonList("enable-automation"));

                // Disable automation extension
                chromeOptions.setExperimentalOption(
                        "useAutomationExtension",
                        false);

                // Hide WebDriver detection from websites
                chromeOptions.addArguments(
                        "--disable-blink-features=AutomationControlled");

                // Use a normal browser User-Agent
                chromeOptions.addArguments(
                        "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/120.0.0.0 Safari/537.36");

                // Enable headless mode if requested
                if (isHeadless) {

                    chromeOptions.addArguments("--headless=new");

                    chromeOptions.addArguments("--disable-gpu");
                }

                // Create Chrome browser instance
                driver.set(new ChromeDriver(chromeOptions));

                break;

            // ===========================
            // Firefox Browser
            // ===========================
            case "firefox":

                // Automatically download and configure FirefoxDriver
                WebDriverManager.firefoxdriver().setup();

                // Create Firefox options
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                // Reduce WebDriver detection
                firefoxOptions.addPreference(
                        "dom.webdriver.enabled",
                        false);

                firefoxOptions.addPreference(
                        "useAutomationExtension",
                        false);

                // Override User-Agent
                firefoxOptions.addPreference(
                        "general.useragent.override",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0)"
                        + " Gecko/20100101 Firefox/128.0");

                // Enable Headless Mode
                if (isHeadless) {

                    firefoxOptions.addArguments("-headless");
                }

                // Create Firefox browser instance
                driver.set(new FirefoxDriver(firefoxOptions));

                break;

            // ===========================
            // Microsoft Edge Browser
            // ===========================
            case "edge":

                // Automatically download and configure EdgeDriver
                WebDriverManager.edgedriver().setup();

                // Create Edge browser options
                EdgeOptions edgeOptions = new EdgeOptions();

                // Start browser maximized
                edgeOptions.addArguments("--start-maximized");

                // Disable GPU acceleration
                edgeOptions.addArguments("--disable-gpu");

                // Hide Selenium automation indicators
                edgeOptions.setExperimentalOption(
                        "excludeSwitches",
                        Collections.singletonList("enable-automation"));

                edgeOptions.setExperimentalOption(
                        "useAutomationExtension",
                        false);

                edgeOptions.addArguments(
                        "--disable-blink-features=AutomationControlled");

                // Enable headless mode if requested
                if (isHeadless) {

                    edgeOptions.addArguments("--headless=new");
                }

                // Create Edge browser instance
                driver.set(new EdgeDriver(edgeOptions));

                break;

            // Invalid browser name
            default:

                throw new IllegalArgumentException(
                        "Unsupported browser: " + browser);
        }
    }

    // Returns the WebDriver instance of the current thread
    public static WebDriver getDriver() {

        return driver.get();
    }

    // Closes browser and removes ThreadLocal reference
    public static void quitDriver() {

        // Check whether browser exists
        if (driver.get() != null) {

            try {

                // Close all browser windows and end WebDriver session
                driver.get().quit();

            } finally {

                // Remove ThreadLocal reference
                // Prevents memory leaks during parallel execution
                driver.remove();
            }
        }
    }
}
