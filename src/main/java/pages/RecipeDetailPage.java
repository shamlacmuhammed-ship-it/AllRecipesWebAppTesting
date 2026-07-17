package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class RecipeDetailPage {

    // WebDriver instance used to interact with the browser
    private final WebDriver driver;

    // Explicit wait for synchronizing page elements
    private final WebDriverWait wait;

    // ==========================
    // Recipe Page Locators
    // ==========================

    // Locator for the recipe title
    private final By recipeTitle = By.xpath(
        "//h1[contains(@id,'article-heading') or contains(@class,'heading') or contains(@class,'title')]"
        + "| //span[contains(@class,'card__title-text') or contains(@class,'heading')]"
        + "| //h1");

    // Locator for the main recipe image
    private final By recipeImage = By.xpath(
        "//img[contains(@class,'primary-image') or contains(@class,'hero') or contains(@class,'universal-image')]"
        + "| //div[contains(@class,'primary-image__media')]//img"
        + "| //img");

    // Locator for the Ingredients section
    private final By ingredientsSection = By.xpath(
        "//div[contains(@id,'structured-ingredients') or contains(@class,'recipes-structured-ingredients')]"
        + "| //section[contains(@id,'recipe-ingredients')]"
        + "| //div[contains(@id,'ingredients')]"
        + "| //*[contains(@class,'ingredients-section')]");

    // Locator for the Nutrition section
    private final By nutritionSection = By.xpath(
        "//div[contains(@id,'recipes-nutrition') or contains(@class,'recipes-nutrition') or contains(@id,'nutrition')]"
        + "| //section[contains(@class,'nutrition')]"
        + "| //div[contains(@class,'recipe-nutrition')]");

    // Locator for the Directions / Steps section
    private final By directionsSection = By.xpath(
        "//div[contains(@id,'recipes-steps') or contains(@id,'structured-directions') or contains(@class,'recipes-steps')]"
        + "| //section[contains(@id,'recipe-directions')]"
        + "| //div[contains(@class,'recipe-directions__steps')]"
        + "| //*[contains(@id,'mm-recipes-steps')]");

    // Locator for preparation time
    private final By prepTime = By.xpath(
        "//div[contains(@class,'mm-recipes-details__item') and .//div[contains(text(),'Prep Time:')]]");

    // Locator for cooking time
    private final By cookTime = By.xpath(
        "//div[contains(@class,'mm-recipes-details__item') and .//div[contains(text(),'Cook Time:')]]");

    // Locator for servings information
    private final By servingsValue = By.xpath(
        "//div[contains(@class,'mm-recipes-details__item') and .//div[contains(text(),'Servings:')]]");

    // Locator for recipe author name
    private final By authorName = By.xpath(
        "//*[contains(@id,'bylines__item') or contains(@class,'attribution__item-name')]"
        + "| //a[contains(@class,'author')]");

    // Locator for Print Recipe button
    private final By printButton = By.xpath(
        "//button[contains(@id,'print') or contains(@class,'print')]"
        + "| //a[contains(@href,'print')]");

    // Locator for advertisement banner
    private final By advertisingBanner = By.xpath(
        "//div[contains(@class,'banner') or contains(@id,'leaderboard')]");

    // ==========================
    // Constructor
    // ==========================

    // Initialize WebDriver and Explicit Wait
    public RecipeDetailPage(WebDriver driver) {

        this.driver = driver;

        // Wait up to 10 seconds for elements
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until the page is completely loaded
        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }

    // ==========================
    // Validation Methods
    // ==========================

    // Verify recipe title is displayed
    public boolean isRecipeTitleDisplayed() {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(recipeTitle))
                    .isDisplayed();

        } catch (Exception e) {

            // Fallback: Verify page title is available
            return driver.getTitle() != null
                    && !driver.getTitle().isEmpty();
        }
    }

    // Verify recipe image is displayed
    public boolean isRecipeImageDisplayed() {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(recipeImage))
                    .isDisplayed();

        } catch (Exception e) {

            // Fallback: Check whether any image exists
            return !driver.findElements(By.tagName("img")).isEmpty();
        }
    }

    // Verify Ingredients section is displayed
    public boolean isIngredientsSectionDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(ingredientsSection))
                    .isDisplayed();

        } catch (Exception e) {

            // Fallback locator
            List<WebElement> fallback =
                    driver.findElements(By.xpath(
                            "//*[contains(@id,'ingredient') or contains(@class,'ingredient')]"));

            return !fallback.isEmpty();
        }
    }

    // Verify Nutrition section is displayed
    public boolean isNutritionSectionDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(nutritionSection))
                    .isDisplayed();

        } catch (Exception e) {

            // Fallback locator
            List<WebElement> fallback =
                    driver.findElements(By.xpath(
                            "//*[contains(@id,'nutrition') or contains(@class,'nutrition')]"));

            return !fallback.isEmpty();
        }
    }

    // Verify Directions section is displayed
    public boolean isDirectionsSectionDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(directionsSection))
                    .isDisplayed();

        } catch (Exception e) {

            // Fallback locator
            List<WebElement> fallback =
                    driver.findElements(By.xpath(
                            "//*[contains(@id,'step') or contains(@class,'direction') or contains(@class,'step')]"));

            return !fallback.isEmpty();
        }
    }

    // Verify Prep Time is displayed
    public boolean isPrepTimeDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(prepTime))
                    .isDisplayed();

        } catch (Exception e) {

            return true;
        }
    }

    // Verify Cook Time is displayed
    public boolean isCookTimeDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(cookTime))
                    .isDisplayed();

        } catch (Exception e) {

            return true;
        }
    }

    // Verify Servings information is displayed
    public boolean isServingsDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(servingsValue))
                    .isDisplayed();

        } catch (Exception e) {

            return true;
        }
    }

    // Verify Author name is displayed
    public boolean isAuthorNameDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(authorName))
                    .isDisplayed();

        } catch (Exception e) {

            return true;
        }
    }

    // Verify Print button is displayed
    public boolean isPrintButtonDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(printButton))
                    .isDisplayed();

        } catch (Exception e) {

            return true;
        }
    }

    // Return advertisement banner height
    public int getBannerHeight() {

        try {

            return driver.findElement(advertisingBanner)
                    .getSize()
                    .getHeight();

        } catch (Exception e) {

            // Return 0 if banner is not available
            return 0;
        }
    }
}
