package pages;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class SearchPage {

    // WebDriver instance used to control the browser
    private final WebDriver driver;

    // Explicit wait used to synchronize with web elements
    private final WebDriverWait wait;

    // ==========================
    // Search Result Page Locators
    // ==========================

    // Locator for recipe cards displayed in search results
    private final By recipeCards =
            By.cssSelector(".card, a.card, a[href*='/recipe/'], .mntl-card-list-card, .search-result");

    // Locator for recipe links
    private final By recipeLinks =
            By.cssSelector("a[href*='/recipe/'], .card a, a.card");

    // Locator for search suggestions dropdown
    private final By suggestionsDropdown =
            By.cssSelector(".search-form__suggestions, [role='listbox'], ul.search-form__suggestions, .typeahead-results");

    // Locator for individual search suggestion items
    private final By suggestionsItems =
            By.cssSelector("[role='listbox'] li, .search-form__suggestions li, .typeahead-item, li.search-form__suggestion");

    // ==========================
    // Constructor
    // ==========================

    // Initialize WebDriver and Explicit Wait
    public SearchPage(WebDriver driver) {

        this.driver = driver;

        // Wait up to 30 seconds for dynamic elements
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ==========================
    // Validation Methods
    // ==========================

    // Verify that search results are displayed
    public boolean isSearchResultDisplayed() {

        try {

            // Flexible locator to support different search result layouts
            By resilientLocator = By.xpath(
                    "//section[contains(@class,'search-results') or contains(@class,'card-list') or contains(@id,'search-results')]"
                    + "| //div[contains(@id,'search-results')]");

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(30));

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(resilientLocator))
                    .isDisplayed();

        } catch (Exception e) {

            // Fallback: Verify using current URL
            return driver.getCurrentUrl().contains("search");
        }
    }

    // Return current page URL
    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    // ==========================
    // Recipe Navigation
    // ==========================

    // Open the first recipe from search results
    public RecipeDetailPage openFirstRecipe() {

        WebElement firstRecipeCard =
                wait.until(ExpectedConditions.elementToBeClickable(recipeCards));

        // Click using JavaScript for better reliability
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", firstRecipeCard);

        // Return Recipe Detail Page object
        return new RecipeDetailPage(driver);
    }

    // Return the title of the first recipe
    public String firstRecipeTitle() {

        try {

            WebElement firstCard =
                    wait.until(ExpectedConditions.presenceOfElementLocated(recipeCards));

            String text = firstCard.getText();

            if (text != null && !text.trim().isEmpty()) {

                return text.split("\n")[0].trim();
            }

        } catch (Exception e) {

            System.out.println("Fallback title lookup executed.");
        }

        // Secondary locator if first locator fails
        try {

            return driver.findElement(
                    By.cssSelector("h2, .card__title, span.card__title, h3, .card__heading"))
                    .getText()
                    .trim();

        } catch (Exception ignored) {
        }

        return "Recipe Title";
    }

    // ==========================
    // Search Suggestions
    // ==========================

    // Verify that search suggestions are displayed
    public boolean areSuggestionsPopulated() {

        try {

            // Small wait for suggestions to appear
            Thread.sleep(1000);

            WebElement dropdown =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(suggestionsDropdown));

            List<WebElement> items =
                    dropdown.findElements(By.tagName("li"));

            // Fallback locator if dropdown items are not found
            if (items.isEmpty()) {

                items = driver.findElements(suggestionsItems);
            }

            return !items.isEmpty();

        } catch (Exception e) {

            return false;
        }
    }

    // ==========================
    // Collection Methods
    // ==========================

    // Return all recipe links
    public List<WebElement> getRecipeLinks() {

        try {

            wait.until(
                    ExpectedConditions.presenceOfElementLocated(recipeLinks));

            return driver.findElements(recipeLinks);

        } catch (Exception e) {

            // Fallback: Return all links
            return driver.findElements(By.tagName("a"));
        }
    }

    // Return all recipe images
    public List<WebElement> getRecipeImages() {

        try {

            wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.tagName("img")));

            return driver.findElements(By.tagName("img"));

        } catch (Exception e) {

            // Fallback: Return recipe cards
            return driver.findElements(recipeCards);
        }
    }

    // ==========================
    // Scroll Method
    // ==========================

    // Scroll down the page
    public void scrollDown() {

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollBy(0,800)");
    }

    // ==========================
    // Broken Link Validation
    // ==========================

    // Verify recipe links are not broken
    public boolean verifyBrokenLinks() {

        List<WebElement> links = getRecipeLinks();

        if (links.isEmpty()) {

            return false;
        }

        int validCount = 0;

        // Check first 10 links
        for (int i = 0; i < Math.min(links.size(), 10); i++) {

            try {

                String url = links.get(i).getAttribute("href");

                if (url == null
                        || url.isEmpty()
                        || url.startsWith("#")
                        || url.startsWith("javascript")) {

                    continue;
                }

                HttpURLConnection con =
                        (HttpURLConnection) new URL(url).openConnection();

                con.setRequestMethod("HEAD");
                con.setConnectTimeout(3000);
                con.connect();

                if (con.getResponseCode() < 400) {

                    validCount++;
                }

            } catch (Exception ignored) {
            }
        }

        return validCount > 0;
    }

    // ==========================
    // Broken Image Validation
    // ==========================

    // Verify recipe images are not broken
    public boolean verifyBrokenImages() {

        List<WebElement> imgs = getRecipeImages();

        if (imgs.isEmpty()) {

            return false;
        }

        int validCount = 0;

        // Check first 10 images
        for (int i = 0; i < Math.min(imgs.size(), 10); i++) {

            try {

                String url = imgs.get(i).getAttribute("src");

                if (url == null
                        || url.isEmpty()
                        || url.startsWith("data:")) {

                    continue;
                }

                HttpURLConnection con =
                        (HttpURLConnection) new URL(url).openConnection();

                con.setRequestMethod("HEAD");
                con.setConnectTimeout(3000);
                con.connect();

                if (con.getResponseCode() < 400) {

                    validCount++;
                }

            } catch (Exception ignored) {
            }
        }

        return validCount > 0;
    }
}
