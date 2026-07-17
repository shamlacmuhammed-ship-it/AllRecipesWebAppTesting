package tests;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.BaseClass;
import pages.HomePage;
import pages.SearchPage;
import utilities.ExcelUtils;

public class SearchTest extends BaseClass {

    // Page Object references
    private HomePage home;
    private SearchPage search;

    // Reads search keywords from Excel using DataProvider
    @DataProvider(name = "SearchTerms")
    public Object[][] data() {
        return ExcelUtils.getTestData(
                "src/main/resources/testdata/TestData.xlsx",
                "SearchTerms",
                1);
    }

    // Runs before every test
    @BeforeMethod
    public void setupPage() {

        try {
            // Clear browser data for clean test execution
            driver.manage().deleteAllCookies();
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
            ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
        } catch (Exception ignored) {}

        // Open home page
        driver.get("https://allrecipes.com");

        // Initialize Page Objects
        home = new HomePage(driver);
        search = new SearchPage(driver);
    }

    // Verify search bar is visible
    @Test(priority = 1)
    public void verifySearchBarDisplayed() {

        Assert.assertTrue(home.isSearchBarDisplayed(),
                "Search bar is not displayed on the home page.");
    }

    // Verify keyword search using Excel data
    @Test(dataProvider = "SearchTerms", priority = 2)
    public void verifyKeywordSearch(String keyword) {

        // Skip empty Excel rows
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }

        driver.get("https://allrecipes.com");

        // Reload Home Page object
        home = new HomePage(driver);

        // Small wait for page stability
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        // Perform search
        search = home.searchRecipe(keyword);

        // Verify search results page
        Assert.assertTrue(search.isSearchResultDisplayed(),
                "Search results are not displayed for: " + keyword);

        // Verify URL contains search
        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("search"),
                "Search URL not loaded.");
    }

    // Verify search using invalid keyword
    @Test(priority = 3)
    public void verifyInvalidKeyword() {

        driver.get("https://allrecipes.com");

        home = new HomePage(driver);

        search = home.searchRecipe("xyz123456");

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        boolean navigated =
                wait.until(ExpectedConditions.urlContains("search"));

        Assert.assertTrue(navigated,
                "Search page was not displayed.");
    }

    // Verify empty search
    @Test(priority = 4)
    public void verifyEmptySearch() {

        driver.get("https://allrecipes.com");

        home = new HomePage(driver);

        // Store current URL
        String beforeSearchUrl = driver.getCurrentUrl();

        // Type empty value
        home.typeSearch("");

        // Verify URL remains unchanged
        String afterSearchUrl = driver.getCurrentUrl();

        Assert.assertEquals(afterSearchUrl,
                beforeSearchUrl,
                "Empty search changed the page URL.");
    }

    // Verify search suggestions
    @Test(priority = 5,
          description = "Verify suggestions while typing")
    public void verifySuggestions() {

        driver.get("https://allrecipes.com");

        home = new HomePage(driver);

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        By searchLocator =
                By.id("mntl-search-form--open__search-input");

        WebElement input =
                wait.until(ExpectedConditions.elementToBeClickable(searchLocator));

        // Focus on search box
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].focus();", input);

        // Clear previous value
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].value='';", input);

        // Type partial keyword
        String partial = "chi";

        for (char ch : partial.toCharArray()) {

            input.sendKeys(String.valueOf(ch));

            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {}
        }

        // Verify entered text
        Assert.assertEquals(input.getAttribute("value"), "chi");

        // Verify suggestions are displayed
        Assert.assertTrue(search.areSuggestionsPopulated(),
                "Suggestions are not displayed.");
    }

    // Verify first recipe result
    @Test(priority = 6)
    public void verifyRecipeMatch() {

        driver.get("https://allrecipes.com");

        home = new HomePage(driver);

        search = home.searchRecipe("Chicken");

        Assert.assertTrue(search.isSearchResultDisplayed());

        // Read first recipe title
        String title = search.firstRecipeTitle();

        Assert.assertFalse(title.isEmpty(),
                "Recipe title is empty.");
    }

    // Verify broken images
    @Test(priority = 7)
    public void verifyBrokenImages() {

        driver.get("https://allrecipes.com");

        home = new HomePage(driver);

        search = home.searchRecipe("Chicken");

        Assert.assertTrue(search.isSearchResultDisplayed());

        // Verify image links
        Assert.assertTrue(search.verifyBrokenImages(),
                "Broken images found.");
    }

    // Verify broken hyperlinks
    @Test(priority = 8)
    public void verifyBrokenLinks() {

        driver.get("https://allrecipes.com");

        home = new HomePage(driver);

        search = home.searchRecipe("Chicken");

        Assert.assertTrue(search.isSearchResultDisplayed());

        // Verify hyperlinks
        Assert.assertTrue(search.verifyBrokenLinks(),
                "Broken links found.");
    }

    // Verify page scrolling
    @Test(priority = 9)
    public void verifyScroll() {

        driver.get("https://allrecipes.com");

        home = new HomePage(driver);

        search = home.searchRecipe("Chicken");

        Assert.assertTrue(search.isSearchResultDisplayed());

        // Scroll the search results page
        search.scrollDown();
    }
}
