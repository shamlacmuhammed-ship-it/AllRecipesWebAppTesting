package tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseClass;

// Test class for validating Allrecipes Home Page
public class HomeTest extends BaseClass {

    // Explicit Wait object
    private WebDriverWait wait;

    // Initialize Explicit Wait before every test
    @BeforeMethod
    public void init() {

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===================================================
    // HP_001 & HP_002
    // Verify Homepage URL and Page Title
    // ===================================================
    @Test(priority = 1)
    public void verifyHomepageLoadingAndMetadata() {

        // Wait until page title contains "Allrecipes"
        wait.until(ExpectedConditions.titleContains("Allrecipes"));

        // Verify URL
        Assert.assertTrue(driver.getCurrentUrl().contains("allrecipes.com"));

        // Verify title is not empty
        Assert.assertFalse(driver.getTitle().isEmpty());
    }

    // ===================================================
    // HP_003 & HP_004
    // Verify Header Elements
    // ===================================================
    @Test(priority = 2)
    public void verifyCoreHeaderElements() {

        // Locate website logo
        WebElement logo =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[local-name()='svg' and contains(@class,'icon-logo')]")));

        // Locate search box
        WebElement search =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("mntl-search-form--open__search-input")));

        // Locate navigation menu
        WebElement nav =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//nav[contains(@id,'header-nav')]")));

        // Verify all elements are displayed
        Assert.assertTrue(logo.isDisplayed());
        Assert.assertTrue(search.isDisplayed());
        Assert.assertTrue(nav.isDisplayed());
    }

    // ===================================================
    // HP_005
    // Verify Header Menu Items
    // ===================================================
    @Test(priority = 3)
    public void verifyHeaderMenusContent() {

        List<WebElement> menus =
                driver.findElements(
                        By.xpath("//nav[contains(@id,'header-nav')]//a"));

        // Verify minimum number of menu items
        Assert.assertTrue(menus.size() >= 5,
                "Header menu items are missing.");
    }

    // ===================================================
    // HP_006
    // Verify Search Bar
    // ===================================================
    @Test(priority = 4)
    public void verifySearchBarDisplayed() {

        WebElement search =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("mntl-search-form--open__search-input")));

        Assert.assertTrue(search.isDisplayed());
    }

    // ===================================================
    // HP_007
    // Verify Homepage Images
    // ===================================================
    @Test(priority = 5)
    public void verifyHomepageImagesLoad() {

        // Get all image elements
        List<WebElement> images =
                driver.findElements(By.tagName("img"));

        Assert.assertFalse(images.isEmpty(),
                "No images found.");

        for (WebElement img : images) {

            String src = img.getAttribute("src");

            // Fallback for lazy-loaded images
            if (src == null || src.isBlank()) {

                src = img.getAttribute("data-src");
            }

            // Verify image source
            Assert.assertNotNull(src,
                    "Image source is missing.");

            Assert.assertFalse(src.trim().isEmpty(),
                    "Image source is empty.");
        }
    }

    // ===================================================
    // HP_008
    // Verify Homepage Content
    // ===================================================
    @Test(priority = 6)
    public void verifyHomepageContentSections() {

        // Verify main content section
        WebElement main =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("main")));

        Assert.assertTrue(main.isDisplayed());
    }

    // ===================================================
    // HP_009
    // Verify Page Scrolling
    // ===================================================
    @Test(priority = 7)
    public void verifyHomepageScrolling() {

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        // Scroll to bottom
        js.executeScript(
                "window.scrollTo(0,document.body.scrollHeight)");

        // Get vertical scroll position
        Long scroll =
                ((Number) js.executeScript(
                        "return window.pageYOffset"))
                        .longValue();

        Assert.assertTrue(scroll > 0);
    }

    // ===================================================
    // HP_010
    // Verify Browser Refresh
    // ===================================================
    @Test(priority = 8)
    public void verifyBrowserNavigation() {

        // Refresh page
        driver.navigate().refresh();

        // Wait for search box
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mntl-search-form--open__search-input")));

        // Verify page title
        Assert.assertTrue(driver.getTitle().contains("Allrecipes"));
    }

    // ===================================================
    // HP_011
    // Verify My Recipes Menu
    // ===================================================
    @Test(priority = 9)
    public void verifyMyRecipesMenuAvailable() {

        List<WebElement> accountIcon =
                driver.findElements(
                        By.cssSelector(
                                "a[href*='authentication'], a[href*='my-recipes']"));

        Assert.assertFalse(accountIcon.isEmpty(),
                "Account/My Recipes icon is not displayed.");
    }

    // ===================================================
    // HP_012
    // Verify Newsletter Link
    // ===================================================
    @Test(priority = 10)
    public void verifyNewsletterLinkAvailable() {

        List<WebElement> newsletter =
                driver.findElements(
                        By.xpath("//a[contains(.,'Newsletters')]"));

        Assert.assertFalse(newsletter.isEmpty(),
                "Newsletter link not found.");
    }

    // ===================================================
    // HP_013
    // Verify Sweepstakes Link
    // ===================================================
    @Test(priority = 11)
    public void verifySweepstakesLinkAvailable() {

        List<WebElement> sweepstakes =
                driver.findElements(
                        By.xpath("//a[contains(.,'Sweepstakes')]"));

        Assert.assertFalse(sweepstakes.isEmpty(),
                "Sweepstakes link not found.");
    }

    // ===================================================
    // HP_014
    // Verify Footer
    // ===================================================
    @Test(priority = 12)
    public void verifyFooterDisplayed() {

        WebElement footer =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("footer")));

        Assert.assertTrue(footer.isDisplayed());
    }
}
