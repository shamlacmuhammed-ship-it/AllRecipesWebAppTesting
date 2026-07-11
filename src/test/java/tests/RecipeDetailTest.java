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

public class RecipeDetailTest extends BaseClass {
    private RecipeDetailPage recipePage;

    @DataProvider(name = "ExcelRecipeCardsProvider")
    public Object[][] getRecipeUrlsFromExcel() throws IOException {
        String excelPath = System.getProperty("user.dir") + "/src/main/resources/testdata/TestData.xlsx";
        FileInputStream fis = new FileInputStream(excelPath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet("RecipeLinks");
        
        int totalRows = sheet.getLastRowNum();
        List<Object[]> validUrls = new ArrayList<>();

        for (int i = 1; i <= totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    String url = cell.toString().trim();
                    if (!url.isEmpty()) {
                        validUrls.add(new Object[]{ url });
                    }
                }
            }
        }
        workbook.close();
        fis.close();

        // Transforms structural list values directly into clean TestNG executable matrices
        Object[][] data = new Object[validUrls.size()][1];
        for (int i = 0; i < validUrls.size(); i++) {
            data[i][0] = validUrls.get(i)[0];
        }
        return data;
    }
    
    @Test(dataProvider = "ExcelRecipeCardsProvider", priority = 1)
    public void verifyRecipeContentProfile(String recipeUrl) {
        driver.get(recipeUrl);
        
        // Document Boundary Synchronization: Stops DOM tracing until asset networks finalize parsing
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState")
                .equals("complete"));

        this.recipePage = new RecipeDetailPage(driver);

        // Standardized Framework Assertions (REC_001, REC_002, REC_004, REC_005, REC_006)
        Assert.assertTrue(recipePage.isRecipeTitleDisplayed(), "Structural assertion failed: Recipe title heading text layout is missing or unrendered on domain layout target: " + recipeUrl);
        Assert.assertTrue(recipePage.isRecipeImageDisplayed(), "Structural assertion failed: Core hero media banner is missing or unrendered: " + recipeUrl);
        Assert.assertTrue(recipePage.isIngredientsSectionDisplayed(), "Structural assertion failed: Recipe ingredients node mapping wrapper container is missing: " + recipeUrl);
        Assert.assertTrue(recipePage.isNutritionSectionDisplayed(), "Structural assertion failed: Nutritional data panel section is missing: " + recipeUrl);
        Assert.assertTrue(recipePage.isDirectionsSectionDisplayed(), "Structural assertion failed: Cooking directions layout steps canvas is missing: " + recipeUrl);
    }

    @Test(dataProvider = "ExcelRecipeCardsProvider", priority = 2)
    public void verifyRecipeTimingAndUtilityOptions(String recipeUrl) {
        driver.get(recipeUrl);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState")
                .equals("complete"));

        this.recipePage = new RecipeDetailPage(driver);

        // Core Utility Footprint Verifications (REC_009, REC_010, REC_012, REC_013, REC_014)
        Assert.assertTrue(recipePage.isPrepTimeDisplayed(), "Utility option missing: Preparation countdown metrics missing: " + recipeUrl);
        Assert.assertTrue(recipePage.isCookTimeDisplayed(), "Utility option missing: Cooking active processing time metrics missing: " + recipeUrl);
        Assert.assertTrue(recipePage.isServingsDisplayed(), "Utility option missing: Output yield portion serving quantities missing: " + recipeUrl);
        Assert.assertTrue(recipePage.isAuthorNameDisplayed(), "Utility option missing: Publisher profile signature line element is missing: " + recipeUrl);
        Assert.assertTrue(recipePage.isPrintButtonDisplayed(), "Utility option missing: Media conversion action control print element is missing: " + recipeUrl);
    }

    @Test(dataProvider = "ExcelRecipeCardsProvider", priority = 3)
    public void verifyRecipeRatingsAreDisplayed(String recipeUrl) {
        driver.get(recipeUrl);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState")
                .equals("complete"));
        
        List<WebElement> ratings = driver.findElements(By.xpath("//div[contains(@id, 'review-bar__star-rating')]"));
        boolean isRatingVisible = !ratings.isEmpty() && ratings.get(0).isDisplayed();
        
        // REC_008: Controlled boundary verification failure targeting intentional structural defect tracking
        Assert.assertTrue(isRatingVisible, "DEFECT TRACKING [REC_008]: Review and user evaluation bar interface component hidden or missing inside layout view canvas: " + recipeUrl);
    }

    @Test(dataProvider = "ExcelRecipeCardsProvider", priority = 4)
    public void verifyAdvertisingBannerLayout(String recipeUrl) {
        driver.get(recipeUrl);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState")
                .equals("complete"));
                
        this.recipePage = new RecipeDetailPage(driver);
        
        int bannerHeight = recipePage.getBannerHeight();
        boolean isLayoutClean = bannerHeight < 200;

        // REC_016: Controlled boundary verification failure tracking layout space optimization degradation
        Assert.assertTrue(isLayoutClean, "DEFECT TRACKING [REC_016]: Aggressive marketing container boundaries generated view displacement over recipe content spaces: " + recipeUrl);
    }
}
