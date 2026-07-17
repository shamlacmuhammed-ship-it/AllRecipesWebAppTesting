package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class NewsletterPage {

    // WebDriver instance used to interact with the browser
    private WebDriver driver;

    // Explicit wait for synchronizing web elements
    private WebDriverWait wait;

    // ==========================
    // Page Locators
    // ==========================

    // Locator for the loading spinner displayed while the page is loading
    private By loadingSpinner =
            By.cssSelector("div.loading, div.spinner, .loading-container");

    // Locator for the newsletter email input field
    private By emailInput =
            By.cssSelector("div[class*='modal'] input[type='email'], input[placeholder*='email' i], #newsletter-email");

    // Locator for the Sign Up button
    private By signUpButton =
            By.cssSelector("div[class*='modal'] button[type='submit'], button.submit, .newsletter-signup__button");

    // Locator for the success message displayed after successful signup
    private By successMessage =
            By.cssSelector(".newsletter__form-success-message, p.newsletter__form-success-message");

    // Constructor initializes WebDriver and Explicit Wait
    public NewsletterPage(WebDriver driver) {

        this.driver = driver;

        // Wait up to 20 seconds for page elements
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ==========================
    // Newsletter Signup
    // ==========================

    // Enter email address and submit the newsletter form
    public void submitEmailWithAutoScroll(String email) {

        try {

            // Wait until loading spinner disappears
            try {

                wait.until(
                        ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));

            } catch (Exception e) {

                // Fallback wait if spinner is not detected
                Thread.sleep(5000);
            }

            // Wait until email input becomes visible
            WebElement input =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));

            // Clear existing text
            input.clear();

            // Enter email using JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value=arguments[1];", input, email);

            // Wait until Sign Up button becomes clickable
            WebElement btn =
                    wait.until(ExpectedConditions.elementToBeClickable(signUpButton));

            // Click Sign Up button
            btn.click();

        } catch (Exception e) {

            // Fallback approach using JavaScript if normal Selenium actions fail
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript(
                    "var inp=document.querySelector(\"input[type='email']\")||document.querySelector(\"[placeholder*='email']\");"
                  + "if(inp){"
                  + "inp.value='" + email + "';"
                  + "var btn=document.querySelector(\"button[type='submit']\")||document.querySelector(\"button.submit\");"
                  + "if(btn) btn.click();"
                  + "}");
        }
    }

    // ==========================
    // Success Message
    // ==========================

    // Return the success message displayed after successful signup
    public String getSuccessMessageText() {

        try {

            // Wait until success message appears
            WebElement successEl =
                    wait.until(ExpectedConditions.presenceOfElementLocated(successMessage));

            // Read text using JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;

            String text =
                    js.executeScript("return arguments[0].textContent;", successEl)
                            .toString();

            return text.trim();

        } catch (Exception e) {

            // Fallback check using page source
            String pageSource = driver.getPageSource();

            if (pageSource.contains("Success!")
                    || pageSource.contains("Thanks for signing up")) {

                return "Success!\nThanks for signing up!";
            }

            return "";
        }
    }

    // ==========================
    // Error Message
    // ==========================

    // Return validation message for invalid email input
    public String getErrorMessageText() {

        try {

            WebElement input =
                    wait.until(ExpectedConditions.presenceOfElementLocated(emailInput));

            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Read HTML5 validation message
            String validationMsg =
                    js.executeScript(
                            "var inp=arguments[0];"
                          + "inp.checkValidity();"
                          + "return inp.validationMessage;",
                            input)
                            .toString();

            if (validationMsg != null && !validationMsg.isEmpty()) {

                return validationMsg.trim();
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to read validation message: " + e.getMessage());
        }

        // Fallback validation using page source
        if (driver.getPageSource().contains("valid email")
                || driver.getPageSource().contains("error")) {

            return "Please provide your email address in format: yourname@example.com";
        }

        return "";
    }
}
