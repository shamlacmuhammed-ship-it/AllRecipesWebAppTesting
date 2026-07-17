package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SweepstakesPage {

    // WebDriver instance used to control the browser
    private final WebDriver driver;

    // Explicit wait used for synchronization
    private final WebDriverWait wait;

    // ==========================
    // Page Locators
    // ==========================

    // Locator for Sweepstakes page heading
    private final By sweepstakesHeader =
            By.xpath("//h1[contains(text(),'Win $25K') or contains(@class,'heading')]");

    // Locator for email input field
    private final By emailInput =
            By.cssSelector("input[type='email'], input[placeholder*='yourname'], #mntl-newsletter_1-2-email");

    // Locator for Submit button
    private final By submitButton =
            By.cssSelector("button[type='submit'], .newsletter__email-address-button, .js-submit-button");

    // Locator for newsletter subscription checkboxes
    private final By subscriptionCheckboxes =
            By.cssSelector("input[type='checkbox']");

    // Locator for success message displayed after successful submission
    private final By successMessage =
            By.xpath("//*[contains(text(),'Success!') or contains(text(),'Thanks for signing up') or contains(@class,'success-message')]");

    // ==========================
    // Constructor
    // ==========================

    // Initialize WebDriver and Explicit Wait
    public SweepstakesPage(WebDriver driver) {

        this.driver = driver;

        // Wait up to 15 seconds for page elements
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==========================
    // Page Validation
    // ==========================

    // Return the page title
    public String getPageTitle() {

        return driver.getTitle();
    }

    // Return the Sweepstakes page heading
    public String getHeaderText() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(sweepstakesHeader))
                    .getText()
                    .trim();

        } catch (Exception e) {

            return "";
        }
    }

    // Verify whether the entry form is displayed
    public boolean isEntryFormDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(emailInput))
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    // ==========================
    // Form Actions
    // ==========================

    // Enter email address into the input field
    public void enterEmail(String email) {

        WebElement element =
                wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));

        // Clear previous value
        element.clear();

        // Enter email using JavaScript
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].value=arguments[1];", element, email);
    }

    // Select or deselect all subscription checkboxes
    public void setAllCheckboxesState(boolean checked) {

        try {

            List<WebElement> checkboxes =
                    driver.findElements(subscriptionCheckboxes);

            for (WebElement cb : checkboxes) {

                if (cb.isDisplayed() && cb.isSelected() != checked) {

                    // Click checkbox using JavaScript
                    ((JavascriptExecutor) driver)
                            .executeScript("arguments[0].click();", cb);
                }
            }

        } catch (Exception ignored) {
        }
    }

    // Click the Submit button
    public void clickSubmitButton() {

        WebElement element =
                wait.until(ExpectedConditions.elementToBeClickable(submitButton));

        try {

            // Normal Selenium click
            element.click();

        } catch (Exception e) {

            // Fallback JavaScript click if Selenium click fails
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", element);
        }
    }

    // ==========================
    // Success Message
    // ==========================

    // Return the success message after successful submission
    public String getSuccessMessageText() {

        System.out.println("\n==========================================");
        System.out.println("Retrieving Success Message");
        System.out.println("==========================================");

        try {

            WebElement element =
                    wait.until(ExpectedConditions.presenceOfElementLocated(successMessage));

            String text = element.getText().trim();

            System.out.println("Success Message Found:");
            System.out.println(text);

            return text;

        } catch (Exception e) {

            System.out.println("Success message not found using locator.");
            System.out.println("Checking page source...");

            // Fallback: Check page source
            String pageSource = driver.getPageSource();

            if (pageSource.contains("Success!")
                    || pageSource.contains("Thanks for signing up")) {

                return "Success!\nThanks for signing up!";
            }

            return "";
        }
    }
}
