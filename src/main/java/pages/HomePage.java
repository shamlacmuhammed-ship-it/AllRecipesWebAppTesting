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

    // WebDriver instance to interact with the browser
    private final WebDriver driver;

    // Explicit wait for synchronizing with web elements
    private final WebDriverWait wait;

    // ==========================
    // Navigation Menu Locators
    // ==========================

    // Locators for the homepage navigation menu
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
    // Search Bar Locator
    // ==========================

    // Locator for the search input field
    private final By searchInput = By.xpath(
            "//input[@id='mntl-search-form--open__search-input' "
                    + "or @placeholder='Find a recipe or ingredient']");

    // ==========================
    // Header Links
    // ==========================

    // Locator for Newsletters link
    private final By newslettersMenuLink =
            By.xpath("//a[contains(.,'Newsletters')]");

    // Locator for My Recipes link
    private final By myRecipesLink =
            By.xpath("//a[contains(@class,'myr-login-trigger')]");

    // ==========================
    // Content Section Locators
    // ==========================

    // Locator for active menu item
    private final By activeMenuElement =
            By.xpath("//nav//a[contains(@class,'active') or @aria-current='page']");

    // Locator for advertisement banner
    private final By advertisingBanner =
            By.xpath("//div[contains(@class,'banner') or contains(@id,'leaderboard')]");

    // Locator for featured content section
    private final By featuredRecipesWrapper = By.tagName("main");

    // Locator for Latest section
    private final By latestSectionHeader =
            By.xpath("//*[contains(text(),'Latest')]");

    // Constructor initializes WebDriver and Explicit Wait
    public HomePage(WebDriver driver) {

        this.driver = driver;

        // Wait up to 20 seconds for page elements
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Wait until the page is fully loaded
        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }

    // ==========================
    // Validation Methods
    // ==========================

    // Check whether website logo is displayed
    public boolean isLogoDisplayed() {
        return !driver.findElements(
                By.xpath("//*[local-name()='svg' and contains(@class,'icon-logo')]"))
                .isEmpty();
    }

    // Check whether search bar is displayed
    public boolean isSearchBarDisplayed() {
        return !driver.findElements(searchInput).isEmpty();
    }

    // Check whether navigation menu is displayed
    public boolean isNavigationMenuDisplayed() {
        return !driver.findElements(By.tagName("nav")).isEmpty();
    }

    // Check whether current menu is highlighted
    public boolean isActiveMenuHighlighted() {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(activeMenuElement))
                    .isDisplayed();

        } catch (TimeoutException e) {
            return false;
        }
    }

    // Check whether advertisement banner is displayed
    public boolean isBannerDisplayed() {
        return !driver.findElements(advertisingBanner).isEmpty();
    }

    // Check whether featured content section is displayed
    public boolean isFeaturedContentDisplayed() {
        return !driver.findElements(featuredRecipesWrapper).isEmpty();
    }

    // Check whether Latest section is displayed
    public boolean isTheLatestSectionDisplayed() {
        return !driver.findElements(latestSectionHeader).isEmpty();
    }

    // ==========================
    // Search Methods
    // ==========================

    // Search for a recipe and return SearchPage object
    public SearchPage searchRecipe(String recipe) {

        // Wait until search box becomes visible
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput));

        // Clear previous search value
        input.clear();

        // Enter recipe name
        input.sendKeys(recipe);

        // Press Enter to perform search
        input.sendKeys(Keys.ENTER);

        // Navigate to Search Results page
        return new SearchPage(driver);
    }

    // Type text into search box without submitting
    public void typeSearch(String text) {

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput));

        input.clear();

        input.sendKeys(text);
    }

    // ==========================
    // Scroll Methods
    // ==========================

    // Scroll to the bottom of the page
    public void scrollPageToBottom() {

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");

        // Wait until page loading is completed
        try {
            wait.until(d ->
                    ((JavascriptExecutor) d)
                            .executeScript("return document.readyState")
                            .equals("complete"));

        } catch (Exception e) {

            System.out.println("Page load wait timed out after scroll.");
        }
    }

    // Return current vertical scroll position
    public long getVerticalScrollOffset() {

        Number value = (Number)
                ((JavascriptExecutor) driver)
                        .executeScript("return window.pageYOffset;");

        return value.longValue();
    }

    // ==========================
    // Collection Methods
    // ==========================

    // Return all image elements from the page
    public List<WebElement> getAllImages() {
        return driver.findElements(By.tagName("img"));
    }

    // Return all hyperlink elements from the page
    public List<WebElement> getAllAnchorLinks() {
        return driver.findElements(By.tagName("a"));
    }

    // ==========================
    // Navigation Methods
    // ==========================

    // Click the Newsletters link
    public NewsletterPage clickNewslettersMenuLink() {

        WebElement link = wait.until(
                ExpectedConditions.elementToBeClickable(newslettersMenuLink));

        // Use JavaScript click for better reliability
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", link);

        return new NewsletterPage(driver);
    }

    // Click the My Recipes link
    public void clickMyRecipes() {

        WebElement link = wait.until(
                ExpectedConditions.elementToBeClickable(myRecipesLink));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", link);
    }

    // ==========================
    // Navigation Menu Click Methods
    // ==========================

    // Click Dinner Tonight menu
    public void clickDinnerTonight() {
        wait.until(ExpectedConditions.elementToBeClickable(dinnerTonight)).click();
    }

    // Click Recipes menu
    public void clickRecipes() {
        wait.until(ExpectedConditions.elementToBeClickable(recipes)).click();
    }

    // Click Ingredients menu
    public void clickIngredients() {
        wait.until(ExpectedConditions.elementToBeClickable(ingredients)).click();
    }

    // Click Occasions menu
    public void clickOccasions() {
        wait.until(ExpectedConditions.elementToBeClickable(occasions)).click();
    }

    // Click Cuisines menu
    public void clickCuisines() {
        wait.until(ExpectedConditions.elementToBeClickable(cuisines)).click();
    }

    // Click In the Kitchen menu
    public void clickInTheKitchen() {
        wait.until(ExpectedConditions.elementToBeClickable(inTheKitchen)).click();
    }

    // Click News menu
    public void clickNews() {
        wait.until(ExpectedConditions.elementToBeClickable(news)).click();
    }

    // Click Community menu
    public void clickCommunity() {
        wait.until(ExpectedConditions.elementToBeClickable(community)).click();
    }

    // Click Video menu
    public void clickVideo() {
        wait.until(ExpectedConditions.elementToBeClickable(video)).click();
    }

    // Click About Us menu
    public void clickAboutUs() {
        wait.until(ExpectedConditions.elementToBeClickable(aboutUs)).click();
    }
}
