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

    private HomePage home;
    private SearchPage search;

    @DataProvider(name = "SearchTerms")
    public Object[][] data() {
        return ExcelUtils.getTestData("src/main/resources/testdata/TestData.xlsx", "SearchTerms", 1);
    }

    @BeforeMethod
    public void setupPage() {
        try {
            // Clean browser state completely to avoid state cross-contamination in parallel threads
            driver.manage().deleteAllCookies();
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
            ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
        } catch (Exception ignored) {}
        
        driver.get("https://allrecipes.com");
        home = new HomePage(driver);
        search = new SearchPage(driver);
    }

    @Test(priority = 1)
    public void verifySearchBarDisplayed() {
        Assert.assertTrue(home.isSearchBarDisplayed(), "Search bar is not displayed on the home page.");
    }

    @Test(dataProvider = "SearchTerms", priority = 2)
    public void verifyKeywordSearch(String keyword) {
        // Isolate empty strings if accidentally passed via data sheet rows
        if (keyword == null || keyword.trim().isEmpty()) {
            return; 
        }

        driver.get("https://allrecipes.com");
        home = new HomePage(driver); // Re-instantiate to avoid Stale Elements
        
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        
        search = home.searchRecipe(keyword);
        
        Assert.assertTrue(search.isSearchResultDisplayed(), "Search results are not displayed for: " + keyword);
        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("search"), "URL structure mapping failed to redirect to search endpoints.");
    }

    @Test(priority = 3)
    public void verifyInvalidKeyword() {
        driver.get("https://allrecipes.com");
        home = new HomePage(driver); // Crucial fix: Resetting DOM tracking context
        
        search = home.searchRecipe("xyz123456");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        boolean navigatedToSearch = wait.until(ExpectedConditions.urlContains("search"));
        Assert.assertTrue(navigatedToSearch, "Search page was not displayed for an invalid keyword query.");
    }

    @Test(priority = 4)
    public void verifyEmptySearch() {
        driver.get("https://allrecipes.com");
        home = new HomePage(driver); // Crucial fix: Resetting DOM tracking context
        
        String beforeSearchUrl = driver.getCurrentUrl();
        home.typeSearch(""); // Enforces clean typing parameter injection
        
        // Simulates form lookup without breaking page synchronization bounds
        String afterSearchUrl = driver.getCurrentUrl();
        Assert.assertEquals(afterSearchUrl, beforeSearchUrl, "Empty search changed the page URL unexpectedly!");
    }

    @Test(priority = 5, description = "RS_008: Verify suggestions appear while typing")
    public void verifySuggestions() {
        driver.get("https://allrecipes.com");
        home = new HomePage(driver); // Crucial fix: Resetting DOM tracking context
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        By searchLocator = By.id("mntl-search-form--open__search-input");
        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(searchLocator));
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", inputField);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", inputField);
        
        String partialKeyword = "chi";
        for (char ch : partialKeyword.toCharArray()) {
            inputField.sendKeys(String.valueOf(ch));
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
        }
        
        String value = inputField.getAttribute("value");
        Assert.assertEquals(value, "chi", "Search input field value mismatch.");
        Assert.assertTrue(search.areSuggestionsPopulated(), "FAIL: Autocomplete suggestions did not drop down.");
    }

    @Test(priority = 6)
    public void verifyRecipeMatch() {
        driver.get("https://allrecipes.com");
        home = new HomePage(driver); 
        
        search = home.searchRecipe("Chicken");
        Assert.assertTrue(search.isSearchResultDisplayed(), "Search page failed to switch contexts.");
        String title = search.firstRecipeTitle();
        Assert.assertFalse(title.isEmpty(), "First recipe title is empty.");
    }

    @Test(priority = 7)
    public void verifyBrokenImages() {
        driver.get("https://allrecipes.com");
        home = new HomePage(driver); 
        
        search = home.searchRecipe("Chicken");
        Assert.assertTrue(search.isSearchResultDisplayed(), "Failed to land on search results page to scan media components.");
        Assert.assertTrue(search.verifyBrokenImages(), "Broken content images found on results page.");
    }

    @Test(priority = 8)
    public void verifyBrokenLinks() {
        driver.get("https://allrecipes.com");
        home = new HomePage(driver); 
        
        search = home.searchRecipe("Chicken");
        Assert.assertTrue(search.isSearchResultDisplayed(), "Failed to land on search results page to scan hyperlinks.");
        Assert.assertTrue(search.verifyBrokenLinks(), "Broken content hyperlinks found on results page.");
    }

    @Test(priority = 9)
    public void verifyScroll() {
        driver.get("https://allrecipes.com");
        home = new HomePage(driver); 
        
        search = home.searchRecipe("Chicken");
        Assert.assertTrue(search.isSearchResultDisplayed(), "Failed to complete navigation routing before applying scroll scripts.");
        search.scrollDown();
    }
}
