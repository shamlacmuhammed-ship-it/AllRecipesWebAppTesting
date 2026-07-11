package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Collections;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initializeDriver(String browser) {

        // Prevent opening multiple browser instances in the same thread
        if (driver.get() != null) {
            return;
        }

        if (browser == null || browser.trim().isEmpty()) {
            browser = "chrome";
        }

        // Required to run tests in headless mode using a Maven command
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        switch (browser.toLowerCase().trim()) {

            case "chrome":
                // Automatically download and manage the ChromeDriver version
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");

                // Anti-bot: Hide Selenium automation indicators
                chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                }

                driver.set(new ChromeDriver(chromeOptions));
                break;

            case "firefox":
                // Automatically download and manage the FirefoxDriver version
                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions = new FirefoxOptions();

                // Anti-bot: Hide WebDriver signatures
                firefoxOptions.addPreference("dom.webdriver.enabled", false);
                firefoxOptions.addPreference("useAutomationExtension", false);
                firefoxOptions.addPreference(
                        "general.useragent.override",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0");

                if (isHeadless) {
                    firefoxOptions.addArguments("-headless");
                }

                driver.set(new FirefoxDriver(firefoxOptions));
                break;

            case "edge":
                // Automatically download and manage the EdgeDriver version
                // This also helps resolve driver version mismatch errors
                WebDriverManager.edgedriver().setup();

                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--disable-gpu");

                // Anti-bot: Hide Selenium automation indicators
                edgeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                edgeOptions.setExperimentalOption("useAutomationExtension", false);
                edgeOptions.addArguments("--disable-blink-features=AutomationControlled");

                if (isHeadless) {
                    edgeOptions.addArguments("--headless=new");
                }

                driver.set(new EdgeDriver(edgeOptions));
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                driver.get().quit();
            } finally {
                // Remove the ThreadLocal reference to prevent memory leaks
                driver.remove();
            }
        }
    }
}