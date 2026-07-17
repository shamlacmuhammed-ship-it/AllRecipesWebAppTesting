package tests;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.RecipeDetailPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

// Test class for Recipe Detail Page
public class RecipeDetailTest extends BaseClass {

    // Page Object reference
    private RecipeDetailPage recipePage;

    // ===================================================
    // Data Provider
    // Reads Recipe URLs from Excel
    // ===================================================
    @DataProvider(name = "ExcelRecipeCardsProvider")
    public Object[][] getRecipeUrlsFromExcel() throws IOException {

        // Excel file path
        String excelPath = System.getProperty("user.dir")
                + "/src/main/resources/testdata/TestData.xlsx";

        FileInputStream fis = new FileInputStream(excelPath);
        Workbook workbook = new XSSFWorkbook(fis);

        // Read RecipeLinks sheet
        Sheet sheet = workbook.getSheet("RecipeLinks");

        int totalRows = sheet.getLastRowNum();

        List<Object[]> validUrls = new ArrayList<>();

        // Read all recipe URLs
        for (int i = 1; i <= totalRows; i++) {

            Row row = sheet.getRow(i);

            if (row != null) {

                Cell cell = row.getCell(0);

                if (cell != null) {

                    String url = cell.toString().trim();

                    // Ignore empty rows
                    if (!url.isEmpty()) {

                        validUrls.add(new Object[]{url});
                    }
                }
            }
        }

        workbook.close();
        fis.close();

        // Convert List into Object[][] for TestNG DataProvider
        Object[][] data = new Object[validUrls.size()][1];

        for (int i = 0; i < validUrls.size(); i++) {

            data[i][0] = validUrls.get(i)[0];
        }

        return data;
    }

    // ===================================================
    // REC_001, REC_002, REC_004, REC_005, REC_006
    // Verify Recipe Content
    // ===================================================
    @Test(dataProvider = "ExcelRecipeCardsProvider", priority = 1)
    public void verifyRecipeContentProfile(String recipeUrl) {

        // Open recipe page
        driver.get(recipeUrl);

        // Wait until page completely loads
        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        recipePage = new RecipeDetailPage(driver);

        // Verify recipe title
        Assert.assertTrue(recipePage.isRecipeTitleDisplayed());

        // Verify recipe image
        Assert.assertTrue(recipePage.isRecipeImageDisplayed());

        // Verify ingredients section
        Assert.assertTrue(recipePage.isIngredientsSectionDisplayed());

        // Verify nutrition section
        Assert.assertTrue(recipePage.isNutritionSectionDisplayed());

        // Verify cooking directions
        Assert.assertTrue(recipePage.isDirectionsSectionDisplayed());
    }

    // ===================================================
    // REC_009, REC_010, REC_012, REC_013, REC_014
    // Verify Recipe Utility Information
    // ===================================================
    @Test(dataProvider = "ExcelRecipeCardsProvider", priority = 2)
    public void verifyRecipeTimingAndUtilityOptions(String recipeUrl) {

        driver.get(recipeUrl);

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        recipePage = new RecipeDetailPage(driver);

        // Verify preparation time
        Assert.assertTrue(recipePage.isPrepTimeDisplayed());

        // Verify cooking time
        Assert.assertTrue(recipePage.isCookTimeDisplayed());

        // Verify servings
        Assert.assertTrue(recipePage.isServingsDisplayed());

        // Verify author name
        Assert.assertTrue(recipePage.isAuthorNameDisplayed());

        // Verify print button
        Assert.assertTrue(recipePage.isPrintButtonDisplayed());
    }

    // ===================================================
    // REC_008
    // Verify Ratings Display
    // ===================================================
    @Test(dataProvider = "ExcelRecipeCardsProvider", priority = 3)
    public void verifyRecipeRatingsAreDisplayed(String recipeUrl) {

        driver.get(recipeUrl);

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        // Locate rating section
        List<WebElement> ratings =
                driver.findElements(By.xpath(
                        "//div[contains(@id,'review-bar__star-rating')]"));

        boolean isRatingVisible =
                !ratings.isEmpty()
                        && ratings.get(0).isDisplayed();

        // Verify rating section
        Assert.assertTrue(isRatingVisible,
                "Recipe ratings are not displayed.");
    }

    // ===================================================
    // REC_016
    // Verify Advertisement Banner Height
    // ===================================================
    @Test(dataProvider = "ExcelRecipeCardsProvider", priority = 4)
    public void verifyAdvertisingBannerLayout(String recipeUrl) {

        driver.get(recipeUrl);

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        recipePage = new RecipeDetailPage(driver);

        // Get banner height
        int bannerHeight =
                recipePage.getBannerHeight();

        // Verify advertisement banner is not oversized
        boolean isLayoutClean =
                bannerHeight < 200;

        Assert.assertTrue(isLayoutClean,
                "Advertisement banner occupies too much space.");
    }
}
