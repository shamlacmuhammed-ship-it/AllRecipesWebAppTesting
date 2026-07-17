package pages;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyRecipesPage {

    // WebDriver instance used to control the browser
    private WebDriver driver;

    // Explicit wait used for synchronization
    private WebDriverWait wait;

    // ==========================
    // Page Locators
    // ==========================

    // Locator for the recipe search box
    private By searchBox = By.xpath(
            "//input[contains(@placeholder,'recipe') or contains(@placeholder,'ingredient')]");

    // Locator for the Log In button
    private By loginButton = By.xpath(
            "//a[contains(@href,'login') or contains(@href,'authentication') or contains(text(),'Log In')]");

    // Locator for the Join / Sign Up button
    private By joinButton = By.xpath(
            "//a[contains(text(),'Join') or contains(text(),'Sign Up')]");

    // Locator for the main page heading
    private By heroHeading = By.xpath("//h1 | //h2");

    // Constructor initializes WebDriver and Explicit Wait
    public MyRecipesPage(WebDriver driver) {

        this.driver = driver;

        // Wait up to 15 seconds for page elements
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait until the page body is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
    }

    // ==========================
    // Validation Methods
    // ==========================

    // Verify whether the search box is displayed
    public boolean isSearchDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(searchBox))
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    // Verify whether the Log In button is displayed
    public boolean isLoginDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(loginButton))
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    // Verify whether the Join button is displayed
    public boolean isJoinDisplayed() {

        try {

            return driver.findElements(joinButton).size() > 0;

        } catch (Exception e) {

            return false;
        }
    }

    // Verify whether the page heading is displayed
    public boolean isHeadingDisplayed() {

        try {

            return driver.findElements(heroHeading).size() > 0;

        } catch (Exception e) {

            return false;
        }
    }

    // ==========================
    // Search Method
    // ==========================

    // Search for a recipe using the search box
    public void searchRecipe(String recipe) {

        // Wait until the search box is clickable
        WebElement box =
                wait.until(ExpectedConditions.elementToBeClickable(searchBox));

        // Clear previous text
        box.clear();

        // Enter recipe name
        box.sendKeys(recipe);

        // Press Enter to perform the search
        box.sendKeys(Keys.ENTER);
    }

    // ==========================
    // Navigation Methods
    // ==========================

    // Click the Log In button
    public void clickLogin() {

        WebElement login =
                wait.until(ExpectedConditions.elementToBeClickable(loginButton));

        // Perform JavaScript click for better reliability
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", login);
    }

    // Click the Join / Sign Up button
    public void clickJoin() {

        WebElement join =
                wait.until(ExpectedConditions.elementToBeClickable(joinButton));

        // Perform JavaScript click for better reliability
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", join);
    }

}
