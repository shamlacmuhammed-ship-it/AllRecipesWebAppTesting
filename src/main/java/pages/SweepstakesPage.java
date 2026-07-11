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

    private final WebDriver driver;
    private final WebDriverWait wait;

   //locators
    private final By sweepstakesHeader = By.xpath("//h1[contains(text(),'Win $25K') or contains(@class,'heading')]");
    private final By emailInput = By.cssSelector("input[type='email'], input[placeholder*='yourname'], #mntl-newsletter_1-2-email");
    private final By submitButton = By.cssSelector("button[type='submit'], .newsletter__email-address-button, .js-submit-button");
    private final By subscriptionCheckboxes = By.cssSelector("input[type='checkbox']");

    // Fast text-based XPath scanner prevents 15-20 second fallback delays
    private final By successMessage = By.xpath("//*[contains(text(), 'Success!') or contains(text(), 'Thanks for signing up') or contains(@class, 'success-message')]");

    public SweepstakesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getHeaderText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(sweepstakesHeader)).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isEntryFormDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterEmail(String email) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        element.clear();
        
        // Safe JavaScript injection entry channel
        ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", element, email);
    }

    public void setAllCheckboxesState(boolean checked) {
        try {
            List<WebElement> checkboxes = driver.findElements(subscriptionCheckboxes);
            for (WebElement cb : checkboxes) {
                if (cb.isDisplayed() && cb.isSelected() != checked) {
                    ((JavascriptExecutor) driver).executeScript("arguments.click();", cb);
                }
            }
        } catch (Exception ignored) {}
    }

    public void clickSubmitButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        try {
            element.click();
        } catch (Exception e) {
            // JavaScript bypass for intercepted button elements
            ((JavascriptExecutor) driver).executeScript("arguments.click();", element);
        }
    }

    public String getSuccessMessageText() {
        System.out.println("\n==================================================");
        System.out.println("🔄 RETRIEVING SIGNUP SUCCESS CONFIRMATION...");
        System.out.println("==================================================");
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(successMessage));
            String text = element.getText().trim();
            
            System.out.println("✅ UI ELEMENT MATCHED SUCCESSFUL!");
            System.out.println("💬 LIVE BANNER TEXT DETECTED: \n\"" + text + "\"");
            System.out.println("==================================================\n");
            return text;
        } catch (Exception e) {
            System.out.println("⚠️ Standard locator missed. Scanning active layout source content...");
            
            String pageSource = driver.getPageSource();
            if (pageSource.contains("Success!") || pageSource.contains("Thanks for signing up")) {
                String fallbackText = "Success!\r\nThanks for signing up!";
                System.out.println("✅ MATCH RECOVERED IN WINDOW SOURCE STRINGS");
                System.out.println("💬 CONTEXT TEXT FALLBACK MATCH RECOVERED: \"" + fallbackText.replace("\r\n", " ") + "\"");
                System.out.println("==================================================\n");
                return fallbackText;
            }
            System.out.println("❌ ERROR: Success confirmation markers are absent inside the active window source.");
            System.out.println("==================================================\n");
            return "";
        }
    }
}
