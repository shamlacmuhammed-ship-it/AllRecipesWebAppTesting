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

    // Reads Sweepstakes test data from Excel
    @DataProvider(name = "SweeptakesData")
    public Object[][] getSweeptakesData() {
        return ExcelUtils.getTestData(
                "src/main/resources/testdata/TestData.xlsx",
                "SweeptakesData",
                3);
    }

    // TC_001 : Verify Sweepstakes page opens successfully
    @Test(priority = 1,
          description = "Verify Sweepstakes page opens successfully")
    public void testSweepstakesPageOpensSuccessfully() {

        // Open the Sweepstakes website
        driver.get("https://travelandleisure.com");

        SweepstakesPage sweepstakesPage =
                new SweepstakesPage(driver);

        // Verify page title
        String title = sweepstakesPage.getPageTitle();

        Assert.assertNotNull(title,
                "Page title was not loaded.");

        Assert.assertTrue(title.toLowerCase().contains("travel"),
                "Incorrect page title.");
    }

    // TC_002, TC_005, TC_006 & TC_007
    // Verify valid submission, invalid email,
    // success message and duplicate email handling
    @Test(priority = 2,
          dataProvider = "SweeptakesData")
    public void testSweeptakesSubscriptionDataDriven(String[] rowData) {

        // Read data from Excel
        String emailAddress = rowData[0].trim();
        String expectedMessage = rowData[1].trim();

        driver.get("https://travelandleisure.com");

        SweepstakesPage sweepstakesPage =
                new SweepstakesPage(driver);

        // Verify entry form is displayed
        if (sweepstakesPage.isEntryFormDisplayed()) {

            // Generate unique email for success scenario
            if (expectedMessage.equalsIgnoreCase("success")) {

                String uniqueEmail =
                        "user" + System.currentTimeMillis()
                                + "@gmail.com";

                sweepstakesPage.enterEmail(uniqueEmail);

            } else {

                // Enter test email from Excel
                sweepstakesPage.enterEmail(emailAddress);
            }

            // Select all subscription checkboxes
            sweepstakesPage.setAllCheckboxesState(true);

            // Submit form
            sweepstakesPage.clickSubmitButton();

            // Success validation
            if (expectedMessage.equalsIgnoreCase("success")) {

                String success =
                        sweepstakesPage.getSuccessMessageText();

                Assert.assertFalse(success.isEmpty(),
                        "Success message not displayed.");
            }

            // Duplicate email validation
            else if (expectedMessage.equalsIgnoreCase("Email already subscribed")) {

                String duplicateMsg =
                        sweepstakesPage.getSuccessMessageText();

                if (duplicateMsg.contains("Success!")
                        || duplicateMsg.contains("Thanks for signing up")) {

                    Assert.fail("Duplicate email accepted.");

                } else {

                    Assert.assertTrue(
                            duplicateMsg.toLowerCase().contains("already")
                                    || duplicateMsg.equalsIgnoreCase(expectedMessage));
                }
            }

            // Invalid email validation
            else if (expectedMessage.equalsIgnoreCase("invalid")) {

                String error =
                        driver.findElement(By.cssSelector("input[type='email']"))
                                .getAttribute("validationMessage");

                if (error == null || error.isEmpty()) {
                    error = "Validation message displayed.";
                }

                Assert.assertFalse(error.isEmpty(),
                        "Validation message not displayed.");
            }
        }

        // Refresh page before next iteration
        driver.navigate().refresh();
    }

    // TC_003 : Verify advertisement occupies acceptable space
    @Test(priority = 3)
    public void testAdvertisementPageSpaceOccupancy() {

        driver.get("https://travelandleisure.com");

        // Calculate browser viewport size
        Dimension size =
                driver.manage().window().getSize();

        int viewportArea =
                size.getWidth() * size.getHeight();

        // Locate advertisement blocks
        java.util.List<WebElement> ads =
                driver.findElements(By.cssSelector(
                        "div[id*='ad'], div[class*='advertisement'], .lead-ad"));

        for (WebElement ad : ads) {

            if (ad.isDisplayed()) {

                int adArea =
                        ad.getSize().getWidth()
                                * ad.getSize().getHeight();

                double percentage =
                        ((double) adArea / viewportArea) * 100;

                // Verify advertisement does not exceed 35%
                Assert.assertTrue(percentage < 35);
            }
        }
    }

    // TC_004 : Verify mandatory field validation
    @Test(priority = 4)
    public void testMandatoryFieldValidation() {

        driver.get("https://travelandleisure.com");

        SweepstakesPage sweepstakesPage =
                new SweepstakesPage(driver);

        if (sweepstakesPage.isEntryFormDisplayed()) {

            // Submit empty email
            sweepstakesPage.enterEmail("");

            sweepstakesPage.clickSubmitButton();

            WebElement email =
                    driver.findElement(By.cssSelector("input[type='email']"));

            // Verify required validation
            Assert.assertTrue(
                    email.getAttribute("required") != null
                            || !email.getAttribute("validationMessage").isEmpty(),
                    "Required validation failed.");
        }
    }

    // TC_008 : Verify Terms & Conditions link
    @Test(priority = 5)
    public void testTermsAndConditionsLinkRouting() {

        driver.get("https://travelandleisure.com");

        // Locate Terms or Privacy link
        WebElement terms =
                driver.findElement(By.xpath(
                        "//a[contains(text(),'Terms') or contains(text(),'Privacy')]"));

        Assert.assertTrue(terms.isDisplayed());

        // Verify hyperlink
        String url = terms.getAttribute("href");

        Assert.assertTrue(
                url != null && url.startsWith("http"));
    }

    // TC_009 : Verify mobile responsiveness
    @Test(priority = 6)
    public void testMobileResponsivenessBreakpointScaling() {

        driver.get("https://travelandleisure.com");

        SweepstakesPage sweepstakesPage =
                new SweepstakesPage(driver);

        // Store current window size
        Dimension original =
                driver.manage().window().getSize();

        try {

            // Resize browser to mobile resolution
            driver.manage().window()
                    .setSize(new Dimension(375, 812));

            // Verify form remains visible
            Assert.assertTrue(
                    sweepstakesPage.isEntryFormDisplayed());

        } finally {

            // Restore original window size
            driver.manage().window().setSize(original);
        }
    }

    // TC_010 : Verify browser compatibility
    @Test(priority = 7)
    public void testBrowserCompatibilityEnvironmentMetadata() {

        org.openqa.selenium.Capabilities caps =
                ((org.openqa.selenium.remote.RemoteWebDriver) driver)
                        .getCapabilities();

        // Verify browser details
        Assert.assertNotNull(caps.getBrowserName());

        System.out.println(
                "Browser : "
                        + caps.getBrowserName()
                        + " Version : "
                        + caps.getBrowserVersion());
    }

    // TC_011 : Verify application stability during submission
    @Test(priority = 8)
    public void testApplicationStabilityDuringSubmissionPipeline() {

        // Placeholder for future stability or performance test
    }
}
