package tests;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.HomePage;
import pages.MyRecipesPage;

// Test class for validating My Recipes page functionality
public class MyRecipesTest extends BaseClass {

    // Page Object references
    HomePage home;
    MyRecipesPage myRecipes;

    // Execute before every test method
    @BeforeMethod
    public void setupPage() {

        // Open the Allrecipes homepage
        driver.get("https://www.allrecipes.com");

        // Initialize Home Page object
        home = new HomePage(driver);

        // Store current browser window
        String parent = driver.getWindowHandle();

        // Click My Recipes link
        home.clickMyRecipes();

        // Wait until a new browser tab opens
        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

        wait.until(d -> d.getWindowHandles().size() > 1);

        // Switch to the newly opened tab
        for (String tab : driver.getWindowHandles()) {

            if (!tab.equals(parent)) {

                driver.switchTo().window(tab);
                break;
            }
        }

        // Wait until My Recipes page is loaded
        wait.until(ExpectedConditions.urlContains("myrecipes"));

        // Initialize My Recipes Page object
        myRecipes = new MyRecipesPage(driver);
    }

    // ===========================================
    // MR_001
    // Verify Search Bar
    // ===========================================
    @Test(priority = 1)
    public void verifySearchBar() {

        Assert.assertTrue(myRecipes.isSearchDisplayed());
    }

    // ===========================================
    // MR_002
    // Verify Login Button
    // ===========================================
    @Test(priority = 2)
    public void verifyLoginButton() {

        Assert.assertTrue(myRecipes.isLoginDisplayed());
    }

    // ===========================================
    // MR_003
    // Verify Join Button
    // ===========================================
    @Test(priority = 3)
    public void verifyJoinButton() {

        Assert.assertTrue(myRecipes.isJoinDisplayed());
    }

    // ===========================================
    // MR_004
    // Verify Hero Heading
    // ===========================================
    @Test(priority = 4)
    public void verifyHeroHeading() {

        Assert.assertTrue(
                myRecipes.isHeadingDisplayed(),
                "Hero heading is not displayed.");
    }

    // ===========================================
    // MR_005
    // Verify Recipe Search
    // ===========================================
    @Test(priority = 5)
    public void verifyRecipeSearch() {

        // Search for a recipe
        myRecipes.searchRecipe("Chicken");

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until search results page opens
        wait.until(ExpectedConditions.urlContains("search"));

        // Verify search page
        Assert.assertTrue(driver.getCurrentUrl().contains("search"));
    }

    // ===========================================
    // MR_006
    // Verify Login Navigation
    // ===========================================
    @Test(priority = 6)
    public void verifyLoginNavigation() {

        // Click Login button
        myRecipes.clickLogin();

        // Verify login page is opened
        Assert.assertTrue(
                driver.getCurrentUrl().contains("login")
                        || driver.getCurrentUrl().contains("authentication")
                        || driver.getCurrentUrl().contains("identity"));
    }

    // ===========================================
    // MR_007
    // Verify Join Navigation
    // ===========================================
    @Test(priority = 7)
    public void verifyJoinNavigation() {

        // Skip test if Join button is unavailable
        if (!myRecipes.isJoinDisplayed()) {

            System.out.println(
                    "Join button is not available on current website.");
            return;
        }

        // Click Join button
        myRecipes.clickJoin();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait until registration page opens
        boolean redirected =
                wait.until(ExpectedConditions.or(

                        ExpectedConditions.urlContains("signup"),
                        ExpectedConditions.urlContains("register"),
                        ExpectedConditions.urlContains("identity"),
                        ExpectedConditions.urlContains("authentication")
                ));

        // Verify successful navigation
        Assert.assertTrue(
                redirected,
                "Join page was not opened.");
    }
}
