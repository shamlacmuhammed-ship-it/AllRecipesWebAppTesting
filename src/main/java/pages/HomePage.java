package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ==========================
    // Navigation Menu
    // ==========================

    private final By dinnerTonight = By.xpath("//nav//a[normalize-space()='Dinner Tonight']");
    private final By recipes = By.xpath("//nav//a[normalize-space()='Recipes']");
    private final By ingredients = By.xpath("//nav//a[normalize-space()='Ingredients']");
    private final By occasions = By.xpath("//nav//a[normalize-space()='Occasions']");
    private final By cuisines = By.xpath("//nav//a[normalize-space()='Cuisines']");
    private final By inTheKitchen = By.xpath("//nav//a[normalize-space()='In the Kitchen']");
    private final By news = By.xpath("//nav//a[normalize-space()='News']");
    private final By community = By.xpath("//nav//a[normalize-space()='Community']");
    private final By video = By.xpath("//nav//a[normalize-space()='Video']");
    private final By aboutUs = By.xpath("//nav//a[normalize-space()='About Us']");

    // ==========================
    // Search
    // ==========================

    private final By searchInput = By.xpath(
            "//input[@id='mntl-search-form--open__search-input' "
                    + "or @placeholder='Find a recipe or ingredient']");

    // ==========================
    // Header Links
    // ==========================

    private final By newslettersMenuLink = By.xpath(
            "//a[contains(.,'Newsletters')]");

    private final By myRecipesLink =
            By.xpath("//a[contains(@class,'myr-login-trigger')]");
    // ==========================
    // Content
    // ==========================

    private final By activeMenuElement = By.xpath(
            "//nav//a[contains(@class,'active') or @aria-current='page']");

    private final By advertisingBanner = By.xpath(
            "//div[contains(@class,'banner') or contains(@id,'leaderboard')]");

    private final By featuredRecipesWrapper = By.tagName("main");

    private final By latestSectionHeader = By.xpath(
            "//*[contains(text(),'Latest')]");

    public HomePage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }

    // ==========================
    // Validation Methods
    // ==========================

    public boolean isLogoDisplayed() {
        return !driver.findElements(
                By.xpath("//*[local-name()='svg' and contains(@class,'icon-logo')]"))
                .isEmpty();
    }

    public boolean isSearchBarDisplayed() {
        return !driver.findElements(searchInput).isEmpty();
    }

    public boolean isNavigationMenuDisplayed() {
        return !driver.findElements(By.tagName("nav")).isEmpty();
    }

    public boolean isActiveMenuHighlighted() {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(activeMenuElement))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isBannerDisplayed() {

        return !driver.findElements(advertisingBanner).isEmpty();
    }

    public boolean isFeaturedContentDisplayed() {

        return !driver.findElements(featuredRecipesWrapper).isEmpty();
    }

    public boolean isTheLatestSectionDisplayed() {

        return !driver.findElements(latestSectionHeader).isEmpty();
    }

    // ==========================
    // Search
    // ==========================

    public SearchPage searchRecipe(String recipe) {

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput));

        input.clear();
        input.sendKeys(recipe);
        input.sendKeys(Keys.ENTER); 

        return new SearchPage(driver);
    }

    public void typeSearch(String text) {

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput));

        input.clear();
        input.sendKeys(text);
    }

    // ==========================
    // Scroll
    // ==========================

    public void scrollPageToBottom() {
        // 1.scroll to bottom of page using javascript
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");

        // 2.In Firefox, instead of waiting for specific elements,
     // wait only until the page has fully loaded.
        try {
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            System.out.println("Page load wait timed out after scroll, continuing test.");
        }
    }

    public long getVerticalScrollOffset() {

        Number value = (Number) ((JavascriptExecutor) driver)
                .executeScript("return window.pageYOffset;");

        return value.longValue();
    }

    // ==========================
    // Collections
    // ==========================

    public List<WebElement> getAllImages() {
        return driver.findElements(By.tagName("img"));
    }

    public List<WebElement> getAllAnchorLinks() {
        return driver.findElements(By.tagName("a"));
    }

    // ==========================
    // Navigation Methods
    // ==========================

    public NewsletterPage clickNewslettersMenuLink() {

        WebElement link = wait.until(
                ExpectedConditions.elementToBeClickable(newslettersMenuLink));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", link);

        return new NewsletterPage(driver);
    }

    public void clickMyRecipes() {

        WebElement link = wait.until(
                ExpectedConditions.elementToBeClickable(myRecipesLink));

        ((JavascriptExecutor)driver)
                .executeScript("arguments[0].click();", link);

    }
    // ==========================
    // Menu Clicks
    // ==========================

    public void clickDinnerTonight() {
        wait.until(ExpectedConditions.elementToBeClickable(dinnerTonight)).click();
    }

    public void clickRecipes() {
        wait.until(ExpectedConditions.elementToBeClickable(recipes)).click();
    }

    public void clickIngredients() {
        wait.until(ExpectedConditions.elementToBeClickable(ingredients)).click();
    }

    public void clickOccasions() {
        wait.until(ExpectedConditions.elementToBeClickable(occasions)).click();
    }

    public void clickCuisines() {
        wait.until(ExpectedConditions.elementToBeClickable(cuisines)).click();
    }

    public void clickInTheKitchen() {
        wait.until(ExpectedConditions.elementToBeClickable(inTheKitchen)).click();
    }

    public void clickNews() {
        wait.until(ExpectedConditions.elementToBeClickable(news)).click();
    }

    public void clickCommunity() {
        wait.until(ExpectedConditions.elementToBeClickable(community)).click();
    }

    public void clickVideo() {
        wait.until(ExpectedConditions.elementToBeClickable(video)).click();
    }

    public void clickAboutUs() {
        wait.until(ExpectedConditions.elementToBeClickable(aboutUs)).click();
    }
}