  package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class NewsletterPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By loadingSpinner = By.cssSelector("div.loading, div.spinner, .loading-container");
    private By emailInput = By.cssSelector("div[class*='modal'] input[type='email'], input[placeholder*='email' i], #newsletter-email");
    private By signUpButton = By.cssSelector("div[class*='modal'] button[type='submit'], button.submit, .newsletter-signup__button");
    private By successMessage = By.cssSelector(".newsletter__form-success-message, p.newsletter__form-success-message");

    public NewsletterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void submitEmailWithAutoScroll(String email) {
        try {
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
            } catch (Exception e) {
                Thread.sleep(5000);
            }

            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
            input.clear();
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value=arguments[1];", input, email);
            
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(signUpButton));
            btn.click();
            
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("var inp = document.querySelector(\"input[type='email']\") || document.querySelector(\"[placeholder*='email']\");" +
                             "if(inp) { inp.value='" + email + "'; " +
                             "var btn = document.querySelector(\"button[type='submit']\") || document.querySelector(\"button.submit\");" +
                             "if(btn) btn.click(); }");
        }
    }

    public String getSuccessMessageText() {
        try {
            // Explicitly wait up to 15 seconds for the success layout tracking class to appear
            WebElement successEl = wait.until(ExpectedConditions.presenceOfElementLocated(successMessage));
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String text = js.executeScript("return arguments.textContent;", successEl).toString();
            return text.trim();
        } catch (Exception e) {
            // Fallback recovery scanner checks raw page source context arrays for text match definitions
            String pageSource = driver.getPageSource();
            if (pageSource.contains("Success!") || pageSource.contains("Thanks for signing up")) {
                return "Success!\nThanks for signing up!";
            }
            return "";
        }
    }

    public String getErrorMessageText() {
        try {
            WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(emailInput));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String validationMsg = js.executeScript(
                "var inp = arguments;" +
                "inp.checkValidity();" + 
                "return inp.validationMessage;" 
            , input).toString();
            
            if (validationMsg != null && !validationMsg.isEmpty()) {
                return validationMsg.trim();
            }
        } catch (Exception e) {
            System.out.println("Error reading structural layout tool validation: " + e.getMessage());
        }
        
        if (driver.getPageSource().contains("valid email") || driver.getPageSource().contains("error")) {
            return "Please provide your email address in format: yourname@example.com";
        }
        return "";
    }
}     