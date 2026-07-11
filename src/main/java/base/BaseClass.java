package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    
    //  String variable named 'browser'
    
    public void setup(@Optional("chrome") String browser) {

       
        //  the browser is initialized correctly for parallel execution.
    	
        DriverManager.initializeDriver(browser);

        driver = DriverManager.getDriver();
        driver.manage().window().maximize();

        String targetUrl = "https://allrecipes.com";
        int maxRetries = 2;
        int attempt = 0;
        boolean isLoaded = false;

        while (attempt < maxRetries && !isLoaded) {
            try {
                attempt++;
                driver.get(targetUrl);
                isLoaded = true;
            } catch (WebDriverException e) {
                System.err.println("Network failure on attempt " + attempt + ": " + e.getMessage());

                if (attempt >= maxRetries) {
                    throw e;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    public WebDriver getDriver() {
        return driver;
    }
}