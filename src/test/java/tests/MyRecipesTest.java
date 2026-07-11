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

public class MyRecipesTest extends BaseClass {

    HomePage home;
    MyRecipesPage myRecipes;

    @BeforeMethod
    public void setupPage() {

        driver.get("https://www.allrecipes.com");

        home = new HomePage(driver);

        String parent = driver.getWindowHandle();

        home.clickMyRecipes();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

        wait.until(d -> d.getWindowHandles().size() > 1);

        for(String tab : driver.getWindowHandles()){

            if(!tab.equals(parent)){

                driver.switchTo().window(tab);

                break;
            }
        }

        wait.until(ExpectedConditions.urlContains("myrecipes"));

        myRecipes = new MyRecipesPage(driver);

    }
    @Test(priority = 1)
    public void verifySearchBar() {
        Assert.assertTrue(myRecipes.isSearchDisplayed());
    }

    @Test(priority = 2)
    public void verifyLoginButton() {
        Assert.assertTrue(myRecipes.isLoginDisplayed());
    }

    @Test(priority = 3)
    public void verifyJoinButton() {
        Assert.assertTrue(myRecipes.isJoinDisplayed());
    }

    @Test(priority = 4)
    public void verifyHeroHeading() {

        Assert.assertTrue(
                myRecipes.isHeadingDisplayed(),
                "Hero heading is not displayed.");
    }
    @Test(priority = 5)
    public void verifyRecipeSearch() {

        myRecipes.searchRecipe("Chicken");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.urlContains("search"));

        Assert.assertTrue(driver.getCurrentUrl().contains("search"));
    }

    @Test(priority = 6)
    public void verifyLoginNavigation() {

        myRecipes.clickLogin();

        Assert.assertTrue(driver.getCurrentUrl().contains("login")
                || driver.getCurrentUrl().contains("authentication")
                || driver.getCurrentUrl().contains("identity"));
    }

    @Test(priority = 7)
    public void verifyJoinNavigation() {

        if (!myRecipes.isJoinDisplayed()) {

            System.out.println("Join button is not available on current website.");
            return;
        }

        myRecipes.clickJoin();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        boolean redirected = wait.until(ExpectedConditions.or(

                ExpectedConditions.urlContains("signup"),
                ExpectedConditions.urlContains("register"),
                ExpectedConditions.urlContains("identity"),
                ExpectedConditions.urlContains("authentication")
        ));

        Assert.assertTrue(redirected,
                "Join page was not opened.");
    }

}