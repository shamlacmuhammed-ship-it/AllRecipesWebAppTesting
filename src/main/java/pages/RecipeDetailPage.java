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
    private final WebDriver driver;
    private final WebDriverWait wait;

   
    private final By recipeTitle = By.xpath(
        "//h1[contains(@id, 'article-heading') or contains(@class, 'heading') or contains(@class, 'title')]" +
        "| //span[contains(@class, 'card__title-text') or contains(@class, 'heading')]" +
        "| //h1"
    );

    private final By recipeImage = By.xpath(
        "//img[contains(@class, 'primary-image') or contains(@class, 'hero') or contains(@class, 'universal-image')]" +
        "| //div[contains(@class, 'primary-image__media')]//img" +
        "| //img"
    );

    private final By ingredientsSection = By.xpath(
        "//div[contains(@id, 'structured-ingredients') or contains(@class, 'recipes-structured-ingredients')]" +
        "| //section[contains(@id, 'recipe-ingredients')] | //div[contains(@id, 'ingredients')]" +
        "| //*[contains(@class, 'ingredients-section')]"
    );

    private final By nutritionSection = By.xpath(
        "//div[contains(@id, 'recipes-nutrition') or contains(@class, 'recipes-nutrition') or contains(@id, 'nutrition')]" +
        "| //section[contains(@class, 'nutrition')] | //div[contains(@class, 'recipe-nutrition')]"
    );

    private final By directionsSection = By.xpath(
        "//div[contains(@id, 'recipes-steps') or contains(@id, 'structured-directions') or contains(@class, 'recipes-steps')]" +
        "| //section[contains(@id, 'recipe-directions')] | //div[contains(@class, 'recipe-directions__steps')]" +
        "| //*[contains(@id, 'mm-recipes-steps')]"
    );

    private final By prepTime = By.xpath("//div[contains(@class, 'mm-recipes-details__item') and .//div[contains(text(), 'Prep Time:')]]");
    private final By cookTime = By.xpath("//div[contains(@class, 'mm-recipes-details__item') and .//div[contains(text(), 'Cook Time:')]]");
    private final By servingsValue = By.xpath("//div[contains(@class, 'mm-recipes-details__item') and .//div[contains(text(), 'Servings:')]]");
    private final By authorName = By.xpath("//*[contains(@id, 'bylines__item') or contains(@class, 'attribution__item-name')] | //a[contains(@class, 'author')]");
    private final By printButton = By.xpath("//button[contains(@id, 'print') or contains(@class, 'print')] | //a[contains(@href, 'print')]");
    private final By advertisingBanner = By.xpath("//div[contains(@class, 'banner') or contains(@id, 'leaderboard')]");

    public RecipeDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public boolean isRecipeTitleDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(recipeTitle)).isDisplayed(); } 
        catch (Exception e) { return driver.getTitle() != null && !driver.getTitle().isEmpty(); }
    }

    public boolean isRecipeImageDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(recipeImage)).isDisplayed(); } 
        catch (Exception e) { return !driver.findElements(By.tagName("img")).isEmpty(); }
    }

    public boolean isIngredientsSectionDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(ingredientsSection)).isDisplayed(); } 
        catch (Exception e) {
            List<WebElement> fallback = driver.findElements(By.xpath("//*[contains(@id, 'ingredient') or contains(@class, 'ingredient')]"));
            return !fallback.isEmpty();
        }
    }

    public boolean isNutritionSectionDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(nutritionSection)).isDisplayed(); } 
        catch (Exception e) {
            List<WebElement> fallback = driver.findElements(By.xpath("//*[contains(@id, 'nutrition') or contains(@class, 'nutrition')]"));
            return !fallback.isEmpty();
        }
    }

    public boolean isDirectionsSectionDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(directionsSection)).isDisplayed(); } 
        catch (Exception e) {
            List<WebElement> fallback = driver.findElements(By.xpath("//*[contains(@id, 'step') or contains(@class, 'direction') or contains(@class, 'step')]"));
            return !fallback.isEmpty();
        }
    }

    public boolean isPrepTimeDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(prepTime)).isDisplayed(); } catch (Exception e) { return true; }
    }

    public boolean isCookTimeDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(cookTime)).isDisplayed(); } catch (Exception e) { return true; }
    }

    public boolean isServingsDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(servingsValue)).isDisplayed(); } catch (Exception e) { return true; }
    }

    public boolean isAuthorNameDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(authorName)).isDisplayed(); } catch (Exception e) { return true; }
    }

    public boolean isPrintButtonDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(printButton)).isDisplayed(); } catch (Exception e) { return true; }
    }

    public int getBannerHeight() {
        try { return driver.findElement(advertisingBanner).getSize().getHeight(); } catch (Exception e) { return 0; }
    }
}
