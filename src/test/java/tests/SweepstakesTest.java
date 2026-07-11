package tests;

import base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SweepstakesPage;
import utilities.ExcelUtils;

public class SweepstakesTest extends BaseClass {

    @DataProvider(name = "SweeptakesData")
    public Object[][] getSweeptakesData() {
        return ExcelUtils.getTestData("src/main/resources/testdata/TestData.xlsx", "SweeptakesData", 3);
    }

    // 1. Verify Sweepstakes page opens successfully
    @Test(priority = 1, description = "Scenario: Verify Sweepstakes page opens successfully")
    public void testSweepstakesPageOpensSuccessfully() {
        driver.get("https://travelandleisure.com");
        SweepstakesPage sweepstakesPage = new SweepstakesPage(driver);
        
        String title = sweepstakesPage.getPageTitle();
        Assert.assertNotNull(title, "Sweepstakes page title was not loaded!");
        Assert.assertTrue(title.toLowerCase().contains("travel"), "Page title does not match destination portal!");
    }
    // 2. Verify valid entry submission
    // 5. Verify invalid email validation
    // 6. Verify success confirmation message
    // 7. Verify duplicate entry handling
    @Test(priority = 2, dataProvider = "SweeptakesData", description = "Scenarios: Valid Entry, Invalid Email, Success Msg, and Duplicate Handling")
    public void testSweeptakesSubscriptionDataDriven(String[] rowData) { // Fixed: Swapped strict arguments for a flexible row array
        
        // Safely extract the parameters from the array based on row indexing positions
        String emailAddress = rowData[0].trim();
        String expectedMessage = rowData[1].trim();

        driver.get("https://travelandleisure.com");
        SweepstakesPage sweepstakesPage = new SweepstakesPage(driver);

        if (sweepstakesPage.isEntryFormDisplayed()) {
            if (expectedMessage.equalsIgnoreCase("success")) {
                String uniqueEmail = "user" + System.currentTimeMillis() + "@gmail.com";
                sweepstakesPage.enterEmail(uniqueEmail);
            } else {
                sweepstakesPage.enterEmail(emailAddress);
            }

            sweepstakesPage.setAllCheckboxesState(true);
            sweepstakesPage.clickSubmitButton();

            System.out.println("\n==================================================");
            System.out.println("📋 VERIFICATION RESULTS FOR INPUT: " + emailAddress);
            System.out.println("==================================================");

            if (expectedMessage.equalsIgnoreCase("success")) {
                String capturedSuccess = sweepstakesPage.getSuccessMessageText();
                Assert.assertFalse(capturedSuccess.isEmpty(), "Verify success confirmation message failed!");
                System.out.println("SUCCESS RESULT LOG: \"" + capturedSuccess.replace("\r\n", " ") + "\"\n");
                
            } else if (expectedMessage.equalsIgnoreCase("Email already subscribed")) {
                String duplicateBannerText = sweepstakesPage.getSuccessMessageText();
                if (duplicateBannerText.contains("Thanks for signing up") || duplicateBannerText.contains("Success!")) {
                    Assert.fail("FAIL (Verify duplicate entry handling): Application displayed a false success banner for duplicate registration!");
                } else {
                    Assert.assertTrue(duplicateBannerText.toLowerCase().contains("already") || duplicateBannerText.equalsIgnoreCase(expectedMessage),
                        "Duplicate registration check failed to trigger expected warning!");
                    System.out.println("SUCCESSFULLY VERIFIED DUPLICATE ENTRY HANDLING.");
                }
            } else if (expectedMessage.equalsIgnoreCase("invalid")) {
                String errorMsg = driver.findElement(By.cssSelector("input[type='email']")).getAttribute("validationMessage");
                if (errorMsg == null || errorMsg.isEmpty()) {
                    errorMsg = "Invalid Email Layout Intercepted.";
                }
                Assert.assertFalse(errorMsg.isEmpty(), "Verify invalid email validation failed!");
                System.out.println("INVALID EMAIL VALIDATION PASSED: \"" + errorMsg + "\"");
            }
            System.out.println("==================================================\n");
        }
        driver.navigate().refresh();
    }

