package pages;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyRecipesPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Search box
    private By searchBox = By.xpath(
            "//input[contains(@placeholder,'recipe') or contains(@placeholder,'ingredient')]");

    // Login button
    private By loginButton = By.xpath(
            "//a[contains(@href,'login') or contains(@href,'authentication') or contains(text(),'Log In')]");

    // Join button
    private By joinButton = By.xpath(
            "//a[contains(text(),'Join') or contains(text(),'Sign Up')]");

    // Hero heading (updated)
    private By heroHeading = By.xpath("//h1 | //h2");

    public MyRecipesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
    }

    public boolean isSearchDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isJoinDisplayed() {
        try {
            return driver.findElements(joinButton).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isHeadingDisplayed() {
        try {
            return driver.findElements(heroHeading).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void searchRecipe(String recipe) {

        WebElement box = wait.until(ExpectedConditions.elementToBeClickable(searchBox));

        box.clear();
        box.sendKeys(recipe);
        box.sendKeys(Keys.ENTER);
    }

    public void clickLogin() {

        WebElement login =
                wait.until(ExpectedConditions.elementToBeClickable(loginButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", login);
    }

    public void clickJoin() {

        WebElement join =
                wait.until(ExpectedConditions.elementToBeClickable(joinButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", join);
    }

}