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

public class HomeTest extends BaseClass {

    private WebDriverWait wait;

    @BeforeMethod
    public void init() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // HP_001 & HP_002
    @Test(priority = 1)
    public void verifyHomepageLoadingAndMetadata() {

        wait.until(ExpectedConditions.titleContains("Allrecipes"));

        Assert.assertTrue(driver.getCurrentUrl().contains("allrecipes.com"));
        Assert.assertFalse(driver.getTitle().isEmpty());
    }

    // HP_003 & HP_004
    @Test(priority = 2)
    public void verifyCoreHeaderElements() {

        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[local-name()='svg' and contains(@class,'icon-logo')]")));

        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mntl-search-form--open__search-input")));

        WebElement nav = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//nav[contains(@id,'header-nav')]")));

        Assert.assertTrue(logo.isDisplayed());
        Assert.assertTrue(search.isDisplayed());
        Assert.assertTrue(nav.isDisplayed());
    }

    // HP_005
    @Test(priority = 3)
    public void verifyHeaderMenusContent() {

        List<WebElement> menus =
                driver.findElements(By.xpath("//nav[contains(@id,'header-nav')]//a"));

        Assert.assertTrue(menus.size() >= 5,
                "Header menu items are missing.");
    }

    // HP_006
    @Test(priority = 4)
    public void verifySearchBarDisplayed() {

        WebElement search =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("mntl-search-form--open__search-input")));

        Assert.assertTrue(search.isDisplayed());
    }

    // HP_007
    @Test(priority = 5)
    public void verifyHomepageImagesLoad() {

        List<WebElement> images = driver.findElements(By.tagName("img"));

        Assert.assertFalse(images.isEmpty(), "No images found.");

        for (WebElement img : images) {

            String src = img.getAttribute("src");

            if (src == null || src.isBlank()) {
                src = img.getAttribute("data-src");
            }

            Assert.assertNotNull(src, "Image source is missing.");
            Assert.assertFalse(src.trim().isEmpty(), "Image source is empty.");
        }
    }

    // HP_008
    @Test(priority = 6)
    public void verifyHomepageContentSections() {

        WebElement main =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("main")));

        Assert.assertTrue(main.isDisplayed());
    }

    // HP_009
    @Test(priority = 7)
    public void verifyHomepageScrolling() {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("window.scrollTo(0,document.body.scrollHeight)");

        Long scroll =
                ((Number) js.executeScript("return window.pageYOffset")).longValue();

        Assert.assertTrue(scroll > 0);
    }

    // HP_010
    @Test(priority = 8)
    public void verifyBrowserNavigation() {

        driver.navigate().refresh();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mntl-search-form--open__search-input")));

        Assert.assertTrue(driver.getTitle().contains("Allrecipes"));
    }

    // HP_011
    @Test(priority = 9)
    public void verifyMyRecipesMenuAvailable() {

        List<WebElement> accountIcon = driver.findElements(By.cssSelector(
            "a[href*='authentication'], a[href*='my-recipes']"));

        Assert.assertFalse(accountIcon.isEmpty(),
                "Account/My Recipes icon is not displayed.");
    }

    // HP_012
    @Test(priority = 10)
    public void verifyNewsletterLinkAvailable() {

        List<WebElement> newsletter =
                driver.findElements(By.xpath("//a[contains(.,'Newsletters')]"));

        Assert.assertFalse(newsletter.isEmpty(),
                "Newsletter link not found.");
    }

    // HP_013
    @Test(priority = 11)
    public void verifySweepstakesLinkAvailable() {

        List<WebElement> sweepstakes =
                driver.findElements(By.xpath("//a[contains(.,'Sweepstakes')]"));

        Assert.assertFalse(sweepstakes.isEmpty(),
                "Sweepstakes link not found.");
    }

    // HP_014
    @Test(priority = 12)
    public void verifyFooterDisplayed() {

        WebElement footer =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("footer")));

        Assert.assertTrue(footer.isDisplayed());
    }
}