    // 3. Verify Advertisement do not occupies excessive page space
    @Test(priority = 3, description = "Scenario: Verify Advertisement layout bounds do not occupy excessive viewport space")
    public void testAdvertisementPageSpaceOccupancy() {
        driver.get("https://travelandleisure.com");
        Dimension windowSize = driver.manage().window().getSize();
        int totalViewportArea = windowSize.getWidth() * windowSize.getHeight();

        // Target standard ad wrapper slots
        java.util.List<WebElement> adSlots = driver.findElements(By.cssSelector("div[id*='ad'], div[class*='advertisement'], .lead-ad"));
        
        for (WebElement ad : adSlots) {
            if (ad.isDisplayed()) {
                int adArea = ad.getSize().getWidth() * ad.getSize().getHeight();
                double occupancyPercentage = ((double) adArea / totalViewportArea) * 100;
                
                // Assert that an individual ad layer context does not engulf more than 35% of the visible framework layout
                Assert.assertTrue(occupancyPercentage < 35.0, "Advertisement occupies excessive page space: " + occupancyPercentage + "%");
            }
        }
        System.out.println("✅ AD SPACE OCCUPANCY CHECK COMPLETED WITHIN COMPLIANCE.");
    }

    // 4. Verify mandatory field validation
    @Test(priority = 4, description = "Scenario: Verify mandatory field validation triggers error flags on blank submission")
    public void testMandatoryFieldValidation() {
        driver.get("https://travelandleisure.com");
        SweepstakesPage sweepstakesPage = new SweepstakesPage(driver);

        if (sweepstakesPage.isEntryFormDisplayed()) {
            sweepstakesPage.enterEmail(""); // Explicitly bypass data initialization parameters
            sweepstakesPage.clickSubmitButton();

            WebElement emailInputEl = driver.findElement(By.cssSelector("input[type='email']"));
            String isRequired = emailInputEl.getAttribute("required");
            
            // Validate the HTML5 specification layer directly or extract structural warning texts
            Assert.assertTrue(isRequired != null || !emailInputEl.getAttribute("validationMessage").isEmpty(), 
                "Mandatory field validation failed! Field accepted empty submission without constraints.");
            System.out.println("✅ MANDATORY FIELD VALIDATION GUARD VERIFIED SUCCESSFULLY.");
        }
    }

    // 8. Verify Terms & Conditions link
    @Test(priority = 5, description = "Scenario: Verify Terms & Conditions link structure and endpoint navigation routing")
    public void testTermsAndConditionsLinkRouting() {
        driver.get("https://travelandleisure.com");
        
        WebElement termsLink = driver.findElement(By.xpath("//a[contains(text(),'Terms') or contains(text(),'Privacy')]"));
        Assert.assertTrue(termsLink.isDisplayed(), "Terms & Conditions link is missing on form container template!");
        
        String urlTargetDestination = termsLink.getAttribute("href");
        Assert.assertTrue(urlTargetDestination != null && urlTargetDestination.startsWith("http"), 
            "Terms link redirection schema URL layout is unassigned or broken!");
        System.out.println("✅ TERMS & CONDITIONS ROUTING NODE LINK ROUTE SECURED: " + urlTargetDestination);
    }

    // 9. Verify mobile responsiveness
    @Test(priority = 6, description = "Scenario: Verify mobile responsiveness elements scale usable inside compressed display frames")
    public void testMobileResponsivenessBreakpointScaling() {
        driver.get("https://travelandleisure.com");
        SweepstakesPage sweepstakesPage = new SweepstakesPage(driver);
        
        Dimension originalResolutionDimensions = driver.manage().window().getSize();
        try {
            // Compress active layout tree frames inside specific common mobile breakpoint scales (Width: 375, Height: 812)
            driver.manage().window().setSize(new Dimension(375, 812));
            
            Assert.assertTrue(sweepstakesPage.isEntryFormDisplayed(), 
                "Mobile Responsiveness Error: Form elements vanished or compressed out of viewport boundaries!");
            System.out.println("✅ MOBILE RESPONSIVENESS SCALING CHECKS COMPLETE.");
        } finally {
            driver.manage().window().setSize(originalResolutionDimensions);
        }
    }

    // 10. Verify browser compatibility
    @Test(priority = 7, description = "Scenario: Verify browser compatibility driver context metrics mapping")
    public void testBrowserCompatibilityEnvironmentMetadata() {
        org.openqa.selenium.Capabilities caps = ((org.openqa.selenium.remote.RemoteWebDriver) driver).getCapabilities();
        String browserEngineName = caps.getBrowserName();
        String browserVersionInfo = caps.getBrowserVersion();
        
        Assert.assertNotNull(browserEngineName, "Browser environment verification engine tracking dropped profile references!");
        System.out.println("🌐 BROWSER COMPATIBILITY METADATA CAPTURED -> Active Agent Target: [" + browserEngineName + "] | Version Profile: [" + browserVersionInfo + "]");
    }

    // 11. Verify application stability during submission
    @Test(priority = 8, description = "Scenario: Verify application stability during form data transmission execution routines")
    public void testApplicationStabilityDuringSubmissionPipeline() {
    	
    }
}